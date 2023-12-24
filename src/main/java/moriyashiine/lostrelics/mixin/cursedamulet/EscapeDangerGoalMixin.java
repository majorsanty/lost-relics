/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.lostrelics.mixin.cursedamulet;

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
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EscapeDangerGoal.class)
public class EscapeDangerGoalMixin {
	@Shadow
	@Final
	protected PathAwareEntity mob;

	@Inject(method = "isInDanger", at = @At("RETURN"), cancellable = true)
	private void lostrelics$cursedAmulet$fear(CallbackInfoReturnable<Boolean> cir) {
		if (!cir.getReturnValueZ() && !mob.getWorld().getEntitiesByClass(LivingEntity.class, new Box(mob.getBlockPos()).expand(6), foundEntity -> LostRelicsUtil.hasTrinket(foundEntity, ModItems.CURSED_AMULET)).isEmpty()) {
			cir.setReturnValue(true);
		}
	}
}
