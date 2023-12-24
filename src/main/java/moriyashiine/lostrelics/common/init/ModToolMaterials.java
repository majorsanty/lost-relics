/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.lostrelics.common.init;

import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.fabricmc.yarn.constants.MiningLevels;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

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
			return 1.5F;
		}

		@Override
		public int getMiningLevel() {
			return MiningLevels.IRON;
		}

		@Override
		public int getEnchantability() {
			return 20;
		}

		@Override
		public Ingredient getRepairIngredient() {
			return Ingredient.fromTag(ConventionalItemTags.QUARTZ);
		}
	};
}
