package org.celestialworkshop.behemoths.client.guis.overlays;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.celestialworkshop.behemoths.Behemoths;
import org.celestialworkshop.behemoths.entities.BanishingStampede;
import org.celestialworkshop.behemoths.entities.Hollowborne;
import org.celestialworkshop.behemoths.misc.utils.ClientUtils;

public class BehemothJumpBarOverlay {

    protected static final ResourceLocation BARS_LOCATION = Behemoths.prefix("textures/gui/hud_misc.png");

    public static void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        Entity mount = gui.getMinecraft().player.getControlledVehicle();
        if (!gui.getMinecraft().options.hideGui) {
            if (mount instanceof BanishingStampede stampede) {
                gui.setupOverlayRenderState(true, false);
                renderBanishingStampede(gui, stampede, guiGraphics, screenWidth, screenHeight);
            }

            if (mount instanceof Hollowborne hollowborne) {
                gui.setupOverlayRenderState(true, false);
                renderHollowborne(gui, hollowborne, guiGraphics, screenWidth, screenHeight);
            }
        }
    }

    public static void renderHollowborne(ForgeGui gui, Hollowborne hollowborne, GuiGraphics guiGraphics, int screenWidth, int screenHeight) {
        float scale = hollowborne.getMountJumpManager().getJumpScale();

        int barLength = 183;
        int shownBarLength = (int) (scale * (barLength + 1));

        int xx = screenWidth / 2 - 91;
        int yy = screenHeight - 32 - 3;

        if (hollowborne.shouldSmashFromPower(hollowborne.getMountJumpManager().jumpPower)) {
            xx += hollowborne.getRandom().nextIntBetweenInclusive(-1, 1);
            yy += hollowborne.getRandom().nextIntBetweenInclusive(-1, 1);
        }

        guiGraphics.blit(BARS_LOCATION, xx, yy, 0, 64, barLength, 16);
        if (shownBarLength > 0) {
            guiGraphics.blit(BARS_LOCATION, xx, yy, 0, 80, shownBarLength, 16);
        }

        int cooldown = hollowborne.getMountJumpManager().jumpCooldown;
        if(cooldown > 0) {
            Component text = Component.literal(String.valueOf(cooldown / 20));
            ClientUtils.drawCenteredOutlinedWordWrap(guiGraphics, gui.getFont(), text,xx + barLength / 2, yy + 4, barLength, 0xffffff, 0x202024);
        }
    }

    public static void renderBanishingStampede(ForgeGui gui, BanishingStampede stampede, GuiGraphics guiGraphics, int screenWidth, int screenHeight) {
        float scale = stampede.getMountJumpManager().getJumpScale();

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

        if (stampede.isWithinRamThreshold(stampede.getMountJumpManager().jumpPower)) {
            guiGraphics.blit(BARS_LOCATION, xx + 69, yy - 1, 42, 32, 44, 18);
        }

        int cooldown = stampede.getMountJumpManager().jumpCooldown;
        if(cooldown > 0) {
            Component text = Component.literal(String.valueOf(cooldown / 20));
            ClientUtils.drawCenteredOutlinedWordWrap(guiGraphics, gui.getFont(), text,xx + barLength / 2, yy + 4, barLength, 0xffd900, 0x2e1600);
        }
    }
}
