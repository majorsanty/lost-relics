/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.lostrelics.mixin.tripletoothedsnake;

import moriyashiine.lostrelics.common.init.ModItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	@SuppressWarnings("ConstantValue")
	@Inject(method = "heal", at = @At("HEAD"), cancellable = true)
	private void lostrelics$tripleToothedSnake$preventHealing(float amount, CallbackInfo ci) {
		if (amount > 0 && (Object) this instanceof PlayerEntity player && player.getItemCooldownManager().isCoolingDown(ModItems.TRIPLE_TOOTHED_SNAKE)) {
			ci.cancel();
		}
	}
}
