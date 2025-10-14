package org.millenaire.common.blocks;

import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.material.MapColor;

public class BlockDecorativePane extends IronBarsBlock {
    public BlockDecorativePane() { 
        super(Properties.of().mapColor(MapColor.METAL));
    }
}