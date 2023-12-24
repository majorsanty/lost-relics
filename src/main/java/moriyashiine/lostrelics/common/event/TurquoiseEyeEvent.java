/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.lostrelics.common.event;

import moriyashiine.lostrelics.common.LostRelicsUtil;
import moriyashiine.lostrelics.common.init.ModItems;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class TurquoiseEyeEvent implements ServerLivingEntityEvents.AllowDamage {
	@Override
	public boolean allowDamage(LivingEntity entity, DamageSource source, float amount) {
		if (entity.getHealth() == entity.getMaxHealth() && source.getAttacker() instanceof LivingEntity livingAttacker && LostRelicsUtil.applyCooldownAndDamage(livingAttacker, ModItems.TURQUOISE_EYE, 600, 1)) {
			entity.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 100, 1));
			entity.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 100, 1));
		}
		return true;
	}

	public static boolean applyTurquoiseEye(LivingEntity entity, DamageSource source) {
		if (source.getAttacker() instanceof LivingEntity attacker) {
			return applyTurquoiseEye(entity, attacker);
		}
		return false;
	}

	public static boolean applyTurquoiseEye(LivingEntity entity, LivingEntity attacker) {
		return entity.getHealth() == entity.getMaxHealth() && !LostRelicsUtil.isCoolingDown(attacker, ModItems.TURQUOISE_EYE) && LostRelicsUtil.hasTrinket(attacker, ModItems.TURQUOISE_EYE);
	}
}
