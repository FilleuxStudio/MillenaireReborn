package org.millenaire.common.items;

import net.minecraft.world.item.*;

public class ItemMillTool 
{
    public static final Tier TOOLS_NORMAN = new MillTier(2, 1561, 10.0F, 4.0F, 10);
    public static final Tier TOOLS_OBSIDIAN = new MillTier(3, 1561, 6.0F, 2.0F, 25);

    public static class ItemMillAxe extends AxeItem
    {
        public ItemMillAxe(Tier material, Properties properties) { 
            super(material, 6.0F, -3.1F, properties); 
        }
    }

    public static class ItemMillShovel extends ShovelItem
    {
        public ItemMillShovel(Tier material, Properties properties) { 
            super(material, 1.5F, -3.0F, properties); 
        }
    }

    public static class ItemMillPickaxe extends PickaxeItem
    {
        public ItemMillPickaxe(Tier material, Properties properties) { 
            super(material, 1, -2.8F, properties); 
        }
    }

    public static class ItemMillHoe extends HoeItem
    {
        public ItemMillHoe(Tier material, Properties properties) { 
            super(material, (int)-material.getAttackDamageBonus(), -3.0F, properties); 
        }
    }
    
    public static class ItemMillMace extends SwordItem
    {
        public ItemMillMace(Tier material, Properties properties) { 
            super(material, 3, -2.4F, properties); 
        }
        
        @Override
        public void onCraftedBy(ItemStack stack, Level worldIn, Player playerIn)
        {
            if (!stack.isEnchanted()) {
                stack.enchant(net.minecraft.world.item.enchantment.Enchantments.KNOCKBACK, 2);
            }
        }
    }
    
    // Classe helper pour les matériaux d'outils
    public static class MillTier implements Tier {
        private final int level;
        private final int uses;
        private final float speed;
        private final float attackDamageBonus;
        private final int enchantability;
        
        public MillTier(int level, int uses, float speed, float attackDamageBonus, int enchantability) {
            this.level = level;
            this.uses = uses;
            this.speed = speed;
            this.attackDamageBonus = attackDamageBonus;
            this.enchantability = enchantability;
        }
        
        @Override
        public int getUses() {
            return uses;
        }
        
        @Override
        public float getSpeed() {
            return speed;
        }
        
        @Override
        public float getAttackDamageBonus() {
            return attackDamageBonus;
        }
        
        @Override
        public int getLevel() {
            return level;
        }
        
        @Override
        public int getEnchantmentValue() {
            return enchantability;
        }
        
        @Override
        public net.minecraft.world.item.crafting.Ingredient getRepairIngredient() {
            return net.minecraft.world.item.crafting.Ingredient.of(net.minecraft.tags.ItemTags.PLANKS);
        }
    }
}