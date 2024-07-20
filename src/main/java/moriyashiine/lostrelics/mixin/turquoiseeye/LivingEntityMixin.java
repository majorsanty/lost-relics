/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.lostrelics.mixin.turquoiseeye;

import moriyashiine.lostrelics.common.event.TurquoiseEyeEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@ModifyVariable(method = "damage", at = @At("HEAD"), argsOnly = true)
	private DamageSource lostrelics$turquoiseEye$magicDamage(DamageSource value) {
		if (TurquoiseEyeEvent.applyTurquoiseEye((LivingEntity) (Object) this, value) && getRandom().nextFloat() < 0.25F) {
			return getWorld().getDamageSources().indirectMagic(value.getSource(), value.getAttacker());
		}
		return value;
	}

	@ModifyVariable(method = "damage", at = @At("HEAD"), argsOnly = true)
	private float lostrelics$turquoiseEye$damageMultiplier(float value, DamageSource source) {
		if (TurquoiseEyeEvent.applyTurquoiseEye((LivingEntity) (Object) this, source)) {
			return value * 1.5F;
		}
		return value;
	}
}
