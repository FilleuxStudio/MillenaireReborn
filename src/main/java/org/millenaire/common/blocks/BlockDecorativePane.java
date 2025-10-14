package org.millenaire.common.blocks;

import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.BlockBehaviour; // Ajouté pour la clarté

public class BlockDecorativePane extends IronBarsBlock {
    // CORRECTION : Prend les propriétés en argument (attendu par MillBlocks.java)
    // et les passe au super.
    public BlockDecorativePane(BlockBehaviour.Properties properties) {
        super(properties);
    }
}