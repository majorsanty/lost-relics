/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.lostrelics.common.init;

import moriyashiine.lostrelics.common.LostRelics;
import moriyashiine.lostrelics.common.entity.DoppelgangerEntity;
import moriyashiine.lostrelics.common.entity.projectile.SmokeBallEntity;
import moriyashiine.lostrelics.common.entity.projectile.TaintedBloodCrystalEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModEntityTypes {
	public static final EntityType<DoppelgangerEntity> DOPPELGANGER = EntityType.Builder.create(DoppelgangerEntity::new, SpawnGroup.MISC).disableSummon().dimensions(0.6F, 1.8F).build();
	public static final EntityType<SmokeBallEntity> SMOKE_BALL = EntityType.Builder.<SmokeBallEntity>create(SmokeBallEntity::new, SpawnGroup.MISC).disableSummon().dimensions(0.5F, 0.5F).maxTrackingRange(4).trackingTickInterval(20).build();
	public static final EntityType<TaintedBloodCrystalEntity> TAINTED_BLOOD_CRYSTAL = EntityType.Builder.<TaintedBloodCrystalEntity>create(TaintedBloodCrystalEntity::new, SpawnGroup.MISC).dimensions(0.5F, 0.5F).maxTrackingRange(4).trackingTickInterval(20).build();

	public static void init() {
		Registry.register(Registries.ENTITY_TYPE, LostRelics.id("doppelganger"), DOPPELGANGER);
		Registry.register(Registries.ENTITY_TYPE, LostRelics.id("smoke_ball"), SMOKE_BALL);
		Registry.register(Registries.ENTITY_TYPE, LostRelics.id("tainted_blood_crystal"), TAINTED_BLOOD_CRYSTAL);
		FabricDefaultAttributeRegistry.register(DOPPELGANGER, DoppelgangerEntity.createDoppelgangerAttributes());
	}
}
