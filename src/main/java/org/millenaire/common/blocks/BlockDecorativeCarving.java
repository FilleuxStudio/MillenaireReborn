package org.millenaire.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockDecorativeCarving extends BlockDecorativeOriented {

    private static final VoxelShape NORTH_SOUTH_SHAPE = box(4.0D, 0.0D, 0.0D, 12.0D, 8.0D, 16.0D);
    private static final VoxelShape EAST_WEST_SHAPE = box(0.0D, 0.0D, 4.0D, 16.0D, 8.0D, 12.0D);

    public BlockDecorativeCarving(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(FACING);
        return (direction == Direction.NORTH || direction == Direction.SOUTH) ? NORTH_SOUTH_SHAPE : EAST_WEST_SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return this.getShape(state, level, pos, context);
    }

    @Override
    public float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
        return 0.85F;
    }
}