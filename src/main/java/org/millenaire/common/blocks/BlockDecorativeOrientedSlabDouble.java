package org.millenaire.common.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.Blocks; 
import net.minecraft.world.level.block.state.BlockBehaviour;

public class BlockDecorativeOrientedSlabDouble extends BlockOrientedSlab {
    
    public static final MapCodec<BlockDecorativeOrientedSlabDouble> CODEC = simpleCodec(BlockDecorativeOrientedSlabDouble::new);

    public BlockDecorativeOrientedSlabDouble(BlockBehaviour.Properties properties) { 
        // L'erreur de constructeur est corrigée en appelant le super constructeur avec Blocks.AIR
        super(properties, Blocks.AIR);
    }
    
    // getCodec() doit être surchargé pour la dalle double
    @Override
    protected MapCodec<? extends SlabBlock> getCodec() {
        return CODEC;
    }

    // isDouble() est conservée SANS @Override car elle est utilisée par BlockOrientedSlab
    // mais n'est plus dans l'API de SlabBlock.
    public boolean isDouble() { 
        return true; 
    }
}