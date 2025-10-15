package org.millenaire.common.items;

import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

public class ItemMillArmor {
    // Définition des matériaux d'armure
    public static final ArmorMaterial ARMOR_NORMAN = createArmorMaterial(
        "norman", 33, 
        new int[]{3, 8, 6, 3}, 
        10, 
        SoundEvents.ARMOR_EQUIP_IRON, 
        2.0F, 0.0F, 
        () -> Ingredient.of(Items.IRON_INGOT)
    );
    
    public static final ArmorMaterial ARMOR_JAPANESE_WARRIOR_RED = createArmorMaterial(
        "japanese_warrior_red", 33,
        new int[]{2, 6, 5, 2},
        25,
        SoundEvents.ARMOR_EQUIP_IRON,
        1.0F, 0.0F,
        () -> Ingredient.of(Items.IRON_INGOT)
    );
    
    public static final ArmorMaterial ARMOR_JAPANESE_WARRIOR_BLUE = createArmorMaterial(
        "japanese_warrior_blue", 33,
        new int[]{2, 6, 5, 2},
        25,
        SoundEvents.ARMOR_EQUIP_IRON,
        1.0F, 0.0F,
        () -> Ingredient.of(Items.IRON_INGOT)
    );
    
    public static final ArmorMaterial ARMOR_JAPANESE_GUARD = createArmorMaterial(
        "japanese_guard", 25,
        new int[]{2, 5, 4, 1},
        25,
        SoundEvents.ARMOR_EQUIP_IRON,
        0.5F, 0.0F,
        () -> Ingredient.of(Items.IRON_INGOT)
    );
    
    public static final ArmorMaterial ARMOR_BYZANTINE = createArmorMaterial(
        "byzantine", 33,
        new int[]{3, 8, 6, 3},
        20,
        SoundEvents.ARMOR_EQUIP_IRON,
        1.5F, 0.0F,
        () -> Ingredient.of(Items.IRON_INGOT)
    );
    
    public static final ArmorMaterial ARMOR_MAYAN_QUEST = createArmorMaterial(
        "mayan_quest", 5,
        new int[]{1, 3, 2, 1},
        35,
        SoundEvents.ARMOR_EQUIP_LEATHER,
        0.0F, 0.0F,
        () -> Ingredient.of(Items.GOLD_INGOT)
    );
    
    // Méthode utilitaire pour créer des matériaux d'armure
    private static ArmorMaterial createArmorMaterial(String name, int durability, 
                                                   int[] protection, int enchantability, 
                                                   Holder<SoundEvent> equipSound, 
                                                   float toughness, float knockbackResistance, 
                                                   Supplier<Ingredient> repairIngredient) {
        EnumMap<ArmorItem.Type, Integer> protectionMap = Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
            map.put(ArmorItem.Type.BOOTS, protection[3]);
            map.put(ArmorItem.Type.LEGGINGS, protection[2]);
            map.put(ArmorItem.Type.CHESTPLATE, protection[1]);
            map.put(ArmorItem.Type.HELMET, protection[0]);
        });
        
        return new ArmorMaterial(
            protectionMap,
            enchantability,
            equipSound,
            repairIngredient,
            List.of(), // Layers - laisser vide pour l'instant
            toughness,
            knockbackResistance
        );
    }
    
    public static class MayanQuestCrown extends ArmorItem {
        public MayanQuestCrown(Holder<ArmorMaterial> material, Properties properties) {
            super(material, Type.HELMET, properties);
        }
    }
}