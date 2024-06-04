/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.lostrelics.common.init;

import moriyashiine.lostrelics.common.LostRelics;
import moriyashiine.lostrelics.common.item.*;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Rarity;

public class ModItems {
	public static ItemGroup GROUP;

	public static final Item CURSED_AMULET = new CursedAmuletItem();
	public static final Item SMOKING_MIRROR = new SmokingMirrorItem();
	public static final Item TRIPLE_TOOTHED_SNAKE = new TripleToothedSnakeItem();
	public static final Item TAINTED_BLOOD_CRYSTAL = new TaintedBloodCrystalItem(new Item.Settings().fireproof().rarity(Rarity.UNCOMMON));
	public static final Item TURQUOISE_EYE = new RelicItem(104, Items.SKELETON_SKULL, Items.WITHER_SKELETON_SKULL);

	public static void init() {
		GROUP = FabricItemGroup.builder().displayName(Text.translatable("itemGroup." + LostRelics.MOD_ID)).icon(CURSED_AMULET::getDefaultStack).entries((displayContext, entries) -> {
			entries.add(CURSED_AMULET);
			entries.add(SMOKING_MIRROR);
			entries.add(TRIPLE_TOOTHED_SNAKE);
			entries.add(TAINTED_BLOOD_CRYSTAL);
			entries.add(TURQUOISE_EYE);
		}).build();
		Registry.register(Registries.ITEM_GROUP, LostRelics.id(LostRelics.MOD_ID), GROUP);
		Registry.register(Registries.ITEM, LostRelics.id("cursed_amulet"), CURSED_AMULET);
		Registry.register(Registries.ITEM, LostRelics.id("smoking_mirror"), SMOKING_MIRROR);
		Registry.register(Registries.ITEM, LostRelics.id("triple_toothed_snake"), TRIPLE_TOOTHED_SNAKE);
		Registry.register(Registries.ITEM, LostRelics.id("tainted_blood_crystal"), TAINTED_BLOOD_CRYSTAL);
		Registry.register(Registries.ITEM, LostRelics.id("turquoise_eye"), TURQUOISE_EYE);
	}
}
