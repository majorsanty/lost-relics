/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.lostrelics.common.tag;

import moriyashiine.lostrelics.common.LostRelics;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class ModItemTags {
	public static final TagKey<Item> RELICS = TagKey.of(RegistryKeys.ITEM, LostRelics.id("relics"));

	public static final TagKey<Item> JUNGLE_RELICS = TagKey.of(RegistryKeys.ITEM, LostRelics.id("jungle_relics"));
}
