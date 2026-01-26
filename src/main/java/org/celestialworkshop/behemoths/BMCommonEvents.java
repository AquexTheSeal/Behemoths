package org.celestialworkshop.behemoths;

import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import org.celestialworkshop.behemoths.commands.PandemoniumCommand;
import org.celestialworkshop.behemoths.world.clientdata.ClientPandemoniumData;
import org.celestialworkshop.behemoths.world.savedata.WorldPandemoniumData;

@Mod.EventBusSubscriber(modid = Behemoths.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BMCommonEvents {

    @SubscribeEvent
    public static void onWorldTick(TickEvent.LevelTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (event.side == LogicalSide.SERVER) {
                WorldPandemoniumData.get((ServerLevel) event.level).tickPandemoniumWorld(event.level);
            } else {
                ClientPandemoniumData.tickPandemoniumClient();
            }
        }
    }

    @SubscribeEvent
    public static void onCommandRegistry(RegisterCommandsEvent event) {
        PandemoniumCommand.register(event.getDispatcher());
    }
}
