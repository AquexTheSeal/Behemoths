package org.celestialworkshop.behemoths.client.guis.overlays;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.celestialworkshop.behemoths.client.guis.screens.VotingSelectionScreen;
import org.celestialworkshop.behemoths.registries.BMKeybinds;
import org.celestialworkshop.behemoths.world.clientdata.ClientPandemoniumData;

import java.awt.*;

public class VotingProgressOverlay {

    public static void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        if (ClientPandemoniumData.localRemainingTime > 0 && !(Minecraft.getInstance().screen instanceof VotingSelectionScreen)) {

            int fr = new Color(30, 0, 0, (int) (0.85f * 255)).getRGB();
            int to = new Color(0, 0, 0, 0).getRGB();
            guiGraphics.fill(0, 0, screenWidth, 20, fr);
            guiGraphics.fillGradient(0, 20, screenWidth, 35, fr, to);

            int yy = 2;

            String key = BMKeybinds.openVotingProgress.getTranslatedKeyMessage().getString();
            guiGraphics.drawCenteredString(gui.getFont(), Component.translatable("overlay.behemoths.voting_progress", key), screenWidth / 2, yy, -1);

            int barW = 224;
            int barH = 11;
            guiGraphics.blit(VotingSelectionScreen.MAIN_TEXTURE, (screenWidth/2) - (barW/2), yy + 10, 0, 219, barW, barH);
            int max = ClientPandemoniumData.localMaxTime;
            int current = ClientPandemoniumData.localRemainingTime;
            float ratio = current / (float) max;
            int delta = (int) (ratio * barW);
            guiGraphics.blit(VotingSelectionScreen.MAIN_TEXTURE, (screenWidth/2) - (barW/2), yy + 10, 0, 230, delta, barH);
        }
    }
}
