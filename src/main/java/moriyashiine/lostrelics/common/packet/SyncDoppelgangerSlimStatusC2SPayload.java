/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.lostrelics.common.packet;

import moriyashiine.lostrelics.common.LostRelics;
import moriyashiine.lostrelics.common.entity.DoppelgangerEntity;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Uuids;

import java.util.UUID;

public record SyncDoppelgangerSlimStatusC2SPayload(UUID uuid, boolean slim) implements CustomPayload {
	public static final CustomPayload.Id<SyncDoppelgangerSlimStatusC2SPayload> ID = CustomPayload.id(LostRelics.id("sync_doppelganger_slim_status_cs2").toString());
	public static final PacketCodec<PacketByteBuf, SyncDoppelgangerSlimStatusC2SPayload> CODEC = PacketCodec.tuple(
			Uuids.PACKET_CODEC, SyncDoppelgangerSlimStatusC2SPayload::uuid,
			PacketCodecs.BOOL, SyncDoppelgangerSlimStatusC2SPayload::slim,
			SyncDoppelgangerSlimStatusC2SPayload::new);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}

	public static void send(UUID uuid, boolean slim) {
		ClientPlayNetworking.send(new SyncDoppelgangerSlimStatusC2SPayload(uuid, slim));
	}

	public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<SyncDoppelgangerSlimStatusC2SPayload> {
		@Override
		public void receive(SyncDoppelgangerSlimStatusC2SPayload payload, ServerPlayNetworking.Context context) {
			DoppelgangerEntity.SLIM_STATUSES.put(payload.uuid(), payload.slim());
		}
	}
}