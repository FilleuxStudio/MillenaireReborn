package org.millenaire.common.items;

import org.millenaire.common.PlayerTracker;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class ItemMillSeeds extends ItemNameBlockItem
{
    public ItemMillSeeds(Block crops, Properties properties)
    {
        super(crops, properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context)
    {
        Level worldIn = context.getLevel();
        Player playerIn = context.getPlayer();
        
        if (!worldIn.isClientSide() && playerIn != null && 
            PlayerTracker.get(playerIn).canPlayerUseCrop(this)) {
            return super.useOn(context);
        }
        
        return InteractionResult.FAIL;
    }
}