package org.millenaire.common.blocks;

import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import java.util.function.Supplier;

public class BlockDecorativeOrientedStairs extends StairBlock {
    
    public BlockDecorativeOrientedStairs(Supplier<BlockState> state, Properties properties) {
        super(state, properties);
    }
}