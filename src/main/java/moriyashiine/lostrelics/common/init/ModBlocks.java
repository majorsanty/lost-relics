/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.lostrelics.common.init;

import moriyashiine.lostrelics.common.LostRelics;
import moriyashiine.lostrelics.common.block.AltarBlock;
import moriyashiine.lostrelics.common.tag.ModItemTag;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import static net.minecraft.block.AbstractBlock.Settings.copy;

public class ModBlocks {
    public static Block JUNGLE_ALTAR = new AltarBlock(copy(Blocks.OBSIDIAN), ModItemTag.JUNGLE_RELICS);

    public static void init() {
        Registry.register(Registries.BLOCK, LostRelics.id("jungle_altar"), JUNGLE_ALTAR);
    }
}
