package moriyashiine.lostrelics.client.render.entity;

/*
 * All Rights Reserved (c) MoriyaShiine
 */

import com.mojang.blaze3d.systems.RenderSystem;
import moriyashiine.lostrelics.client.render.entity.feature.DoppelgangerArmorFeatureRenderer;
import moriyashiine.lostrelics.client.render.entity.feature.DoppelgangerHeldItemFeatureRenderer;
import moriyashiine.lostrelics.common.entity.DoppelgangerEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class DoppelgangerEntityRenderer extends BipedEntityRenderer<DoppelgangerEntity, PlayerEntityModel<DoppelgangerEntity>> {
	public final PlayerEntityModel<DoppelgangerEntity> defaultModel, slimModel;

	public DoppelgangerEntityRenderer(EntityRendererFactory.Context ctx) {
		super(ctx, new PlayerEntityModel<>(ctx.getPart(EntityModelLayers.PLAYER), false), 0.5F);
		defaultModel = model;
		slimModel = new PlayerEntityModel<>(ctx.getPart(EntityModelLayers.PLAYER_SLIM), true);
		addFeature(new DoppelgangerArmorFeatureRenderer<>(this, ctx));
		addFeature(new DoppelgangerHeldItemFeatureRenderer<>(this, ctx.getHeldItemRenderer()));
	}

	@Override
	public Identifier getTexture(DoppelgangerEntity entity) {
		if (entity.getEntityToCopy() instanceof AbstractClientPlayerEntity clientPlayer) {
			return clientPlayer.getSkinTexture();
		}
		return DefaultSkinHelper.getTexture();
	}

	@Override
	public void render(DoppelgangerEntity mobEntity, float yaw, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light) {
		if (mobEntity.getDataTracker().get(DoppelgangerEntity.SLIM)) {
			model = slimModel;
		} else {
			model = defaultModel;
		}
		if (!MinecraftClient.getInstance().isPaused()) {
			for (int i = 0; i < 4; i++) {
				mobEntity.getWorld().addParticle(ParticleTypes.SMOKE, mobEntity.getParticleX(0.5), mobEntity.getRandomBodyY(), mobEntity.getParticleZ(0.5), MathHelper.nextDouble(mobEntity.getRandom(), -0.1, 0.1), MathHelper.nextDouble(mobEntity.getRandom(), -0.1, 0.1), MathHelper.nextDouble(mobEntity.getRandom(), -0.1, 0.1));
			}
		}
		RenderSystem.setShaderColor(0.75F, 0.75F, 0.75F, 0.8F);
		super.render(mobEntity, yaw, tickDelta, matrixStack, vertexConsumerProvider, light);
	}

	@Override
	public boolean shouldRender(DoppelgangerEntity mobEntity, Frustum frustum, double x, double y, double z) {
		if (mobEntity.getEntityToCopy() == null) {
			return false;
		}
		return super.shouldRender(mobEntity, frustum, x, y, z);
	}

	@Override
	protected void scale(DoppelgangerEntity entity, MatrixStack matrices, float amount) {
		matrices.scale(0.9375F, 0.9375F, 0.9375F);
	}
}
