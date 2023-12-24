/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.lostrelics.mixin.tripletoothedsnake;

import moriyashiine.lostrelics.common.item.TaintedBloodCrystalItem;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Mixin(ArrowEntity.class)
public class ArrowEntityMixin {
	@ModifyVariable(method = "initFromStack", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/potion/PotionUtil;getCustomPotionEffects(Lnet/minecraft/item/ItemStack;)Ljava/util/List;"))
	private Collection<StatusEffectInstance> lostrelics$tripleToothedSnake$tippedDurationFix(Collection<StatusEffectInstance> value, ItemStack stack) {
		if (TaintedBloodCrystalItem.isSpecialPotion(stack)) {
			List<StatusEffectInstance> list = new ArrayList<>(value);
			for (int i = value.size() - 1; i >= 0; i--) {
				StatusEffectInstance instance = list.get(i);
				list.set(i, new StatusEffectInstance(instance.getEffectType(), instance.getDuration() / 8, instance.getAmplifier(), instance.isAmbient(), instance.shouldShowParticles(), instance.shouldShowIcon()));
			}
			return list;
		}
		return value;
	}
}
