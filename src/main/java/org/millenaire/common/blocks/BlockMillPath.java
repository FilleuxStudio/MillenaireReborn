package org.millenaire.common.blocks;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.MapColor;

public class BlockMillPath extends Block {
    public static final EnumProperty<EnumType> VARIANT = EnumProperty.create("variant", EnumType.class);

    public BlockMillPath(Properties properties) {
        super(properties.mapColor(MapColor.DIRT));
        this.registerDefaultState(this.stateDefinition.any().setValue(VARIANT, EnumType.DIRT));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(VARIANT);
    }

    public enum EnumType implements StringRepresentable {
        DIRT("dirt"),
        GRAVEL("gravel"),
        SLAB("slab"),
        SANDSTONE_SLAB("sandstone_slab"),
        OCHRE_SLAB("ochre_slab"),
        SLAB_AND_GRAVEL("slab_and_gravel");

        private final String name;

        EnumType(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }
}