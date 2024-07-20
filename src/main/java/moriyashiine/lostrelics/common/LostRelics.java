/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.lostrelics.common;

import moriyashiine.lostrelics.client.payload.SyncDoppelgangerSlimStatusS2CPayload;
import moriyashiine.lostrelics.common.event.SmokingMirrorEvent;
import moriyashiine.lostrelics.common.event.TurquoiseEyeEvent;
import moriyashiine.lostrelics.common.init.*;
import moriyashiine.lostrelics.common.payload.SyncDoppelgangerSlimStatusC2SPayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

public class LostRelics implements ModInitializer {
	public static final String MOD_ID = "lostrelics";

	@Override
	public void onInitialize() {
		ModBlocks.init();
		ModBlockEntityTypes.init();
		ModDataComponentTypes.init();
		ModEntityTypes.init();
		ModItems.init();
		ModRecipeSerializers.init();
		ModSoundEvents.init();
		initEvents();
		initPayloads();
	}

	public static Identifier id(String value) {
		return Identifier.of(MOD_ID, value);
	}

	private void initEvents() {
		ServerLivingEntityEvents.ALLOW_DAMAGE.register(new SmokingMirrorEvent());
		ServerLivingEntityEvents.ALLOW_DAMAGE.register(new TurquoiseEyeEvent());
	}

	private void initPayloads() {
		// client payloads
		PayloadTypeRegistry.playS2C().register(SyncDoppelgangerSlimStatusS2CPayload.ID, SyncDoppelgangerSlimStatusS2CPayload.CODEC);
		// common payloads
		PayloadTypeRegistry.playC2S().register(SyncDoppelgangerSlimStatusC2SPayload.ID, SyncDoppelgangerSlimStatusC2SPayload.CODEC);
		// common receivers
		ServerPlayNetworking.registerGlobalReceiver(SyncDoppelgangerSlimStatusC2SPayload.ID, new SyncDoppelgangerSlimStatusC2SPayload.Receiver());
	}
}