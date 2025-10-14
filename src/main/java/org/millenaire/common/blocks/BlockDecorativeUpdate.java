package org.millenaire.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class BlockDecorativeUpdate extends Block {
    private final BlockState updateState;

    public BlockDecorativeUpdate(Properties properties, BlockState updateIn) {
        super(properties);
        updateState = updateIn;
    }

    @Override
    public void randomTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        int i = random.nextInt(3);

        if (i > 1) {
            world.setBlock(pos, updateState, 3);
        }
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }
}