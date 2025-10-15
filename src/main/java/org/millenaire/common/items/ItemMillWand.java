package org.millenaire.common.items;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class ItemMillWand extends Item {
    
    public ItemMillWand(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();
        
        if(player != null) {
            if(world.getBlockState(pos).getBlock() == Blocks.GOLD_BLOCK) {
                if(!world.isClientSide()) {
                    player.displayClientMessage(Component.literal("Wand used on gold block"), true);
                }
                return InteractionResult.sidedSuccess(world.isClientSide());
            }
        }
        
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity entity, InteractionHand hand) {
        // Logique d'interaction avec les entités
        if(!player.level().isClientSide()) {
            player.displayClientMessage(Component.literal("Interacted with entity: " + entity.getName().getString()), true);
        }
        return InteractionResult.sidedSuccess(player.level().isClientSide());
    }
}