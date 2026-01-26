package org.celestialworkshop.behemoths.client.tooltips;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import org.joml.Matrix4f;

public record SpecialtyTooltip() implements TooltipComponent, ClientTooltipComponent {

    public static final int TEXT_HEIGHT = 8;
    public static final int TEXT_GAP = 2;
    public static final int BULLET_POINT_GAP = 4;

    @Override
    public int getHeight() {
        int rY = TEXT_HEIGHT + TEXT_GAP;
        rY += TEXT_HEIGHT + TEXT_GAP;
        return rY;
    }

    @Override
    public int getWidth(Font pFont) {
        return pFont.width(this.getSpecialityTitle());
    }

    @Override
    public void renderText(Font pFont, int pX, int pY, Matrix4f pMatrix, MultiBufferSource.BufferSource pBufferSource) {
    }

    @Override
    public void renderImage(Font pFont, int pX, int pY, GuiGraphics pGuiGraphics) {
        int rY = pY + TEXT_HEIGHT + TEXT_GAP;
        pGuiGraphics.drawString(pFont, this.getSpecialityTitle(), pX, rY, 0xFFFFFF);
    }

    public Component getSpecialityTitle() {
        return Component.translatable("tooltip.behemoths.specialty")
                .withStyle(style -> style.withColor(TextColor.fromRgb(0x6e6e6e)))
                .append(Component.literal(" [Shift]")
                        .withStyle(style -> style.withColor(TextColor.fromRgb(0x4d4d4d)))
                );
    }
}
