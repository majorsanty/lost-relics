/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.lostrelics.mixin.cursedamulet;

import moriyashiine.lostrelics.common.LostRelicsUtil;
import moriyashiine.lostrelics.common.init.ModItems;
import moriyashiine.lostrelics.common.init.ModTags;
import net.minecraft.entity.EntityGroup;
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
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	@ModifyVariable(method = "damage", at = @At("HEAD"), argsOnly = true)
	private float lostrelics$cursedAmulet$fireWeakness(float value, DamageSource source) {
		if (source.isIn(DamageTypeTags.IS_FIRE) && hasCursedAmulet()) {
			return value * 1.5F;
		}
		return value;
	}

	@Inject(method = "canHaveStatusEffect", at = @At("RETURN"), cancellable = true)
	private void lostrelics$cursedAmulet$statusEffectImmunity(StatusEffectInstance effect, CallbackInfoReturnable<Boolean> cir) {
		if (cir.getReturnValueZ() && effect.getEffectType().getCategory() == StatusEffectCategory.HARMFUL && !ModTags.StatusEffectTags.isIn(effect, ModTags.StatusEffectTags.BYPASSES_CURSED_AMULET) && hasCursedAmulet()) {
			cir.setReturnValue(false);
		}
	}

	@Inject(method = "getGroup", at = @At("HEAD"), cancellable = true)
	private void lostrelics$cursedAmulet$undead(CallbackInfoReturnable<EntityGroup> cir) {
		if (hasCursedAmulet()) {
			cir.setReturnValue(EntityGroup.UNDEAD);
		}
	}

	@SuppressWarnings("ConstantValue")
	@Inject(method = "tryUseTotem", at = @At(value = "RETURN", ordinal = 1), cancellable = true)
	private void lostrelics$cursedAmulet$preventDeath(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
		if (!cir.getReturnValueZ() && (Object) this instanceof PlayerEntity player && player.getRandom().nextFloat() < 1 / 3F && !player.getItemCooldownManager().isCoolingDown(ModItems.CURSED_AMULET) && hasCursedAmulet()) {
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
			cir.setReturnValue(true);
		}
	}

	@Unique
	private boolean hasCursedAmulet() {
		return LostRelicsUtil.hasTrinket((LivingEntity) (Object) this, ModItems.CURSED_AMULET);
	}
}
