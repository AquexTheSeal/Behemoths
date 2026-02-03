package org.celestialworkshop.behemoths.api.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;

/**
 * Base class for all Behemoths UI elements.
 * Provides common functionality for positioning, sizing, rendering, and interaction.
 */
public abstract class BMUIElement<T extends Screen> {
    public final Minecraft minecraft;
    protected final T screen;
    protected int x;
    protected int y;

    protected BMUIElement(T screen) {
        this.screen = screen;
        this.minecraft = Minecraft.getInstance();
    }

    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public T getScreen() {
        return screen;
    }

    public abstract int getWidth();

    public abstract int getHeight();

    public int getRenderX() {
        return x;
    }

    public int getRenderY() {
        return y;
    }

    public abstract void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick);

    public void postInit() {
    }

    public void tick() {
    }

    public void onClick() {
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return mouseX >= getRenderX() && mouseX <= getRenderX() + getWidth() && mouseY >= getRenderY() && mouseY <= getRenderY() + getHeight();
    }

    public void setupClick(int mouseX, int mouseY) {
        if (isHovered(mouseX, mouseY)) {
            this.onClick();
        }
    }

    public void setupRender(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void drawShadowedWordWrap(GuiGraphics guiGraphics, Font pFont, FormattedText pText, int pX, int pY, int pLineWidth, int pColor) {
        for (FormattedCharSequence formattedcharsequence : pFont.split(pText, pLineWidth)) {
            guiGraphics.drawString(pFont, formattedcharsequence, pX, pY, pColor, true);
            pY += 9;
        }
    }

    public void drawCenteredOutlinedWordWrap(GuiGraphics guiGraphics, Font pFont, FormattedText pText, int pX, int pY, int pLineWidth, int pColor, int pOutlineColor) {
        for (FormattedCharSequence formattedcharsequence : pFont.split(pText, pLineWidth)) {
            pFont.drawInBatch8xOutline(formattedcharsequence, pX - ((float) pFont.width(formattedcharsequence) / 2), pY, pColor, pOutlineColor, guiGraphics.pose().last().pose(), guiGraphics.bufferSource(), LightTexture.FULL_BRIGHT);
            pY += 9;
        }
    }

    public void drawCenteredWordWrap(GuiGraphics guiGraphics, Font pFont, FormattedText pText, int pX, int pY, int pLineWidth, int pColor, boolean shadow) {
        for (FormattedCharSequence formattedcharsequence : pFont.split(pText, pLineWidth)) {
            this.drawCenteredString(guiGraphics, pFont, formattedcharsequence, pX, pY, pColor, shadow);
            pY += 9;
        }
    }

    public void drawCenteredString(GuiGraphics guiGraphics, Font pFont, FormattedCharSequence pText, int pX, int pY, int pColor, boolean shadow) {
        guiGraphics.drawString(pFont, pText, pX - pFont.width(pText) / 2, pY, pColor, shadow);
    }
}