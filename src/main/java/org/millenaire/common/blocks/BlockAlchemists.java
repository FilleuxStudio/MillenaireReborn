package org.millenaire.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

public class BlockAlchemists extends Block {
    private static final int EXPLOSIONRADIUS = 32;

    public BlockAlchemists() {
        super(Properties.of()
            .mapColor(MapColor.STONE)
            .strength(-1.0F, 6000000.0F)
            .noLootTable()
        );
    }

    private void alchemistExplosion(final Level world, final int i, final int j, final int k) {
        world.removeBlock(new BlockPos(i, j, k), false);
        world.addParticle(ParticleTypes.EXPLOSION_EMITTER, i + 0.5D, j + 0.5D, k + 0.5D, 0.0D, 0.0D, 0.0D);
        world.playSound(null, new BlockPos(i, j, k), SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 8.0F, 
            (1.0F + (world.random.nextFloat() - world.random.nextFloat()) * 0.2F) * 0.9F);
        
        for (int y = EXPLOSIONRADIUS; y >= -EXPLOSIONRADIUS; y--) {
            if (y + j >= 0 && y + j < world.getMaxBuildHeight()) {
                for (int x = -EXPLOSIONRADIUS; x <= EXPLOSIONRADIUS; x++) {
                    for (int z = -EXPLOSIONRADIUS; z <= EXPLOSIONRADIUS; z++) {
                        if (x * x + y * y + z * z <= EXPLOSIONRADIUS * EXPLOSIONRADIUS) {
                            world.addParticle(ParticleTypes.SMOKE, i + x + 0.5, j + y + 0.5, k + z + 0.5, 0.0D, 0.0D, 0.0D);
                            final BlockState blockState = world.getBlockState(new BlockPos(i + x, j + y, k + z));
                            if (!blockState.isAir()) {
                                world.removeBlock(new BlockPos(i + x, j + y, k + z), false);
                            }
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public void onBlockExploded(BlockState state, Level world, BlockPos pos, Explosion explosion) {
        alchemistExplosion(world, pos.getX(), pos.getY(), pos.getZ());
        super.onBlockExploded(state, world, pos, explosion);
    }
}