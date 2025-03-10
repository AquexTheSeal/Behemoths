package org.aqutheseal.behemoths.events;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.RandomSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.aqutheseal.behemoths.Behemoths;
import org.aqutheseal.behemoths.entity.SkyCharydbis;
import org.aqutheseal.behemoths.item.BallistaItem;
import org.aqutheseal.behemoths.util.mixin.IScreenShaker;

@Mod.EventBusSubscriber(modid = Behemoths.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ModClientEvents {

    @SubscribeEvent
    public static void onRenderGuiOverlayPre(RenderGuiOverlayEvent.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        boolean flag = player.getMainHandItem().getItem() instanceof BallistaItem;
        boolean flag1 = player.getUseItem().getItem() instanceof BallistaItem;
        if (event.getOverlay() == VanillaGuiOverlay.CROSSHAIR.type() && (flag || flag1)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onRenderPlayer(RenderLivingEvent event) {
        if (event.getEntity().getVehicle() instanceof SkyCharydbis) {
            //event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onViewport(ViewportEvent.ComputeCameraAngles event) {
        RandomSource random = Minecraft.getInstance().level.random;
        if (!Minecraft.getInstance().isPaused()) {
            if (Minecraft.getInstance().player instanceof IScreenShaker shaker) {
                event.setRoll((float) (event.getRoll() + random.nextGaussian() * shaker.behemoths$getShakeIntensity()));
                event.setPitch((float) (event.getPitch() + random.nextGaussian() * shaker.behemoths$getShakeIntensity()));
                event.setYaw((float) (event.getYaw() + random.nextGaussian() * shaker.behemoths$getShakeIntensity()));
            }
        }
    }
}
