/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.lostrelics.common.entity.projectile;

import moriyashiine.lostrelics.common.init.ModEntityTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class SmokeBallEntity extends Entity {
	private Entity owner = null, target = null;
	private UUID ownerUUID = null, targetUUID = null;
	private float damage = 0;

	public SmokeBallEntity(EntityType<?> entityType, World world) {
		super(entityType, world);
		noClip = true;
	}

	public SmokeBallEntity(World world, LivingEntity owner, LivingEntity target, float damage) {
		this(ModEntityTypes.SMOKE_BALL, world);
		setPosition(owner.getX(), owner.getEyeY(), owner.getZ());
		this.damage = damage;
		setOwner(owner);
		setTarget(target);
	}

	@Override
	protected void readCustomDataFromNbt(NbtCompound nbt) {
		ownerUUID = nbt.getUuid("Owner");
		targetUUID = nbt.getUuid("Target");
	}

	@Override
	protected void writeCustomDataToNbt(NbtCompound nbt) {
		nbt.putUuid("Owner", ownerUUID);
		nbt.putUuid("Target", targetUUID);
	}

	@Override
	protected void initDataTracker() {
	}

	@Override
	public void tick() {
		super.tick();
		if (!getWorld().isClient) {
			Entity target = getTarget(), owner = getOwner();
			if (target == null || target.isRemoved() || owner == null || owner.isRemoved() || !getWorld().isChunkLoaded(getBlockPos())) {
				discard();
				return;
			}
			addVelocity(new Vec3d(target.getX() - getX(), target.getBodyY(0.5) - getY(), target.getZ() - getZ()));
			setVelocity(getVelocity().normalize().multiply(0.5));
			velocityDirty = true;
			getWorld().getEntitiesByClass(LivingEntity.class, new Box(getBlockPos()), foundEntity -> foundEntity == target).forEach(foundEntity -> {
				foundEntity.damage(getWorld().getDamageSources().indirectMagic(this, owner), damage);
				discard();
			});
		}
		setPosition(getX() + getVelocity().getX(), getY() + getVelocity().getY(), getZ() + getVelocity().getZ());
	}

	@Nullable
	private Entity getOwner() {
		if (owner != null && !owner.isRemoved()) {
			return owner;
		}
		if (ownerUUID != null && getWorld() instanceof ServerWorld serverWorld) {
			owner = serverWorld.getEntity(ownerUUID);
			return owner;
		}
		return null;
	}

	public void setOwner(Entity owner) {
		this.owner = owner;
		this.ownerUUID = owner.getUuid();
	}

	@Nullable
	private Entity getTarget() {
		if (target != null && !target.isRemoved()) {
			return target;
		}
		if (targetUUID != null && getWorld() instanceof ServerWorld serverWorld) {
			target = serverWorld.getEntity(targetUUID);
			return target;
		}
		return null;
	}

	public void setTarget(Entity target) {
		this.target = target;
		this.targetUUID = target.getUuid();
	}
}
