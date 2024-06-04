/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.lostrelics.mixin.turquoiseeye.client;

import moriyashiine.lostrelics.common.event.TurquoiseEyeEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
	@Shadow
	@Nullable
	public ClientPlayerEntity player;

	@Inject(method = "hasOutline", at = @At("HEAD"), cancellable = true)
	private void lostrelics$turquoiseEye$outlineIndicator(Entity entity, CallbackInfoReturnable<Boolean> cir) {
		if (player != null && entity instanceof LivingEntity living && living.distanceTo(player) < 32 && living.canSee(player) && TurquoiseEyeEvent.applyTurquoiseEye(living, player)) {
			cir.setReturnValue(true);
		}
	}
}
