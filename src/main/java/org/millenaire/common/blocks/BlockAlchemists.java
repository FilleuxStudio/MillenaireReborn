package org.millenaire.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor; 

public class BlockAlchemists extends Block {
    private static final int EXPLOSIONRADIUS = 32;

    // CORRECTION 1: Constructeur mis à jour pour accepter BlockBehaviour.Properties
    public BlockAlchemists(BlockBehaviour.Properties properties) {
        super(properties);
    }
    
    // NOTE: Si vous ne voulez pas passer les propriétés depuis MillBlocks, vous pouvez utiliser:
    /*
    public BlockAlchemists() {
        super(BlockBehaviour.Properties.of()
            .mapColor(MapColor.STONE)
            .strength(-1.0F, 6000000.0F) // Anciennes valeurs de résistance/dureté
            .noLootTable()
        );
    }
    */

    private void alchemistExplosion(final Level world, final int i, final int j, final int k) {
        BlockPos centerPos = new BlockPos(i, j, k);
        
        world.removeBlock(centerPos, false);
        
        // Ancienne: EnumParticleTypes.EXPLOSION_HUGE (maintenant ParticleTypes.EXPLOSION_EMITTER)
        world.addParticle(ParticleTypes.EXPLOSION_EMITTER, i + 0.5D, j + 0.5D, k + 0.5D, 0.0D, 0.0D, 0.0D);
        
        // CORRECTION 2: playSound doit utiliser .getOrThrow() pour obtenir le SoundEvent non-Holderisé.
        // C'est l'approche la plus stable pour la 1.21.10.
        world.playSound(
            null, 
            centerPos, 
            SoundEvents.GENERIC_EXPLODE.value(),
            SoundSource.BLOCKS, 
            8.0F, 
            (1.0F + (world.random.nextFloat() - world.random.nextFloat()) * 0.2F) * 0.9F
        );
        
        for (int y = EXPLOSIONRADIUS; y >= -EXPLOSIONRADIUS; y--) {
            // Mise à jour de la vérification de hauteur (ancienne: 128)
            if (y + j >= world.getMinBuildHeight() && y + j < world.getMaxBuildHeight()) {
                for (int x = -EXPLOSIONRADIUS; x <= EXPLOSIONRADIUS; x++) {
                    for (int z = -EXPLOSIONRADIUS; z <= EXPLOSIONRADIUS; z++) {
                        if (x * x + y * y + z * z <= EXPLOSIONRADIUS * EXPLOSIONRADIUS) {
                            
                            // Ancienne: EnumParticleTypes.SMOKE_NORMAL (maintenant ParticleTypes.SMOKE)
                            world.addParticle(ParticleTypes.SMOKE, i + x + 0.5, j + y + 0.5, k + z + 0.5, 0.0D, 0.0D, 0.0D);
                            
                            BlockPos targetPos = new BlockPos(i + x, j + y, k + z);
                            final BlockState blockState = world.getBlockState(targetPos);
                            
                            // Ancienne: if (block != Blocks.air) / world.setBlockToAir()
                            if (!blockState.isAir()) {
                                world.removeBlock(targetPos, false);
                            }
                        }
                    }
                }
            }
        }
    }
    

    @Override
    public void onBlockExploded(BlockState state, Level world, BlockPos pos, Explosion explosion) {
        // La méthode onBlockExploded remplace onBlockDestroyedByExplosion
        alchemistExplosion(world, pos.getX(), pos.getY(), pos.getZ());
    }
}