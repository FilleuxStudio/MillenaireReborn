package org.millenaire.common.items;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.List;

public class ItemMillAmulet extends Item
{
    private int[] colorAlchemist = new int[]{9868950, 10132109, 10395268, 10658427, 11053168, 11316327, 11579486, 11842645, 12237387, 12500545, 12763705, 13026863, 13421605, 13684764, 13947923, 14211082};
    private int[] colorVishnu = new int[]{236, 983260, 2031820, 3080380, 4063405, 5111965, 6160525, 7209085, 8192110, 9240670, 10289230, 11337790, 12320815, 13369375, 14417935, 15466496};
    private int[] colorYggdrasil = new int[]{396556, 990493, 1453614, 2113086, 2576206, 3104864, 3698799, 4227457, 4755857, 5350050, 5878706, 6407106, 7001299, 7464165, 8058100, 8388606, 
            8781823, 9306111, 9895935, 10420223, 10944511, 11534335, 12058623, 12648447, 13172735, 13762559, 14286847, 14876671, 15400959, 15925247, 16515071, 16777213};

    public ItemMillAmulet(Properties properties)
    {
        super(properties);
    }

    @Override
    public ItemStack use(Level world, Player player, net.minecraft.world.InteractionHand hand) 
    {
        ItemStack itemstack = player.getItemInHand(hand);
        
        if(this == MillItems.amuletSkollHati && !world.isClientSide())
        {
            final long time = world.getDayTime() + 24000L;

            if (time % 24000L > 11000L && time % 24000L < 23500L) 
            {
                world.setDayTime(time - time % 24000L - 500L);
            } 
            else 
            {
                world.setDayTime(time - time % 24000L + 13000L);
            }

            itemstack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(hand));
        }

        return itemstack;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        if(this == MillItems.amuletSkollHati)
            return;

        int visScore = 0;

        if(this == MillItems.amuletAlchemist && entityIn instanceof Player)
        {
            int radius = 5;
            BlockPos pos = entityIn.blockPosition();

            final int startY = Math.max(pos.getY() - radius, worldIn.getMinBuildHeight());
            final int endY = Math.min(pos.getY() + radius, worldIn.getMaxBuildHeight());

            for (int i = pos.getX() - radius; i < pos.getX() + radius; i++) 
            {
                for (int j = pos.getZ() - radius; j < pos.getZ() + radius; j++) 
                {
                    for (int k = startY; k < endY; k++) 
                    {
                        final Block block = worldIn.getBlockState(new BlockPos(i, k, j)).getBlock();
                        if (block == Blocks.COAL_ORE)
                            visScore++;
                        else if (block == Blocks.DIAMOND_ORE)
                            visScore += 30;
                        else if (block == Blocks.EMERALD_ORE)
                            visScore += 30;
                        else if (block == Blocks.GOLD_ORE)
                            visScore += 10;
                        else if (block == Blocks.IRON_ORE)
                            visScore += 5;
                        else if (block == Blocks.LAPIS_ORE)
                            visScore += 10;
                        else if (block == Blocks.REDSTONE_ORE)
                            visScore += 5;
                    }
                }
            }

            if (visScore > 100)
                visScore = 100;

            visScore = (visScore * 15) / 100;
        }

        if(this == MillItems.amuletVishnu && entityIn instanceof Player)
        {
            double level;
            final int radius = 20;
            double closestDistance = Double.MAX_VALUE;

            final List<Monster> entities = worldIn.getEntitiesOfClass(Monster.class, 
                    new AABB(entityIn.xo, entityIn.yo, entityIn.zo, entityIn.xo + 1.0D, entityIn.yo + 1.0D, entityIn.zo + 1.0D).inflate(20, 20, 20));

            for (final Entity ent : entities) 
            {
                if (entityIn.distanceTo(ent) < closestDistance)
                    closestDistance = entityIn.distanceTo(ent);
            }

            if (closestDistance > radius) {
                level = 0;
            } else {
                level = (radius - closestDistance) / radius;
            }

            visScore = (int) (level * 15);
        }

        if(this == MillItems.amuletYggdrasil && entityIn instanceof Player)
        {
            int level = (int) Math.floor(entityIn.getY());

            if(level > 255)
            {
                level = 255;
            }
            else if(level < worldIn.getMinBuildHeight())
            {
                level = worldIn.getMinBuildHeight();
            }

            visScore = level / 8;
        }

        CompoundTag nbt;
        if(stack.getTag() == null)
            nbt = new CompoundTag();
        else
            nbt = stack.getTag();

        nbt.putInt("score", visScore);
        stack.setTag(nbt);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public int getColor(ItemStack stack, int renderPass)
    {
        CompoundTag nbt = stack.getTag();

        if(renderPass != 0)
        {
            if(nbt == null)
            {
                if(this == MillItems.amuletAlchemist)
                    return colorAlchemist[0];
                if(this == MillItems.amuletVishnu)
                    return colorVishnu[0];
                if(this == MillItems.amuletYggdrasil)
                    return colorYggdrasil[16];
            }
            int score = nbt.getInt("score");

            if(this == MillItems.amuletAlchemist)
                return colorAlchemist[score];
            if(this == MillItems.amuletVishnu)
                return colorVishnu[score];
            if(this == MillItems.amuletYggdrasil)
                return colorYggdrasil[score];
        }
        return 16777215;
    }

    @Override
    public int getMaxStackSize(ItemStack stack) { return 1; }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
    {
        return !(oldStack.getItem() == this && newStack.getItem() == this);
    }
}