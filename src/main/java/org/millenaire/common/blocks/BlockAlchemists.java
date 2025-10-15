package org.millenaire.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

public class BlockAlchemists extends Block {
    private static final int EXPLOSION_RADIUS = 32;

    public BlockAlchemists(Properties properties) {
        super(properties.mapColor(MapColor.STONE));
    }

    private void alchemistExplosion(final Level level, final int x, final int y, final int z) {
        level.removeBlock(new BlockPos(x, y, z), false);
        level.addParticle(ParticleTypes.EXPLOSION_EMITTER, x + 0.5D, y + 0.5D, z + 0.5D, 0.0D, 0.0D, 0.0D);
        level.playSound(null, x, y, z, SoundEvents.GENERIC_EXPLODE.value(), SoundSource.BLOCKS, 8.0F, 
            (1.0F + (level.random.nextFloat() - level.random.nextFloat()) * 0.2F) * 0.9F);
        
        for (int dy = EXPLOSION_RADIUS; dy >= -EXPLOSION_RADIUS; dy--) {
            if (y + dy >= level.getMinBuildHeight() && y + dy < level.getMaxBuildHeight()) {
                for (int dx = -EXPLOSION_RADIUS; dx <= EXPLOSION_RADIUS; dx++) {
                    for (int dz = -EXPLOSION_RADIUS; dz <= EXPLOSION_RADIUS; dz++) {
                        if (dx * dx + dy * dy + dz * dz <= EXPLOSION_RADIUS * EXPLOSION_RADIUS) {
                            level.addParticle(ParticleTypes.SMOKE, x + dx, y + dy + 0.5D, z + dz, 0.0D, 0.0D, 0.0D);
                            final BlockPos pos = new BlockPos(x + dx, y + dy, z + dz);
                            final BlockState blockState = level.getBlockState(pos);
                            if (!blockState.isAir()) {
                                level.removeBlock(pos, false);
                            }
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public void onBlockExploded(BlockState state, Level level, BlockPos pos, Explosion explosion) {
        alchemistExplosion(level, pos.getX(), pos.getY(), pos.getZ());
        super.onBlockExploded(state, level, pos, explosion);
    }
}