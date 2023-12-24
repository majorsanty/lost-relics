/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.lostrelics.common;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketsApi;
import moriyashiine.lostrelics.common.item.RelicItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;

public class LostRelicsUtil {
	public static boolean hasTrinket(LivingEntity entity, Item item) {
		boolean[] equipped = {false};
		TrinketsApi.getTrinketComponent(entity).ifPresent(trinketComponent -> {
			if (trinketComponent.isEquipped(stack -> stack.isOf(item) && RelicItem.isUsable(stack))) {
				equipped[0] = true;
			}
		});
		return equipped[0];
	}

	public static boolean damageTrinket(LivingEntity entity, Item item, int damage) {
		boolean[] damaged = {false};
		TrinketsApi.getTrinketComponent(entity).ifPresent(trinketComponent -> {
			for (Pair<SlotReference, ItemStack> pair : trinketComponent.getEquipped(item)) {
				if (pair.getRight().isOf(item) && damageTrinket(entity, pair.getRight(), damage)) {
					damaged[0] = true;
					break;
				}
			}
		});
		return damaged[0];
	}

	public static boolean damageTrinket(LivingEntity entity, ItemStack stack, int damage) {
		if (RelicItem.isUsable(stack, damage - 1)) {
			stack.damage(damage, entity, stackUser -> {
			});
			return true;
		}
		return false;
	}

	public static boolean isCoolingDown(LivingEntity entity, Item item) {
		return entity instanceof PlayerEntity player && player.getItemCooldownManager().isCoolingDown(item);
	}

	public static boolean applyCooldownAndDamage(LivingEntity entity, Item item, int cooldown, int damage) {
		if (entity instanceof PlayerEntity player) {
			if (player.getItemCooldownManager().isCoolingDown(item)) {
				return false;
			}
			if (damageTrinket(entity, item, damage)) {
				player.getItemCooldownManager().set(item, cooldown);
				return true;
			}
			return false;
		}
		return damageTrinket(entity, item, damage);
	}

	public static boolean applyCooldownAndDamage(LivingEntity entity, ItemStack stack, int cooldown, int damage) {
		if (entity instanceof PlayerEntity player) {
			if (player.getItemCooldownManager().isCoolingDown(stack.getItem())) {
				return false;
			}
			if (damageTrinket(entity, stack, damage)) {
				player.getItemCooldownManager().set(stack.getItem(), cooldown);
				return true;
			}
			return false;
		}
		return damageTrinket(entity, stack, damage);
	}
}
