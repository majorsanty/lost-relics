/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.lostrelics.common.init;

import moriyashiine.lostrelics.common.LostRelics;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;

public class ModSoundEvents {
	public static final SoundEvent ENTITY_GENERIC_TRANSFORM = SoundEvent.of(LostRelics.id("entity.generic.transform"));
	public static final SoundEvent ENTITY_GENERIC_SPAWN = SoundEvent.of(LostRelics.id("entity.generic.spawn"));
	public static final SoundEvent ENTITY_TAINTED_BLOOD_CRYSTAL_SHATTER = SoundEvent.of(LostRelics.id("entity.tainted_blood_crystal.shatter"));

	public static final SoundEvent ITEM_RELIC_TOGGLE = SoundEvent.of(LostRelics.id("item.relic.toggle"));

	public static void init() {
		Registry.register(Registries.SOUND_EVENT, ENTITY_GENERIC_TRANSFORM.getId(), ENTITY_GENERIC_TRANSFORM);
		Registry.register(Registries.SOUND_EVENT, ENTITY_GENERIC_SPAWN.getId(), ENTITY_GENERIC_SPAWN);
		Registry.register(Registries.SOUND_EVENT, ENTITY_TAINTED_BLOOD_CRYSTAL_SHATTER.getId(), ENTITY_TAINTED_BLOOD_CRYSTAL_SHATTER);
		Registry.register(Registries.SOUND_EVENT, ITEM_RELIC_TOGGLE.getId(), ITEM_RELIC_TOGGLE);
	}
}
