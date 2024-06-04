/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.lostrelics.mixin.cursedamulet;

import moriyashiine.lostrelics.common.LostRelicsUtil;
import moriyashiine.lostrelics.common.init.ModItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.registry.tag.EntityTypeTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.function.Predicate;

@Mixin(ActiveTargetGoal.class)
public class ActiveTargetGoalMixin {
	@ModifyVariable(method = "<init>(Lnet/minecraft/entity/mob/MobEntity;Ljava/lang/Class;IZZLjava/util/function/Predicate;)V", at = @At("HEAD"), argsOnly = true)
	private static Predicate<LivingEntity> lostrelics$cursedAmulet$undeadNeutrality(Predicate<LivingEntity> value, MobEntity mob) {
		if (mob.getType().isIn(EntityTypeTags.UNDEAD)) {
			Predicate<LivingEntity> notWearingCursedAmulet = entity -> !LostRelicsUtil.hasEquippedRelic(entity, ModItems.CURSED_AMULET);
			if (value == null) {
				return notWearingCursedAmulet;
			}
			return value.and(notWearingCursedAmulet);
		}
		return value;
	}
}
