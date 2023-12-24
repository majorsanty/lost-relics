/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.lostrelics.common.recipe;

import moriyashiine.lostrelics.common.init.ModItems;
import moriyashiine.lostrelics.common.init.ModRecipeSerializers;
import moriyashiine.lostrelics.common.item.TripleToothedSnakeItem;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtil;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class TaintedBloodCrystalRecipe extends SpecialCraftingRecipe {
	public TaintedBloodCrystalRecipe(Identifier id, CraftingRecipeCategory category) {
		super(id, category);
	}

	@Override
	public boolean matches(RecipeInputInventory inventory, World world) {
		int foundSnakes = 0;
		for (int i = 0; i < inventory.size(); i++) {
			ItemStack stack = inventory.getStack(i);
			if (stack.isOf(ModItems.TRIPLE_TOOTHED_SNAKE)) {
				if (TripleToothedSnakeItem.getCharges(stack) > 0) {
					foundSnakes++;
				}
			} else if (!stack.isEmpty()) {
				return false;
			}
		}
		return foundSnakes == 1;
	}

	@Override
	public ItemStack craft(RecipeInputInventory inventory, DynamicRegistryManager registryManager) {
		for (int i = 0; i < inventory.size(); i++) {
			ItemStack stack = inventory.getStack(i);
			if (stack.isOf(ModItems.TRIPLE_TOOTHED_SNAKE)) {
				ItemStack crystal = new ItemStack(ModItems.TAINTED_BLOOD_CRYSTAL, TripleToothedSnakeItem.getCharges(stack));
				PotionUtil.setCustomPotionEffects(crystal, PotionUtil.getCustomPotionEffects(stack));
				return crystal;
			}
		}
		return ItemStack.EMPTY;
	}

	@Override
	public DefaultedList<ItemStack> getRemainder(RecipeInputInventory inventory) {
		DefaultedList<ItemStack> remainder = DefaultedList.ofSize(inventory.size(), ItemStack.EMPTY);
		for (int i = 0; i < remainder.size(); i++) {
			ItemStack stack = inventory.getStack(i);
			TripleToothedSnakeItem.setCharges(stack, 0);
			remainder.set(i, stack.copy());
		}
		return remainder;
	}

	@Override
	public boolean fits(int width, int height) {
		return width + height == 1;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ModRecipeSerializers.TAINTED_BLOOD_CRYSTAL;
	}
}
