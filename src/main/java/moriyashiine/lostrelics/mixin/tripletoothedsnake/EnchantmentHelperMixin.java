/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.lostrelics.mixin.tripletoothedsnake;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.lostrelics.common.init.ModItems;
import moriyashiine.lostrelics.common.item.RelicItem;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
	@ModifyReturnValue(method = "getAttackDamage", at = @At("RETURN"))
	private static float lostrelics$tripleToothedSnake(float original, ItemStack stack) {
		if (stack.isOf(ModItems.TRIPLE_TOOTHED_SNAKE) && !RelicItem.isUsable(stack)) {
			return original - 5;
		}
		return original;
	}
}
