/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.lostrelics.common.init;

import moriyashiine.lostrelics.common.LostRelics;
import moriyashiine.lostrelics.common.entity.DoppelgangerEntity;
import moriyashiine.lostrelics.common.entity.projectile.SmokeBallEntity;
import moriyashiine.lostrelics.common.entity.projectile.TaintedBloodCrystalEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModEntityTypes {
	public static final EntityType<DoppelgangerEntity> DOPPELGANGER = FabricEntityTypeBuilder.createLiving().entityFactory(DoppelgangerEntity::new).defaultAttributes(DoppelgangerEntity::createDoppelgangerAttributes).disableSummon().dimensions(EntityDimensions.fixed(0.6F, 1.8F)).trackRangeChunks(8).build();
	public static final EntityType<SmokeBallEntity> SMOKE_BALL = FabricEntityTypeBuilder.<SmokeBallEntity>create(SpawnGroup.MISC, SmokeBallEntity::new).disableSummon().dimensions(EntityDimensions.fixed(0.5F, 0.5F)).trackRangeChunks(4).trackedUpdateRate(20).build();
	public static final EntityType<TaintedBloodCrystalEntity> TAINTED_BLOOD_CRYSTAL = FabricEntityTypeBuilder.<TaintedBloodCrystalEntity>create(SpawnGroup.MISC, TaintedBloodCrystalEntity::new).dimensions(EntityDimensions.fixed(0.5F, 0.5F)).trackRangeChunks(4).trackedUpdateRate(20).build();

	public static void init() {
		Registry.register(Registries.ENTITY_TYPE, LostRelics.id("doppelganger"), DOPPELGANGER);
		Registry.register(Registries.ENTITY_TYPE, LostRelics.id("smoke_ball"), SMOKE_BALL);
		Registry.register(Registries.ENTITY_TYPE, LostRelics.id("tainted_blood_crystal"), TAINTED_BLOOD_CRYSTAL);
	}
}
