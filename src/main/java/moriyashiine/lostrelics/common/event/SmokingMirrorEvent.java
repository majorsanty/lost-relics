/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.lostrelics.common.event;

import moriyashiine.lostrelics.common.LostRelicsUtil;
import moriyashiine.lostrelics.common.entity.projectile.SmokeBallEntity;
import moriyashiine.lostrelics.common.init.ModItems;
import moriyashiine.lostrelics.common.init.ModSoundEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.registry.tag.DamageTypeTags;

public class SmokingMirrorEvent implements ServerLivingEntityEvents.AllowDamage {
	@Override
	public boolean allowDamage(LivingEntity entity, DamageSource source, float amount) {
		if (entity.getRandom().nextBoolean() && source.isIn(DamageTypeTags.WITCH_RESISTANT_TO) && source.getAttacker() instanceof LivingEntity attacker && LostRelicsUtil.applyCooldownAndDamage(entity, ModItems.SMOKING_MIRROR, 60, 1)) {
			entity.getWorld().spawnEntity(new SmokeBallEntity(entity.getWorld(), entity, attacker, amount * 1.2F));
			entity.getWorld().playSound(null, entity.getBlockPos(), ModSoundEvents.ENTITY_GENERIC_SPAWN, entity.getSoundCategory(), 1, 1);
			return false;
		}
		return true;
	}
}
