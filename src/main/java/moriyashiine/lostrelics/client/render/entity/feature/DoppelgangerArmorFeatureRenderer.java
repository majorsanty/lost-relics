/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.lostrelics.client.render.entity.feature;

import moriyashiine.lostrelics.common.entity.DoppelgangerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.ArmorEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;

public class DoppelgangerArmorFeatureRenderer<T extends LivingEntity> extends ArmorFeatureRenderer<T, PlayerEntityModel<T>, ArmorEntityModel<T>> {
	private final ArmorEntityModel<T> defaultInnerModel, defaultOuterModel, slimInnerModel, slimOuterModel;

	private boolean slim = false;

	public DoppelgangerArmorFeatureRenderer(FeatureRendererContext<T, PlayerEntityModel<T>> featureContext, EntityRendererFactory.Context factoryContext) {
		super(featureContext, null, null, factoryContext.getModelManager());
		defaultInnerModel = new ArmorEntityModel<>(factoryContext.getPart(EntityModelLayers.PLAYER_INNER_ARMOR));
		defaultOuterModel = new ArmorEntityModel<>(factoryContext.getPart(EntityModelLayers.PLAYER_OUTER_ARMOR));
		slimInnerModel = new ArmorEntityModel<>(factoryContext.getPart(EntityModelLayers.PLAYER_SLIM_INNER_ARMOR));
		slimOuterModel = new ArmorEntityModel<>(factoryContext.getPart(EntityModelLayers.PLAYER_SLIM_OUTER_ARMOR));
	}

	@Override
	protected ArmorEntityModel<T> getModel(EquipmentSlot slot) {
		if (usesInnerModel(slot)) {
			return slim ? slimInnerModel : defaultInnerModel;
		}
		return slim ? slimOuterModel : defaultOuterModel;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, T livingEntity, float f, float g, float h, float j, float k, float l) {
		T entity = livingEntity;
		if (livingEntity instanceof DoppelgangerEntity doppelganger) {
			slim = doppelganger.getDataTracker().get(DoppelgangerEntity.SLIM);
			entity = (T) doppelganger.getEntityToCopy();
		}
		super.render(matrixStack, vertexConsumerProvider, light, entity, f, g, h, j, k, l);
	}
}
