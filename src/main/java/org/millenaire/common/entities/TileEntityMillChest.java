package org.millenaire.common.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.millenaire.common.blocks.MillBlockEntities;

public class TileEntityMillChest extends ChestBlockEntity {
    private boolean isLocked = true;
    
    public TileEntityMillChest(BlockPos pos, BlockState state) {
        super(MillBlockEntities.MILL_CHEST.get(), pos, state);
    }
    
    public boolean setLock() {
        isLocked = !isLocked;
        
        // Handle adjacent chest synchronization if needed
        return isLocked;
    }
    
    public boolean isLockedFor(Player player) {
        if (player == null) {
            return false;
        }
        
        // Final Building building = mw.getBuilding(buildingPos);
        // if (building == null) return true;
        // if (building.lockedForPlayer(player.getDisplayName())) return true;
        
        return isLocked;
    }
    
    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        
        if (compound.contains("millChestLocked")) {
            isLocked = compound.getBoolean("millChestLocked");
        }
    }
    
    @Override
    protected void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putBoolean("millChestLocked", isLocked);
    }
    
    @Override
    public Component getDisplayName() {
        return Component.translatable("container.millenaire.chest");
    }
    
    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory playerInventory) {
        return ChestMenu.oneRow(id, playerInventory, this);
    }
    
    @Override
    public boolean canOpen(Player player) {
        return !isLockedFor(player) && super.canOpen(player);
    }
}