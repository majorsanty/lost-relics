/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.lostrelics.client.packet;

import moriyashiine.lostrelics.common.LostRelics;
import moriyashiine.lostrelics.common.entity.DoppelgangerEntity;
import moriyashiine.lostrelics.common.packet.SyncDoppelgangerSlimStatusC2SPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Uuids;

import java.util.UUID;

public record SyncDoppelgangerSlimStatusS2CPayload(UUID uuid) implements CustomPayload {
	public static final CustomPayload.Id<SyncDoppelgangerSlimStatusS2CPayload> ID = CustomPayload.id(LostRelics.id("sync_doppelganger_slim_status_s2c").toString());
	public static final PacketCodec<PacketByteBuf, SyncDoppelgangerSlimStatusS2CPayload> CODEC = PacketCodec.tuple(
			Uuids.PACKET_CODEC, SyncDoppelgangerSlimStatusS2CPayload::uuid,
			SyncDoppelgangerSlimStatusS2CPayload::new);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}

	public static void send(ServerPlayerEntity player, DoppelgangerEntity entity) {
		ServerPlayNetworking.send(player, new SyncDoppelgangerSlimStatusS2CPayload(entity.getUuid()));
	}

	public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<SyncDoppelgangerSlimStatusS2CPayload> {
		@Override
		public void receive(SyncDoppelgangerSlimStatusS2CPayload payload, ClientPlayNetworking.Context context) {
			SyncDoppelgangerSlimStatusC2SPayload.send(payload.uuid(), context.player().getSkinTextures().model() == SkinTextures.Model.SLIM);
		}
	}
}