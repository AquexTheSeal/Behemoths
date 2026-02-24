package org.celestialworkshop.behemoths;

import com.mojang.datafixers.util.Either;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.celestialworkshop.behemoths.api.camera.CragpiercerCameraManager;
import org.celestialworkshop.behemoths.api.camera.ScreenShakeHandler;
import org.celestialworkshop.behemoths.client.guis.tooltips.HeartTooltip;
import org.celestialworkshop.behemoths.client.guis.tooltips.SpecialtyTooltip;
import org.celestialworkshop.behemoths.config.BMConfigManager;
import org.celestialworkshop.behemoths.entities.ai.mount.CustomJumpingMount;
import org.celestialworkshop.behemoths.entities.ai.mount.MountJumpManager;
import org.celestialworkshop.behemoths.items.BehemothHeartItem;
import org.celestialworkshop.behemoths.registries.BMCapabilities;
import org.celestialworkshop.behemoths.registries.BMKeybinds;
import org.celestialworkshop.behemoths.world.clientdata.ClientPandemoniumData;

@Mod.EventBusSubscriber(modid = Behemoths.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class BMClientEvents {

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            if (event.phase == TickEvent.Phase.END) {
                ScreenShakeHandler.clientTick();
                CragpiercerCameraManager.clientTick();

                int time = ClientPandemoniumData.localRemainingTime;
                while (BMKeybinds.openVotingProgress.consumeClick()) {
                    if (mc.screen == null && time > 0) {
                        ClientPandemoniumData.openPandemoniumSelection(ClientPandemoniumData.localSelectableCurses, false);
                    }
                }

                Entity vehicle = mc.player.getControlledVehicle();
                if (vehicle instanceof CustomJumpingMount mount) {
                    boolean isJumping = mc.player.input.jumping;
                    MountJumpManager jumpManager = mount.getMountJumpManager();
                    jumpManager.jumpableClientTick(isJumping);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onClientLogout(ClientPlayerNetworkEvent.LoggingOut event) {
        ClientPandemoniumData.reset();
    }

    @SubscribeEvent
    public static void onGatherTooltipComponents(RenderTooltipEvent.GatherComponents event) {
        ItemStack stack = event.getItemStack();
        if (stack.getItem() instanceof BehemothHeartItem item) {
            event.getTooltipElements().add(Either.right(new HeartTooltip(BehemothHeartItem.getHeartEnergy(stack), item.getMaxHeartEnergy())));
        }

        stack.getCapability(BMCapabilities.ITEM_SPECIALTY).ifPresent(handler -> {
            event.getTooltipElements().add(Either.right(new SpecialtyTooltip(handler.getSpecialties())));
        });
    }

    @SubscribeEvent
    public static void onPreRenderOverlay(RenderGuiOverlayEvent.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        if (event.getOverlay() == VanillaGuiOverlay.EXPERIENCE_BAR.type() || event.getOverlay() == VanillaGuiOverlay.JUMP_BAR.type()) {
            Entity vehicle = mc.player.getControlledVehicle();
            if (vehicle instanceof CustomJumpingMount) {
                event.cancel();
            }
        }
    }

    @SubscribeEvent
    public static void onCameraAnglesCompute(ViewportEvent.ComputeCameraAngles event) {
        if (BMConfigManager.CLIENT.enableScreenShake.get()) {
            ScreenShakeHandler.modifyCameraAngles(event);
        }
    }
}
