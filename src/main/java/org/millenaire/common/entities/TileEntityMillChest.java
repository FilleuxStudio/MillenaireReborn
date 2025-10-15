package org.millenaire.common.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class TileEntityMillChest extends ChestBlockEntity {
    private boolean isLocked = true;
    
    public TileEntityMillChest(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MILL_CHEST.get(), pos, state);
    }

    @Override
    protected @NotNull Component getDefaultName() {
        return Component.translatable("container.millenaire.chest");
    }

    @Override
    protected @NotNull AbstractContainerMenu createMenu(int containerId, Inventory playerInventory) {
        return ChestMenu.oneRow(containerId, playerInventory);
    }

    public boolean toggleLock() {
        isLocked = !isLocked;
        return isLocked;
    }

    public boolean isLockedFor(Player player) {
        if (player == null) {
            return false;
        }
        return isLocked;
    }

    @Override
    public boolean stillValid(Player player) {
        return !isLockedFor(player) && super.stillValid(player);
    }

    @Override
    protected void saveAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.saveAdditional(compound, registries);
        compound.putBoolean("millChestLocked", isLocked);
    }

    @Override
    protected void loadAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.loadAdditional(compound, registries);
        if (compound.contains("millChestLocked")) {
            isLocked = compound.getBoolean("millChestLocked");
        }
    }

    protected @NotNull ContainerOpenersCounter getOpenersCounter() {
        return new ContainerOpenersCounter() {
            @Override
            protected void onOpen(@NotNull net.minecraft.world.level.Level level, @NotNull BlockPos pos, @NotNull BlockState state) {
                level.blockEvent(pos, state.getBlock(), 1, getOpenCount(level, pos));
            }

            @Override
            protected void onClose(@NotNull net.minecraft.world.level.Level level, @NotNull BlockPos pos, @NotNull BlockState state) {
                level.blockEvent(pos, state.getBlock(), 1, getOpenCount(level, pos));
            }

            @Override
            protected void openerCountChanged(@NotNull net.minecraft.world.level.Level level, @NotNull BlockPos pos, @NotNull BlockState state, int oldCount, int newCount) {
            }

            @Override
            protected boolean isOwnContainer(@NotNull Player player) {
                return player.containerMenu instanceof ChestMenu menu;
            }
        };
    }
}