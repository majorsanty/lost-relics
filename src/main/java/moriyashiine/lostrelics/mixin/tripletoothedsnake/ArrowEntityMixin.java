/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.lostrelics.mixin.tripletoothedsnake;

import moriyashiine.lostrelics.common.init.ModDataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ArrowEntity.class)
public abstract class ArrowEntityMixin extends PersistentProjectileEntity {
	protected ArrowEntityMixin(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
		super(entityType, world);
	}

	@ModifyVariable(method = "onHit", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/entity/projectile/ArrowEntity;getPotionContents()Lnet/minecraft/component/type/PotionContentsComponent;"))
	private PotionContentsComponent lostrelics$tripleToothedSnake(PotionContentsComponent value) {
		if (asItemStack().contains(ModDataComponentTypes.TAINTED_POTION)) {
			PotionContentsComponent reduced = PotionContentsComponent.DEFAULT;
			for (StatusEffectInstance instance : value.getEffects()) {
				reduced = reduced.with(new StatusEffectInstance(instance.getEffectType(), Math.max(1, instance.getDuration() / 8), instance.getAmplifier(), instance.isAmbient(), instance.shouldShowParticles(), instance.shouldShowIcon()));
			}
			return reduced;
		}
		return value;
	}
}
