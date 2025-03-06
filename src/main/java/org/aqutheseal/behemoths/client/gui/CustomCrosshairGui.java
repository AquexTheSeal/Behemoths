package org.aqutheseal.behemoths.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.aqutheseal.behemoths.Behemoths;
import org.aqutheseal.behemoths.item.BallistaItem;

public class CustomCrosshairGui {

    protected static final ResourceLocation BALLISTA_CROSSHAIR_LOCATION = Behemoths.location("textures/gui/ballista_crosshair.png");
    protected static final ResourceLocation BALLISTA_CROSSHAIR_EXTRA_LOCATION = Behemoths.location("textures/gui/ballista_crosshair_part.png");

    public static void render(ForgeGui gui, GuiGraphics pGuiGraphics, float partialTick, int screenWidth, int screenHeight) {
        Minecraft mc = Minecraft.getInstance();
        Options options = mc.options;
        LocalPlayer player = mc.player;

        RenderSystem.assertOnRenderThread();

        if (options.getCameraType().isFirstPerson()) {
            ItemStack mainHandItem = player.getMainHandItem();
            ItemStack usedItem = player.getUseItem();
            if (mainHandItem.getItem() instanceof BallistaItem || usedItem.getItem() instanceof BallistaItem) {
                PoseStack poseStack = RenderSystem.getModelViewStack();
                poseStack.pushPose();
                RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                RenderSystem.enableBlend();
                int textureHeight = 24;
                int clampedProgress = 0;
                if (usedItem.getItem() instanceof BallistaItem) {
                    int progress = textureHeight * (usedItem.getItem().getUseDuration(usedItem) - player.getTicksUsingItem()) / CrossbowItem.getChargeDuration(usedItem);
                    clampedProgress = Mth.clamp(progress, 0, textureHeight);
                    if (clampedProgress <= 0) {
                        RenderSystem.setShaderColor(0, 1, 0, 1);
                    }
                } else if (!CrossbowItem.isCharged(mainHandItem)) {
                    clampedProgress = textureHeight;
                }
                pGuiGraphics.blit(BALLISTA_CROSSHAIR_LOCATION, (screenWidth - 25) / 2, (screenHeight - 25) / 2, 0, 0, 24, textureHeight, 24, 24);
                pGuiGraphics.blit(BALLISTA_CROSSHAIR_EXTRA_LOCATION, (screenWidth - 25) / 2, (screenHeight - 25) / 2, 0, 0, 24, textureHeight - clampedProgress, 24, 24);
                RenderSystem.defaultBlendFunc();
                poseStack.popPose();
            }
        }
    }
}
