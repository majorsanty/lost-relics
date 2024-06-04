/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.lostrelics.mixin.tripletoothedsnake;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.lostrelics.common.init.ModDataComponentTypes;
import net.minecraft.component.ComponentHolder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements ComponentHolder {
	@Shadow
	public abstract String getTranslationKey();

	@Shadow
	public abstract boolean isOf(Item item);

	@ModifyReturnValue(method = "getName", at = @At(value = "RETURN", ordinal = 2))
	private Text lostrelics$tripleToothedSnake$taintedBloodCrystalPotionName(Text original) {
		if (contains(ModDataComponentTypes.TAINTED_POTION)) {
			if (isOf(Items.POTION) || isOf(Items.SPLASH_POTION) || isOf(Items.LINGERING_POTION) || isOf(Items.TIPPED_ARROW)) {
				return Text.translatable((getTranslationKey() + ".lostrelics.tainted_potion").replace(".effect.empty", ""));
			}
		}
		return original;
	}
}
