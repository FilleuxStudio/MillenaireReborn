package org.millenaire.common.items;

import org.millenaire.Millenaire;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ItemMillParchment extends Item {
    public final String title;
    public final String[] contents;

    public ItemMillParchment(String titleIn, String[] contentIn, Properties properties) {
        super(properties);
        title = titleIn;
        contents = contentIn;
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        
        if(world.isClientSide()) {
            // Ouvre le GUI du parchemin
            // player.openGui(Millenaire.instance, 0, world, player.getBlockX(), player.getBlockY(), player.getBlockZ());
        }
        
        return InteractionResultHolder.sidedSuccess(itemStack, world.isClientSide());
    }
}