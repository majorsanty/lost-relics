/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.lostrelics.common.init;

import com.mojang.serialization.Codec;
import moriyashiine.lostrelics.common.LostRelics;
import net.minecraft.component.ComponentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.dynamic.Codecs;

public class ModDataComponentTypes {
	public static final ComponentType<Boolean> HIDE_SKELETON = new ComponentType.Builder<Boolean>().codec(Codec.BOOL).packetCodec(PacketCodecs.BOOL).build();
	public static final ComponentType<Boolean> TAINTED_POTION = new ComponentType.Builder<Boolean>().codec(Codec.BOOL).packetCodec(PacketCodecs.BOOL).build();
	public static final ComponentType<Integer> SNAKE_CHARGE = new ComponentType.Builder<Integer>().codec(Codecs.POSITIVE_INT).packetCodec(PacketCodecs.VAR_INT).build();

	public static void init() {
		Registry.register(Registries.DATA_COMPONENT_TYPE, LostRelics.id("hide_skeleton"), HIDE_SKELETON);
		Registry.register(Registries.DATA_COMPONENT_TYPE, LostRelics.id("tainted_potion"), TAINTED_POTION);
		Registry.register(Registries.DATA_COMPONENT_TYPE, LostRelics.id("snake_charge"), SNAKE_CHARGE);
	}
}
