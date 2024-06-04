/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.lostrelics.common.init;

import moriyashiine.lostrelics.common.LostRelics;
import moriyashiine.lostrelics.common.blockentity.AltarBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.datafixer.TypeReferences;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Util;

public class ModBlockEntityTypes {
	public static final BlockEntityType<AltarBlockEntity> ALTAR = BlockEntityType.Builder.create(AltarBlockEntity::new, ModBlocks.JUNGLE_ALTAR).build(Util.getChoiceType(TypeReferences.BLOCK_ENTITY, LostRelics.id("altar").toString()));

	public static void init() {
		Registry.register(Registries.BLOCK_ENTITY_TYPE, LostRelics.id("altar"), ALTAR);
	}
}
