package org.celestialworkshop.behemoths.api.camera;

import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.celestialworkshop.behemoths.Behemoths;
import org.celestialworkshop.behemoths.entities.misc.Cragpiercer;
import org.celestialworkshop.behemoths.network.BMNetwork;
import org.celestialworkshop.behemoths.network.c2s.CragpiercerKeyInputPacket;
import org.celestialworkshop.behemoths.network.c2s.CragpiercerRotationInputPacket;
import org.jetbrains.annotations.Nullable;

@Mod.EventBusSubscriber(modid = Behemoths.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class CragpiercerCameraManager {
    private static final Minecraft mc = Minecraft.getInstance();


    private static byte lastLocalXRInput = 0;
    private static byte lastLocalYRInput = 0;
    private static boolean lastLocalShootInput = false;

    public static void enterBallistaCamera(Cragpiercer piercer) {
        mc.setCameraEntity(piercer);
    }

    public static @Nullable Cragpiercer getPiercer() {
        return mc.getCameraEntity() instanceof Cragpiercer crag ? crag : null;
    }

    public static void clientTick() {
        if (getPiercer() == null) {
            return;
        }

        if (getPiercer().isRemoved()) {
            exitCamera();
            return;
        }

        if (mc.player != null) {
            mc.player.input.jumping = false;
        }

        mc.options.setCameraType(CameraType.THIRD_PERSON_BACK);
        handleInput();
    }

    private static void handleInput() {
        byte localXRInput = 0;
        byte localYRInput = 0;

        if (mc.options.keyLeft.isDown()) localYRInput = -1;
        if (mc.options.keyRight.isDown()) localYRInput = 1;
        if (mc.options.keyDown.isDown()) localXRInput = -1;
        if (mc.options.keyUp.isDown()) localXRInput = 1;

        if (localXRInput != lastLocalXRInput || localYRInput != lastLocalYRInput) {
            lastLocalXRInput = localXRInput;
            lastLocalYRInput = localYRInput;
            BMNetwork.sendToServer(new CragpiercerRotationInputPacket(getPiercer().getId(), localXRInput, localYRInput));
        }

        boolean localShootInput = mc.options.keyJump.isDown();
        if (localShootInput != lastLocalShootInput) {
            lastLocalShootInput = localShootInput;
            if (localShootInput) {
                BMNetwork.sendToServer(new CragpiercerKeyInputPacket(getPiercer().getId(), (byte)2));
                getPiercer().shootInput = true;
            } else {
                BMNetwork.sendToServer(new CragpiercerKeyInputPacket(getPiercer().getId(), (byte)1));
                getPiercer().shootInput = false;
            }
        }

        if (mc.options.keyShift.isDown()) {
            BMNetwork.sendToServer(new CragpiercerKeyInputPacket(getPiercer().getId(), (byte)0));
        }
    }

    public static boolean isInCamera() {
        return getPiercer() != null;
    }

    public static void exitCamera() {
        if (mc.player != null) {
            mc.options.setCameraType(CameraType.FIRST_PERSON);
            mc.setCameraEntity(mc.player);
            lastLocalXRInput = 0;
            lastLocalYRInput = 0;
            lastLocalShootInput = false;
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onInteractionKey(InputEvent.InteractionKeyMappingTriggered event) {
        if (isInCamera()) {
            if (event.isAttack() || event.isUseItem() || event.shouldSwingHand() || event.isPickBlock()) {
                event.setCanceled(true);
                event.setSwingHand(false);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onMouseScroll(InputEvent.MouseScrollingEvent event) {
        if (isInCamera()) {
            event.setCanceled(true);
        }
    }
}
