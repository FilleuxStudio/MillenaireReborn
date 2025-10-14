package org.millenaire.common.items;

import java.util.List;

import org.millenaire.CommonUtilities;
import org.millenaire.Millenaire;
import org.millenaire.PlayerTracker;
import org.millenaire.common.blocks.BlockMillChest;
import org.millenaire.common.blocks.BlockMillCrops;
import org.millenaire.common.blocks.MillBlocks;
import org.millenaire.common.blocks.StoredPosition;
import org.millenaire.common.entities.EntityMillVillager;
import org.millenaire.common.blocks.TileEntityMillChest;
import org.millenaire.common.networking.PacketExportBuilding;
import org.millenaire.common.networking.PacketImportBuilding;
import org.millenaire.common.networking.PacketSayTranslatedMessage;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class ItemMillWand extends Item
{
    public ItemMillWand(Properties properties) { 
        super(properties.stacksTo(1)); 
    }

    @Override
    public InteractionResult useOn(UseOnContext context)
    {
        Level worldIn = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player playerIn = context.getPlayer();
        ItemStack stack = context.getItemInHand();
        
        if(this == MillItems.WAND_NEGATION)
        {
            if(worldIn.getBlockState(pos).getBlock() == MillBlocks.VILLAGE_STONE.get())
            {
                CompoundTag nbt = new CompoundTag();
                stack.setTag(nbt);
                nbt.putInt("X", pos.getX());
                nbt.putInt("Y", pos.getY());
                nbt.putInt("Z", pos.getZ());

                if(worldIn.isClientSide())
                {
                    // playerIn.openGui(Millenaire.instance, 2, worldIn, playerIn.getBlockX(), playerIn.getBlockY(), playerIn.getBlockZ());
                }
            }
        }

        if(this == MillItems.WAND_SUMMONING)
        {
            if(worldIn.getBlockState(pos).getBlock() == Blocks.GOLD_BLOCK)
            {
                if(!worldIn.isClientSide() && playerIn instanceof ServerPlayer)
                {	
                    System.out.println("Gold Creation");
                    // Millenaire.NETWORK.sendTo(new PacketSayTranslatedMessage("message.notimplemented"), (ServerPlayer)playerIn);
                }
            }
            else if(worldIn.getBlockState(pos).getBlock() == Blocks.OBSIDIAN)
            {	
                System.out.println("Obsidian Creation");

                CompoundTag nbt = new CompoundTag();
                stack.setTag(nbt);
                nbt.putInt("X", pos.getX());
                nbt.putInt("Y", pos.getY());
                nbt.putInt("Z", pos.getZ());

                if(!worldIn.isClientSide() && playerIn instanceof ServerPlayer)
                {
                    // Millenaire.NETWORK.sendTo(new PacketSayTranslatedMessage("message.notimplemented"), (ServerPlayer)playerIn);
                } 
            }
        }

        if(this == MillItems.WAND_CREATIVE)
        {
            // Control whether or not you can plant crops
            if(worldIn.getBlockState(pos).getBlock() instanceof BlockMillCrops)
            {
                if(playerIn.isShiftKeyDown())
                {
                    boolean hasCrop = PlayerTracker.get(playerIn).canPlayerUseCrop(((BlockMillCrops)worldIn.getBlockState(pos).getBlock()).getSeed());
                    System.out.println(worldIn.getBlockState(pos).getBlock().asItem().toString());

                    if(worldIn.isClientSide())
                    {
                        if(hasCrop)
                        {
                            playerIn.sendSystemMessage(Component.literal(playerIn.getDisplayName().getString() + " can no longer plant " + worldIn.getBlockState(pos).getBlock().getName().getString()));
                        }
                        else
                        {
                            playerIn.sendSystemMessage(Component.literal(playerIn.getDisplayName().getString() + " already could not plant " + worldIn.getBlockState(pos).getBlock().getName().getString()));
                        }
                    }
                }
                else
                {
                    boolean succeeded = false;
                    if(!PlayerTracker.get(playerIn).canPlayerUseCrop(((BlockMillCrops)worldIn.getBlockState(pos).getBlock()).getSeed()))
                    {
                        PlayerTracker.get(playerIn).setCanUseCrop(((BlockMillCrops)worldIn.getBlockState(pos).getBlock()).getSeed(), true);
                        succeeded = true;
                    }

                    if(worldIn.isClientSide())
                    {
                        if(succeeded)
                        {
                            playerIn.sendSystemMessage(Component.literal(playerIn.getDisplayName().getString() + " can now plant " + worldIn.getBlockState(pos).getBlock().getName().getString()));
                        }
                        else
                        {
                            playerIn.sendSystemMessage(Component.literal(playerIn.getDisplayName().getString() + " can already plant " + worldIn.getBlockState(pos).getBlock().getName().getString()));
                        }
                    }
                }
            }
            // Allow you to plant all Crops
            else if(worldIn.getBlockState(pos).getBlock() == Blocks.CAKE)
            {
                if(!PlayerTracker.get(playerIn).canPlayerUseCrop(MillItems.GRAPES))
                {
                    PlayerTracker.get(playerIn).setCanUseCrop(MillItems.GRAPES, true);
                }

                if(!PlayerTracker.get(playerIn).canPlayerUseCrop(MillItems.MAIZE))
                {
                    PlayerTracker.get(playerIn).setCanUseCrop(MillItems.MAIZE, true);
                }

                if(!PlayerTracker.get(playerIn).canPlayerUseCrop(MillItems.RICE))
                {
                    PlayerTracker.get(playerIn).setCanUseCrop(MillItems.RICE, true);
                }

                if(!PlayerTracker.get(playerIn).canPlayerUseCrop(MillItems.TURMERIC))
                {
                    PlayerTracker.get(playerIn).setCanUseCrop(MillItems.TURMERIC, true);
                }

                if(worldIn.isClientSide())
                {
                    playerIn.sendSystemMessage(Component.literal(playerIn.getDisplayName().getString() + " can now plant everything"));
                }
            }
            // Lock and Unlock Chests
            else if(worldIn.getBlockState(pos).getBlock() instanceof BlockMillChest)
            {
                // boolean isLocked = ((TileEntityMillChest)worldIn.getBlockEntity(pos)).setLock();

                if(worldIn.isClientSide())
                {
                    // if(isLocked)
                    // {
                    //     playerIn.sendSystemMessage(Component.literal("Chest is now Locked"));
                    // }
                    // else
                    // {
                    //     playerIn.sendSystemMessage(Component.literal("Chest is now Unlocked"));
                    // }
                }
            }
            else if(worldIn.getBlockState(pos).getBlock() instanceof StoredPosition)
            {
                if(playerIn.isShiftKeyDown())
                {
                    worldIn.removeBlock(pos, false);
                }
                else
                {
                    // worldIn.setBlockState(pos, worldIn.getBlockState(pos).cycleProperty(StoredPosition.VARIANT));
                }
            }
            // Fixes All Denier in your inventory (if no specific block/entity is clicked)
            else
            {
                CommonUtilities.changeMoney(playerIn);
                if(worldIn.isClientSide())
                {
                    playerIn.sendSystemMessage(Component.literal("Fixing Denier in " + playerIn.getDisplayName().getString() + "'s Inventory"));
                }
            }
        }

        if(this == MillItems.TUNING_FORK)
        {
            BlockState state = worldIn.getBlockState(pos);
            String output = state.getBlock().getDescriptionId() + " -";

            for(Property<?> prop : state.getProperties())
            {
                output = output.concat(" " + prop.getName() + ":" + state.getValue(prop).toString());
            }

            playerIn.sendSystemMessage(Component.literal(output));
        }

        return InteractionResult.PASS;
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity entity, InteractionHand hand)
    {
        if(stack.getItem() == MillItems.WAND_NEGATION && entity instanceof EntityMillVillager)
        {
            ((EntityMillVillager)entity).isPlayerInteracting = true;

            CompoundTag nbt = new CompoundTag();
            player.getItemInHand(hand).setTag(nbt); 
            nbt.putInt("ID", entity.getId());

            if(player.level().isClientSide())
            {
                // player.openGui(Millenaire.instance, 3, player.level(), player.getBlockX(), player.getBlockY(), player.getBlockZ());
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn)
    {
        if(stack.getItem() == MillItems.WAND_CREATIVE)
        {
            tooltip.add(Component.literal("Creative Mode ONLY").withStyle(ChatFormatting.BOLD));
        }
    }
}