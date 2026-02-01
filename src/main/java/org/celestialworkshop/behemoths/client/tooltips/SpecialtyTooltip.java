package org.celestialworkshop.behemoths.client.tooltips;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.util.FastColor;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import org.celestialworkshop.behemoths.api.item.specialty.SpecialtyInstance;
import org.joml.Matrix4f;

import java.util.Collections;
import java.util.List;

public record SpecialtyTooltip(List<SpecialtyInstance> instances) implements TooltipComponent, ClientTooltipComponent {

    public static final int MAX_TEXT_WIDTH = 200;
    public static final int GAP = 2;

    @Override
    public int getHeight() {
        Font font = Minecraft.getInstance().font;
        int rY = font.lineHeight * 2;
        for (SpecialtyInstance instance : this.instances) {
            rY += font.lineHeight;
            if (this.shouldShowDescription()) {
                rY += font.wordWrapHeight(instance.getDisplayDescription(), MAX_TEXT_WIDTH);
            }
        }
        return rY;
    }

    @Override
    public int getWidth(Font pFont) {
        int largest = pFont.width(this.getSpecialityTitle());

        for (SpecialtyInstance instance : this.instances) {
            largest = Math.max(largest, pFont.width(instance.getDisplayName()) + pFont.lineHeight + GAP);
            if (this.shouldShowDescription()) {
                List<FormattedText> descLines = pFont.getSplitter().splitLines(instance.getDisplayDescription(), MAX_TEXT_WIDTH, Style.EMPTY);
                for (FormattedText line : descLines) {
                    largest = Math.max(largest, pFont.width(line) + pFont.lineHeight + GAP);
                }
            }
        }

        return largest;
    }

    @Override
    public void renderText(Font pFont, int pX, int pY, Matrix4f pMatrix, MultiBufferSource.BufferSource pBufferSource) {
    }

    @Override
    public void renderImage(Font pFont, int pX, int pY, GuiGraphics pGuiGraphics) {
        int rY = pY + pFont.lineHeight;

        pGuiGraphics.drawString(pFont, this.getSpecialityTitle(), pX, rY, 0xFFFFFF);

        for (SpecialtyInstance instance : this.instances) {
            rY += pFont.lineHeight;

            pGuiGraphics.drawString(pFont, instance.getDisplayName(), pX + pFont.lineHeight + GAP, rY, 0xFFFFFF);

            int intColor = instance.specialty().getCategory().getColor().getColor().intValue();
            float r = FastColor.ARGB32.red(intColor) / 255.0F;
            float g = FastColor.ARGB32.green(intColor) / 255.0F;
            float b = FastColor.ARGB32.blue(intColor) / 255.0F;
            pGuiGraphics.setColor(r, g, b, 1.0F);
            pGuiGraphics.blit(instance.getDisplayTextureLocation(), pX, rY, 0, 0, 8, 8, 8, 8);
            pGuiGraphics.setColor(1f, 1f, 1f, 1f);

            if (this.shouldShowDescription()) {
                rY += pFont.lineHeight;
                pGuiGraphics.drawWordWrap(pFont, instance.getDisplayDescription(), pX + pFont.lineHeight + GAP, rY, MAX_TEXT_WIDTH, 0x636363);
                rY += pFont.wordWrapHeight(instance.getDisplayDescription(), MAX_TEXT_WIDTH) - pFont.lineHeight;
            }
        }
    }

    public Component getSpecialityTitle() {
        return Component.translatable("tooltip.behemoths.specialty")
                .withStyle(style -> style.withColor(TextColor.fromRgb(0x6e6e6e)))
                .append(Component.literal(" [Shift]")
                        .withStyle(style -> style.withColor(TextColor.fromRgb(0x4d4d4d)))
                );
    }

    public boolean shouldShowDescription() {
        return Screen.hasShiftDown() || Screen.hasControlDown();
    }
}
