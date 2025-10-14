package org.millenaire.common.items;

import org.millenaire.common.blocks.BlockDecorativeOrientedSlabDouble;
import org.millenaire.common.blocks.BlockDecorativeOrientedSlabHalf;

import net.minecraft.world.item.SlabItem;
import net.minecraft.world.level.block.Block;

public class ItemOrientedSlab extends SlabItem
{
    public ItemOrientedSlab(Block block, Properties properties) 
    {
        super(block, properties);
    }
    
    @Override
    public int getDamage(ItemStack stack) {
        return 0;
    }
}