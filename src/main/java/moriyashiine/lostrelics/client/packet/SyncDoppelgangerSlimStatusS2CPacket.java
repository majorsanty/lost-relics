/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.lostrelics.client.packet;

import io.netty.buffer.Unpooled;
import moriyashiine.lostrelics.common.LostRelics;
import moriyashiine.lostrelics.common.entity.DoppelgangerEntity;
import moriyashiine.lostrelics.common.packet.SyncDoppelgangerSlimStatusC2SPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.UUID;

public class SyncDoppelgangerSlimStatusS2CPacket {
	public static final Identifier ID = LostRelics.id("sync_doppelganger_slim_status_s2c");

	public static void send(ServerPlayerEntity player, DoppelgangerEntity entity) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeUuid(entity.getUuid());
		ServerPlayNetworking.send(player, ID, buf);
	}

	public static class Receiver implements ClientPlayNetworking.PlayChannelHandler {
		@Override
		public void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
			UUID uuid = buf.readUuid();
			client.execute(() -> {
				if (client.player != null) {
					SyncDoppelgangerSlimStatusC2SPacket.send(uuid, client.player.getModel().equals("slim"));
				}
			});
		}
	}
}