package org.millenaire.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockOrientedSlab extends SlabBlock {
    protected static final VoxelShape BOTTOM_AABB = box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);
    protected static final VoxelShape TOP_AABB = box(0.0D, 8.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    
    public BlockOrientedSlab(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        SlabType slabtype = state.getValue(TYPE);
        return slabtype == SlabType.DOUBLE ? Shapes.block() : (slabtype == SlabType.TOP ? TOP_AABB : BOTTOM_AABB);
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState state) {
        return state.getValue(TYPE) != SlabType.DOUBLE;
    }

    @Override
    public boolean skipRendering(BlockState state, BlockState adjacentState, Direction direction) {
        if (adjacentState.is(this)) {
            if (direction.getAxis() == Direction.Axis.Y) {
                SlabType currentType = state.getValue(TYPE);
                SlabType adjacentType = adjacentState.getValue(TYPE);
                return (currentType == SlabType.TOP && adjacentType == SlabType.TOP && direction == Direction.UP) ||
                       (currentType == SlabType.BOTTOM && adjacentType == SlabType.BOTTOM && direction == Direction.DOWN) ||
                       (currentType == SlabType.DOUBLE && adjacentType == SlabType.DOUBLE);
            }
        }
        return super.skipRendering(state, adjacentState, direction);
    }
}