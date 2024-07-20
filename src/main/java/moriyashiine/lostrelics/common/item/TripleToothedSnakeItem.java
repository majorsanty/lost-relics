/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.lostrelics.common.item;

import moriyashiine.lostrelics.common.init.ModDataComponentTypes;
import moriyashiine.lostrelics.common.init.ModToolMaterials;
import moriyashiine.lostrelics.common.tag.ModStatusEffectTags;
import net.minecraft.block.BlockState;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class TripleToothedSnakeItem extends SwordItem {
	public TripleToothedSnakeItem() {
		super(ModToolMaterials.TRIPLE_TOOTHED_SNAKE, RelicItem.relicSettings().component(ModDataComponentTypes.SNAKE_CHARGE, 0).attributeModifiers(SwordItem.createAttributeModifiers(ModToolMaterials.TRIPLE_TOOTHED_SNAKE, 5, -2.4F)));
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack stack = user.getStackInHand(hand);
		if (user.isSneaking() && RelicItem.isUsable(stack)) {
			if (getCharges(stack) == 0) {
				List<StatusEffectInstance> effects = new ArrayList<>();
				for (StatusEffectInstance instance : user.getStatusEffects()) {
					if (instance.getEffectType().value().getCategory() != StatusEffectCategory.BENEFICIAL && !instance.getEffectType().isIn(ModStatusEffectTags.CANNOT_BE_SIPHONED)) {
						effects.add(instance);
					}
				}
				if (!effects.isEmpty()) {
					if (!world.isClient) {
						setCharges(stack, 4);
						stack.set(DataComponentTypes.POTION_CONTENTS, create(effects));
						effects.forEach(instance -> user.removeStatusEffect(instance.getEffectType()));
						user.getItemCooldownManager().set(this, 600);
						float absorption = user.getAbsorptionAmount();
						user.setAbsorptionAmount(0);
						user.damage(world.getDamageSources().indirectMagic(user, user), 8);
						user.setAbsorptionAmount(absorption);
					}
					return TypedActionResult.success(stack, world.isClient);
				}
			}
		}
		return TypedActionResult.pass(stack);
	}

	@Override
	public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		super.postHit(stack, target, attacker);
		int charges = getCharges(stack);
		if (charges > 0) {
			if (stack.contains(DataComponentTypes.POTION_CONTENTS)) {
				stack.get(DataComponentTypes.POTION_CONTENTS).getEffects().forEach(instance -> target.addStatusEffect(new StatusEffectInstance(instance)));
			}
			setCharges(stack, charges - 1);
		}
		if (stack.getDamage() == stack.getMaxDamage()) {
			setCharges(stack, 0);
			return true;
		}
		return true;
	}

	@Override
	public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
		super.postMine(stack, world, state, pos, miner);
		if (stack.getDamage() == stack.getMaxDamage()) {
			setCharges(stack, 0);
			return true;
		}
		return true;
	}

	@Override
	public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
		return !miner.isCreative();
	}

	@Override
	public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
		Items.POTION.appendTooltip(stack, context, tooltip, type);
	}

	public static int getCharges(ItemStack stack) {
		return stack.getOrDefault(ModDataComponentTypes.SNAKE_CHARGE, 0);
	}

	public static void setCharges(ItemStack stack, int charges) {
		charges = Math.max(0, charges);
		stack.set(ModDataComponentTypes.SNAKE_CHARGE, charges);
		if (charges == 0) {
			stack.remove(DataComponentTypes.POTION_CONTENTS);
		}
	}

	public static PotionContentsComponent create(Iterable<StatusEffectInstance> effects) {
		PotionContentsComponent potionContentsComponent = PotionContentsComponent.DEFAULT;
		for (StatusEffectInstance instance : effects) {
			potionContentsComponent = potionContentsComponent.with(instance);
		}
		return potionContentsComponent;
	}
}
