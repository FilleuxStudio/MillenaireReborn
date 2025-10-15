package org.millenaire.common.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class TileEntityMillSign extends SignBlockEntity {
    public static final int ETAT_CIVIL = 1;
    // ... autres constantes

    private int signType = 0;
    private BlockPos villageStoneLocation;
    private UUID villageId;

    public TileEntityMillSign(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MILL_SIGN.get(), pos, state);
    }

    public boolean onClicked(Player player) {
        if (player.level().isClientSide()) {
            return true;
        }
        return executeCommand(player);
    }

    public boolean executeCommand(Player player) {
        if (level != null && !level.isClientSide()) {
            // Logique des panneaux
            return true;
        }
        return false;
    }

    public void setSignType(int type) {
        this.signType = type;
        setChanged();
    }

    public int getSignType() {
        return signType;
    }

    public void setVillageStoneLocation(BlockPos pos) {
        this.villageStoneLocation = pos;
        setChanged();
    }

    public static void tick(Level level, BlockPos pos, BlockState state, TileEntityMillSign sign) {
        if (level.isClientSide() || sign.villageStoneLocation == null) {
            return;
        }
        sign.updateSignText();
    }

    private void updateSignText() {
        if (level == null || villageStoneLocation == null) {
            return;
        }

        BlockEntity blockEntity = level.getBlockEntity(villageStoneLocation);
        if (blockEntity instanceof TileEntityVillageStone villageStone) {
            // Utilisation des méthodes de SignBlockEntity
            this.setMessage(0, Component.literal("Population"));
            this.setMessage(1, Component.literal(villageStone.testVar + " habitants"));
        }
    }

    private void setMessage(int i, MutableComponent literal) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setMessage'");
    }

    @Override
    protected void saveAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.saveAdditional(compound, registries);
        compound.putInt("SignType", signType);
        if (villageStoneLocation != null) {
            compound.putInt("VillageStoneX", villageStoneLocation.getX());
            compound.putInt("VillageStoneY", villageStoneLocation.getY());
            compound.putInt("VillageStoneZ", villageStoneLocation.getZ());
        }
        if (villageId != null) {
            compound.putUUID("VillageId", villageId);
        }
    }

    @Override
    protected void loadAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.loadAdditional(compound, registries);
        signType = compound.getInt("SignType");
        if (compound.contains("VillageStoneX")) {
            int x = compound.getInt("VillageStoneX");
            int y = compound.getInt("VillageStoneY");
            int z = compound.getInt("VillageStoneZ");
            villageStoneLocation = new BlockPos(x, y, z);
        }
        if (compound.contains("VillageId")) {
            villageId = compound.getUUID("VillageId");
        }
    }
}