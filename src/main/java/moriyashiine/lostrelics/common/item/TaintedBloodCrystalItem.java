/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.lostrelics.common.item;

import moriyashiine.lostrelics.common.entity.projectile.TaintedBloodCrystalEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TaintedBloodCrystalItem extends ArrowItem {
	public TaintedBloodCrystalItem(Item.Settings settings) {
		super(settings);
	}

	@Override
	public PersistentProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter, @Nullable ItemStack shotFrom) {
		return new TaintedBloodCrystalEntity(world, shooter, stack.copyWithCount(1), shotFrom);
	}

	@Override
	public ProjectileEntity createEntity(World world, Position pos, ItemStack stack, Direction direction) {
		TaintedBloodCrystalEntity crystal = new TaintedBloodCrystalEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack.copyWithCount(1), null);
		crystal.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
		return crystal;
	}

	@Override
	public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
		Items.POTION.appendTooltip(stack, context, tooltip, type);
	}
}
