package org.celestialworkshop.behemoths;

import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.celestialworkshop.behemoths.entities.Archzombie;
import org.celestialworkshop.behemoths.entities.HollowborneTurret;
import org.celestialworkshop.behemoths.network.BMNetwork;
import org.celestialworkshop.behemoths.registries.BMEntityTypes;

@Mod.EventBusSubscriber(modid = Behemoths.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BMCommonSetup {

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            BMNetwork.register();
        });
    }

    @SubscribeEvent
    public static void onSpawnPlacementRegistry(SpawnPlacementRegisterEvent event) {
        event.register(BMEntityTypes.ARCHZOMBIE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Archzombie::checkArchzombieSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(BMEntityTypes.HOLLOWBORNE_TURRET.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, HollowborneTurret::checkHollowborneSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);

    }
}
