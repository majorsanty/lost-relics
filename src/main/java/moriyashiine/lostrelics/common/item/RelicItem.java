/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.lostrelics.common.item;

import dev.emi.trinkets.api.TrinketItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Rarity;

import java.util.Arrays;
import java.util.List;

public class RelicItem extends TrinketItem {
	private final List<Item> repairIngredients;

	public RelicItem(Settings settings, Item... repairIngredients) {
		super(settings);
		this.repairIngredients = Arrays.asList(repairIngredients);
	}

	public RelicItem() {
		this(relicSettings());
	}

	public RelicItem(int damage, Item... repairIngredients) {
		this(relicSettings().maxDamage(damage + 1), repairIngredients);
	}

	@Override
	public boolean canRepair(ItemStack stack, ItemStack ingredient) {
		for (Item item : repairIngredients) {
			if (ingredient.isOf(item)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isEnchantable(ItemStack stack) {
		return false;
	}

	public static FabricItemSettings relicSettings() {
		return new FabricItemSettings().fireproof().rarity(Rarity.RARE).maxCount(1);
	}

	public static boolean isUsable(ItemStack stack, int damage) {
		return !stack.isDamageable() || stack.getDamage() + damage < stack.getMaxDamage() - 1;
	}

	public static boolean isUsable(ItemStack stack) {
		return isUsable(stack, 0);
	}
}
