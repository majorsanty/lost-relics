/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.lostrelics.mixin.cursedamulet;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.lostrelics.common.LostRelicsUtil;
import moriyashiine.lostrelics.common.init.ModItems;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(VillagerEntity.class)
public class VillagerEntityMixin {
	@ModifyReturnValue(method = "getReputation", at = @At("RETURN"))
	private int lostrelics$cursedAmulet(int original, PlayerEntity player) {
		if (LostRelicsUtil.hasEquippedRelic(player, ModItems.CURSED_AMULET)) {
			return original - 128;
		}
		return original;
	}
}
