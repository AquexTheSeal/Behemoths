package org.celestialworkshop.behemoths;

import com.mojang.datafixers.util.Either;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.celestialworkshop.behemoths.client.guis.tooltips.HeartTooltip;
import org.celestialworkshop.behemoths.client.guis.tooltips.SpecialtyTooltip;
import org.celestialworkshop.behemoths.items.BehemothHeartItem;
import org.celestialworkshop.behemoths.registries.BMCapabilities;
import org.celestialworkshop.behemoths.registries.BMKeybinds;
import org.celestialworkshop.behemoths.world.clientdata.ClientPandemoniumData;

@Mod.EventBusSubscriber(modid = Behemoths.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class BMClientEvents {

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            int time = ClientPandemoniumData.localRemainingTime;
            while (BMKeybinds.openVotingProgress.consumeClick()) {
                if (Minecraft.getInstance().screen == null && time > 0) {
                    ClientPandemoniumData.openPandemoniumSelection(ClientPandemoniumData.localSelectableCurses, false);
                }
            }

            if (time > 0 && time % 15 == 0) {

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
}
