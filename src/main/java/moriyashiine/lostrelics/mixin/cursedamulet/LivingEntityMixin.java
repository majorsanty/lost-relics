/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.lostrelics.mixin.cursedamulet;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.lostrelics.common.LostRelicsUtil;
import moriyashiine.lostrelics.common.init.ModItems;
import moriyashiine.lostrelics.common.tag.ModStatusEffectTags;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	@ModifyVariable(method = "damage", at = @At("HEAD"), argsOnly = true)
	private float lostrelics$cursedAmulet$fireWeakness(float value, DamageSource source) {
		if (source.isIn(DamageTypeTags.IS_FIRE) && hasCursedAmulet()) {
			return value * 1.5F;
		}
		return value;
	}

	@ModifyReturnValue(method = "canBreatheInWater", at = @At("RETURN"))
	private boolean lostrelics$cursedAmulet$breatheUnderwater(boolean original) {
		return original || hasCursedAmulet();
	}

	@ModifyExpressionValue(method = "canHaveStatusEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityType;isIn(Lnet/minecraft/registry/tag/TagKey;)Z", ordinal = 2))
	private boolean lostrelics$cursedAmulet$statusEffectImmunity(boolean original, StatusEffectInstance effect) {
		return original || (effect.getEffectType().value().getCategory() == StatusEffectCategory.HARMFUL && !effect.getEffectType().isIn(ModStatusEffectTags.BYPASSES_CURSED_AMULET) && hasCursedAmulet());
	}

	@ModifyReturnValue(method = "hasInvertedHealingAndHarm", at = @At("RETURN"))
	private boolean lostrelics$cursedAmulet$invertedHealingAndHarm(boolean original) {
		return original || hasCursedAmulet();
	}

	@ModifyReturnValue(method = "tryUseTotem", at = @At(value = "RETURN", ordinal = 1))
	private boolean lostrelics$cursedAmulet$preventDeath(boolean original) {
		if (!original && (Object) this instanceof PlayerEntity player && player.getRandom().nextFloat() < 1 / 3F && !player.getItemCooldownManager().isCoolingDown(ModItems.CURSED_AMULET) && hasCursedAmulet()) {
			if (player instanceof ServerPlayerEntity serverPlayer) {
				serverPlayer.incrementStat(Stats.USED.getOrCreateStat(ModItems.CURSED_AMULET));
			}
			player.setHealth(1);
			player.clearStatusEffects();
			player.addStatusEffect(new StatusEffectInstance(net.minecraft.entity.effect.StatusEffects.REGENERATION, 900, 1));
			player.addStatusEffect(new StatusEffectInstance(net.minecraft.entity.effect.StatusEffects.ABSORPTION, 100, 1));
			player.addStatusEffect(new StatusEffectInstance(net.minecraft.entity.effect.StatusEffects.FIRE_RESISTANCE, 800));
			player.addStatusEffect(new StatusEffectInstance(net.minecraft.entity.effect.StatusEffects.HUNGER, 1600, 1));
			player.addStatusEffect(new StatusEffectInstance(net.minecraft.entity.effect.StatusEffects.SLOWNESS, 1600, 1));
			player.addStatusEffect(new StatusEffectInstance(net.minecraft.entity.effect.StatusEffects.WEAKNESS, 1600));
			player.getWorld().sendEntityStatus(player, EntityStatuses.USE_TOTEM_OF_UNDYING);
			player.getItemCooldownManager().set(ModItems.CURSED_AMULET, 6000);
			return true;
		}
		return original;
	}

	@Unique
	private boolean hasCursedAmulet() {
		return LostRelicsUtil.hasEquippedRelic((LivingEntity) (Object) this, ModItems.CURSED_AMULET);
	}
}
