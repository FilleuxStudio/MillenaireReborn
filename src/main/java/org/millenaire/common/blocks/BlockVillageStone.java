package org.millenaire.common.blocks;


import org.millenaire.common.entities.TileEntityVillageStone;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class BlockVillageStone extends BaseEntityBlock {

    public BlockVillageStone(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntityVillageStone(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide) {
            player.displayClientMessage(Component.literal("The Village name almost seems to shimmer in the twilight"), true);
        }

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof TileEntityVillageStone te) {
            if (te.testVar < 16) {
                te.testVar++;
            } else {
                te.testVar = 0;
            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    public void negate(Level level, BlockPos pos, Player player) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof TileEntityVillageStone te) {
            te.willExplode = true;
            level.scheduleTick(pos, this, 60);
            level.playSound(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 
                net.minecraft.sounds.SoundEvents.PORTAL_TRIGGER, net.minecraft.sounds.SoundSource.BLOCKS, 1.0F, 0.01F);
        } else {
            System.err.println("Negation failed. TileEntity not loaded correctly.");
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof TileEntityVillageStone te && te.willExplode) {
            level.removeBlock(pos, false);
            level.explode(new PrimedTnt(level, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, null), 
                pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 2.0F, true, Level.ExplosionInteraction.TNT);
        }
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'codec'");
    }
}