package org.celestialworkshop.behemoths.client.guis.itemdecorations;

import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.IItemDecorator;
import org.celestialworkshop.behemoths.Behemoths;
import org.celestialworkshop.behemoths.items.BehemothHeartItem;

public class BehemothHeartDecoration implements IItemDecorator {

    public static final ResourceLocation TEXTURE = Behemoths.prefix("textures/gui/hud_items.png");

    @Override
    public boolean render(GuiGraphics guiGraphics, Font font, ItemStack stack, int xOffset, int yOffset) {
        if (stack.getItem() instanceof BehemothHeartItem heart) {
            if (BehemothHeartItem.hasMaxHeartEnergy(stack)) {

                LocalPlayer tickBasis = Minecraft.getInstance().player;
                int tick = tickBasis.tickCount;
                int width = 24;
                int height = 24;
                int centerX = (xOffset - 4) + (width / 2);
                int centerY = (yOffset - 4) + (height / 2);

                float a = 0.3f + (0.2f * Mth.sin(tick * 0.2f));
                guiGraphics.setColor(1f, 1f, 1f, a);

                guiGraphics.pose().pushPose();
                guiGraphics.pose().translate(centerX, centerY, 0);
                guiGraphics.pose().mulPose(Axis.ZP.rotationDegrees(tick * 3));
                guiGraphics.pose().translate(-width / 2f, -height / 2f, 0);
                guiGraphics.blit(TEXTURE, 0, 0, 112, 0, width, height);
                guiGraphics.pose().popPose();

                guiGraphics.pose().pushPose();
                guiGraphics.pose().translate(centerX, centerY, 0);
                guiGraphics.pose().mulPose(Axis.ZP.rotationDegrees(tick * 6));
                guiGraphics.pose().translate(-width / 2f, -height / 2f, 0);
                guiGraphics.blit(TEXTURE, 0, 0, 112 + width, 0, width, height);
                guiGraphics.pose().popPose();

                guiGraphics.setColor(1f, 1f, 1f, 1f);
            }
        }
        return false;
    }
}
