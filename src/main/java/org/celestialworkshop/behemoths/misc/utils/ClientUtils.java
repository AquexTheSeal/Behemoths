package org.celestialworkshop.behemoths.misc.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.celestialworkshop.behemoths.client.guis.screens.ColossangrimScreen;

public class ClientUtils {

    public static LivingEntity tryCreateDisplayEntity(Level level, Player player, EntityType<?> entityType) {
        LivingEntity entity;
        if (entityType != null && entityType.create(level) instanceof LivingEntity living) {
            entity = living;
        } else {
            entity = new RemotePlayer((ClientLevel) level, player.getGameProfile()) {
                @Override
                public Component getDisplayName() {
                    return Component.empty();
                }
            };
        }
        return entity;
    }

    public static void openColossangrimScreen() {
        ColossangrimScreen dict = new ColossangrimScreen();
        Minecraft.getInstance().setScreen(dict);
    }

    public static void drawShadowedWordWrap(GuiGraphics guiGraphics, Font pFont, FormattedText pText, int pX, int pY, int pLineWidth, int pColor) {
        for (FormattedCharSequence formattedcharsequence : pFont.split(pText, pLineWidth)) {
            guiGraphics.drawString(pFont, formattedcharsequence, pX, pY, pColor, true);
            pY += 9;
        }
    }

    public static void drawCenteredOutlinedWordWrap(GuiGraphics guiGraphics, Font pFont, FormattedText pText, int pX, int pY, int pLineWidth, int pColor, int pOutlineColor) {
        for (FormattedCharSequence formattedcharsequence : pFont.split(pText, pLineWidth)) {
            pFont.drawInBatch8xOutline(formattedcharsequence, pX - ((float) pFont.width(formattedcharsequence) / 2), pY, pColor, pOutlineColor, guiGraphics.pose().last().pose(), guiGraphics.bufferSource(), LightTexture.FULL_BRIGHT);
            pY += 9;
        }
    }

    public static void drawCenteredWordWrap(GuiGraphics guiGraphics, Font pFont, FormattedText pText, int pX, int pY, int pLineWidth, int pColor, boolean shadow) {
        for (FormattedCharSequence formattedcharsequence : pFont.split(pText, pLineWidth)) {
            drawCenteredString(guiGraphics, pFont, formattedcharsequence, pX, pY, pColor, shadow);
            pY += 9;
        }
    }

    public static void drawCenteredString(GuiGraphics guiGraphics, Font pFont, FormattedCharSequence pText, int pX, int pY, int pColor, boolean shadow) {
        guiGraphics.drawString(pFont, pText, pX - pFont.width(pText) / 2, pY, pColor, shadow);
    }
}
