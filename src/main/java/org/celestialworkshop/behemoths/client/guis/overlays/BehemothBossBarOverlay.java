package org.celestialworkshop.behemoths.client.guis.overlays;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.CustomizeGuiOverlayEvent;
import org.celestialworkshop.behemoths.Behemoths;

import java.awt.*;

public class BehemothBossBarOverlay {

    protected static final ResourceLocation BARS_LOCATION = Behemoths.prefix("textures/gui/hud_misc.png");
    protected static final ResourceLocation EYES_LOCATION = Behemoths.prefix("textures/gui/charydbis_bossbar_eye.png");

    public static void renderSkyCharydbisBossBar(CustomizeGuiOverlayEvent.BossEventProgress event) {
        GuiGraphics guiGraphics = event.getGuiGraphics();
        Minecraft mc = Minecraft.getInstance();
        float ageInTicks = mc.player.tickCount + mc.getPartialTick();
        int x = event.getX();
        int y = event.getY();
        float progress = event.getBossEvent().getProgress();

        // Bar Background
        guiGraphics.blit(BARS_LOCATION, x, y, 0, 96, 186, 32);

        // Empty Bar
        guiGraphics.blit(BARS_LOCATION, x, y + 8, 0, 128, 186, 16);
        // Full Bar
        guiGraphics.blit(BARS_LOCATION, x, y + 8, 0, 144, (int)(186 * progress), 16);

        // Eye Accessory
        int idx = (mc.player.tickCount / 3) % 24;
        guiGraphics.blit(EYES_LOCATION, x + (186 / 2) - 11, y + 6, idx * 24, 0, 24, 24, 576, 24);

        // Text
        Component name = event.getBossEvent().getName();
        int nameWidth = mc.font.width(name);
        mc.font.drawInBatch8xOutline(name.getVisualOrderText(), x + ((float) 186 / 2) - ((float) nameWidth / 2), y,
                new Color(241, 255, 96).getRGB(), new Color(0, 68, 58).getRGB(),
                guiGraphics.pose().last().pose(), guiGraphics.bufferSource(), LightTexture.FULL_BRIGHT
        );

        event.setIncrement(42);
    }
}
