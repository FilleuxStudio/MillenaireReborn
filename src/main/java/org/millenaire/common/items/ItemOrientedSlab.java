package org.millenaire.common.items;

import org.millenaire.common.blocks.BlockDecorativeOrientedSlabDouble;
import org.millenaire.common.blocks.BlockDecorativeOrientedSlabHalf;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;

public class ItemOrientedSlab extends BlockItem {
    
    private final SlabBlock singleSlab;
    private final SlabBlock doubleSlab;

    public ItemOrientedSlab(Block block, BlockDecorativeOrientedSlabHalf singleSlab, BlockDecorativeOrientedSlabDouble doubleSlab, Properties properties) {
        super(block, properties);
        this.singleSlab = singleSlab;
        this.doubleSlab = doubleSlab;
    }
    
    @Override
    public int getDamage(ItemStack stack) {
        return 0;
    }
}