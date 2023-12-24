/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.lostrelics.mixin.cursedamulet.client;

import moriyashiine.lostrelics.client.render.model.entity.RelicSkeletonModel;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(HeldItemRenderer.class)
public class HeldItemRendererMixin {
	@ModifyArg(method = "renderArm", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/util/Identifier;)V"))
	private Identifier lostrelics$cursedAmulet$relicSkeletonTextureArm(Identifier value) {
		return RelicSkeletonModel.getRelicSkeletonTexture(value);
	}

	@ModifyArg(method = "renderArmHoldingItem", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/util/Identifier;)V"))
	private Identifier lostrelics$cursedAmulet$relicSkeletonTextureArmHoldingItem(Identifier value) {
		return RelicSkeletonModel.getRelicSkeletonTexture(value);
	}
}
