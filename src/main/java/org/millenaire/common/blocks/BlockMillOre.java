package org.millenaire.common.blocks;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.MapColor;

public class BlockMillOre extends Block {
    public static final EnumProperty<EnumMillOre> VARIANT = EnumProperty.create("variant", EnumMillOre.class);
    private final EnumMillOre oreType;

    public BlockMillOre(EnumMillOre oretype, Properties properties) {
        super(properties.mapColor(MapColor.STONE));
        this.oreType = oretype;
        this.registerDefaultState(this.stateDefinition.any().setValue(VARIANT, oretype));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(VARIANT);
    }

    public enum EnumMillOre implements StringRepresentable {
        GALIANITE("galianite", 2, 2);

        private final String name;
        final int harvestLevel;
        final int maxDropped;

        EnumMillOre(String name, int tool, int maxDropped) {
            this.name = name;
            this.harvestLevel = tool;
            this.maxDropped = maxDropped;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }
}