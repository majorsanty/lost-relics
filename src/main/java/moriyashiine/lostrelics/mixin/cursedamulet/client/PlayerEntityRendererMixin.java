/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.lostrelics.mixin.cursedamulet.client;

import moriyashiine.lostrelics.client.render.entity.model.entity.RelicSkeletonModel;
import moriyashiine.lostrelics.common.init.ModEntityComponents;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
	@Unique
	private PlayerEntityModel<AbstractClientPlayerEntity> normalModel, relicSkeletonModel;

	@Unique
	private boolean isRelicSkeleton = false;

	public PlayerEntityRendererMixin(EntityRendererFactory.Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
		super(ctx, model, shadowRadius);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void lostrelics$cursedAmulet$initializeRelicSkeletonModel(EntityRendererFactory.Context ctx, boolean slim, CallbackInfo ci) {
		normalModel = model;
		relicSkeletonModel = new RelicSkeletonModel<>(ctx.getPart(RelicSkeletonModel.LAYER));
	}

	@Inject(method = "getTexture(Lnet/minecraft/client/network/AbstractClientPlayerEntity;)Lnet/minecraft/util/Identifier;", at = @At("HEAD"), cancellable = true)
	private void lostrelics$cursedAmulet$relicSkeletonTexture(AbstractClientPlayerEntity player, CallbackInfoReturnable<Identifier> cir) {
		if (isRelicSkeleton) {
			cir.setReturnValue(RelicSkeletonModel.getRelicSkeletonTexture(player));
		}
	}

	@ModifyArg(method = "renderArm", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/RenderLayer;getEntitySolid(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/render/RenderLayer;"))
	private Identifier lostrelics$cursedAmulet$relicSkeletonTextureArm(Identifier value) {
		return RelicSkeletonModel.getRelicSkeletonTexture(value);
	}

	@Inject(method = "renderArm", at = @At("HEAD"))
	private void lostrelics$cursedAmulet$updateRelicSkeletonState(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player, ModelPart arm, ModelPart sleeve, CallbackInfo ci) {
		updateRelicSkeletonState(player);
	}

	@Inject(method = "render(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"))
	private void lostrelics$cursedAmulet$updateRelicSkeletonState(AbstractClientPlayerEntity player, float yaw, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, CallbackInfo ci) {
		updateRelicSkeletonState(player);
	}

	@Inject(method = "render(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("TAIL"))
	private void lostrelics$cursedAmulet$resetSkeleton(AbstractClientPlayerEntity player, float yaw, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, CallbackInfo ci) {
		if (player.currentScreenHandler == null) {
			model = normalModel;
		}
	}

	@Unique
	private void updateRelicSkeletonState(PlayerEntity player) {
		isRelicSkeleton = ModEntityComponents.CURSED_AMULET.get(player).shouldTheSkeletonAppear();
		if (isRelicSkeleton) {
			model = relicSkeletonModel;
		} else {
			model = normalModel;
		}
	}
}
