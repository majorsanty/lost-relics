/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.lostrelics.common.entity;

import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import moriyashiine.lostrelics.common.init.ModItems;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.OptionalInt;
import java.util.UUID;

public class DoppelgangerEntity extends TameableEntity {
	public static final Object2BooleanMap<UUID> SLIM_STATUSES = new Object2BooleanOpenHashMap<>();

	public static final TrackedData<Boolean> SLIM = DataTracker.registerData(DoppelgangerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	public static final TrackedData<Boolean> MIRROR_DEMON = DataTracker.registerData(DoppelgangerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	public static final TrackedData<OptionalInt> TARGET_PLAYER = DataTracker.registerData(DoppelgangerEntity.class, TrackedDataHandlerRegistry.OPTIONAL_INT);

	private final MeleeAttackGoal meleeAttackGoal = new MeleeAttackGoal(this, 1, true);

	private int ticksExisted = 0, projectileCooldown = 0;

	public DoppelgangerEntity(EntityType<? extends TameableEntity> entityType, World world) {
		super(entityType, world);
	}

	public static DefaultAttributeContainer.Builder createDoppelgangerAttributes() {
		return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_ATTACK_DAMAGE).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.35);
	}

	@Nullable
	@Override
	public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
		return null;
	}

	@Override
	public boolean isBreedingItem(ItemStack stack) {
		return false;
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		dataTracker.set(SLIM, nbt.getBoolean("Slim"));
		dataTracker.set(MIRROR_DEMON, nbt.getBoolean("MirrorDemon"));
		if (nbt.contains("TargetPlayer")) {
			dataTracker.set(TARGET_PLAYER, OptionalInt.of(nbt.getInt("TargetPlayer")));
		}
		ticksExisted = nbt.getInt("TicksExisted");
		projectileCooldown = nbt.getInt("ProjectileCooldown");
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		nbt.putBoolean("Slim", dataTracker.get(SLIM));
		nbt.putBoolean("MirrorDemon", dataTracker.get(MIRROR_DEMON));
		dataTracker.get(TARGET_PLAYER).ifPresent(targetPlayer -> nbt.putInt("TargetPlayer", targetPlayer));
		nbt.putInt("TicksExisted", ticksExisted);
		nbt.putInt("ProjectileCooldown", projectileCooldown);
	}

	@Override
	protected void initDataTracker(DataTracker.Builder builder) {
		super.initDataTracker(builder);
		builder.add(SLIM, false);
		builder.add(MIRROR_DEMON, false);
		builder.add(TARGET_PLAYER, OptionalInt.empty());
	}

	@Override
	protected void initGoals() {
		goalSelector.add(1, new SwimGoal(this));
		goalSelector.add(3, new FollowOwnerGoal(this, 1, 10, 2));
		goalSelector.add(4, new WanderAroundFarGoal(this, 1));
		goalSelector.add(5, new LookAtEntityGoal(this, PlayerEntity.class, 8));
		goalSelector.add(5, new LookAroundGoal(this));
		targetSelector.add(1, new TrackOwnerAttackerGoal(this));
		targetSelector.add(2, new AttackWithOwnerGoal(this));
	}

	@Override
	public void tick() {
		super.tick();
		ticksExisted++;
		if (projectileCooldown > 0) {
			projectileCooldown--;
		}
		if (!getWorld().isClient) {
			if (SLIM_STATUSES.containsKey(getUuid())) {
				dataTracker.set(SLIM, SLIM_STATUSES.removeBoolean(getUuid()));
			}
			if (ticksExisted >= 600 || getOwner() == null || getOwner().isDead() || distanceTo(getOwner()) > 32) {
				discard();
				return;
			}
			for (EntityAttribute attribute : Registries.ATTRIBUTE) {
				RegistryEntry<EntityAttribute> entry = Registries.ATTRIBUTE.getEntry(attribute);
				if (entry != EntityAttributes.GENERIC_MOVEMENT_SPEED && getAttributes().hasAttribute(entry) && getEntityToCopy().getAttributes().hasAttribute(entry)) {
					getAttributeInstance(entry).setBaseValue(getEntityToCopy().getAttributeValue(entry));
				}
			}
			if (getOwner() instanceof PlayerEntity player) {
				player.getItemCooldownManager().set(ModItems.SMOKING_MIRROR, dataTracker.get(MIRROR_DEMON) ? 1200 : 600);
			}
			boolean hasMeleeAttackGoal = false;
			for (PrioritizedGoal goal : goalSelector.getGoals()) {
				if (goal.getGoal() == meleeAttackGoal) {
					hasMeleeAttackGoal = true;
					break;
				}
			}
			ItemStack rangedStack = ItemStack.EMPTY;
			if (getEntityToCopy().getMainHandStack().getItem() instanceof RangedWeaponItem) {
				rangedStack = getEntityToCopy().getMainHandStack();
			} else if (getEntityToCopy().getOffHandStack().getItem() instanceof RangedWeaponItem) {
				rangedStack = getEntityToCopy().getOffHandStack();
			}
			if (!rangedStack.isEmpty()) {
				if (hasMeleeAttackGoal) {
					goalSelector.remove(meleeAttackGoal);
				}
				if (getTarget() != null) {
					lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, getTarget().getPos());
					if (projectileCooldown == 0) {
						projectileCooldown = MathHelper.nextInt(getRandom(), 40, 120);
						ItemStack projectileType = getEntityToCopy().getProjectileType(rangedStack);
						if (projectileType.isEmpty()) {
							projectileType = new ItemStack(Items.ARROW);
						}
						PersistentProjectileEntity projectile = ProjectileUtil.createArrowProjectile(this, projectileType, 1, rangedStack);
						double dX = getTarget().getX() - getX();
						double dY = getTarget().getBodyY(1 / 3F) - projectile.getY();
						double dZ = getTarget().getZ() - getZ();
						projectile.setVelocity(dX, dY + Math.sqrt(dX * dX + dZ * dZ) * 0.2F, dZ, 1.6F, 10);
						playSound(SoundEvents.ENTITY_ARROW_SHOOT, 1, 1 / (getRandom().nextFloat() * 0.4F + 0.8F));
						getWorld().spawnEntity(projectile);
					}
				}
			} else if (!hasMeleeAttackGoal) {
				goalSelector.add(2, meleeAttackGoal);
			}
		}
	}

	@Override
	public boolean damage(DamageSource source, float amount) {
		if (dataTracker.get(MIRROR_DEMON)) {
			amount /= 2;
		}
		return super.damage(source, amount);
	}

	@Override
	public boolean canBeLeashed() {
		return false;
	}

	@Override
	public boolean canBeHitByProjectile() {
		return false;
	}

	@Override
	public boolean canTarget(LivingEntity target) {
		if (target instanceof DoppelgangerEntity doppelganger && getOwner() == doppelganger.getOwner()) {
			return false;
		}
		return super.canTarget(target);
	}

	@Override
	public void setTarget(@Nullable LivingEntity target) {
		super.setTarget(target);
		if (dataTracker.get(MIRROR_DEMON)) {
			if (target == null) {
				dataTracker.set(TARGET_PLAYER, OptionalInt.empty());
			} else if (target instanceof PlayerEntity) {
				dataTracker.set(TARGET_PLAYER, OptionalInt.of(target.getId()));
			}
		}
	}

	@Override
	public void setOwner(PlayerEntity player) {
		setTamed(true, false);
		setOwnerUuid(player.getUuid());
	}

	@Override
	public void onDeath(DamageSource damageSource) {
		boolean showDeathMessages = false;
		if (!getWorld().isClient && getWorld().getGameRules().getBoolean(GameRules.SHOW_DEATH_MESSAGES)) {
			showDeathMessages = true;
			getWorld().getGameRules().get(GameRules.SHOW_DEATH_MESSAGES).set(false, getServer());
		}
		super.onDeath(damageSource);
		if (!getWorld().isClient && showDeathMessages) {
			getWorld().getGameRules().get(GameRules.SHOW_DEATH_MESSAGES).set(true, getServer());
		}
	}

	public LivingEntity getEntityToCopy() {
		if (dataTracker.get(TARGET_PLAYER).isPresent()) {
			return (LivingEntity) getWorld().getEntityById(dataTracker.get(TARGET_PLAYER).getAsInt());
		}
		return getOwner();
	}
}
