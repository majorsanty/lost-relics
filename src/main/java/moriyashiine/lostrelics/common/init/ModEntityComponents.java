/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.lostrelics.common.init;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import moriyashiine.lostrelics.common.LostRelics;
import moriyashiine.lostrelics.common.component.entity.CursedAmuletComponent;
import moriyashiine.lostrelics.common.component.entity.SmokingMirrorComponent;

public class ModEntityComponents implements EntityComponentInitializer {
	public static final ComponentKey<CursedAmuletComponent> CURSED_AMULET = ComponentRegistry.getOrCreate(LostRelics.id("cursed_amulet"), CursedAmuletComponent.class);
	public static final ComponentKey<SmokingMirrorComponent> SMOKING_MIRROR = ComponentRegistry.getOrCreate(LostRelics.id("smoking_mirror"), SmokingMirrorComponent.class);

	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.registerForPlayers(CURSED_AMULET, CursedAmuletComponent::new, RespawnCopyStrategy.LOSSLESS_ONLY);
		registry.registerForPlayers(SMOKING_MIRROR, SmokingMirrorComponent::new, RespawnCopyStrategy.LOSSLESS_ONLY);
	}
}
