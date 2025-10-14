package org.millenaire.common.items;

import org.millenaire.Millenaire;
import org.millenaire.common.blocks.BlockMillSign;
import org.millenaire.common.blocks.MillBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class ItemMillSign extends Item
{
    public ItemMillSign(Properties properties) { 
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context)
    {
        Level worldIn = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player playerIn = context.getPlayer();
        BlockState state = worldIn.getBlockState(pos);

        if (context.getClickedFace() == null) {
            return InteractionResult.FAIL;
        }

        if (!state.getMaterial().isSolid()) {
            return InteractionResult.FAIL;
        }

        BlockPos newPos = pos.relative(context.getClickedFace());

        if (!playerIn.mayUseItemAt(newPos, context.getClickedFace(), context.getItemInHand())) {
            return InteractionResult.FAIL;
        }
        else if (worldIn.isClientSide()) {
            return InteractionResult.SUCCESS;
        }
        else {
            worldIn.setBlock(newPos, MillBlocks.BLOCK_MILL_SIGN.get().defaultBlockState()
                    .setValue(BlockMillSign.FACING, context.getClickedFace()), 3);

            context.getItemInHand().shrink(1);
            return InteractionResult.SUCCESS;
        }
    }
}