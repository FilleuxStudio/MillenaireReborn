package org.millenaire.common.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.millenaire.common.blocks.MillBlockEntities;

public class TileEntityMillSign extends SignBlockEntity {
    public static final int ETAT_CIVIL = 1;
    public static final int CONSTRUCTIONS = 2;
    public static final int PROJECTS = 3;
    public static final int HOUSE = 4;
    public static final int RESOURCES = 5;
    public static final int ARCHIVES = 6;
    public static final int VILLAGE_MAP = 7;
    public static final int MILITARY = 8;
    public static final int TRADE_GOODS = 9;
    public static final int INN_VISITORS = 10;
    public static final int MARKET_MERCHANTS = 11;
    public static final int CONTROLLED_PROJECTS = 12;
    public static final int CONTROLLED_MILITARY = 13;

    private int thisSignType = 0;
    private BlockPos villageStoneLocation;

    public TileEntityMillSign(BlockPos pos, BlockState state) {
        super(MillBlockEntities.MILL_SIGN.get(), pos, state);
    }

    public boolean executeCommand(Player player) {
        // Display GuiPanel with appropriate info based on SignType
        return false;
    }

    public void setSignType(int typeIn) { 
        thisSignType = typeIn; 
    }

    public static void tick(org.minecraft.world.level.Level level, BlockPos pos, BlockState state, TileEntityMillSign sign) {
        if (sign.villageStoneLocation != null) {
            // Update sign text based on village stone data
            sign.messages[0] = Component.literal("The End is Nigh");
            // TileEntityVillageStone TEVS = (TileEntityVillageStone) level.getBlockEntity(sign.villageStoneLocation);
            // if (TEVS != null) {
            //     sign.messages[1] = Component.literal(TEVS.testVar + " clicks");
            // }
        }
    }
}