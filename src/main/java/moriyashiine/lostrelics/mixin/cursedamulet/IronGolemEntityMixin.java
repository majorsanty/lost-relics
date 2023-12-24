/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.lostrelics.mixin.cursedamulet;

import moriyashiine.lostrelics.common.LostRelicsUtil;
import moriyashiine.lostrelics.common.init.ModItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Predicate;

@Mixin(IronGolemEntity.class)
public class IronGolemEntityMixin {
	@ModifyArg(method = "initGoals", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/goal/ActiveTargetGoal;<init>(Lnet/minecraft/entity/mob/MobEntity;Ljava/lang/Class;IZZLjava/util/function/Predicate;)V", ordinal = 0))
	private Predicate<LivingEntity> lostrelics$cursedAmulet$angerGolems(Predicate<LivingEntity> value) {
		return value.or(entity -> LostRelicsUtil.hasTrinket(entity, ModItems.CURSED_AMULET));
	}
}
