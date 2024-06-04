/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.lostrelics.common.init;

import moriyashiine.lostrelics.common.LostRelics;
import moriyashiine.lostrelics.common.recipe.TaintedBloodCrystalRecipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModRecipeSerializers {
	public static final RecipeSerializer<TaintedBloodCrystalRecipe> TAINTED_BLOOD_CRYSTAL = new SpecialRecipeSerializer<>(TaintedBloodCrystalRecipe::new);

	public static void init() {
		Registry.register(Registries.RECIPE_SERIALIZER, LostRelics.id("tainted_blood_crystal"), TAINTED_BLOOD_CRYSTAL);
	}
}
