package org.millenaire.common.items;

import org.millenaire.common.blocks.BlockDecorativeEarth;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class ItemBlockDecorativeEarth extends BlockItem
{
    public ItemBlockDecorativeEarth(Block block, Properties properties) 
    {
        super(block, properties);
    }

    @Override
    public String getDescriptionId(ItemStack stack)
    {
        return ((BlockDecorativeEarth)this.getBlock()).getDescriptionId(stack);
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