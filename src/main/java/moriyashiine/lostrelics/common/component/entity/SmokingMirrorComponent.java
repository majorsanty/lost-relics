/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.lostrelics.common.component.entity;

import moriyashiine.lostrelics.common.init.ModEntityComponents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryWrapper;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

public class SmokingMirrorComponent implements AutoSyncedComponent, CommonTickingComponent {
	private final PlayerEntity obj;
	private int invisiblityTimer = 0;

	public SmokingMirrorComponent(PlayerEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		invisiblityTimer = tag.getInt("InvisibilityTimer");
	}

	@Override
	public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		tag.putInt("InvisibilityTimer", invisiblityTimer);
	}

	@Override
	public void tick() {
		if (invisiblityTimer > 0) {
			if (obj.getWorld().isClient && obj.getRandom().nextBoolean()) {
				obj.getWorld().addParticle(ParticleTypes.SMOKE, obj.getParticleX(0.5), obj.getY(), obj.getParticleZ(0.5), 0, 0, 0);
			}
			invisiblityTimer--;
		}
	}

	public void sync() {
		ModEntityComponents.SMOKING_MIRROR.sync(obj);
	}

	public void setInvisiblityTimer(int invisiblityTimer) {
		this.invisiblityTimer = invisiblityTimer;
	}

	public int getInvisiblityTimer() {
		return invisiblityTimer;
	}
}
