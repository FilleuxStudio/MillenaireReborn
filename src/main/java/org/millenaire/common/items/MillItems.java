package org.millenaire.common.items;


import org.millenaire.Millenaire;
import org.millenaire.common.blocks.MillBlocks;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.function.Supplier;

public class MillItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Millenaire.MODID);
    
    // Basic items
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
    
    public static final DeferredItem<Item> WOOL_CLOTHES = ITEMS.register("wool_clothes", 
        () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SILK_CLOTHES = ITEMS.register("silk_clothes", 
        () -> new Item(new Item.Properties()));
    
    public static final DeferredItem<Item> GALIANITE_DUST = ITEMS.register("galianite_dust", 
        () -> new Item(new Item.Properties()));
    
    // Crops
    public static final DeferredItem<Item> TURMERIC = ITEMS.register("turmeric", 
        () -> new ItemNameBlockItem(MillBlocks.CROP_TURMERIC.get(), new Item.Properties()));
    public static final DeferredItem<Item> RICE = ITEMS.register("rice", 
        () -> new ItemNameBlockItem(MillBlocks.CROP_RICE.get(), new Item.Properties()));
    public static final DeferredItem<Item> MAIZE = ITEMS.register("maize", 
        () -> new ItemNameBlockItem(MillBlocks.CROP_MAIZE.get(), new Item.Properties()));
    public static final DeferredItem<Item> GRAPES = ITEMS.register("grapes", 
        () -> new ItemNameBlockItem(MillBlocks.CROP_GRAPE_VINE.get(), new Item.Properties()));
    
    // Foods
    public static final DeferredItem<Item> CIDER_APPLE = ITEMS.register("cider_apple", 
        () -> new Item(new Item.Properties().food(
            new FoodProperties.Builder().nutrition(1).saturationModifier(0.05f).build()
        )));
    
    public static final DeferredItem<Item> CIDER = ITEMS.register("cider", 
        () -> new ItemMillFood(new Item.Properties().food(
            new FoodProperties.Builder().nutrition(4).saturationModifier(0.0f).build()
        ).stacksTo(16), true));
    
    public static final DeferredItem<Item> CALVA = ITEMS.register("calva", 
        () -> new ItemMillFood(new Item.Properties().food(
            new FoodProperties.Builder().nutrition(8).saturationModifier(0.0f).build()
        ).stacksTo(16), true));
    
    // Tools
    public static final DeferredItem<Item> NORMAN_AXE = ITEMS.register("norman_axe", 
        () -> new AxeItem(Tiers.IRON, new Item.Properties().attributes(AxeItem.createAttributes(Tiers.IRON, 6.0F, -3.1F))));
    
    public static final DeferredItem<Item> NORMAN_SHOVEL = ITEMS.register("norman_shovel", 
        () -> new ShovelItem(Tiers.IRON, new Item.Properties().attributes(ShovelItem.createAttributes(Tiers.IRON, 1.5F, -3.0F))));
    
    public static final DeferredItem<Item> NORMAN_PICKAXE = ITEMS.register("norman_pickaxe", 
        () -> new PickaxeItem(Tiers.IRON, new Item.Properties().attributes(PickaxeItem.createAttributes(Tiers.IRON, 1, -2.8F))));
    
    public static final DeferredItem<Item> NORMAN_HOE = ITEMS.register("norman_hoe", 
        () -> new HoeItem(Tiers.IRON, new Item.Properties().attributes(HoeItem.createAttributes(Tiers.IRON, 0, -3.0F))));
    
    public static final DeferredItem<Item> NORMAN_SWORD = ITEMS.register("norman_sword", 
        () -> new SwordItem(Tiers.IRON, new Item.Properties().attributes(SwordItem.createAttributes(Tiers.IRON, 3, -2.4F))));
    
    // Mayan tools
    public static final DeferredItem<Item> MAYAN_AXE = ITEMS.register("mayan_axe", 
        () -> new AxeItem(Tiers.STONE, new Item.Properties().attributes(AxeItem.createAttributes(Tiers.STONE, 6.0F, -3.2F))));
    
    public static final DeferredItem<Item> MAYAN_SHOVEL = ITEMS.register("mayan_shovel", 
        () -> new ShovelItem(Tiers.STONE, new Item.Properties().attributes(ShovelItem.createAttributes(Tiers.STONE, 1.5F, -3.0F))));
    
    public static final DeferredItem<Item> MAYAN_PICKAXE = ITEMS.register("mayan_pickaxe", 
        () -> new PickaxeItem(Tiers.STONE, new Item.Properties().attributes(PickaxeItem.createAttributes(Tiers.STONE, 1, -2.8F))));
    
    public static final DeferredItem<Item> MAYAN_HOE = ITEMS.register("mayan_hoe", 
        () -> new HoeItem(Tiers.STONE, new Item.Properties().attributes(HoeItem.createAttributes(Tiers.STONE, -1, -2.0F))));
    
    public static final DeferredItem<Item> MAYAN_MACE = ITEMS.register("mayan_mace", 
        () -> new SwordItem(Tiers.STONE, new Item.Properties().attributes(SwordItem.createAttributes(Tiers.STONE, 3, -2.4F))));
    
    // Byzantine tools
    public static final DeferredItem<Item> BYZANTINE_MACE = ITEMS.register("byzantine_mace", 
        () -> new SwordItem(Tiers.IRON, new Item.Properties().attributes(SwordItem.createAttributes(Tiers.IRON, 4, -2.0F))));
    
    // Japanese tools
    public static final DeferredItem<Item> JAPANESE_SWORD = ITEMS.register("japanese_sword", 
        () -> new SwordItem(Tiers.IRON, new Item.Properties().attributes(SwordItem.createAttributes(Tiers.IRON, 3, -2.0F))));
    
    public static final DeferredItem<Item> JAPANESE_BOW = ITEMS.register("japanese_bow", 
        () -> new BowItem(new Item.Properties().durability(384)));
    
// Armor
public static final DeferredItem<Item> NORMAN_HELMET = ITEMS.register("norman_helmet", 
    () -> new ArmorItem(ItemMillArmor.ARMOR_NORMAN, ArmorItem.Type.HELMET, new Item.Properties()));

public static final DeferredItem<Item> NORMAN_CHESTPLATE = ITEMS.register("norman_chestplate", 
    () -> new ArmorItem(ItemMillArmor.ARMOR_NORMAN, ArmorItem.Type.CHESTPLATE, new Item.Properties()));

public static final DeferredItem<Item> NORMAN_LEGGINGS = ITEMS.register("norman_leggings", 
    () -> new ArmorItem(ItemMillArmor.ARMOR_NORMAN, ArmorItem.Type.LEGGINGS, new Item.Properties()));

public static final DeferredItem<Item> NORMAN_BOOTS = ITEMS.register("norman_boots", 
    () -> new ArmorItem(ItemMillArmor.ARMOR_NORMAN, ArmorItem.Type.BOOTS, new Item.Properties()));

// Pour la couronne maya
public static final DeferredItem<Item> MAYAN_QUEST_CROWN = ITEMS.register("mayan_quest_crown", 
    () -> new ItemMillArmor.MayanQuestCrown(ItemMillArmor.ARMOR_MAYAN_QUEST, new Item.Properties()));
    
    // Wands
    public static final DeferredItem<Item> WAND_SUMMONING = ITEMS.register("wand_summoning", 
        () -> new ItemMillWand(new Item.Properties().stacksTo(1)));
    
    public static final DeferredItem<Item> WAND_NEGATION = ITEMS.register("wand_negation", 
        () -> new ItemMillWand(new Item.Properties().stacksTo(1)));
    
    public static final DeferredItem<Item> WAND_CREATIVE = ITEMS.register("wand_creative", 
        () -> new ItemMillWand(new Item.Properties().stacksTo(1)));
    
    public static final DeferredItem<Item> TUNING_FORK = ITEMS.register("tuning_fork", 
        () -> new ItemMillWand(new Item.Properties().stacksTo(1)));
    
    // Amulets
    public static final DeferredItem<Item> AMULET_SKOLL_HATI = ITEMS.register("amulet_skoll_hati", 
        () -> new ItemMillAmulet(new Item.Properties().stacksTo(1)));
    
    public static final DeferredItem<Item> AMULET_ALCHEMIST = ITEMS.register("amulet_alchemist", 
        () -> new ItemMillAmulet(new Item.Properties().stacksTo(1)));
    
    public static final DeferredItem<Item> AMULET_VISHNU = ITEMS.register("amulet_vishnu", 
        () -> new ItemMillAmulet(new Item.Properties().stacksTo(1)));
    
    public static final DeferredItem<Item> AMULET_YGGDRASIL = ITEMS.register("amulet_yggdrasil", 
        () -> new ItemMillAmulet(new Item.Properties().stacksTo(1)));
    
    // Wallet
    public static final DeferredItem<Item> ITEM_MILL_PURSE = ITEMS.register("item_mill_purse", 
        () -> new ItemMillWallet(new Item.Properties().stacksTo(1)));
    
    // Sign
    public static final DeferredItem<Item> ITEM_MILL_SIGN = ITEMS.register("item_mill_sign", 
        () -> new ItemMillSign(new Item.Properties()));
    
    // Parchments
    public static final DeferredItem<Item> NORMAN_VILLAGER_PARCHMENT = ITEMS.register("norman_villager_parchment", 
        () -> new ItemMillParchment("scroll.normanVillager.title", 
            new String[]{"scroll.normanVillager.leaders", "scroll.normanVillager.men", "scroll.normanVillager.women", "scroll.normanVillager.children"}, 
            new Item.Properties()));
    
    // Ajoutez ici tous les autres items au besoin...
}