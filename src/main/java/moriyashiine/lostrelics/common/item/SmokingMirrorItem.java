/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.lostrelics.common.item;

import moriyashiine.lostrelics.client.packet.SyncDoppelgangerSlimStatusS2CPacket;
import moriyashiine.lostrelics.common.LostRelicsUtil;
import moriyashiine.lostrelics.common.entity.DoppelgangerEntity;
import moriyashiine.lostrelics.common.init.ModDamageTypes;
import moriyashiine.lostrelics.common.init.ModEntityComponents;
import moriyashiine.lostrelics.common.init.ModEntityTypes;
import moriyashiine.lostrelics.common.init.ModSoundEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class SmokingMirrorItem extends RelicItem {
	public SmokingMirrorItem() {
		super(104, Items.CRYING_OBSIDIAN);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack stack = user.getStackInHand(hand);
		if (isUsable(stack, 10)) {
			if (!world.isClient) {
				boolean mirrorDemon = user.isSneaking();
				if (LostRelicsUtil.applyCooldownAndDamage(user, stack, mirrorDemon ? 1200 : 600, mirrorDemon ? 70 : 10)) {
					user.damage(ModDamageTypes.relic(world), user.getMaxHealth() * (mirrorDemon ? 0.5F : 0.25F));
					world.playSound(null, user.getBlockPos(), ModSoundEvents.ENTITY_GENERIC_TRANSFORM, user.getSoundCategory(), 1, 1);
					if (user.isAlive()) {
						ModEntityComponents.SMOKING_MIRROR.maybeGet(user).ifPresent(smokingMirrorComponent -> {
							smokingMirrorComponent.setInvisiblityTimer(mirrorDemon ? 600 : 200);
							smokingMirrorComponent.sync();
						});
						for (int i = 0; i < (mirrorDemon ? 1 : 4); i++) {
							boolean teleportOnPlayer = true;
							DoppelgangerEntity doppelganger = ModEntityTypes.DOPPELGANGER.create(world);
							BlockPos.Mutable mutable = new BlockPos.Mutable();
							for (int tries = 0; tries < 24; tries++) {
								mutable.set(user.getX() + MathHelper.nextInt(user.getRandom(), -6, 6), user.getY() + MathHelper.nextInt(user.getRandom(), 0, 6), user.getZ() + MathHelper.nextInt(user.getRandom(), -6, 6));
								while (mutable.getY() > world.getBottomY() && !world.getBlockState(mutable).blocksMovement()) {
									mutable.move(Direction.DOWN);
								}
								if (world.getBlockState(mutable).blocksMovement()) {
									mutable.move(Direction.UP);
									teleportOnPlayer = false;
									break;
								}
							}
							if (teleportOnPlayer) {
								mutable.set(user.getBlockPos());
							}
							doppelganger.teleport(mutable.getX(), mutable.getY(), mutable.getZ());
							doppelganger.setOwner(user);
							if (mirrorDemon) {
								doppelganger.getDataTracker().set(DoppelgangerEntity.MIRROR_DEMON, true);
							}
							SyncDoppelgangerSlimStatusS2CPacket.send((ServerPlayerEntity) user, doppelganger);
							world.playSound(null, user.getBlockPos(), ModSoundEvents.ENTITY_GENERIC_SPAWN, doppelganger.getSoundCategory(), 1, 1);
							((ServerWorld) world).spawnParticles(ParticleTypes.SMOKE, doppelganger.getParticleX(0.5), doppelganger.getRandomBodyY(), doppelganger.getParticleZ(0.5), 8, MathHelper.nextDouble(doppelganger.getRandom(), -0.5, 0.5), MathHelper.nextDouble(doppelganger.getRandom(), -0.5, 0.5), MathHelper.nextDouble(doppelganger.getRandom(), -0.5, 0.5), 1);
							world.spawnEntity(doppelganger);
						}
					}
				}
			}
			return TypedActionResult.success(stack, world.isClient);
		}
		return TypedActionResult.pass(stack);
	}
}
