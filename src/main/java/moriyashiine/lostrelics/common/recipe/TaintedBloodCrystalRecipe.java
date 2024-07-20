/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.lostrelics.common.recipe;

import moriyashiine.lostrelics.common.init.ModItems;
import moriyashiine.lostrelics.common.init.ModRecipeSerializers;
import moriyashiine.lostrelics.common.item.TripleToothedSnakeItem;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class TaintedBloodCrystalRecipe extends SpecialCraftingRecipe {
	public TaintedBloodCrystalRecipe(CraftingRecipeCategory category) {
		super(category);
	}

	@Override
	public boolean matches(CraftingRecipeInput input, World world) {
		int foundSnakes = 0;
		for (int i = 0; i < input.getSize(); i++) {
			ItemStack stack = input.getStackInSlot(i);
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
	public ItemStack craft(CraftingRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
		for (int i = 0; i < input.getSize(); i++) {
			ItemStack stack = input.getStackInSlot(i);
			if (stack.isOf(ModItems.TRIPLE_TOOTHED_SNAKE)) {
				ItemStack crystal = new ItemStack(ModItems.TAINTED_BLOOD_CRYSTAL, TripleToothedSnakeItem.getCharges(stack));
				crystal.set(DataComponentTypes.POTION_CONTENTS, stack.get(DataComponentTypes.POTION_CONTENTS));
				return crystal;
			}
		}
		return ItemStack.EMPTY;
	}

	@Override
	public DefaultedList<ItemStack> getRemainder(CraftingRecipeInput input) {
		DefaultedList<ItemStack> remainder = DefaultedList.ofSize(input.getSize(), ItemStack.EMPTY);
		for (int i = 0; i < remainder.size(); i++) {
			ItemStack stack = input.getStackInSlot(i);
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
