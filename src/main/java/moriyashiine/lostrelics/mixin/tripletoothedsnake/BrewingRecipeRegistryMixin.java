/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.lostrelics.mixin.tripletoothedsnake;

import moriyashiine.lostrelics.common.LostRelics;
import moriyashiine.lostrelics.common.init.ModItems;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.BrewingRecipeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(BrewingRecipeRegistry.class)
public class BrewingRecipeRegistryMixin {
	@Inject(method = "craft", at = @At("RETURN"), cancellable = true)
	private static void lostrelics$tripleToothedSnake$taintedBloodCrystalBrewing(ItemStack ingredient, ItemStack input, CallbackInfoReturnable<ItemStack> cir) {
		if (input.getItem() instanceof PotionItem potionItem && PotionUtil.getPotion(input) == Potions.AWKWARD && ingredient.isOf(ModItems.TAINTED_BLOOD_CRYSTAL)) {
			List<StatusEffectInstance> effects = PotionUtil.getCustomPotionEffects(ingredient);
			ItemStack potion = PotionUtil.setCustomPotionEffects(new ItemStack(potionItem), effects);
			potion.getOrCreateNbt().putInt(PotionUtil.CUSTOM_POTION_COLOR_KEY, PotionUtil.getColor(effects));
			potion.getOrCreateSubNbt(LostRelics.MOD_ID).putBoolean("SpecialPotion", true);
			cir.setReturnValue(potion);
		}
	}

	@Inject(method = "hasPotionRecipe", at = @At("RETURN"), cancellable = true)
	private static void lostrelics$tripleToothedSnake$allowTaintedBloodCrystal(ItemStack input, ItemStack ingredient, CallbackInfoReturnable<Boolean> cir) {
		if (!cir.getReturnValueZ() && PotionUtil.getPotion(input) == Potions.AWKWARD && ingredient.isOf(ModItems.TAINTED_BLOOD_CRYSTAL)) {
			cir.setReturnValue(true);
		}
	}

	@Inject(method = "isPotionRecipeIngredient", at = @At("RETURN"), cancellable = true)
	private static void lostrelics$tripleToothedSnake$allowTaintedBloodCrystal(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		if (!cir.getReturnValueZ() && stack.isOf(ModItems.TAINTED_BLOOD_CRYSTAL)) {
			cir.setReturnValue(true);
		}
	}
}
