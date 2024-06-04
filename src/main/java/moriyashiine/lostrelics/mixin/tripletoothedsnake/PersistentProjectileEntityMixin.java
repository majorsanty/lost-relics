/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.lostrelics.mixin.tripletoothedsnake;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import moriyashiine.lostrelics.common.entity.projectile.TaintedBloodCrystalEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PersistentProjectileEntity.class)
public class PersistentProjectileEntityMixin {
	@WrapWithCondition(method = "onEntityHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;setStuckArrowCount(I)V"))
	private boolean lostrelics$tripleToothedSnake$taintedBloodShardCondition(LivingEntity instance, int stuckArrowCount) {
		return !((Object) this instanceof TaintedBloodCrystalEntity);
	}
}
