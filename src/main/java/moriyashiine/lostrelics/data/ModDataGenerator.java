/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.lostrelics.data;

import moriyashiine.lostrelics.data.generators.ModEntityTypeTagProvider;
import moriyashiine.lostrelics.data.generators.ModItemTagProvider;
import moriyashiine.lostrelics.data.generators.ModModelProvider;
import moriyashiine.lostrelics.data.generators.ModStatusEffectTagProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class ModDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(ModItemTagProvider::new);
		pack.addProvider(ModEntityTypeTagProvider::new);
		pack.addProvider(ModStatusEffectTagProvider::new);
		pack.addProvider(ModModelProvider::new);
	}
}
