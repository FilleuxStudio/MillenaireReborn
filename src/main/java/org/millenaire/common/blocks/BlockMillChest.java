package org.millenaire.common.blocks;

import org.millenaire.Millenaire;
import org.millenaire.common.entities.TileEntityMillChest;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;

public class BlockMillChest extends ChestBlock {
    
    public BlockMillChest(Properties properties) {
        super(properties, () -> MillBlocks.MILL_CHEST_ENTITY.get());
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntityMillChest(pos, state);
    }

    protected void open(Level level, BlockPos pos, Player player) {
        BlockEntity blockentity = level.getBlockEntity(pos);
        if (blockentity instanceof TileEntityMillChest) {
            player.openMenu((TileEntityMillChest) blockentity);
        }
    }

    private boolean isBlocked(Level level, BlockPos pos) {
        return this.isBelowSolidBlock(level, pos) || this.isCatSittingOnChest(level, pos);
    }

    private boolean isBelowSolidBlock(Level level, BlockPos pos) {
        BlockPos abovePos = pos.above();
        return level.getBlockState(abovePos).isRedstoneConductor(level, abovePos);
    }

    private boolean isCatSittingOnChest(Level level, BlockPos pos) {
        for (Cat cat : level.getEntitiesOfClass(Cat.class, new AABB(pos.getX(), pos.getY() + 1, pos.getZ(), pos.getX() + 1, pos.getY() + 2, pos.getZ() + 1))) {
            if (cat.isInSittingPose()) {
                return true;
            }
        }
        return false;
    }
}