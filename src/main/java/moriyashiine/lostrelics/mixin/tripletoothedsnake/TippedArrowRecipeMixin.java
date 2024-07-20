/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.lostrelics.mixin.tripletoothedsnake;

import moriyashiine.lostrelics.common.init.ModDataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.TippedArrowRecipe;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(TippedArrowRecipe.class)
public class TippedArrowRecipeMixin {
	@Inject(method = "craft(Lnet/minecraft/recipe/input/CraftingRecipeInput;Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;)Lnet/minecraft/item/ItemStack;", at = @At(value = "RETURN", ordinal = 1), locals = LocalCapture.CAPTURE_FAILHARD)
	private void lostrelics$tripleToothedSnake$taintedBloodCrystalPotion(CraftingRecipeInput craftingRecipeInput, RegistryWrapper.WrapperLookup wrapperLookup, CallbackInfoReturnable<ItemStack> cir, ItemStack inventoryStack, ItemStack itemStack2) {
		if (inventoryStack.contains(ModDataComponentTypes.TAINTED_POTION)) {
			cir.getReturnValue().set(ModDataComponentTypes.TAINTED_POTION, true);
		}
	}
}
