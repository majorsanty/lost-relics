/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.lostrelics.client;

import dev.emi.trinkets.api.client.TrinketRendererRegistry;
import moriyashiine.lostrelics.client.packet.SyncDoppelgangerSlimStatusS2CPayload;
import moriyashiine.lostrelics.client.render.blockentity.AltarBlockEntityRenderer;
import moriyashiine.lostrelics.client.render.entity.DoppelgangerEntityRenderer;
import moriyashiine.lostrelics.client.render.entity.SmokeBallEntityRenderer;
import moriyashiine.lostrelics.client.render.entity.TaintedBloodCrystalEntityRenderer;
import moriyashiine.lostrelics.client.render.model.entity.RelicSkeletonModel;
import moriyashiine.lostrelics.client.render.model.trinket.FaceTrinketRenderer;
import moriyashiine.lostrelics.client.render.model.trinket.NecklaceTrinketRenderer;
import moriyashiine.lostrelics.common.LostRelics;
import moriyashiine.lostrelics.common.init.ModBlockEntityTypes;
import moriyashiine.lostrelics.common.init.ModEntityTypes;
import moriyashiine.lostrelics.common.init.ModItems;
import moriyashiine.lostrelics.common.item.RelicItem;
import moriyashiine.lostrelics.common.item.TripleToothedSnakeItem;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ChargedProjectilesComponent;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

public class LostRelicsClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		BlockEntityRendererFactories.register(ModBlockEntityTypes.ALTAR, ctx -> new AltarBlockEntityRenderer());
		ModelPredicateProviderRegistry.register(ModItems.TURQUOISE_EYE, new Identifier("broken"), (stack, world, entity, seed) -> RelicItem.isUsable(stack) ? 0 : 1);
		ModelPredicateProviderRegistry.register(ModItems.SMOKING_MIRROR, new Identifier("broken"), (stack, world, entity, seed) -> RelicItem.isUsable(stack) ? 0 : 1);
		ModelPredicateProviderRegistry.register(ModItems.TRIPLE_TOOTHED_SNAKE, new Identifier("one"), (stack, world, entity, seed) -> RelicItem.isUsable(stack) && TripleToothedSnakeItem.getCharges(stack) == 1 ? 1 : 0);
		ModelPredicateProviderRegistry.register(ModItems.TRIPLE_TOOTHED_SNAKE, new Identifier("two"), (stack, world, entity, seed) -> RelicItem.isUsable(stack) && TripleToothedSnakeItem.getCharges(stack) == 2 ? 1 : 0);
		ModelPredicateProviderRegistry.register(ModItems.TRIPLE_TOOTHED_SNAKE, new Identifier("three"), (stack, world, entity, seed) -> RelicItem.isUsable(stack) && TripleToothedSnakeItem.getCharges(stack) == 3 ? 1 : 0);
		ModelPredicateProviderRegistry.register(ModItems.TRIPLE_TOOTHED_SNAKE, new Identifier("four"), (stack, world, entity, seed) -> RelicItem.isUsable(stack) && TripleToothedSnakeItem.getCharges(stack) == 4 ? 1 : 0);
		ModelPredicateProviderRegistry.register(ModItems.TRIPLE_TOOTHED_SNAKE, new Identifier("broken"), (stack, world, entity, seed) -> RelicItem.isUsable(stack) ? 0 : 1);
		ModelPredicateProviderRegistry.register(Items.BOW, LostRelics.id("pulling_tainted_blood_crystal"), (stack, world, entity, seed) -> entity != null && entity.getProjectileType(stack).isOf(ModItems.TAINTED_BLOOD_CRYSTAL) ? 1 : 0);
		ModelPredicateProviderRegistry.register(Items.CROSSBOW, LostRelics.id("tainted_blood_crystal"), (stack, world, entity, seed) -> stack.getOrDefault(DataComponentTypes.CHARGED_PROJECTILES, ChargedProjectilesComponent.DEFAULT).contains(ModItems.TAINTED_BLOOD_CRYSTAL) ? 1 : 0);
		EntityRendererRegistry.register(ModEntityTypes.DOPPELGANGER, DoppelgangerEntityRenderer::new);
		EntityRendererRegistry.register(ModEntityTypes.SMOKE_BALL, SmokeBallEntityRenderer::new);
		EntityRendererRegistry.register(ModEntityTypes.TAINTED_BLOOD_CRYSTAL, TaintedBloodCrystalEntityRenderer::new);
		EntityModelLayerRegistry.registerModelLayer(RelicSkeletonModel.LAYER, RelicSkeletonModel::getTexturedModelData);
		TrinketRendererRegistry.registerRenderer(ModItems.CURSED_AMULET, new NecklaceTrinketRenderer());
		TrinketRendererRegistry.registerRenderer(ModItems.SMOKING_MIRROR, new NecklaceTrinketRenderer());
		TrinketRendererRegistry.registerRenderer(ModItems.TURQUOISE_EYE, new FaceTrinketRenderer());
		initPayloads();
	}

	private void initPayloads() {
		ClientPlayNetworking.registerGlobalReceiver(SyncDoppelgangerSlimStatusS2CPayload.ID, new SyncDoppelgangerSlimStatusS2CPayload.Receiver());
	}
}
