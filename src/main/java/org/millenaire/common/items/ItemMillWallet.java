package org.millenaire.common.items;

import java.util.List;

import org.millenaire.CommonUtilities;
import org.millenaire.client.gui.MillAchievement;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class ItemMillWallet extends Item
{
    public ItemMillWallet(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn)
    {
        ItemStack itemStackIn = playerIn.getItemInHand(handIn);
        
        if(playerIn.getInventory().hasAnyMatching(stack -> 
            stack.getItem() == MillItems.DENIER || 
            stack.getItem() == MillItems.DENIER_ARGENT || 
            stack.getItem() == MillItems.DENIER_OR))
        {
            addDenierToWallet(itemStackIn, playerIn);
        }
        else
        {
            emptyWallet(itemStackIn, playerIn);
        }
        
        return InteractionResultHolder.sidedSuccess(itemStackIn, worldIn.isClientSide());
    }
    
    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn)
    {
        if(stack.hasTag())
        {
            CompoundTag nbt = stack.getTag();
            
            if(nbt.contains("DenierOr") && nbt.getInt("DenierOr") > 0)
            {
                String or = nbt.getInt("DenierOr") + "o ";
                String argent = nbt.getInt("DenierArgent") + "a ";
                String denier = nbt.getInt("Denier") + "d";

                tooltip.add(Component.literal(or).withStyle(ChatFormatting.YELLOW)
                    .append(Component.literal(argent).withStyle(ChatFormatting.GRAY))
                    .append(Component.literal(denier).withStyle(ChatFormatting.GOLD)));
            }
            else if(nbt.contains("DenierArgent") && nbt.getInt("DenierArgent") > 0)
            {
                String argent = nbt.getInt("DenierArgent") + "a ";
                String denier = nbt.getInt("Denier") + "d";

                tooltip.add(Component.literal(argent).withStyle(ChatFormatting.GRAY)
                    .append(Component.literal(denier).withStyle(ChatFormatting.GOLD)));
            }
            else if(nbt.contains("Denier"))
            {
                String denier = nbt.getInt("Denier") + "d";
                tooltip.add(Component.literal(denier).withStyle(ChatFormatting.GOLD));
            }
        }
    }

    private void addDenierToWallet(ItemStack stack, Player playerIn)
    {
        if(stack.getItem() == this)
        {
            CommonUtilities.changeMoney(playerIn);
            
            int denier = 0;
            int argent = 0;
            int or = 0;
            
            for(int i = 0; i < playerIn.getInventory().getContainerSize(); i++)
            {
                ItemStack slotStack = playerIn.getInventory().getItem(i);
                if(!slotStack.isEmpty())
                {
                    if(slotStack.getItem() == MillItems.DENIER)
                    {
                        denier += slotStack.getCount();
                        playerIn.getInventory().setItem(i, ItemStack.EMPTY);
                    }
                    else if(slotStack.getItem() == MillItems.DENIER_ARGENT)
                    {
                        argent += slotStack.getCount();
                        playerIn.getInventory().setItem(i, ItemStack.EMPTY);
                    }
                    else if(slotStack.getItem() == MillItems.DENIER_OR)
                    {
                        or += slotStack.getCount();
                        playerIn.getInventory().setItem(i, ItemStack.EMPTY);
                    }
                }
            }
            
            CompoundTag nbt;
            
            if(!stack.hasTag())
            {
                nbt = new CompoundTag();
                stack.setTag(nbt);
            }
            else
                nbt = stack.getTag();
            
            denier += nbt.getInt("Denier");
            argent += nbt.getInt("DenierArgent");
            or += nbt.getInt("DenierOr");
            
            if(or >= 1)
                playerIn.awardStat(MillAchievement.CRESUS.get());
            
            nbt.putInt("Denier", denier);
            nbt.putInt("DenierArgent", argent);
            nbt.putInt("DenierOr", or);
        }
    }

    private void emptyWallet(ItemStack stack, Player playerIn)
    {
        if(stack.hasTag())
        {
            CompoundTag nbt = stack.getTag();
            
            if(nbt.contains("DenierOr") && nbt.getInt("DenierOr") > 0)
            {
                ItemStack or = new ItemStack(MillItems.DENIER_OR, nbt.getInt("DenierOr"));
                playerIn.getInventory().add(or);
            }
            
            if(nbt.contains("DenierArgent") && nbt.getInt("DenierArgent") > 0)
            {
                ItemStack argent = new ItemStack(MillItems.DENIER_ARGENT, nbt.getInt("DenierArgent"));
                playerIn.getInventory().add(argent);
            }
            
            if(nbt.contains("Denier") && nbt.getInt("Denier") > 0)
            {
                ItemStack denier = new ItemStack(MillItems.DENIER, nbt.getInt("Denier"));
                playerIn.getInventory().add(denier);
            }
            
            stack.setTag(null);
        }
    }
}