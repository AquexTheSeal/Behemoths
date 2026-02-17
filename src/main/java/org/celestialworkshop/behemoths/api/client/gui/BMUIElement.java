package org.celestialworkshop.behemoths.api.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;

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
}