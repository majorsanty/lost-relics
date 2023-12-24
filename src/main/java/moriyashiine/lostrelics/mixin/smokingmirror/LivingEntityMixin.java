/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.lostrelics.mixin.smokingmirror;

import moriyashiine.lostrelics.common.LostRelicsUtil;
import moriyashiine.lostrelics.common.entity.projectile.SmokeBallEntity;
import moriyashiine.lostrelics.common.init.ModItems;
import moriyashiine.lostrelics.common.init.ModSoundEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@SuppressWarnings("ConstantValue")
	@ModifyVariable(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;wakeUp()V", shift = At.Shift.BY, by = 2), argsOnly = true)
	private float lostrelics$smokingMirror$reflect(float value, DamageSource source) {
		if (!source.isIn(DamageTypeTags.WITCH_RESISTANT_TO) && source.getAttacker() instanceof LivingEntity attacker && LostRelicsUtil.applyCooldownAndDamage((LivingEntity) (Object) this, ModItems.SMOKING_MIRROR, 60, 1)) {
			getWorld().spawnEntity(new SmokeBallEntity(getWorld(), (LivingEntity) (Object) this, attacker, value * 0.25F));
			getWorld().playSound(null, getBlockPos(), ModSoundEvents.ENTITY_GENERIC_SPAWN, getSoundCategory(), 1, 1);
			return value * 0.75F;
		}
		return value;
	}
}
