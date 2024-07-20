/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.lostrelics.client.render.block.entity;

import moriyashiine.lostrelics.common.blockentity.AltarBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;

public class AltarBlockEntityRenderer implements BlockEntityRenderer<AltarBlockEntity> {
	private static final MinecraftClient mc = MinecraftClient.getInstance();

	@Override
	public void render(AltarBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		matrices.translate(0.5, 1.25, 0.5);
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(mc.world.getTime() * 4 + tickDelta));
		mc.getItemRenderer().renderItem(null, entity.getStack(), ModelTransformationMode.GROUND, false, matrices, vertexConsumers, mc.world, light, overlay, 0);
	}
}
