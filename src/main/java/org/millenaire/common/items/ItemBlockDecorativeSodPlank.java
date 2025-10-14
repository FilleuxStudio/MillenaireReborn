package org.millenaire.common.items;

import org.millenaire.common.blocks.BlockDecorativeSodPlank;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class ItemBlockDecorativeSodPlank extends BlockItem
{
    public ItemBlockDecorativeSodPlank(Block block, Properties properties) 
    {
        super(block, properties);
    }

    @Override
    public String getDescriptionId(ItemStack stack)
    {
        return ((BlockDecorativeSodPlank)this.getBlock()).getDescriptionId(stack);
    }
    
    @Override
    public int getDamage(ItemStack stack) {
        return 0;
    }
    
    @Override
    public int getMaxDamage(ItemStack stack) {
        return 0;
    }
    
    @Override
    public boolean isDamaged(ItemStack stack) {
        return false;
    }
}