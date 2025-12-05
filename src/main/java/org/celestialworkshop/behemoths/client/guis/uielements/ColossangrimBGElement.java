package org.celestialworkshop.behemoths.client.guis.uielements;

import net.minecraft.client.gui.GuiGraphics;
import org.celestialworkshop.behemoths.api.client.gui.AnimatedUIElement;
import org.celestialworkshop.behemoths.client.guis.screens.ColossangrimScreen;

public class ColossangrimBGElement extends AnimatedUIElement<ColossangrimScreen> {

    public ColossangrimBGElement(ColossangrimScreen screen) {
        super(screen);
    }

    @Override
    public int getWidth() {
        return 176;
    }

    @Override
    public int getHeight() {
        return 160;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.blit(ColossangrimScreen.MAIN_TEXTURE, getRenderX(), getRenderY(), 0, 0, getWidth(), getHeight());
    }
}
