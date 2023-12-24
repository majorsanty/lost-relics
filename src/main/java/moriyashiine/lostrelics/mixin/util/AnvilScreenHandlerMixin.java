/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.lostrelics.mixin.util;

import moriyashiine.lostrelics.common.init.ModTags;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.*;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilScreenHandler.class)
public abstract class AnvilScreenHandlerMixin extends ForgingScreenHandler {
	@Shadow
	@Final
	private Property levelCost;

	public AnvilScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
		super(type, syncId, playerInventory, context);
	}

	@Inject(method = "updateResult", at = @At("TAIL"))
	private void lostrelics$util$fixedRelicRepairCost(CallbackInfo ci) {
		ItemStack slot0 = input.getStack(0);
		if (slot0.isIn(ModTags.ItemTags.RELICS) && slot0.getItem().canRepair(slot0, input.getStack(1))) {
			output.getStack(0).setDamage(0);
			levelCost.set(10);
		}
	}
}
