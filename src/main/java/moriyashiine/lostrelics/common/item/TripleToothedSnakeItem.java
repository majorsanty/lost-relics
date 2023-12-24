/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.lostrelics.common.item;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import moriyashiine.lostrelics.common.LostRelics;
import moriyashiine.lostrelics.common.init.ModTags;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.potion.PotionUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TripleToothedSnakeItem extends SwordItem {
	private final Multimap<EntityAttribute, EntityAttributeModifier> brokenAttributeModifiers, chargedAttributeModifiers;

	public TripleToothedSnakeItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
		super(toolMaterial, attackDamage, attackSpeed, settings);
		brokenAttributeModifiers = LinkedHashMultimap.create();
		brokenAttributeModifiers.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Weapon modifier", 0, EntityAttributeModifier.Operation.ADDITION));
		brokenAttributeModifiers.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Weapon modifier", attackSpeed, EntityAttributeModifier.Operation.ADDITION));
		chargedAttributeModifiers = LinkedHashMultimap.create();
		chargedAttributeModifiers.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Weapon modifier", attackDamage + 3.5F, EntityAttributeModifier.Operation.ADDITION));
		chargedAttributeModifiers.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Weapon modifier", attackSpeed, EntityAttributeModifier.Operation.ADDITION));
	}

	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(ItemStack stack, EquipmentSlot slot) {
		if (slot == EquipmentSlot.MAINHAND) {
			if (!RelicItem.isUsable(stack)) {
				return brokenAttributeModifiers;
			} else if (getCharges(stack) > 0) {
				return chargedAttributeModifiers;
			}
		}
		return super.getAttributeModifiers(stack, slot);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack stack = user.getStackInHand(hand);
		if (user.isSneaking()) {
			if (getCharges(stack) == 0) {
				List<StatusEffectInstance> effects = new ArrayList<>();
				for (StatusEffectInstance instance : user.getStatusEffects()) {
					if (instance.getEffectType().getCategory() != StatusEffectCategory.BENEFICIAL && !ModTags.StatusEffectTags.isIn(instance, ModTags.StatusEffectTags.CANNOT_BE_SIPHONED)) {
						effects.add(instance);
					}
				}
				if (!effects.isEmpty()) {
					if (!world.isClient) {
						setCharges(stack, 4);
						PotionUtil.setCustomPotionEffects(stack, effects);
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
		if (stack.getDamage() < stack.getMaxDamage() - 1) {
			List<StatusEffectInstance> effects = PotionUtil.getCustomPotionEffects(stack);
			if (decrementCharges(stack)) {
				effects.forEach(target::addStatusEffect);
			}
			stack.damage(1, attacker, stackUser -> {
			});
			if (stack.getDamage() == stack.getMaxDamage() - 1) {
				setCharges(stack, 0);
			}
		}
		return true;
	}

	@Override
	public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
		if (state.getHardness(world, pos) != 0) {
			for (int i = 0; i < 2; i++) {
				if (stack.getDamage() < stack.getMaxDamage() - 1) {
					stack.damage(1, miner, stackUser -> {
					});
					if (stack.getDamage() == stack.getMaxDamage() - 1) {
						setCharges(stack, 0);
					}
				}
			}
		}
		return true;
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		if (!PotionUtil.getCustomPotionEffects(stack).isEmpty()) {
			PotionUtil.buildTooltip(stack, tooltip, 1);
		}
	}

	public static int getCharges(ItemStack stack) {
		NbtCompound compound = stack.getSubNbt(LostRelics.MOD_ID);
		return compound != null ? compound.getInt("Charges") : 0;
	}

	public static void setCharges(ItemStack stack, int charges) {
		stack.getOrCreateSubNbt(LostRelics.MOD_ID).putInt("Charges", charges);
		if (charges == 0) {
			stack.getNbt().remove(PotionUtil.CUSTOM_POTION_EFFECTS_KEY);
		}
	}

	public static boolean decrementCharges(ItemStack stack) {
		int charges = getCharges(stack);
		if (charges > 0) {
			setCharges(stack, charges - 1);
			return true;
		}
		return false;
	}
}
