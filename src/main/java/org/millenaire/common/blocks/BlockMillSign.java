package org.millenaire.common.blocks;

import org.millenaire.common.entities.TileEntityMillSign;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;

public class BlockMillSign extends StandingSignBlock {

    public BlockMillSign(Properties properties) {
        // Utilisez WoodType.OAK par défaut
        super(WoodType.OAK, properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntityMillSign(pos, state);
    }
}