/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.lostrelics.common.item;

import moriyashiine.lostrelics.common.LostRelics;
import moriyashiine.lostrelics.common.entity.projectile.TaintedBloodCrystalEntity;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ProjectileDispenserBehavior;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.potion.PotionUtil;
import net.minecraft.text.Text;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TaintedBloodCrystalItem extends ArrowItem {
	public TaintedBloodCrystalItem(Settings settings) {
		super(settings);
		DispenserBlock.registerBehavior(this, new ProjectileDispenserBehavior() {
			@Override
			protected ProjectileEntity createProjectile(World world, Position position, ItemStack stack) {
				TaintedBloodCrystalEntity crystal = new TaintedBloodCrystalEntity(world, position.getX(), position.getY(), position.getZ());
				crystal.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
				return crystal;
			}
		});
	}

	@Override
	public PersistentProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter) {
		TaintedBloodCrystalEntity crystal = new TaintedBloodCrystalEntity(world, shooter);
		crystal.initFromStack(stack);
		return crystal;
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		if (!PotionUtil.getCustomPotionEffects(stack).isEmpty()) {
			PotionUtil.buildTooltip(stack, tooltip, 1);
		}
	}

	public static boolean isSpecialPotion(ItemStack stack) {
		return isSpecialPotion(stack.getSubNbt(LostRelics.MOD_ID));
	}

	public static boolean isSpecialPotion(NbtCompound compound) {
		return compound != null && compound.getBoolean("SpecialPotion");
	}
}
