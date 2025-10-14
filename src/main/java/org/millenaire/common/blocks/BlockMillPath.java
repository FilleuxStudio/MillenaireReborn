package org.millenaire.common.blocks;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockMillPath extends Block {
    public static final EnumProperty<EnumType> VARIANT = EnumProperty.create("variant", EnumType.class);
    private static final VoxelShape SHAPE = box(0.0, 0.0, 0.0, 16.0, 15.0, 16.0);

    // CORRECTION : Accepter les propriétés dans le constructeur
    public BlockMillPath(BlockBehaviour.Properties properties) { 
        super(properties); // Passer les propriétés à la classe mère
        this.registerDefaultState(this.stateDefinition.any().setValue(VARIANT, EnumType.DIRT));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(VARIANT);
    }

    @Override
    public VoxelShape getShape(BlockState state, net.minecraft.world.level.BlockGetter level, net.minecraft.core.BlockPos pos, net.minecraft.world.phys.shapes.CollisionContext context) {
        return SHAPE;
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }

    public static enum EnumType implements StringRepresentable {
        DIRT("dirt"),
        GRAVEL("gravel"),
        SLAB("slab"),
        SANDSTONESLAB("sandstoneslab"),
        OCHRESLAB("ochreslab"),
        SLABANDGRAVEL("slabandgravel");
        
        private final String name;

        EnumType(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }
}