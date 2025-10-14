package org.millenaire.common.items;

import org.millenaire.Millenaire;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.WritableBookItem;
import net.minecraft.world.level.Level;

public class ItemMillParchment extends WritableBookItem
{
    public final String title;
    public final String[] contents;

    public ItemMillParchment(String titleIn, String[] contentIn, Properties properties)
    {
        super(properties);
        title = titleIn;
        contents = contentIn;
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn)
    {
        ItemStack itemStackIn = playerIn.getItemInHand(handIn);
        
        if(worldIn.isClientSide())
        {
            // Ouvre le GUI côté client
            // playerIn.openGui(Millenaire.instance, 0, worldIn, playerIn.getBlockX(), playerIn.getBlockY(), playerIn.getBlockZ());
        }
        
        return InteractionResultHolder.sidedSuccess(itemStackIn, worldIn.isClientSide());
    }
}