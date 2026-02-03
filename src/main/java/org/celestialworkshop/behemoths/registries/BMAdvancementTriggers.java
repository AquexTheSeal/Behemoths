package org.celestialworkshop.behemoths.registries;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.celestialworkshop.behemoths.Behemoths;
import org.celestialworkshop.behemoths.advancmeents.KillBehemothTrigger;

@Mod.EventBusSubscriber(modid = Behemoths.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BMAdvancementTriggers {

    public static KillBehemothTrigger KILL_BEHEMOTH = new KillBehemothTrigger(Behemoths.prefix("kill_behemoth"));

    @SubscribeEvent
    public static void registerTriggers(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            CriteriaTriggers.register(BMAdvancementTriggers.KILL_BEHEMOTH);
        });
    }
}
