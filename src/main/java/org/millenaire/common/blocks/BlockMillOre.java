package org.millenaire.common.blocks;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public class BlockMillOre extends Block {
    private final EnumMillOre oreType;
    
    public BlockMillOre(EnumMillOre oretype) {
        super(Properties.of()
            .mapColor(MapColor.STONE)
            .strength(3.0F, 3.0F)
            .requiresCorrectToolForDrops()
        );
        this.oreType = oretype;
    }

      public BlockMillOre(EnumMillOre oretype, BlockBehaviour.Properties properties) {
        super(properties); // On passe les propriétés au constructeur de la classe mère
        this.oreType = oretype; // On stocke l'Enum
        
        // Toutes les propriétés (mapColor, strength, etc.) sont définies dans MillBlocks
    }
    
    public static enum EnumMillOre implements StringRepresentable {
        GALIANITE("galianite", 2, 2); // Ajoutez l'item dropped si nécessaire

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

        @Override
        public String toString() {
            return this.name;
        }
    }
}