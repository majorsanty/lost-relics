/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.lostrelics.common.component.entity;

import dev.onyxstudios.cca.api.v3.component.Component;
import moriyashiine.lostrelics.common.init.ModSoundEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;

public class CursedAmuletComponent implements Component {
	private final PlayerEntity obj;
	private boolean theSkeletonAppears = false;

	public CursedAmuletComponent(PlayerEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		theSkeletonAppears = tag.getBoolean("TheSkeletonAppears");
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putBoolean("TheSkeletonAppears", theSkeletonAppears);
	}

	public boolean shouldTheSkeletonAppear() {
		return theSkeletonAppears;
	}

	public void toggleTransform(boolean theSkeletonAppears) {
		if (this.theSkeletonAppears != theSkeletonAppears) {
			if (obj.getWorld().isClient) {
				obj.playSound(ModSoundEvents.ENTITY_GENERIC_TRANSFORM, 0.5F, 1);
				for (int i = 0; i < 48; i++) {
					obj.getWorld().addParticle(ParticleTypes.SMOKE, obj.getParticleX(1), obj.getRandomBodyY(), obj.getParticleZ(1), 0, 0, 0);
				}
			}
		}
		this.theSkeletonAppears = theSkeletonAppears;
	}
}
