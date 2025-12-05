package org.celestialworkshop.behemoths.client.guis.overlays;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.celestialworkshop.behemoths.client.guis.screens.PandemoniumCurseSelectionScreen;
import org.celestialworkshop.behemoths.registries.BMKeybinds;
import org.celestialworkshop.behemoths.world.clientdata.ClientPandemoniumData;

public class VotingProgressOverlay {

    public static void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        if (ClientPandemoniumData.localRemainingTime > 0 && !(Minecraft.getInstance().screen instanceof PandemoniumCurseSelectionScreen)) {
            String key = BMKeybinds.openVotingProgress.getTranslatedKeyMessage().getString();
            guiGraphics.drawCenteredString(gui.getFont(), Component.translatable("overlay.behemoths.voting_progress", key), screenWidth / 2, 10, -1);

            int barW = 224;
            int barH = 11;
            guiGraphics.blit(PandemoniumCurseSelectionScreen.MAIN_TEXTURE, (screenWidth/2) - (barW/2), 22, 0, 219, barW, barH);
            int max = ClientPandemoniumData.localMaxTime;
            int current = ClientPandemoniumData.localRemainingTime;
            float ratio = current / (float) max;
            int delta = (int) (ratio * barW);
            guiGraphics.blit(PandemoniumCurseSelectionScreen.MAIN_TEXTURE, (screenWidth/2) - (barW/2), 22, 0, 230, delta, barH);

        }
    }
}
