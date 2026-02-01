package org.celestialworkshop.behemoths.client.guis.overlays;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.celestialworkshop.behemoths.Behemoths;
import org.celestialworkshop.behemoths.entities.BanishingStampede;

public class BanishingStampedeJumpMeterOverlay {

    protected static final ResourceLocation BARS_LOCATION = Behemoths.prefix("textures/gui/hud_misc.png");

    public static void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        Entity mount = gui.getMinecraft().player.getVehicle();
        if (mount instanceof BanishingStampede stampede && !gui.getMinecraft().options.hideGui) {
            gui.setupOverlayRenderState(true, false);
            renderJumpMeter(gui, stampede, guiGraphics, screenWidth, screenHeight);
        }
    }

    public static void renderJumpMeter(ForgeGui gui, BanishingStampede stampede, GuiGraphics guiGraphics, int screenWidth, int screenHeight) {
        LocalPlayer player = gui.getMinecraft().player;
        float scale = player.getJumpRidingScale();

        int barLength = 183;
        int shownBarLength = (int) (scale * (barLength + 1));

        int xx = screenWidth / 2 - 91;
        int yy = screenHeight - 32 - 3;

        guiGraphics.blit(BARS_LOCATION, xx, yy, 0, 0, barLength, 16);
        if (shownBarLength > 0) {
            guiGraphics.blit(BARS_LOCATION, xx, yy, 0, 16, shownBarLength, 16);

            guiGraphics.setColor(1.0F, 1.0F, 1.0F, (scale - 0.5F) * 2.0F);
            guiGraphics.blit(BARS_LOCATION, xx + 70, yy - 1, 0, 32, 42, 18);
            guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        }

        if (stampede.isWithinRamThreshold(scale)) {
            guiGraphics.blit(BARS_LOCATION, xx + 69, yy - 1, 42, 32, 44, 18);
        }
    }
}
