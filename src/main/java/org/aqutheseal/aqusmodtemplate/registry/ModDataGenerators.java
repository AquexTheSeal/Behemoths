package org.aqutheseal.aqusmodtemplate.registry;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.aqutheseal.aqusmodtemplate.Aqusmodtemplate;

@Mod.EventBusSubscriber(modid = Aqusmodtemplate.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModDataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper efh = event.getExistingFileHelper();
        PackOutput packOutput = event.getGenerator().getPackOutput();
    }
}