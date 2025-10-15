package org.millenaire.common.blocks;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.MapColor;

public class BlockDecorativeWood extends Block {
    public static final EnumProperty<EnumType> VARIANT = EnumProperty.create("variant", EnumType.class);

    public BlockDecorativeWood(Properties properties) {
        super(properties.mapColor(MapColor.WOOD));
        this.registerDefaultState(this.stateDefinition.any().setValue(VARIANT, EnumType.PLAIN_TIMBER_FRAME));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(VARIANT);
    }

    public enum EnumType implements StringRepresentable {
        PLAIN_TIMBER_FRAME("plain_timber_frame"),
        CROSS_TIMBER_FRAME("cross_timber_frame"),
        THATCH("thatch"),
        SERICULTURE("sericulture");

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