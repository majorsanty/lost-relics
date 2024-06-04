/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.lostrelics.data.provider;

import moriyashiine.lostrelics.common.init.ModItems;
import moriyashiine.lostrelics.common.tag.ModItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagProvider.ItemTagProvider {
	public ModItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
		super(output, completableFuture);
	}

	@Override
	protected void configure(RegistryWrapper.WrapperLookup arg) {
		getOrCreateTagBuilder(ModItemTags.RELICS)
				.addTag(ModItemTags.JUNGLE_RELICS);
		getOrCreateTagBuilder(ModItemTags.JUNGLE_RELICS)
				.add(ModItems.CURSED_AMULET)
				.add(ModItems.SMOKING_MIRROR)
				.add(ModItems.TRIPLE_TOOTHED_SNAKE)
				.add(ModItems.TURQUOISE_EYE);

		getOrCreateTagBuilder(ItemTags.ARROWS)
				.add(ModItems.TAINTED_BLOOD_CRYSTAL);
		getOrCreateTagBuilder(ItemTags.SWORDS)
				.add(ModItems.TRIPLE_TOOTHED_SNAKE);

		getOrCreateTagBuilder(TagKey.of(RegistryKeys.ITEM, Identifier.of("trinkets", "head/face")))
				.add(ModItems.TURQUOISE_EYE);
		getOrCreateTagBuilder(TagKey.of(RegistryKeys.ITEM, Identifier.of("trinkets", "chest/necklace")))
				.add(ModItems.CURSED_AMULET).add(ModItems.SMOKING_MIRROR);
	}
}
