package org.millenaire.common.items;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class ItemBlockDecorativeWood extends BlockItem {
    
    public ItemBlockDecorativeWood(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
        return super.getDescriptionId(stack);
    }
}