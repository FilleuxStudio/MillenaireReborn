package org.millenaire.client.gui;

import org.millenaire.Millenaire;
import org.millenaire.common.items.ItemMillParchment;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class GuiParchment extends Screen {
    private static final ResourceLocation PARCHMENTGUI = ResourceLocation.fromNamespaceAndPath(Millenaire.MODID, "textures/gui/ml_parchment.png");

    private final ItemMillParchment item;
    private final List<String> stringPages = new ArrayList<>();
    private int page = 0;

    private Button forward;
    private Button backward;

    public GuiParchment(ItemStack stack) {
        super(Component.translatable("gui.millenaire.parchment"));
        
        if (stack.getItem() instanceof ItemMillParchment parchment) {
            item = parchment;
            processContent();
        } else {
            item = null;
            System.err.println("Parchment Gui called from wrong Item. Something failed.");
        }
    }

    private void processContent() {
        if (item == null) return;

        for (String contentKey : item.contents) {
            String current = Component.translatable(contentKey).getString();
            int marker = 0;
            
            while (marker < current.length()) {
                int charsRemaining = 650;
                marker = 0;

                while (marker < current.length() && (charsRemaining > 0 || current.charAt(marker) != ' ')) {
                    if (current.substring(marker).startsWith("\n\n")) {
                        charsRemaining -= 37;
                    }
                    charsRemaining--;
                    marker++;
                }

                if (marker == current.length()) {
                    stringPages.add(current);
                    break;
                } else {
                    String sub = current.substring(0, marker);
                    stringPages.add(sub);
                    current = current.substring(marker + 1);
                    marker = 0;
                }
            }
        }
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

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);
        guiGraphics.blit(PARCHMENTGUI, (this.width - 203) / 2, 2, 0, 0, 203, 219);
        
        if (item != null) {
            String drawTitle = Component.translatable(item.title).getString();
            guiGraphics.drawString(this.font, drawTitle, (this.width - this.font.width(drawTitle)) / 2, 6, 0, false);
        }
        
        if (!stringPages.isEmpty() && page < stringPages.size()) {
            String drawContents = stringPages.get(page);
            guiGraphics.drawWordWrap(this.font, Component.literal(drawContents), (this.width / 2) - 94, 20, 190, 0);
        }
        
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
    }

    private void updateButtons() {
        this.backward.visible = page != 0;
        this.forward.visible = page != stringPages.size() - 1;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}