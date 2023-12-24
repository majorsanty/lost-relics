/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.lostrelics.mixin.tripletoothedsnake;

import moriyashiine.lostrelics.common.item.TaintedBloodCrystalItem;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PotionEntity.class)
public class PotionEntityMixin {
	@Unique
	private boolean specialPotion = false;

	@Inject(method = "applyLingeringPotion", at = @At("HEAD"))
	private void lostrelics$tripleToothedSnake$lingeringDurationFix(ItemStack stack, Potion potion, CallbackInfo ci) {
		specialPotion = TaintedBloodCrystalItem.isSpecialPotion(stack);
	}

	@ModifyArg(method = "applyLingeringPotion", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/AreaEffectCloudEntity;addEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;)V"))
	private StatusEffectInstance lostrelics$tripleToothedSnake$lingeringDurationFix(StatusEffectInstance value) {
		if (specialPotion) {
			return new StatusEffectInstance(value.getEffectType(), value.getDuration() / 4, value.getAmplifier(), value.isAmbient(), value.shouldShowParticles(), value.shouldShowIcon());
		}
		return value;
	}
}
