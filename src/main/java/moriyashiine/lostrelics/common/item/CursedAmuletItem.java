/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.lostrelics.common.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import dev.emi.trinkets.api.SlotReference;
import moriyashiine.lostrelics.common.LostRelics;
import moriyashiine.lostrelics.common.LostRelicsUtil;
import moriyashiine.lostrelics.common.init.ModEntityComponents;
import moriyashiine.lostrelics.common.init.ModSoundEvents;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class CursedAmuletItem extends RelicItem {
	public static final Multimap<EntityAttribute, EntityAttributeModifier> GOOD_MODIFIERS, BAD_MODIFIERS;

	static {
		GOOD_MODIFIERS = HashMultimap.create();
		GOOD_MODIFIERS.put(EntityAttributes.GENERIC_ARMOR, new EntityAttributeModifier(UUID.fromString("4c9510d8-5d90-4b9f-8ee6-4779f824998a"), "Trinket modifier", 4, EntityAttributeModifier.Operation.ADDITION));
		GOOD_MODIFIERS.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(UUID.fromString("723e4c9c-37fe-4a22-a60f-704741b29f22"), "Trinket modifier", 3, EntityAttributeModifier.Operation.ADDITION));

		BAD_MODIFIERS = HashMultimap.create();
		BAD_MODIFIERS.put(EntityAttributes.GENERIC_ARMOR, new EntityAttributeModifier(UUID.fromString("3637dedf-9d72-40a4-bf2b-3f31356af2d1"), "Trinket modifier", -8, EntityAttributeModifier.Operation.ADDITION));
		BAD_MODIFIERS.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(UUID.fromString("bda17160-e891-4635-bf1a-bb4d85b2cdaf"), "Trinket modifier", -6, EntityAttributeModifier.Operation.ADDITION));
	}

	public CursedAmuletItem() {
		super();
	}

	@Override
	public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
		if (entity.age % 20 == 0) {
			applyModifiers(entity, false);
		}
	}

	@Override
	public void onEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
		applyModifiers(entity, false);
		ModEntityComponents.CURSED_AMULET.maybeGet(entity).ifPresent(cursedAmuletComponent -> cursedAmuletComponent.toggleTransform(!shouldHideSkeleton(stack)));
	}

	@Override
	public void onUnequip(ItemStack stack, SlotReference slot, LivingEntity entity) {
		applyModifiers(entity, true);
		ModEntityComponents.CURSED_AMULET.maybeGet(entity).ifPresent(cursedAmuletComponent -> cursedAmuletComponent.toggleTransform(false));
	}

	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid) {
		return GOOD_MODIFIERS;
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
			toggleSkeleton(stack, hideSkeleton -> {
				player.playSound(ModSoundEvents.ITEM_RELIC_TOGGLE, 1, hideSkeleton ? 1 : 0.5F);
				if (LostRelicsUtil.hasSpecificTrinket(player, stack)) {
					ModEntityComponents.CURSED_AMULET.get(player).toggleTransform(hideSkeleton);
				}
			});
			return true;
		}
		return false;
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		tooltip.add(Text.translatable("tooltip." + LostRelics.MOD_ID + ".hide_skeleton", shouldHideSkeleton(stack)).formatted(Formatting.DARK_GRAY));
	}

	private void applyModifiers(LivingEntity entity, boolean remove) {
		if (entity.getWorld().isClient) {
			return;
		}
		if (remove || !(entity.getWorld().isDay() && entity.getWorld().isSkyVisible(entity.getBlockPos()))) {
			applyNegativeModifiers(entity, true);
		} else {
			applyNegativeModifiers(entity, false);
			if (entity.getHealth() > entity.getMaxHealth() && entity.canTakeDamage()) {
				entity.setHealth(entity.getMaxHealth());
			}
		}
	}

	private void applyNegativeModifiers(LivingEntity entity, boolean remove) {
		if (remove) {
			BAD_MODIFIERS.forEach((entityAttribute, entityAttributeModifier) -> {
				EntityAttributeInstance instance = entity.getAttributeInstance(entityAttribute);
				if (instance != null && instance.hasModifier(entityAttributeModifier)) {
					instance.removeModifier(entityAttributeModifier);
				}
			});
		} else {
			BAD_MODIFIERS.forEach((entityAttribute, entityAttributeModifier) -> {
				EntityAttributeInstance instance = entity.getAttributeInstance(entityAttribute);
				if (instance != null && !instance.hasModifier(entityAttributeModifier)) {
					instance.addTemporaryModifier(entityAttributeModifier);
				}
			});
		}
	}

	public static boolean shouldHideSkeleton(ItemStack stack) {
		NbtCompound compound = stack.getSubNbt(LostRelics.MOD_ID);
		return compound != null && compound.getBoolean("HideSkeleton");
	}

	private static void toggleSkeleton(ItemStack stack, Consumer<Boolean> playSound) {
		NbtCompound compound = stack.getOrCreateSubNbt(LostRelics.MOD_ID);
		boolean active = compound.getBoolean("HideSkeleton");
		compound.putBoolean("HideSkeleton", !active);
		playSound.accept(active);
	}
}
