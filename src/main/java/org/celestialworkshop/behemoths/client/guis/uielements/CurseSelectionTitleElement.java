package org.celestialworkshop.behemoths.client.guis.uielements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.celestialworkshop.behemoths.api.client.gui.AnimatedUIElement;
import org.celestialworkshop.behemoths.client.guis.screens.PandemoniumCurseSelectionScreen;
import org.celestialworkshop.behemoths.world.clientdata.ClientPandemoniumData;

public class CurseSelectionTitleElement extends AnimatedUIElement<PandemoniumCurseSelectionScreen> {

    public CurseSelectionTitleElement(PandemoniumCurseSelectionScreen screen) {
        super(screen);
    }

    @Override
    public int getWidth() {
        return 144;
    }

    @Override
    public int getHeight() {
        return 22;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.blit(PandemoniumCurseSelectionScreen.MAIN_TEXTURE, this.getRenderX() - this.getWidth()/2, this.getRenderY() - this.getHeight()/2, 0, 197, this.getWidth(), this.getHeight());

        MutableComponent component = Component.translatable("screen.behemoths.curse_selection.title");
        Font font = Minecraft.getInstance().font;
        font.drawInBatch8xOutline(
                component.getVisualOrderText(),
                this.getRenderX() - font.width(component) / 2F,
                this.getRenderY() - font.lineHeight / 2F,
                -1,
                0,
                guiGraphics.pose().last().pose(),
                guiGraphics.bufferSource(),
                LightTexture.FULL_BRIGHT
        );

        int barW = 224;
        int barH = 11;
        guiGraphics.blit(PandemoniumCurseSelectionScreen.MAIN_TEXTURE, this.getRenderX() - barW/2, this.getRenderY() - (barH/2) + 20, 0, 219, barW, barH);
        int max = ClientPandemoniumData.localMaxTime;
        int current = ClientPandemoniumData.localRemainingTime;
        float ratio = current / (float) max;
        int delta = (int) (ratio * barW);
        guiGraphics.blit(PandemoniumCurseSelectionScreen.MAIN_TEXTURE, this.getRenderX() - barW/2, this.getRenderY() - (barH/2) + 20, 0, 230, delta, barH);

    }
}
