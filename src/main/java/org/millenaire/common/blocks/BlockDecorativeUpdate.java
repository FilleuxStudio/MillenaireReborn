package org.millenaire.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import java.util.function.Supplier;

public class BlockDecorativeUpdate extends Block {
    private final Supplier<BlockState> updateState;

    public BlockDecorativeUpdate(Properties properties, Supplier<BlockState> updateIn) {
        super(properties);
        this.updateState = updateIn;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (random.nextInt(3) > 1) {
            level.setBlock(pos, updateState.get(), 3);
        }
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }
}