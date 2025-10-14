package org.millenaire.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import org.millenaire.common.entities.TileEntityVillageStone;

import javax.annotation.Nullable;

public class BlockVillageStone extends BaseEntityBlock {

    public BlockVillageStone() {
        super(Properties.of()
            .mapColor(MapColor.STONE)
            .strength(-1.0F, 6000000.0F)
            .noLootTable()
        );
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
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
            te.setChanged();
        }

        return InteractionResult.SUCCESS;
    }

    public void negate(Level level, BlockPos pos, Player player) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof TileEntityVillageStone te) {
            te.willExplode = true;
            level.scheduleTick(pos, this, 60);
            level.playSound(null, pos, SoundEvents.PORTAL_TRIGGER, SoundSource.BLOCKS, 1.0F, 0.01F);
            te.setChanged();
        } else {
            System.err.println("Negation failed. BlockEntity not loaded correctly.");
        }
    }

    @Override
    public void tick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof TileEntityVillageStone te && te.willExplode) {
            level.removeBlock(pos, false);
            PrimedTnt tnt = new PrimedTnt(level, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, null);
            level.explode(tnt, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 2.0F, Level.ExplosionInteraction.TNT);
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntityVillageStone(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? null : createTickerHelper(blockEntityType, MillBlocks.VILLAGE_STONE_BLOCK_ENTITY.get(), TileEntityVillageStone::serverTick);
    }
}