package org.celestialworkshop.behemoths;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.celestialworkshop.behemoths.registries.BMKeybinds;
import org.celestialworkshop.behemoths.world.clientdata.ClientPandemoniumData;

@Mod.EventBusSubscriber(modid = Behemoths.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class BMClientEvents {

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            while (BMKeybinds.openVotingProgress.consumeClick()) {
                if (Minecraft.getInstance().screen == null && ClientPandemoniumData.localRemainingTime > 0) {
                    ClientPandemoniumData.openPandemoniumSelection(ClientPandemoniumData.localSelectableCurses, false);
                }
            }
        }
    }
}
