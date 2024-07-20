/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.lostrelics.common.item;

import dev.emi.trinkets.api.SlotReference;
import moriyashiine.lostrelics.common.LostRelics;
import moriyashiine.lostrelics.common.init.ModDataComponentTypes;
import moriyashiine.lostrelics.common.init.ModEntityComponents;
import moriyashiine.lostrelics.common.init.ModSoundEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class CursedAmuletItem extends RelicItem {
	public static final Map<RegistryEntry<EntityAttribute>, EntityAttributeModifier> GOOD_MODIFIERS, BAD_MODIFIERS;

	static {
		GOOD_MODIFIERS = new HashMap<>();
		GOOD_MODIFIERS.put(EntityAttributes.GENERIC_ARMOR, new EntityAttributeModifier(LostRelics.id("cursed_amulet_good_armor"), 4, EntityAttributeModifier.Operation.ADD_VALUE));
		GOOD_MODIFIERS.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(LostRelics.id("cursed_amulet_good_attack_damage"), 3, EntityAttributeModifier.Operation.ADD_VALUE));

		BAD_MODIFIERS = new HashMap<>();
		BAD_MODIFIERS.put(EntityAttributes.GENERIC_ARMOR, new EntityAttributeModifier(LostRelics.id("cursed_amulet_bad_armor"), -4, EntityAttributeModifier.Operation.ADD_VALUE));
		BAD_MODIFIERS.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(LostRelics.id("cursed_amulet_bad_attack_damage"), -3, EntityAttributeModifier.Operation.ADD_VALUE));
	}

	public CursedAmuletItem() {
		super();
	}

	@Override
	public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
		if (entity.age % 20 == 0) {
			updateModifiers(entity, false);
		}
	}

	@Override
	public void onEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
		updateModifiers(entity, false);
		ModEntityComponents.CURSED_AMULET.maybeGet(entity).ifPresent(cursedAmuletComponent -> cursedAmuletComponent.toggleTransform(!shouldHideSkeleton(stack)));
	}

	@Override
	public void onUnequip(ItemStack stack, SlotReference slot, LivingEntity entity) {
		updateModifiers(entity, true);
		ModEntityComponents.CURSED_AMULET.maybeGet(entity).ifPresent(cursedAmuletComponent -> cursedAmuletComponent.toggleTransform(false));
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		if (user.isSneaking()) {
			ItemStack stack = user.getStackInHand(hand);
			if (!world.isClient) {
				toggleSkeleton(stack, hideSkeleton -> user.getWorld().playSound(null, user.getBlockPos(), ModSoundEvents.ITEM_RELIC_TOGGLE, user.getSoundCategory(), 1, hideSkeleton ? 1 : 0.5F));
			}
			return TypedActionResult.success(stack, world.isClient);
		}
		return super.use(world, user, hand);
	}

	@Override
	public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
		if (clickType == ClickType.RIGHT && cursorStackReference.get().isEmpty()) {
			toggleSkeleton(stack, hideSkeleton -> player.playSound(ModSoundEvents.ITEM_RELIC_TOGGLE, 1, hideSkeleton ? 1 : 0.5F));
			return true;
		}
		return false;
	}

	@Override
	public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
		tooltip.add(Text.translatable("tooltip." + LostRelics.MOD_ID + ".hide_skeleton", shouldHideSkeleton(stack)).formatted(Formatting.DARK_GRAY));
		GOOD_MODIFIERS.forEach((attribute, modifier) -> stack.appendAttributeModifierTooltip(text -> tooltip.add(text.copy().append("?").formatted(Formatting.LIGHT_PURPLE)), null, attribute, modifier));
	}

	private void updateModifiers(LivingEntity entity, boolean remove) {
		if (entity.getWorld().isClient) {
			return;
		}
		boolean applyNegative = entity.getWorld().isDay() && entity.getWorld().isSkyVisible(entity.getBlockPos());
		GOOD_MODIFIERS.forEach((attribute, modifier) -> {
			EntityAttributeInstance instance = entity.getAttributeInstance(attribute);
			if (applyNegative || remove) {
				if (instance != null && instance.hasModifier(modifier.id())) {
					instance.removeModifier(modifier);
				}
			} else if (instance != null && !instance.hasModifier(modifier.id())) {
				instance.addPersistentModifier(modifier);
			}
		});
		BAD_MODIFIERS.forEach((attribute, modifier) -> {
			EntityAttributeInstance instance = entity.getAttributeInstance(attribute);
			if (!applyNegative || remove) {
				if (instance != null && instance.hasModifier(modifier.id())) {
					instance.removeModifier(modifier);
				}
			} else if (instance != null && !instance.hasModifier(modifier.id())) {
				instance.addPersistentModifier(modifier);
			}
		});
	}

	public static boolean shouldHideSkeleton(ItemStack stack) {
		return stack.getOrDefault(ModDataComponentTypes.HIDE_SKELETON, false);
	}

	private static void toggleSkeleton(ItemStack stack, Consumer<Boolean> playSound) {
		boolean active = stack.getOrDefault(ModDataComponentTypes.HIDE_SKELETON, false);
		stack.set(ModDataComponentTypes.HIDE_SKELETON, !active);
		playSound.accept(active);
	}
}
