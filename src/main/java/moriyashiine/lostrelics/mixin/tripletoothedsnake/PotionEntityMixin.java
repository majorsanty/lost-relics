/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.lostrelics.mixin.tripletoothedsnake;

import moriyashiine.lostrelics.common.init.ModDataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(PotionEntity.class)
public abstract class PotionEntityMixin extends ThrownItemEntity {
	public PotionEntityMixin(EntityType<? extends ThrownItemEntity> entityType, World world) {
		super(entityType, world);
	}

	@ModifyVariable(method = "applyLingeringPotion", at = @At("HEAD"), argsOnly = true)
	private PotionContentsComponent lostrelics$tripleToothedSnake(PotionContentsComponent value) {
		if (getStack().contains(ModDataComponentTypes.TAINTED_POTION)) {
			PotionContentsComponent reduced = PotionContentsComponent.DEFAULT;
			for (StatusEffectInstance instance : value.getEffects()) {
				reduced = reduced.with(new StatusEffectInstance(instance.getEffectType(), Math.max(1, instance.getDuration() / 4), instance.getAmplifier(), instance.isAmbient(), instance.shouldShowParticles(), instance.shouldShowIcon()));
			}
			return reduced;
		}
		return value;
	}
}
