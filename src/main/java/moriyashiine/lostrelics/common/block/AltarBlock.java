/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.lostrelics.common.block;

import moriyashiine.lostrelics.common.blockentity.AltarBlockEntity;
import moriyashiine.lostrelics.common.init.ModSoundEvents;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class AltarBlock extends HorizontalFacingBlock implements BlockEntityProvider {

    private static final VoxelShape SHAPE = createCuboidShape(0.5, 0, 0.5, 15.5, 15, 15.5);

    public final TagKey<Item> relicTag;

    public AltarBlock(Settings settings, TagKey<Item> relicTag) {
        super(settings);
        this.relicTag = relicTag;
        setDefaultState(getDefaultState().with(FACING, Direction.NORTH));
    }


    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new AltarBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx).with(FACING, ctx.getHorizontalPlayerFacing());
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock())) {
            AltarBlockEntity altarBlockEntity = (AltarBlockEntity) world.getBlockEntity(pos);
            if (!altarBlockEntity.getStack().isEmpty()) {
                ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), altarBlockEntity.getStack().copyAndEmpty());
            }
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        AltarBlockEntity altarBlockEntity = (AltarBlockEntity) world.getBlockEntity(pos);
        ItemStack stack = player.getStackInHand(hand);


        if (altarBlockEntity.getStack().isEmpty()) {
            if (stack.isIn(relicTag)) {
                if (!world.isClient) {
                    world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1, 1);
                    altarBlockEntity.setStack(stack.split(1));
                    altarBlockEntity.markDirty();
                    altarBlockEntity.sync();
                }
                return ActionResult.success(world.isClient);
            }
        } else if (stack.isIn(relicTag)) {
            if (!world.isClient) {
                Item newRelic;
                do {
                    newRelic = Registries.ITEM.getRandom(world.getRandom()).get().value();
                }
                while (!newRelic.getDefaultStack().isIn(relicTag) || stack.isOf(newRelic) || altarBlockEntity.getStack().isOf(newRelic));
                altarBlockEntity.setStack(newRelic.getDefaultStack());
                altarBlockEntity.markDirty();
                altarBlockEntity.sync();
                stack.decrement(1);
                world.playSound(null, pos, ModSoundEvents.BLOCK_ALTAR_CONVERT, SoundCategory.BLOCKS, 1, 1);
                float dX = MathHelper.nextFloat(world.getRandom(), -0.2F, 0.2F);
                float dY = MathHelper.nextFloat(world.getRandom(), -0.2F, 0.2F);
                float dZ = MathHelper.nextFloat(world.getRandom(), -0.2F, 0.2F);
                ((ServerWorld) world).spawnParticles(ParticleTypes.SMOKE, pos.getX() + 0.5, pos.getY() + 1.4, pos.getZ() + 0.5, 48, dX, dY, dZ, 0.15);
            }
            return ActionResult.success(world.isClient);
        } else if (stack.isEmpty()) {
            if (!world.isClient) {
                world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1, 1);
                ItemStack relic = altarBlockEntity.getStack().copyAndEmpty();
                altarBlockEntity.markDirty();
                altarBlockEntity.sync();
                if (!player.giveItemStack(relic)) {
                    player.dropStack(relic);
                }
            }
            return ActionResult.success(world.isClient);
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}
