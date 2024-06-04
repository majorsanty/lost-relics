/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.lostrelics.mixin.util;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import moriyashiine.lostrelics.common.tag.ModItemTags;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
	@Shadow
	public abstract boolean isIn(TagKey<Item> tag);

	@WrapWithCondition(method = "damage(ILnet/minecraft/util/math/random/Random;Lnet/minecraft/server/network/ServerPlayerEntity;Ljava/lang/Runnable;)V", at = @At(value = "INVOKE", target = "Ljava/lang/Runnable;run()V"))
	private boolean lostrelics$preventRelicBreaking(Runnable instance) {
		return !isIn(ModItemTags.RELICS);
	}
}
