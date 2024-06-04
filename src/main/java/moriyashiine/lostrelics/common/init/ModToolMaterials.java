/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.lostrelics.common.init;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.block.Block;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;

public class ModToolMaterials {
	public static final ToolMaterial TRIPLE_TOOTHED_SNAKE = new ToolMaterial() {
		@Override
		public int getDurability() {
			return 53;
		}

		@Override
		public float getMiningSpeedMultiplier() {
			return 6;
		}

		@Override
		public float getAttackDamage() {
			return 0;
		}

		@Override
		public TagKey<Block> getInverseTag() {
			return BlockTags.INCORRECT_FOR_STONE_TOOL;
		}

		@Override
		public int getEnchantability() {
			return 20;
		}

		@Override
		public Ingredient getRepairIngredient() {
			return Ingredient.fromTag(ConventionalItemTags.QUARTZ_GEMS);
		}
	};
}
