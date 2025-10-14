package org.millenaire.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class BlockMillCrops extends CropBlock {
    private final boolean requiresIrrigation;
    private final boolean slowGrowth;
    private final ItemLike seed;
    
    public BlockMillCrops(boolean irrigationIn, boolean growthIn, ItemLike seed) {
        super(Properties.of()
            .noCollission()
            .randomTicks()
            .instabreak()
            .sound(net.minecraft.world.level.block.SoundType.CROP)
        );
        this.requiresIrrigation = irrigationIn;
        this.slowGrowth = growthIn;
        this.seed = seed;
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return this.seed;
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.getBlock() instanceof FarmBlock;
    }

    private float getLocalGrowthChance(Block blockIn, BlockGetter level, BlockPos pos) {
        BlockState groundState = level.getBlockState(pos.below());
        if(!(groundState.getBlock() instanceof FarmBlock)) {
            System.err.println("BlockMillCrop growth logic not applied, unrecognized farmland");
            return getGrowthRate(blockIn, level, pos);
        }
        
        if(requiresIrrigation && groundState.getValue(FarmBlock.MOISTURE) < 1) { 
            return 0.0F; 
        } else {
            if(slowGrowth) {
                return getGrowthRate(blockIn, level, pos) / 2;
            } else {
                return getGrowthRate(blockIn, level, pos);
            }
        }
    }

    private float getGrowthRate(Block block, BlockGetter level, BlockPos pos) {
        // Implémentez la logique de taux de croissance ici
        return 1.0F; // Valeur par défaut
    }
}