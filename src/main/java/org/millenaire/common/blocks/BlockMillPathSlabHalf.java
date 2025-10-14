package org.millenaire.common.blocks;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.material.MapColor;

public class BlockMillPathSlabHalf extends SlabBlock {
    
    public BlockMillPathSlabHalf() {
        super(Properties.of()
            .mapColor(MapColor.DIRT)
            .strength(0.5F)
        );
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState state) {
        return state.getValue(TYPE) != SlabType.DOUBLE;
    }
}