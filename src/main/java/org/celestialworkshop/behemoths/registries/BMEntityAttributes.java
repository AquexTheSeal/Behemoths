package org.celestialworkshop.behemoths.registries;

import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.celestialworkshop.behemoths.Behemoths;
import org.celestialworkshop.behemoths.entities.Archzombie;
import org.celestialworkshop.behemoths.entities.BanishingStampede;
import org.celestialworkshop.behemoths.entities.Hollowborne;
import org.celestialworkshop.behemoths.entities.HollowborneTurret;

@Mod.EventBusSubscriber(modid = Behemoths.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BMEntityAttributes {

    @SubscribeEvent
    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(BMEntityTypes.ARCHZOMBIE.get(), Archzombie.createAttributes());
        event.put(BMEntityTypes.BANISHING_STAMPEDE.get(), BanishingStampede.createAttributes());
        event.put(BMEntityTypes.HOLLOWBORNE.get(), Hollowborne.createAttributes());
        event.put(BMEntityTypes.HOLLOWBORNE_TURRET.get(), HollowborneTurret.createAttributes());
    }
}
