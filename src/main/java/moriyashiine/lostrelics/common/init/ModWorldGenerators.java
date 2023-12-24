/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.lostrelics.common.init;

import moriyashiine.lostrelics.common.LostRelics;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.LootTableEntry;

public class ModWorldGenerators {
	public static void init() {
		LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
			if (id.equals(LootTables.JUNGLE_TEMPLE_CHEST)) {
				tableBuilder.pool(LootPool.builder().with(LootTableEntry.builder(LostRelics.id("inject/jungle_temple_chest")).build()));
			}
		});
	}
}
