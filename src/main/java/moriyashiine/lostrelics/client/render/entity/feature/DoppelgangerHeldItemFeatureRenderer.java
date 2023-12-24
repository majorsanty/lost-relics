/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.lostrelics.client.render.entity.feature;

import moriyashiine.lostrelics.common.entity.DoppelgangerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;

public class DoppelgangerHeldItemFeatureRenderer<T extends LivingEntity> extends HeldItemFeatureRenderer<T, PlayerEntityModel<T>> {
	public DoppelgangerHeldItemFeatureRenderer(FeatureRendererContext<T, PlayerEntityModel<T>> context, HeldItemRenderer heldItemRenderer) {
		super(context, heldItemRenderer);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, T livingEntity, float f, float g, float h, float j, float k, float l) {
		T entity = livingEntity;
		if (livingEntity instanceof DoppelgangerEntity doppelganger) {
			entity = (T) doppelganger.getEntityToCopy();
		}
		super.render(matrixStack, vertexConsumerProvider, light, entity, f, g, h, j, k, l);
	}
}
