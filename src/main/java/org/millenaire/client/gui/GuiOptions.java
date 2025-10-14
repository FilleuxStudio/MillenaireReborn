package org.millenaire.client.gui;

import org.millenaire.common.Millenaire;
import org.millenaire.common.networking.MillPacket;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class GuiOptions extends Screen {
    private static final ResourceLocation OPTIONGUI = ResourceLocation.fromNamespaceAndPath(Millenaire.MODID, "textures/gui/ml_village_chief.png");
    private final String string;
    private final int eventID;

    private Button yes;
    private Button no;

    public GuiOptions(int IDin, String stringIn) {
        super(Component.translatable("gui.millenaire.options"));
        this.string = Component.translatable(stringIn).getString();
        this.eventID = IDin;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);
        guiGraphics.blit(OPTIONGUI, (this.width - 255) / 2, 2, 0, 0, 255, 199);
        
        if (string != null) {
            guiGraphics.drawWordWrap(this.font, Component.literal(string), (this.width / 2) - 94, 20, 190, 0);
        }
        
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
    }

    @Override
    protected void init() {
        this.yes = Button.builder(Component.translatable("gui.yes"), button -> handleYes())
            .bounds((this.width / 2) - 50, (this.height / 2) + 40, 40, 20)
            .build();
            
        this.no = Button.builder(Component.translatable("gui.no"), button -> handleNo())
            .bounds((this.width / 2) + 10, (this.height / 2) + 40, 40, 20)
            .build();
            
        this.addRenderableWidget(yes);
        this.addRenderableWidget(no);
    }

    private void handleYes() {
        if (eventID >= 2 && eventID <= 4) {
            // Envoyer le packet au serveur
            MillPacket packet = new MillPacket(eventID);
            // Millenaire.NETWORK.sendToServer(packet);
        }
        this.minecraft.setScreen(null);
    }

    private void handleNo() {
        this.minecraft.setScreen(null);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}