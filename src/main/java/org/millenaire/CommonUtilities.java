package org.millenaire;

import org.millenaire.client.gui.MillAchievement;
import org.millenaire.common.items.MillItems;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.Random;

public class CommonUtilities {
    public static Random random = new Random();
    
    /**
     * Organizes the player's money
     * @param playerIn The player to organize
     */
    public static void changeMoney(Player playerIn) {
        ItemStack denier = new ItemStack(MillItems.denier, 0);
        ItemStack argent = new ItemStack(MillItems.denierArgent, 0);
        ItemStack or = new ItemStack(MillItems.denierOr, 0);
        
        for(int i = 0; i < playerIn.getInventory().getContainerSize(); i++) {
            ItemStack stack = playerIn.getInventory().getItem(i);
            if(!stack.isEmpty()) {
                if(stack.getItem() == MillItems.denier) {
                    denier.setCount(denier.getCount() + stack.getCount());
                    playerIn.getInventory().setItem(i, ItemStack.EMPTY);
                }
                if(stack.getItem() == MillItems.denierArgent) {
                    argent.setCount(argent.getCount() + stack.getCount());
                    playerIn.getInventory().setItem(i, ItemStack.EMPTY);
                }
                if(stack.getItem() == MillItems.denierOr) {
                    or.setCount(or.getCount() + stack.getCount());
                    playerIn.getInventory().setItem(i, ItemStack.EMPTY);
                }
            }
        }
        
        argent.setCount(argent.getCount() + (denier.getCount() / 64));
        denier.setCount(denier.getCount() % 64);
        
        or.setCount(or.getCount() + (argent.getCount() / 64));
        if(or.getCount() >= 1) {
            // Achievement system changed in newer versions - you'll need to adapt this
            // playerIn.awardStat(MillAchievement.cresus, 1);
        }

        argent.setCount(argent.getCount() % 64);
        
        if(!denier.isEmpty()) {
            playerIn.getInventory().add(denier);
        }
        if(!argent.isEmpty()) {
            playerIn.getInventory().add(argent);
        }
        
        while(or.getCount() > 64) {
            playerIn.getInventory().add(new ItemStack(MillItems.denierOr, 64));
            or.setCount(or.getCount() - 64);
        }

        if(!or.isEmpty()) {
            playerIn.getInventory().add(or);
        }
    }
    
    /**
     * @return A random non-zero float
     */
    public static float getRandomNonzero() { 
        return random.nextFloat() + 0.1f; 
    }
    
    /**
     * Gets a random Millager Gender
     * @return gender value
     */
    public static int randomizeGender() { 
        return random.nextInt(3) - 2; 
    }
    
    /**
     * Gets a valid ground block for generation
     * @param b the block to check
     * @param surface if the ground is on the top of the ground (true) or underground (false)
     * @return the appropriate ground block
     */
    public static Block getValidGroundBlock(final Block b, final boolean surface) {
        if (b == Blocks.BEDROCK || b == Blocks.DIRT || b == Blocks.GRASS_BLOCK) {
            return Blocks.DIRT;
        } else if (b == Blocks.STONE) {
            if (surface) {
                return Blocks.DIRT;
            } else {
                return Blocks.GRASS_BLOCK;
            }
        } else if (b == Blocks.GRAVEL) {
            return Blocks.GRAVEL;
        } else if (b == Blocks.SAND) {
            return Blocks.SAND;
        } else if (b == Blocks.SANDSTONE) {
            if (surface) {
                return Blocks.SAND;
            } else {
                return Blocks.SANDSTONE;
            }
        }

        return null;
    }
}