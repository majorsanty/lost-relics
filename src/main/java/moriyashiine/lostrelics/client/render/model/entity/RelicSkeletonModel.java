/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.lostrelics.client.render.model.entity;

import moriyashiine.lostrelics.common.LostRelics;
import moriyashiine.lostrelics.common.init.ModEntityComponents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class RelicSkeletonModel<T extends LivingEntity> extends PlayerEntityModel<T> {
	public static final EntityModelLayer LAYER = new EntityModelLayer(LostRelics.id("relic_skeleton"), "main");

	private static final Identifier[] RELIC_SKELETON_TEXTURES = new Identifier[6];

	static {
		RELIC_SKELETON_TEXTURES[0] = LostRelics.id("textures/entity/relic_skeleton/amethyst.png");
		RELIC_SKELETON_TEXTURES[1] = LostRelics.id("textures/entity/relic_skeleton/diamond.png");
		RELIC_SKELETON_TEXTURES[2] = LostRelics.id("textures/entity/relic_skeleton/emerald.png");
		RELIC_SKELETON_TEXTURES[3] = LostRelics.id("textures/entity/relic_skeleton/gold.png");
		RELIC_SKELETON_TEXTURES[4] = LostRelics.id("textures/entity/relic_skeleton/jade.png");
		RELIC_SKELETON_TEXTURES[5] = LostRelics.id("textures/entity/relic_skeleton/turquoise.png");
	}

	public RelicSkeletonModel(ModelPart root) {
		super(root, true);
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = BipedEntityModel.getModelData(Dilation.NONE, 0);
		ModelPartData root = modelData.getRoot();
		ModelPartData skull = root.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create().uv(86, 0).cuboid(-3.5F, -7.5F, -4.75F, 7, 6, 7, Dilation.NONE)
				.uv(104, 15).cuboid(-2, -1.5F, -4.75F, 4, 1, 1, new Dilation(0.01F))
				.uv(86, 13).cuboid(-4, -6.5F, -4.8F, 8, 4, 1, new Dilation(0.1F)), ModelTransform.pivot(0, -2, 0));
		skull.addChild("lowerJaw", ModelPartBuilder.create().uv(86, 19).cuboid(-3, -1.6494F, -4.4939F, 6, 2, 6, Dilation.NONE), ModelTransform.of(0, -0.25F, 0, 0.0873F, 0, 0));
		root.addChild(EntityModelPartNames.BODY, ModelPartBuilder.create().uv(87, 45).cuboid(-4, 0, -2, 8, 8, 4, new Dilation(-0.01F))
				.uv(113, 44).cuboid(-1, -1.75F, 1, 2, 12, 1, Dilation.NONE)
				.uv(87, 58).cuboid(-4, 9, -2, 8, 3, 4, new Dilation(-0.01F)), ModelTransform.NONE);
		root.addChild(EntityModelPartNames.RIGHT_ARM, ModelPartBuilder.create().uv(84, 29).mirrored().cuboid(-1, -2, -1, 2, 12, 2, Dilation.NONE).mirrored(false), ModelTransform.NONE);
		root.addChild(EntityModelPartNames.LEFT_ARM, ModelPartBuilder.create().uv(84, 29).cuboid(-1, -2, -1, 2, 12, 2, Dilation.NONE), ModelTransform.NONE);
		root.addChild(EntityModelPartNames.RIGHT_LEG, ModelPartBuilder.create().uv(96, 29).mirrored().cuboid(-1, 0, -1, 2, 12, 2, Dilation.NONE).mirrored(false), ModelTransform.pivot(-2, 0, 0));
		root.addChild(EntityModelPartNames.LEFT_LEG, ModelPartBuilder.create().uv(96, 29).cuboid(-1, 0, -1, 2, 12, 2, Dilation.NONE), ModelTransform.pivot(2, 0, 0));
		root.addChild("ear", ModelPartBuilder.create(), ModelTransform.NONE);
		root.addChild("cloak", ModelPartBuilder.create(), ModelTransform.NONE);
		root.addChild("left_sleeve", ModelPartBuilder.create(), ModelTransform.NONE);
		root.addChild("right_sleeve", ModelPartBuilder.create(), ModelTransform.NONE);
		root.addChild("left_pants", ModelPartBuilder.create(), ModelTransform.NONE);
		root.addChild("right_pants", ModelPartBuilder.create(), ModelTransform.NONE);
		root.addChild(EntityModelPartNames.JACKET, ModelPartBuilder.create(), ModelTransform.NONE);
		return TexturedModelData.of(modelData, 128, 128);
	}

	public static Identifier getRelicSkeletonTexture(PlayerEntity player) {
		int index = player.getUuid().hashCode() % RELIC_SKELETON_TEXTURES.length;
		if (index < 0) {
			index += RELIC_SKELETON_TEXTURES.length;
		}
		return RELIC_SKELETON_TEXTURES[index];
	}

	public static Identifier getRelicSkeletonTexture(Identifier value) {
		PlayerEntity player = MinecraftClient.getInstance().player;
		if (player != null && ModEntityComponents.CURSED_AMULET.get(player).shouldTheSkeletonAppear()) {
			return getRelicSkeletonTexture(player);
		}
		return value;
	}
}
