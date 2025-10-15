package org.millenaire.common.items;

import org.millenaire.common.blocks.MillBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class ItemMillSign extends Item {
    
    public ItemMillSign(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        
        if (player == null) {
            return InteractionResult.FAIL;
        }
        
        BlockPos placedPos = pos.relative(context.getClickedFace());
        
        if (!player.mayUseItemAt(placedPos, context.getClickedFace(), context.getItemInHand())) {
            return InteractionResult.FAIL;
        }
        
        if (world.isClientSide()) {
            return InteractionResult.SUCCESS;
        }
        
        // Logique de placement du signe
        BlockState signState = MillBlocks.BLOCK_MILL_SIGN.get().defaultBlockState();
        // Vous devrez configurer l'état du bloc selon la direction
        
        if (world.setBlock(placedPos, signState, 3)) {
            context.getItemInHand().shrink(1);
            return InteractionResult.SUCCESS;
        }
        
        return InteractionResult.FAIL;
    }
}