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

import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
	@Shadow
	public abstract boolean isIn(TagKey<Item> tag);

	@WrapWithCondition(method = "damage(ILnet/minecraft/server/world/ServerWorld;Lnet/minecraft/server/network/ServerPlayerEntity;Ljava/util/function/Consumer;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;decrement(I)V"))
	private boolean lostrelics$preventRelicBreakingDecrement(ItemStack instance, int amount) {
		return !isIn(ModItemTags.RELICS);
	}

	@WrapWithCondition(method = "damage(ILnet/minecraft/server/world/ServerWorld;Lnet/minecraft/server/network/ServerPlayerEntity;Ljava/util/function/Consumer;)V", at = @At(value = "INVOKE", target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V"))
	private <T> boolean lostrelics$preventRelicBreaking(Consumer<?> instance, T t) {
		return !isIn(ModItemTags.RELICS);
	}
}
