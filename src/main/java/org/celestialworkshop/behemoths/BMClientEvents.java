package org.celestialworkshop.behemoths;

import com.mojang.datafixers.util.Either;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.TieredItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.celestialworkshop.behemoths.client.tooltips.SpecialtyTooltip;
import org.celestialworkshop.behemoths.registries.BMItemTiers;
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

    @SubscribeEvent
    public static void onGatherTooltipComponents(RenderTooltipEvent.GatherComponents event) {
        if (event.getItemStack().getItem() instanceof TieredItem tiered) {
            if (tiered.getTier() == BMItemTiers.MAGNALYTH) {
                event.getTooltipElements().add(Either.right(new SpecialtyTooltip()));
            }
        }
    }
}
