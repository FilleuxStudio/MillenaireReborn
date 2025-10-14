package org.millenaire.common.items;

import org.millenaire.common.gui.MillAchievement;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class ItemMillFood extends Item
{
    private final boolean isDrink;
    private final int healAmount;
    private final int drunkDuration;
    private final int regDuration;

    public ItemMillFood(int healIn, int regIn, int drunkIn, int hungerIn, float saturationIn, boolean drinkIn, Properties properties)
    {    
        super(properties.food(new net.minecraft.world.food.FoodProperties.Builder()
                .nutrition(hungerIn)
                .saturationModifier(saturationIn)
                .build()));
        
        healAmount = healIn;
        regDuration = regIn;
        drunkDuration = drunkIn;
        isDrink = drinkIn;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) { 
        return isDrink ? UseAnim.DRINK : UseAnim.EAT; 
    }
    
    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entityLiving)
    {
        if (entityLiving instanceof Player player) {
            player.getFoodData().eat(stack);
            player.awardStat(Stats.ITEM_USED.get(this));
            
            if (player instanceof ServerPlayer serverPlayer) {
                CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, stack);
            }

            player.heal(healAmount);

            if (isDrink) {
                player.awardStat(MillAchievement.CHEERS.get());
            }

            if (regDuration > 0) {
                player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, regDuration * 20, 0));
            }

            if (drunkDuration > 0) {
                player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, drunkDuration * 20, 0));
            }
            
            stack.shrink(1);
        }
        
        return stack;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn)
    {
        if (isDrink) {
            return ItemUtils.startUsingInstantly(worldIn, playerIn, handIn);
        }
        return super.use(worldIn, playerIn, handIn);
    }
}