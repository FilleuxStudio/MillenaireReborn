package org.millenaire.common.items;

import java.util.List;

import org.millenaire.CommonUtilities;

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

import org.jetbrains.annotations.Nullable;

public class ItemMillWallet extends Item {
    
    public ItemMillWallet(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        
        if(player.getInventory().contains(MillItems.DENIER.get().getDefaultInstance()) || 
           player.getInventory().contains(MillItems.DENIER_ARGENT.get().getDefaultInstance()) || 
           player.getInventory().contains(MillItems.DENIER_OR.get().getDefaultInstance())) {
            addDenierToWallet(itemStack, player);
        } else {
            emptyWallet(itemStack, player);
        }
        
        return InteractionResultHolder.sidedSuccess(itemStack, world.isClientSide());
    }
    
    // Retirez le @Override si ça cause une erreur
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
        CompoundTag nbt = stack.getTag();
        if(nbt != null) {
            if(nbt.contains("DenierOr") && nbt.getInt("DenierOr") > 0) {
                String or = nbt.getInt("DenierOr") + "o ";
                String argent = nbt.getInt("DenierArgent") + "a ";
                String denier = nbt.getInt("Denier") + "d";

                tooltip.add(Component.literal(or).withStyle(ChatFormatting.YELLOW)
                    .append(Component.literal(argent).withStyle(ChatFormatting.GRAY))
                    .append(Component.literal(denier).withStyle(ChatFormatting.GOLD)));
            } else if(nbt.contains("DenierArgent") && nbt.getInt("DenierArgent") > 0) {
                String argent = nbt.getInt("DenierArgent") + "a ";
                String denier = nbt.getInt("Denier") + "d";

                tooltip.add(Component.literal(argent).withStyle(ChatFormatting.GRAY)
                    .append(Component.literal(denier).withStyle(ChatFormatting.GOLD)));
            } else if(nbt.contains("Denier") && nbt.getInt("Denier") > 0) {
                String denier = nbt.getInt("Denier") + "d";
                tooltip.add(Component.literal(denier).withStyle(ChatFormatting.GOLD));
            }
        }
    }

    private void addDenierToWallet(ItemStack stack, Player player) {
        if(stack.getItem() == this) {
            CommonUtilities.changeMoney(player);
            
            int denier = 0;
            int argent = 0;
            int or = 0;
            
            for(int i = 0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack slotStack = player.getInventory().getItem(i);
                if(!slotStack.isEmpty()) {
                    if(slotStack.getItem() == MillItems.DENIER.get()) {
                        denier += slotStack.getCount();
                        player.getInventory().setItem(i, ItemStack.EMPTY);
                    } else if(slotStack.getItem() == MillItems.DENIER_ARGENT.get()) {
                        argent += slotStack.getCount();
                        player.getInventory().setItem(i, ItemStack.EMPTY);
                    } else if(slotStack.getItem() == MillItems.DENIER_OR.get()) {
                        or += slotStack.getCount();
                        player.getInventory().setItem(i, ItemStack.EMPTY);
                    }
                }
            }
            
            CompoundTag nbt = stack.getOrCreateTag();
            
            denier += nbt.getInt("Denier");
            argent += nbt.getInt("DenierArgent");
            or += nbt.getInt("DenierOr");
            
            // Achievement à adapter
            // if(or >= 1) {
            //     player.awardStat(MillAchievement.CRESUS.get());
            // }
            
            nbt.putInt("Denier", denier);
            nbt.putInt("DenierArgent", argent);
            nbt.putInt("DenierOr", or);
        }
    }

    private void emptyWallet(ItemStack stack, Player player) {
        CompoundTag nbt = stack.getTag();
        if(nbt != null) {
            if(nbt.contains("DenierOr") && nbt.getInt("DenierOr") > 0) {
                ItemStack or = new ItemStack(MillItems.DENIER_OR.get(), nbt.getInt("DenierOr"));
                player.getInventory().add(or);
            }
            
            if(nbt.contains("DenierArgent") && nbt.getInt("DenierArgent") > 0) {
                ItemStack argent = new ItemStack(MillItems.DENIER_ARGENT.get(), nbt.getInt("DenierArgent"));
                player.getInventory().add(argent);
            }
            
            if(nbt.contains("Denier") && nbt.getInt("Denier") > 0) {
                ItemStack denier = new ItemStack(MillItems.DENIER.get(), nbt.getInt("Denier"));
                player.getInventory().add(denier);
            }
            
            stack.setTag(null);
        }
    }
}