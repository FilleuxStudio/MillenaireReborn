package org.millenaire.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.*;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.millenaire.common.entities.TileEntityMillChest;

public class BlockMillChest extends ChestBlock {
    
    public BlockMillChest() {
        super(Properties.of()
            .strength(-1.0F, 6000000.0F)
            .noLootTable(),
            () -> MillBlocks.MILL_CHEST_BLOCK_ENTITY.get()
        );
    }

    @Override
    protected void openContainer(Level level, BlockPos pos, Player player) {
        BlockEntity blockentity = level.getBlockEntity(pos);
        if (blockentity instanceof TileEntityMillChest) {
            player.openMenu((MenuProvider)blockentity);
        }
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntityMillChest(pos, state);
    }

    private boolean isBlocked(Level level, BlockPos pos) {
        return isBelowSolidBlock(level, pos) || isCatSittingOnChest(level, pos);
    }

    private boolean isBelowSolidBlock(Level level, BlockPos pos) {
        return level.getBlockState(pos.above()).isRedstoneConductor(level, pos.above());
    }

    private boolean isCatSittingOnChest(Level level, BlockPos pos) {
        for(Cat cat : level.getEntitiesOfClass(Cat.class, new AABB(pos.getX(), pos.getY() + 1, pos.getZ(), pos.getX() + 1, pos.getY() + 2, pos.getZ() + 1))) {
            if (cat.isInSittingPose()) {
                return true;
            }
        }
        return false;
    }
}