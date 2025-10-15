package org.millenaire.common.blocks;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.MapColor;

public class BlockDecorativeStone extends Block {
    public static final EnumProperty<EnumType> VARIANT = EnumProperty.create("variant", EnumType.class);

    public BlockDecorativeStone(Properties properties) {
        super(properties.mapColor(MapColor.STONE));
        this.registerDefaultState(this.stateDefinition.any().setValue(VARIANT, EnumType.GOLD_ORNAMENT));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(VARIANT);
    }

    public enum EnumType implements StringRepresentable {
        GOLD_ORNAMENT("gold_ornament"),
        COOKED_BRICK("cooked_brick"),
        GALIANITE_BLOCK("galianite_block");

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