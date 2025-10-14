package org.millenaire.common.blocks;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec; // Import nécessaire
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockDecorativeCarving extends HorizontalDirectionalBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    
    private static final VoxelShape NORTH_SOUTH_SHAPE = box(4, 0, 0, 12, 8, 16);
    private static final VoxelShape EAST_WEST_SHAPE = box(0, 0, 4, 16, 8, 12);

    // Le constructeur est correct
    public BlockDecorativeCarving(Properties properties) {
        super(properties.mapColor(MapColor.STONE));
    }

    /**
     * CORRECTION MAJEURE: 
     * 1. La méthode est STATIC et DOIT retourner un MapCodec.
     * 2. On utilise la méthode 'simpleCodec' héritée pour créer un MapCodec.
     */
    public MapCodec<BlockDecorativeCarving> codec() {
        return simpleCodec(BlockDecorativeCarving::new);
    }
    
    // Note: On ne met PAS @Override ici, car cette méthode statique
    // est requise par la nouvelle structure de l'API Block.

    // On implémente la méthode d'instance getCodec() pour des raisons de compatibilité/convention
    protected MapCodec<? extends HorizontalDirectionalBlock> getCodec() {
        return codec();
    }


    @Override
    public boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(FACING);
        // La rotation de la forme est gérée ici.
        return (direction == Direction.NORTH || direction == Direction.SOUTH) ? NORTH_SOUTH_SHAPE : EAST_WEST_SHAPE;
    }

    @Override
    public float getShadeBrightness(BlockState state, BlockGetter world, BlockPos pos) {
        return 0.85F;
    }
}