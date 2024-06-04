/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.lostrelics.mixin.cursedamulet;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.lostrelics.common.LostRelicsUtil;
import moriyashiine.lostrelics.common.init.ModItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.EscapeDangerGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EscapeDangerGoal.class)
public class EscapeDangerGoalMixin {
	@Shadow
	@Final
	protected PathAwareEntity mob;

	@ModifyReturnValue(method = "isInDanger", at = @At("RETURN"))
	private boolean lostrelics$cursedAmulet$fear(boolean original) {
		return original || !mob.getWorld().getEntitiesByClass(LivingEntity.class, new Box(mob.getBlockPos()).expand(6), foundEntity -> LostRelicsUtil.hasEquippedRelic(foundEntity, ModItems.CURSED_AMULET)).isEmpty();
	}
}
