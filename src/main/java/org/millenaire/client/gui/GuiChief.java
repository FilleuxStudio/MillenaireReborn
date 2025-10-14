package org.millenaire.client.gui;

import org.millenaire.Millenaire;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class GuiChief extends Screen {
    private static final ResourceLocation CHIEFGUI = ResourceLocation.fromNamespaceAndPath(Millenaire.MODID, "textures/gui/ml_village_chief.png");
    private String string;
    private int page = 0;
    private int maxPage = 4;

    private Button forward;
    private Button backward;

    public GuiChief() {
        super(Component.translatable("gui.millenaire.chief"));
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);
        guiGraphics.blit(CHIEFGUI, (this.width - 255) / 2, 2, 0, 0, 255, 199);
        
        if (string != null) {
            guiGraphics.drawWordWrap(this.font, Component.literal(string), (this.width / 2) - 94, 20, 190, 0);
        }
        
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
    }

    @Override
    protected void init() {
        this.backward = Button.builder(Component.literal("<"), button -> {
            page--;
            updateButtons();
        }).bounds((this.width / 2) - 95, 208, 20, 20).build();
        
        this.forward = Button.builder(Component.literal(">"), button -> {
            page++;
            updateButtons();
        }).bounds((this.width / 2) + 77, 208, 20, 20).build();
        
        this.addRenderableWidget(backward);
        this.addRenderableWidget(forward);
        updateButtons();
    }

    private void updateButtons() {
        this.backward.visible = page != 0;
        this.forward.visible = page != maxPage - 1;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}