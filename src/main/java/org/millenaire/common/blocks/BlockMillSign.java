package org.millenaire.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import org.millenaire.common.entities.TileEntityMillSign;

public class BlockMillSign extends WallSignBlock {
    
    public BlockMillSign() {
        super(Properties.of()
            .mapColor(MapColor.WOOD)
            .noCollission()
            .strength(-1.0F, 3600000.0F)
            .noLootTable(),
            WoodType.OAK
        );
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntityMillSign(pos, state);
    }
}