package org.millenaire.common.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import org.millenaire.common.entities.TileEntityMillSign;

public class BlockMillSign extends WallSignBlock {
    
    public static final MapCodec<WallSignBlock> CODEC = simpleCodec(BlockMillSign::new);

    public BlockMillSign(BlockBehaviour.Properties properties) {
        
        // CORRECTION: Toutes les propriétés sont chaînées directement dans l'appel super()
        // pour contourner la restriction de Java 21 sur les 'Flexible Constructor Bodies'.
        super(properties
            .mapColor(MapColor.WOOD) 
            .strength(-1.0F, 3600000.0F)
            .noCollission() 
            .noLootTable(),
            WoodType.OAK
        );
    }
    
    protected MapCodec<? extends WallSignBlock> getCodec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) { 
        return new TileEntityMillSign(pos, state); 
    }
}