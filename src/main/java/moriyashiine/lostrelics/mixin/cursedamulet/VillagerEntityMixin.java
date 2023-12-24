/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.lostrelics.mixin.cursedamulet;

import moriyashiine.lostrelics.common.LostRelicsUtil;
import moriyashiine.lostrelics.common.init.ModItems;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VillagerEntity.class)
public class VillagerEntityMixin {
	@Inject(method = "getReputation", at = @At("RETURN"), cancellable = true)
	private void lostrelics$cursedAmulet$villagerReputation(PlayerEntity player, CallbackInfoReturnable<Integer> cir) {
		if (LostRelicsUtil.hasTrinket(player, ModItems.CURSED_AMULET)) {
			cir.setReturnValue(cir.getReturnValueI() - 128);
		}
	}
}
