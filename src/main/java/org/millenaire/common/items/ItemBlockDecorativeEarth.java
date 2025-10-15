package org.millenaire.common.items;

import org.millenaire.common.blocks.BlockDecorativeEarth;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class ItemBlockDecorativeEarth extends BlockItem {
    
    public ItemBlockDecorativeEarth(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
        // Appel à la méthode correcte du bloc
        return super.getDescriptionId(stack);
    }
    
    @Override
    public int getDamage(ItemStack stack) {
        return stack.getDamageValue();
    }
}