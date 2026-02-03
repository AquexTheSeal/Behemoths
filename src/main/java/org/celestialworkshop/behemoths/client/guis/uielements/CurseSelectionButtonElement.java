package org.celestialworkshop.behemoths.client.guis.uielements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import org.celestialworkshop.behemoths.api.client.gui.AnimatedUIElement;
import org.celestialworkshop.behemoths.client.guis.screens.VotingSelectionScreen;
import org.celestialworkshop.behemoths.network.BMNetwork;
import org.celestialworkshop.behemoths.network.c2s.CurseSelectionIndexPacket;
import org.celestialworkshop.behemoths.world.clientdata.ClientPandemoniumData;

public class CurseSelectionButtonElement extends AnimatedUIElement<VotingSelectionScreen> {

    public final int index;
    public int clickedTicks;

    public CurseSelectionButtonElement(VotingSelectionScreen screen, int index) {
        super(screen);
        this.index = index;
    }

    @Override
    public int getWidth() {
        return 32;
    }

    @Override
    public int getHeight() {
        return 37;
    }

    @Override
    public void tick() {
        if (clickedTicks > 0) clickedTicks--;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int uOff = 0;
        if (isHovered(mouseX, mouseY)) {
            uOff = 32;
        }
        if (ClientPandemoniumData.localSelectedIndex == index) {
            uOff = 64;
        }
        guiGraphics.blit(VotingSelectionScreen.MAIN_TEXTURE, getRenderX(), getRenderY(), uOff, 160, getWidth(), getHeight());

        if (isHovered(mouseX, mouseY)) {
            Component votingComponent = Component.translatable("screen.behemoths.curse_selection.hover_button");
            guiGraphics.renderTooltip(Minecraft.getInstance().font, votingComponent, mouseX, mouseY);
        }
    }

    @Override
    public void onClick() {
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        clickedTicks = 2;
        ClientPandemoniumData.localSelectedIndex = index;
        BMNetwork.sendToServer(new CurseSelectionIndexPacket(ClientPandemoniumData.localSelectedIndex));
    }
}
