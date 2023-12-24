/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.lostrelics.mixin.tripletoothedsnake;

import moriyashiine.lostrelics.common.LostRelics;
import moriyashiine.lostrelics.common.item.TaintedBloodCrystalItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
	@Shadow
	@Nullable
	public abstract NbtCompound getSubNbt(String key);

	@Shadow
	public abstract boolean isOf(Item item);

	@Shadow
	public abstract String getTranslationKey();

	@Inject(method = "getName", at = @At(value = "RETURN", ordinal = 1), cancellable = true)
	private void lostrelics$tripleToothedSnake$taintedBloodCrystalPotionName(CallbackInfoReturnable<Text> cir) {
		if (TaintedBloodCrystalItem.isSpecialPotion(getSubNbt(LostRelics.MOD_ID))) {
			if (isOf(Items.POTION) || isOf(Items.SPLASH_POTION) || isOf(Items.LINGERING_POTION) || isOf(Items.TIPPED_ARROW)) {
				cir.setReturnValue(Text.translatable((getTranslationKey() + ".lostrelics.specialpotion").replace(".effect.empty", "")));
			}
		}
	}
}
