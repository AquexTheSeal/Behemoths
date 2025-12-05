package org.celestialworkshop.behemoths.client.guis.uielements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvents;
import org.celestialworkshop.behemoths.api.client.gui.AnimatedUIElement;
import org.celestialworkshop.behemoths.client.guis.screens.ColossangrimScreen;

public class ColossangrimArrowsElement extends AnimatedUIElement<ColossangrimScreen> {
    public final boolean isLeft;

    public int clickedTicks;

    public ColossangrimArrowsElement(ColossangrimScreen screen, boolean isLeft) {
        super(screen);
        this.isLeft = isLeft;
    }

    @Override
    public int getWidth() {
        return 16;
    }

    @Override
    public int getHeight() {
        return 48;
    }

    @Override
    public void tick() {
        super.tick();
        if (clickedTicks > 0) {
            clickedTicks--;
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int vOff = 0;
        if (isLeft) {
            vOff = 48;
        }
        if (clickedTicks > 0 || !canProceed()) {
            vOff += 32;
        } else if (isHovered(mouseX, mouseY)) {
            vOff += 16;
        }
        guiGraphics.blit(ColossangrimScreen.MAIN_TEXTURE, getRenderX(), getRenderY(), vOff, 160, getWidth(), getHeight());
    }

    @Override
    public void onClick() {
        if (!canProceed()) return;
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1f));
        this.getScreen().pageNumber += isLeft ? -1 : 1;
        this.getScreen().refreshMobEntries();
        this.clickedTicks = 2;
    }

    public boolean canProceed() {
        if (this.isLeft) {
            return this.getScreen().pageNumber > 0;
        } else {
            return this.getScreen().pageNumber < this.getScreen().getMaxPages();
        }
    }
}
