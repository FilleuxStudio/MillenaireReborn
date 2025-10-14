package org.millenaire.client.gui;

import org.millenaire.common.entities.TileEntityMillChest;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ChestMenu;

public class GuiMillChest extends AbstractContainerScreen<ChestMenu> implements MenuAccess<ChestMenu> {
    private final boolean isLocked;
    private final TileEntityMillChest chest;

    public GuiMillChest(ChestMenu menu, Inventory playerInventory, Component title, TileEntityMillChest entityIn) {
        super(menu, playerInventory, title);
        this.chest = entityIn;
        this.isLocked = entityIn.isLockedFor(playerInventory.player);
        this.imageHeight = 114 + 4 * 18;
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        Component string;
        
        // Vérifier les coffres adjacents (à adapter selon la nouvelle implémentation)
        boolean hasAdjacentChests = false; // chest.hasAdjacentChests();
        
        if (!hasAdjacentChests) {
            string = Component.translatable(isLocked ? "container.millenaire.chest_locked" : "container.millenaire.chest_unlocked");
        } else {
            string = Component.translatable(isLocked ? "container.millenaire.chest_double_locked" : "container.millenaire.chest_double_unlocked");
        }
        
        guiGraphics.drawString(this.font, string, 8, 6, 4210752, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 96 + 2, 4210752, false);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!isLocked) {
            return super.keyPressed(keyCode, scanCode, modifiers);
        } else {
            if (keyCode == 256 || this.minecraft.options.keyInventory.matches(keyCode, scanCode)) {
                this.minecraft.player.closeContainer();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!isLocked) {
            return super.mouseClicked(mouseX, mouseY, button);
        }
        return false;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        // Fond par défaut pour les conteneurs
        // Implémente le rendu spécifique si nécessaire
    }
}