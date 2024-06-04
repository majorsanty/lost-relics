/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.lostrelics.common.blockentity;

import moriyashiine.lostrelics.common.init.ModBlockEntityTypes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class AltarBlockEntity extends BlockEntity {
	private ItemStack stack = ItemStack.EMPTY;

	public AltarBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntityTypes.ALTAR, pos, state);
	}

	@Override
	protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		if (nbt.contains("Stack", NbtElement.COMPOUND_TYPE)) {
			stack = ItemStack.fromNbt(registryLookup, nbt.getCompound("Stack")).orElse(ItemStack.EMPTY);
		} else {
			stack = ItemStack.EMPTY;
		}
	}

	@Override
	protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		nbt.putBoolean("ThisIsNeededToSync", false);
		if (!stack.isEmpty()) {
			nbt.put("Stack", stack.encode(registryLookup));
		}
	}

	@Override
	public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
		NbtCompound nbt = super.toInitialChunkDataNbt(registryLookup);
		writeNbt(nbt, registryLookup);
		return nbt;
	}

	@Nullable
	@Override
	public Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}

	public void sync() {
		getWorld().updateListeners(getPos(), getCachedState(), getCachedState(), Block.NOTIFY_LISTENERS);
	}

	public ItemStack getStack() {
		return stack;
	}

	public void setStack(ItemStack stack) {
		this.stack = stack;
	}
}
