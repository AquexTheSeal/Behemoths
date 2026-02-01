package org.celestialworkshop.behemoths.client.tooltips;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import org.celestialworkshop.behemoths.Behemoths;
import org.joml.Matrix4f;

public record HeartTooltip(int energy, int maxEnergy) implements TooltipComponent, ClientTooltipComponent {

    public static final ResourceLocation LOCATION = Behemoths.prefix("textures/gui/hud_items.png");

    @Override
    public int getHeight() {
        return 9;
    }

    @Override
    public int getWidth(Font pFont) {
        return 112;
    }

    @Override
    public void renderText(Font pFont, int pX, int pY, Matrix4f pMatrix, MultiBufferSource.BufferSource pBufferSource) {
    }

    @Override
    public void renderImage(Font pFont, int pX, int pY, GuiGraphics pGuiGraphics) {
        pGuiGraphics.blit(LOCATION, pX, pY, 0, 0, 112, 9);
        pGuiGraphics.blit(LOCATION, pX, pY, 0, 9, (int) Mth.lerp(energy / (float) maxEnergy, 0, 112), 9);
    }
}
