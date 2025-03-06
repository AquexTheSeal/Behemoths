package org.aqutheseal.behemoths.events;

import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.aqutheseal.behemoths.Behemoths;
import org.aqutheseal.behemoths.entity.SkyCharydbis;
import org.aqutheseal.behemoths.registry.BMEntityTypes;

@Mod.EventBusSubscriber(modid = Behemoths.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModCommonSetup {

    @SubscribeEvent
    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(BMEntityTypes.BARREN_SKY_CHARYDBIS.get(), SkyCharydbis.createAttributes().build());
        event.put(BMEntityTypes.LUSH_SKY_CHARYDBIS.get(), SkyCharydbis.createAttributes().build());
        event.put(BMEntityTypes.NORTHERN_SKY_CHARYDBIS.get(), SkyCharydbis.createAttributes().build());
        event.put(BMEntityTypes.NETHER_SKY_CHARYDBIS.get(), SkyCharydbis.createAttributes().build());
        event.put(BMEntityTypes.SOUL_SKY_CHARYDBIS.get(), SkyCharydbis.createAttributes().build());
        event.put(BMEntityTypes.VOID_SKY_CHARYDBIS.get(), SkyCharydbis.createAttributes().build());
    }
}
