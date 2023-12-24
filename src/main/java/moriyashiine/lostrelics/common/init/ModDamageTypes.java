/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.lostrelics.common.init;

import moriyashiine.lostrelics.common.LostRelics;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.World;

public class ModDamageTypes {
	public static final RegistryKey<DamageType> RELIC = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, LostRelics.id("relic"));

	public static DamageSource relic(World world) {
		return world.getDamageSources().create(RELIC);
	}
}
