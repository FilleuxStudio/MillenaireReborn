package org.millenaire.common.items;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ItemMillFood extends Item {
    private final boolean isDrink;
    
    public ItemMillFood(Properties properties, boolean drink) {
        super(properties);
        this.isDrink = drink;
    }
    
    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity entity) {
        if(entity instanceof Player player) {
            // Logique d'effets lorsqu'on mange/boit
            player.heal(2.0F); // Soigne 1 cœur
            
            if(isDrink) {
                player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 300, 0));
            }
        }
        return super.finishUsingItem(stack, world, entity);
    }
}