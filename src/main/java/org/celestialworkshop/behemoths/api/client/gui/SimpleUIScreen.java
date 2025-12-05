package org.celestialworkshop.behemoths.api.client.gui;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public abstract class SimpleUIScreen extends Screen {
    public final ObjectArrayList<BMUIElement<?>> uiElements = new ObjectArrayList<>();

    protected SimpleUIScreen(Component pTitle) {
        super(pTitle);
    }

    @Override
    protected void init() {
        super.init();
        this.uiElements.clear();
        this.initUIElements();
        for (BMUIElement<?> element : uiElements) {
            element.postInit();
        }
    }

    public abstract void initUIElements();

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        for (BMUIElement<?> element : uiElements) {
            element.setupRender(guiGraphics, mouseX, mouseY, partialTick);
        }
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void tick() {
        super.tick();
        for (BMUIElement<?> element : uiElements) {
            element.tick();
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (BMUIElement<?> element : uiElements) {
            element.setupClick((int) mouseX, (int) mouseY);
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
}
