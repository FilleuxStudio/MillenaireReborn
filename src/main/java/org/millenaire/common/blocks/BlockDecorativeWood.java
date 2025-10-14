package org.millenaire.common.blocks;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.MapColor;

public class BlockDecorativeWood extends Block {
    public static final EnumProperty<EnumType> VARIANT = EnumProperty.create("variant", EnumType.class);

    public BlockDecorativeWood() {
        super(Properties.of()
            .mapColor(MapColor.WOOD)
            .strength(2.0F)
            .sound(net.minecraft.world.level.block.SoundType.WOOD)
        );
        this.registerDefaultState(this.stateDefinition.any().setValue(VARIANT, EnumType.PLAINTIMBERFRAME));
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(VARIANT);
    }

    public enum EnumType implements StringRepresentable {
        PLAINTIMBERFRAME("plaintimberframe"),
        CROSSTIMBERFRAME("crosstimberframe"),
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

        @Override
        public String toString() {
            return this.name;
        }
    }
}