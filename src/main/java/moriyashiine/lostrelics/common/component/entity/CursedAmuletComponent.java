/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.lostrelics.common.component.entity;

import moriyashiine.lostrelics.common.init.ModSoundEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryWrapper;
import org.ladysnake.cca.api.v3.component.Component;

public class CursedAmuletComponent implements Component {
	private final PlayerEntity obj;
	private boolean theSkeletonAppears = false;

	public CursedAmuletComponent(PlayerEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		theSkeletonAppears = tag.getBoolean("TheSkeletonAppears");
	}

	@Override
	public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		tag.putBoolean("TheSkeletonAppears", theSkeletonAppears);
	}

	public boolean shouldTheSkeletonAppear() {
		return theSkeletonAppears;
	}

	public void toggleTransform(boolean theSkeletonAppears) {
		if (this.theSkeletonAppears != theSkeletonAppears) {
			obj.getWorld().playSound(null, obj.getBlockPos(), ModSoundEvents.ENTITY_GENERIC_TRANSFORM, obj.getSoundCategory(), 0.5F, 1);
			if (obj.getWorld().isClient) {
				for (int i = 0; i < 48; i++) {
					obj.getWorld().addParticle(ParticleTypes.SMOKE, obj.getParticleX(1), obj.getRandomBodyY(), obj.getParticleZ(1), 0, 0, 0);
				}
			}
		}
		this.theSkeletonAppears = theSkeletonAppears;
	}
}
