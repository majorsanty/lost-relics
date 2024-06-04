/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.lostrelics.mixin.tripletoothedsnake;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.lostrelics.common.init.ModDataComponentTypes;
import moriyashiine.lostrelics.common.init.ModItems;
import moriyashiine.lostrelics.common.item.TripleToothedSnakeItem;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PotionItem;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.BrewingRecipeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BrewingRecipeRegistry.class)
public class BrewingRecipeRegistryMixin {
	@ModifyReturnValue(method = "craft", at = @At("RETURN"))
	private ItemStack lostrelics$tripleToothedSnake$taintedBloodCrystalBrewing(ItemStack original, ItemStack ingredient, ItemStack input) {
		if (input.contains(ModDataComponentTypes.TAINTED_POTION)) {
			if (input.isOf(Items.POTION) && ingredient.isOf(Items.GUNPOWDER)) {
				return input.copyComponentsToNewStack(Items.SPLASH_POTION, 1);
			} else if (input.isOf(Items.SPLASH_POTION) && ingredient.isOf(Items.DRAGON_BREATH)) {
				return input.copyComponentsToNewStack(Items.LINGERING_POTION, 1);
			}
		}
		if (input.getItem() instanceof PotionItem potionItem && isAwkward(input) && ingredient.isOf(ModItems.TAINTED_BLOOD_CRYSTAL)) {
			ItemStack potionStack = potionItem.getDefaultStack();
			potionStack.set(DataComponentTypes.POTION_CONTENTS, TripleToothedSnakeItem.create(ingredient.get(DataComponentTypes.POTION_CONTENTS).getEffects()));
			potionStack.set(ModDataComponentTypes.TAINTED_POTION, true);
			return potionStack;
		}
		return original;
	}

	@ModifyReturnValue(method = "hasPotionRecipe", at = @At("RETURN"))
	private boolean lostrelics$tripleToothedSnake$allowTaintedBloodCrystal(boolean original, ItemStack input, ItemStack ingredient) {
		if (input.contains(ModDataComponentTypes.TAINTED_POTION)) {
			return false;
		}
		return original || (isAwkward(input) && ingredient.isOf(ModItems.TAINTED_BLOOD_CRYSTAL));
	}

	@ModifyReturnValue(method = "isPotionRecipeIngredient", at = @At("RETURN"))
	private boolean lostrelics$tripleToothedSnake$allowTaintedBloodCrystal(boolean original, ItemStack stack) {
		return original || (stack.isOf(ModItems.TAINTED_BLOOD_CRYSTAL) && stack.getOrDefault(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT).hasEffects());
	}

	@Unique
	private static boolean isAwkward(ItemStack stack) {
		return stack.getOrDefault(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT).potion().orElse(null) == Potions.AWKWARD;
	}
}
