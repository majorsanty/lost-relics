/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.lostrelics.client.render.entity;

import moriyashiine.lostrelics.common.entity.projectile.SmokeBallEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;

public class SmokeBallEntityRenderer extends EntityRenderer<SmokeBallEntity> {
	private static final Identifier TEXTURE = new Identifier("textures/block/redstone_dust_overlay.png");

	public SmokeBallEntityRenderer(EntityRendererFactory.Context context) {
		super(context);
	}

	@Override
	public Identifier getTexture(SmokeBallEntity entity) {
		return TEXTURE;
	}

	@Override
	public void render(SmokeBallEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
		if (!MinecraftClient.getInstance().isPaused()) {
			for (int i = 0; i < 4; i++) {
				entity.getWorld().addParticle(ParticleTypes.SMOKE, entity.getParticleX(0.5), entity.getRandomBodyY(), entity.getParticleZ(0.5), 0, 0, 0);
			}
		}
	}
}
