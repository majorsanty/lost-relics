/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.lostrelics.mixin.tripletoothedsnake.client;

import moriyashiine.lostrelics.common.LostRelics;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.render.model.json.ModelOverride;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.LinkedList;
import java.util.List;

@Mixin(ModelLoader.class)
public class ModelLoaderMixin {
	@Unique
	private static final Identifier BOW = new Identifier("item/bow");

	@Unique
	private static final Identifier CROSSBOW = new Identifier("item/crossbow");

	@Inject(method = "loadModelFromJson", at = @At("RETURN"))
	private void lostrelics$tripleToothedSnake$taintedBloodCrystalModelOverrides(Identifier id, CallbackInfoReturnable<JsonUnbakedModel> cir) {
		if (id.equals(BOW)) {
			int index = 0;
			for (float pull : new float[]{0, 0.65F, 0.9F}) {
				List<ModelOverride.Condition> conditions = new LinkedList<>();
				conditions.add(new ModelOverride.Condition(new Identifier("pulling"), 1));
				conditions.add(new ModelOverride.Condition(new Identifier("pull"), pull));
				conditions.add(new ModelOverride.Condition(LostRelics.id("pulling_tainted_blood_crystal"), 1));
				cir.getReturnValue().getOverrides().add(new ModelOverride(LostRelics.id("item/bow_pulling_tainted_blood_crystal_" + index), conditions));
				index++;
			}
		} else if (id.equals(CROSSBOW)) {
			List<ModelOverride.Condition> conditions = new LinkedList<>();
			conditions.add(new ModelOverride.Condition(new Identifier("charged"), 1));
			conditions.add(new ModelOverride.Condition(LostRelics.id("tainted_blood_crystal"), 1));
			cir.getReturnValue().getOverrides().add(new ModelOverride(LostRelics.id("item/crossbow_tainted_blood_crystal"), conditions));
		}
	}
}
