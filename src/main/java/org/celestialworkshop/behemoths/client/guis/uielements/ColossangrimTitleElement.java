package org.celestialworkshop.behemoths.client.guis.uielements;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.celestialworkshop.behemoths.api.client.gui.AnimatedUIElement;
import org.celestialworkshop.behemoths.client.guis.screens.ColossangrimScreen;

public class ColossangrimTitleElement extends AnimatedUIElement<ColossangrimScreen> {
    public final Component title;
    public final Font font;

    public ColossangrimTitleElement(ColossangrimScreen screen, Font font) {
        super(screen);
        this.title = screen.getTitle();
        this.font = font;
    }

    @Override
    public int getWidth() {
        return font.width(title);
    }

    @Override
    public int getHeight() {
        return font.lineHeight;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        font.drawInBatch8xOutline(
                title.getVisualOrderText(),
                getRenderX(),
                getRenderY(),
                ColossangrimScreen.COLOR_NORMAL,
                ColossangrimScreen.COLOR_BACKGROUND,
                guiGraphics.pose().last().pose(),
                guiGraphics.bufferSource(),
                LightTexture.FULL_BRIGHT
        );

        int pageNumber = getScreen().pageNumber + 1;
        int maxPages = getScreen().getMaxPages() + 1;
        MutableComponent page = Component.literal(pageNumber + " / " + maxPages);

        font.drawInBatch8xOutline(
                page.getVisualOrderText(),
                getRenderX() + (getWidth() / 2F) - font.width(page) / 2F,
                getRenderY() + 10,
                -1,
                ColossangrimScreen.COLOR_BACKGROUND,
                guiGraphics.pose().last().pose(),
                guiGraphics.bufferSource(),
                LightTexture.FULL_BRIGHT
        );
    }
}
