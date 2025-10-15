package org.millenaire.common.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks; 
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;

public class BlockDecorativeOrientedSlabDouble extends BlockOrientedSlab {
    
     public BlockDecorativeOrientedSlabDouble(BlockBehaviour.Properties properties) {
        super(properties);
    }
}