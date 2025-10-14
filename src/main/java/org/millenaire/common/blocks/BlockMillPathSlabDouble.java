package org.millenaire.common.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.SlabType;
import org.millenaire.common.blocks.BlockMillPath.EnumType;

// Hérite de la logique de VoxelShape et de la propriété VARIANT de la dalle simple
public class BlockMillPathSlabDouble extends BlockMillPathSlab {
    
    public static final MapCodec<BlockMillPathSlabDouble> CODEC = simpleCodec(BlockMillPathSlabDouble::new);

    public BlockMillPathSlabDouble(BlockBehaviour.Properties properties) {
        super(properties);
        
        // La dalle double doit toujours avoir TYPE = DOUBLE.
        this.registerDefaultState(this.defaultBlockState()
            .setValue(TYPE, SlabType.DOUBLE)
            .setValue(WATERLOGGED, false)
            .setValue(VARIANT, EnumType.DIRT)
        );
    }

    // Crucial: retourne vrai pour la logique interne qui pourrait en dépendre.
    @Override
    public boolean isDouble() { 
        return true; 
    }
    
    // Implémentation obligatoire du MapCodec
    @Override
    protected MapCodec<? extends SlabBlock> getCodec() {
        return CODEC;
    }
    
    // Le reste (getShape, etc.) est hérité et fonctionne correctement.
}