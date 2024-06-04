/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.lostrelics.mixin.util;

import moriyashiine.lostrelics.common.block.AltarBlock;
import moriyashiine.lostrelics.common.blockentity.AltarBlockEntity;
import moriyashiine.lostrelics.common.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.structure.StructurePiece;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ServerWorldAccess;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StructurePiece.class)
public class StructurePieceMixin {
	@Inject(method = "addChest(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/util/math/BlockBox;Lnet/minecraft/util/math/random/Random;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/block/BlockState;)Z", at = @At("HEAD"), cancellable = true)
	private void lostrelics$generateAltar(ServerWorldAccess world, BlockBox boundingBox, Random random, BlockPos pos, RegistryKey<LootTable> lootTable, @Nullable BlockState block, CallbackInfoReturnable<Boolean> cir) {
		if (lootTable == LootTables.JUNGLE_TEMPLE_CHEST && random.nextInt(4) == 0) {
			placeAltar(world, pos, (AltarBlock) ModBlocks.JUNGLE_ALTAR);
			cir.setReturnValue(true);
		}
	}

	@Unique
	private static void placeAltar(ServerWorldAccess world, BlockPos pos, AltarBlock block) {
		world.setBlockState(pos, block.getDefaultState(), Block.NOTIFY_LISTENERS);
		AltarBlockEntity altarBlockEntity = (AltarBlockEntity) world.getBlockEntity(pos);

		Item relic;
		do {
			relic = Registries.ITEM.getRandom(world.getRandom()).get().value();
		}
		while (!relic.getDefaultStack().isIn(block.relicTag));

		altarBlockEntity.setStack(relic.getDefaultStack());
	}
}
