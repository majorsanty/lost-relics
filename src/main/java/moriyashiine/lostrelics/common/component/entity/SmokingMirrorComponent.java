/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.lostrelics.common.component.entity;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import moriyashiine.lostrelics.common.init.ModEntityComponents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;

public class SmokingMirrorComponent implements AutoSyncedComponent, CommonTickingComponent {
	private final PlayerEntity obj;
	private int invisiblityTimer = 0;

	public SmokingMirrorComponent(PlayerEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		invisiblityTimer = tag.getInt("InvisibilityTimer");
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
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
