package org.millenaire.common.items;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.Tags;

public class ItemMillArmor 
{
    public static final ArmorMaterial ARMOR_NORMAN = new MillArmorMaterial("norman", 66, new int[]{3, 8, 6, 3}, 10);
    public static final ArmorMaterial ARMOR_JAPANESE_WARRIOR_RED = new MillArmorMaterial("japanese_warrior_red", 33, new int[]{2, 6, 5, 2}, 25);
    public static final ArmorMaterial ARMOR_JAPANESE_WARRIOR_BLUE = new MillArmorMaterial("japanese_warrior_blue", 33, new int[]{2, 6, 5, 2}, 25);
    public static final ArmorMaterial ARMOR_JAPANESE_GUARD = new MillArmorMaterial("japanese_guard", 25, new int[]{2, 5, 4, 1}, 25);
    public static final ArmorMaterial ARMOR_BYZANTINE = new MillArmorMaterial("byzantine", 33, new int[]{3, 8, 6, 3}, 20);
    public static final ArmorMaterial ARMOR_MAYAN_QUEST = new MillArmorMaterial("mayan_quest", 5, new int[]{1, 3, 2, 1}, 35);
    
    public static class MayanQuestCrown extends ArmorItem
    {
        public MayanQuestCrown(ArmorMaterial material, Type type, Properties properties) 
        {
            super(material, type, properties);
        }
        
        @Override
        public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected)
        {
            if (!stack.isEnchanted()) {
                stack.enchant(Enchantments.RESPIRATION, 3);
                stack.enchant(Enchantments.AQUA_AFFINITY, 1);
                stack.enchant(Enchantments.ALL_DAMAGE_PROTECTION, 4);
            }
        }
    }
    
    // Classe helper pour les matériaux d'armure
    public static class MillArmorMaterial implements ArmorMaterial {
        private final String name;
        private final int durability;
        private final int[] protection;
        private final int enchantability;
        
        public MillArmorMaterial(String name, int durability, int[] protection, int enchantability) {
            this.name = name;
            this.durability = durability;
            this.protection = protection;
            this.enchantability = enchantability;
        }
        
        @Override
        public int getDurabilityForType(Type type) {
            return durability;
        }
        
        @Override
        public int getDefenseForType(Type type) {
            return protection[type.getSlot().getIndex()];
        }
        
        @Override
        public int getEnchantmentValue() {
            return enchantability;
        }
        
        @Override
        public net.minecraft.sounds.SoundEvent getEquipSound() {
            return net.minecraft.sounds.SoundEvents.ARMOR_EQUIP_IRON;
        }
        
        @Override
        public net.minecraft.world.item.crafting.Ingredient getRepairIngredient() {
            return net.minecraft.world.item.crafting.Ingredient.of(Tags.Items.INGOTS_IRON);
        }
        
        @Override
        public String getName() {
            return "millenaire:" + name;
        }
        
        @Override
        public float getToughness() {
            return 0;
        }
        
        @Override
        public float getKnockbackResistance() {
            return 0;
        }
    }
}