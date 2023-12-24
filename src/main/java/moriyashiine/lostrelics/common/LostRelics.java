/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.lostrelics.common;

import moriyashiine.lostrelics.common.event.SmokingMirrorEvent;
import moriyashiine.lostrelics.common.event.TurquoiseEyeEvent;
import moriyashiine.lostrelics.common.init.*;
import moriyashiine.lostrelics.common.packet.SyncDoppelgangerSlimStatusC2SPacket;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

public class LostRelics implements ModInitializer {
	public static final String MOD_ID = "lostrelics";

	@Override
	public void onInitialize() {
		ModItems.init();
		ModEntityTypes.init();
		ModWorldGenerators.init();
		ModRecipeSerializers.init();
		ModSoundEvents.init();
		initEvents();
		initPackets();
	}

	public static Identifier id(String value) {
		return new Identifier(MOD_ID, value);
	}

	private void initEvents() {
		ServerLivingEntityEvents.ALLOW_DAMAGE.register(new SmokingMirrorEvent());
		ServerLivingEntityEvents.ALLOW_DAMAGE.register(new TurquoiseEyeEvent());
	}

	private void initPackets() {
		ServerPlayNetworking.registerGlobalReceiver(SyncDoppelgangerSlimStatusC2SPacket.ID, new SyncDoppelgangerSlimStatusC2SPacket.Receiver());
	}
}