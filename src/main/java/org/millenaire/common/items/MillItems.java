package org.millenaire.common.items;

import org.millenaire.Millenaire;
import org.millenaire.common.blocks.BlockMillCrops;
import org.millenaire.common.blocks.MillBlocks;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredItem;
import net.minecraft.core.registries.Registries;

public class MillItems 
{
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Millenaire.MODID);
    
    // Basic Items
    public static final DeferredItem<Item> DENIER = ITEMS.register("denier", 
        () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> DENIER_OR = ITEMS.register("denier_or", 
        () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> DENIER_ARGENT = ITEMS.register("denier_argent", 
        () -> new Item(new Item.Properties()));
    
    public static final DeferredItem<Item> SILK = ITEMS.register("silk", 
        () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> OBSIDIAN_FLAKE = ITEMS.register("obsidian_flake", 
        () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> UNKNOWN_POWDER = ITEMS.register("unknown_powder", 
        () -> new Item(new Item.Properties()));
    
    // Crops
    public static final DeferredItem<Item> TURMERIC = ITEMS.register("turmeric", 
        () -> new ItemMillSeeds(MillBlocks.CROP_TURMERIC.get(), new Item.Properties()));
    public static final DeferredItem<Item> RICE = ITEMS.register("rice", 
        () -> new ItemMillSeeds(MillBlocks.CROP_RICE.get(), new Item.Properties()));
    public static final DeferredItem<Item> MAIZE = ITEMS.register("maize", 
        () -> new ItemMillSeeds(MillBlocks.CROP_MAIZE.get(), new Item.Properties()));
    public static final DeferredItem<Item> GRAPES = ITEMS.register("grapes", 
        () -> new ItemMillSeeds(MillBlocks.CROP_GRAPE_VINE.get(), new Item.Properties()));
    
    // Food
    public static final DeferredItem<Item> CIDER_APPLE = ITEMS.register("cider_apple", 
        () -> new ItemMillFood(0, 0, 0, 1, 0.05F, false, new Item.Properties()));
    public static final DeferredItem<Item> CIDER = ITEMS.register("cider", 
        () -> new ItemMillFood(4, 15, 5, 0, 0.0F, true, new Item.Properties()));
    
    // Armor
    public static final DeferredItem<Item> NORMAN_HELMET = ITEMS.register("norman_helmet", 
        () -> new ArmorItem(ItemMillArmor.ARMOR_NORMAN, ArmorItem.Type.HELMET, new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(66))));
    public static final DeferredItem<Item> NORMAN_CHESTPLATE = ITEMS.register("norman_chestplate", 
        () -> new ArmorItem(ItemMillArmor.ARMOR_NORMAN, ArmorItem.Type.CHESTPLATE, new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(66))));
    
    // Wands
    public static final DeferredItem<Item> WAND_SUMMONING = ITEMS.register("wand_summoning", 
        () -> new ItemMillWand(new Item.Properties()));
    public static final DeferredItem<Item> WAND_NEGATION = ITEMS.register("wand_negation", 
        () -> new ItemMillWand(new Item.Properties()));
    public static final DeferredItem<Item> WAND_CREATIVE = ITEMS.register("wand_creative", 
        () -> new ItemMillWand(new Item.Properties()));
    public static final DeferredItem<Item> TUNING_FORK = ITEMS.register("tuning_fork", 
        () -> new ItemMillWand(new Item.Properties()));
    
    // Tools
    public static final DeferredItem<Item> NORMAN_AXE = ITEMS.register("norman_axe", 
        () -> new ItemMillTool.ItemMillAxe(ItemMillTool.TOOLS_NORMAN, new Item.Properties().durability(1561)));
    public static final DeferredItem<Item> NORMAN_SHOVEL = ITEMS.register("norman_shovel", 
        () -> new ItemMillTool.ItemMillShovel(ItemMillTool.TOOLS_NORMAN, new Item.Properties().durability(1561)));
    public static final DeferredItem<Item> NORMAN_PICKAXE = ITEMS.register("norman_pickaxe", 
        () -> new ItemMillTool.ItemMillPickaxe(ItemMillTool.TOOLS_NORMAN, new Item.Properties().durability(1561)));
    
    // Amulets
    public static final DeferredItem<Item> AMULET_SKOLL_HATI = ITEMS.register("amulet_skoll_hati", 
        () -> new ItemMillAmulet(new Item.Properties()));
    
    // Wallet
    public static final DeferredItem<Item> ITEM_MILL_PURSE = ITEMS.register("item_mill_purse", 
        () -> new ItemMillWallet(new Item.Properties()));
    
    // Sign
    public static final DeferredItem<Item> ITEM_MILL_SIGN = ITEMS.register("item_mill_sign", 
        () -> new ItemMillSign(new Item.Properties()));

    // Méthode pour initialiser les propriétés des cultures
    public static void initializeCropProperties() {
        ((BlockMillCrops) MillBlocks.CROP_TURMERIC.get()).setSeed(TURMERIC.get());
        ((BlockMillCrops) MillBlocks.CROP_RICE.get()).setSeed(RICE.get());
        ((BlockMillCrops) MillBlocks.CROP_MAIZE.get()).setSeed(MAIZE.get());
        ((BlockMillCrops) MillBlocks.CROP_GRAPE_VINE.get()).setSeed(GRAPES.get());
    }

    // ItemBlocks
public static final DeferredItem<BlockItem> DECORATIVE_STONE = ITEMS.register("decorative_stone", 
    () -> new ItemBlockDecorativeStone(MillBlocks.DECORATIVE_STONE.get(), new Item.Properties()));
public static final DeferredItem<BlockItem> DECORATIVE_WOOD = ITEMS.register("decorative_wood", 
    () -> new ItemBlockDecorativeWood(MillBlocks.DECORATIVE_WOOD.get(), new Item.Properties()));
public static final DeferredItem<BlockItem> DECORATIVE_EARTH = ITEMS.register("decorative_earth", 
    () -> new ItemBlockDecorativeEarth(MillBlocks.DECORATIVE_EARTH.get(), new Item.Properties()));
public static final DeferredItem<BlockItem> DECORATIVE_SOD_PLANK = ITEMS.register("decorative_sod_plank", 
    () -> new ItemBlockDecorativeSodPlank(MillBlocks.DECORATIVE_SOD_PLANK.get(), new Item.Properties()));
public static final DeferredItem<BlockItem> MILL_PATH = ITEMS.register("mill_path", 
    () -> new ItemMillPath(MillBlocks.MILL_PATH.get(), new Item.Properties()));
public static final DeferredItem<BlockItem> MILL_PATH_SLAB = ITEMS.register("mill_path_slab", 
    () -> new ItemMillPathSlab(MillBlocks.MILL_PATH_SLAB.get(), new Item.Properties()));
public static final DeferredItem<BlockItem> ORIENTED_SLAB = ITEMS.register("oriented_slab", 
    () -> new ItemOrientedSlab(MillBlocks.ORIENTED_SLAB.get(), new Item.Properties()));
}