/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.lostrelics.common;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import moriyashiine.lostrelics.common.item.RelicItem;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;

public class LostRelicsUtil {
	public static boolean hasEquippedRelic(LivingEntity entity, Item item) {
		TrinketComponent trinketComponent = TrinketsApi.TRINKET_COMPONENT.get(entity);
		return trinketComponent.isEquipped(foundStack -> foundStack.isOf(item) && RelicItem.isUsable(foundStack));
	}

	public static boolean damageRelic(LivingEntity entity, Item item, int damage) {
		TrinketComponent trinketComponent = TrinketsApi.TRINKET_COMPONENT.get(entity);
		for (Pair<SlotReference, ItemStack> pair : trinketComponent.getEquipped(item)) {
			if (pair.getRight().isOf(item) && damageRelic(entity, pair.getRight(), damage)) {
				return true;
			}
		}
		return false;
	}

	public static boolean damageRelic(LivingEntity entity, ItemStack stack, int damage) {
		if (RelicItem.isUsable(stack, damage - 1)) {
			stack.damage(damage, entity, EquipmentSlot.MAINHAND);
			return true;
		}
		return false;
	}

	public static boolean isCoolingDown(LivingEntity entity, Item item) {
		return entity instanceof PlayerEntity player && player.getItemCooldownManager().isCoolingDown(item);
	}

	public static boolean cooldownAndDamage(LivingEntity entity, Item item, int cooldown, int damage) {
		if (entity instanceof PlayerEntity player) {
			if (player.getItemCooldownManager().isCoolingDown(item)) {
				return false;
			}
			if (damageRelic(entity, item, damage)) {
				player.getItemCooldownManager().set(item, cooldown);
				return true;
			}
			return false;
		}
		return damageRelic(entity, item, damage);
	}

	public static boolean cooldownAndDamage(LivingEntity entity, ItemStack stack, int cooldown, int damage) {
		if (entity instanceof PlayerEntity player) {
			if (player.getItemCooldownManager().isCoolingDown(stack.getItem())) {
				return false;
			}
			if (damageRelic(entity, stack, damage)) {
				player.getItemCooldownManager().set(stack.getItem(), cooldown);
				return true;
			}
			return false;
		}
		return damageRelic(entity, stack, damage);
	}
}
