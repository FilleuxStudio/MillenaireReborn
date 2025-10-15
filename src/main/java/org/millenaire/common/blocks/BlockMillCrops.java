package org.millenaire.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.MapColor;

public class BlockMillCrops extends CropBlock {
    private static final IntegerProperty AGE = IntegerProperty.create("age", 0, 7);
    private final boolean requiresIrrigation;
    private final boolean slowGrowth;
    
    public BlockMillCrops(Properties properties, boolean irrigationIn, boolean growthIn) {
        super(properties);
        this.requiresIrrigation = irrigationIn;
        this.slowGrowth = growthIn;
    }

    @Override
    public IntegerProperty getAgeProperty() {
        return AGE;
    }

    @Override
    public int getMaxAge() {
        return 7;
    }

    @Override
    protected ItemLike getBaseSeedId() {
        // Ce sera défini dans MillItems
        return null;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!level.isAreaLoaded(pos, 1)) return;
        
        if (level.getRawBrightness(pos, 0) >= 9) {
            int currentAge = this.getAge(state);
            if (currentAge < this.getMaxAge()) {
                float growthChance = getGrowthChance(this, level, pos);
                
                if (growthChance != 0 && random.nextInt((int)(25.0F / growthChance) + 1) == 0) {
                    level.setBlock(pos, this.getStateForAge(currentAge + 1), 2);
                }
            }
        }
    }

    private float getGrowthChance(Block block, Level level, BlockPos pos) {
        BlockState groundState = level.getBlockState(pos.below());
        if (!(groundState.getBlock() instanceof FarmBlock)) {
            System.err.println("BlockMillCrop growth logic not applied, unrecognized farmland");
            return getGrowthSpeed(block, level, pos);
        }
        
        if (requiresIrrigation && groundState.getValue(FarmBlock.MOISTURE) < 1) {
            return 0.0F;
        } else {
            if (slowGrowth) {
                return getGrowthSpeed(block, level, pos) / 2;
            } else {
                return getGrowthSpeed(block, level, pos);
            }
        }
    }

    // Méthode corrigée pour éviter le conflit avec la méthode statique
    public static float getGrowthSpeed(Block block, BlockGetter level, BlockPos pos) {
        // Implémentation simplifiée - vous devrez peut-être l'adapter selon vos besoins
        float f = 1.0F;
        BlockPos blockpos = pos.below();

        for (int i = -1; i <= 1; ++i) {
            for (int j = -1; j <= 1; ++j) {
                float f1 = 0.0F;
                BlockState blockstate = level.getBlockState(blockpos.offset(i, 0, j));
                if (blockstate.getBlock() instanceof FarmBlock) {
                    f1 = 1.0F;
                    if (blockstate.getValue(FarmBlock.MOISTURE) > 0) {
                        f1 = 3.0F;
                    }
                }

                if (i != 0 || j != 0) {
                    f1 /= 4.0F;
                }

                f += f1;
            }
        }

        BlockPos blockpos1 = pos.north();
        BlockPos blockpos2 = pos.south();
        BlockPos blockpos3 = pos.west();
        BlockPos blockpos4 = pos.east();
        boolean flag = block == level.getBlockState(blockpos3).getBlock() || block == level.getBlockState(blockpos4).getBlock();
        boolean flag1 = block == level.getBlockState(blockpos1).getBlock() || block == level.getBlockState(blockpos2).getBlock();
        if (flag && flag1) {
            f /= 2.0F;
        } else {
            boolean flag2 = block == level.getBlockState(blockpos3.north()).getBlock() || block == level.getBlockState(blockpos4.north()).getBlock() || block == level.getBlockState(blockpos4.south()).getBlock() || block == level.getBlockState(blockpos3.south()).getBlock();
            if (flag2) {
                f /= 2.0F;
            }
        }

        return f;
    }
}