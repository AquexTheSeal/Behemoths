package org.aqutheseal.behemoths.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinition;
import net.minecraftforge.common.data.SoundDefinitionsProvider;
import net.minecraftforge.registries.RegistryObject;
import org.aqutheseal.behemoths.Behemoths;
import org.aqutheseal.behemoths.registry.BMSoundEvents;

public class BMSoundDefinitionsProvider  extends SoundDefinitionsProvider {

    public BMSoundDefinitionsProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, Behemoths.MODID, helper);
    }

    @Override
    public void registerSounds() {
        for (RegistryObject<SoundEvent> entry : BMSoundEvents.SOUND_EVENTS.getEntries()) {
            this.defineSoundCompletely(entry.get(), entry.getId().getPath(), entry.getId().getPath());
        }
    }

    public void defineSoundCompletely(SoundEvent mainSound, String subtitle, String... soundFiles) {
        SoundDefinition def = SoundDefinition.definition().subtitle("subtitle." + Behemoths.MODID + "." + subtitle);
        for (String event : soundFiles) {
            def.with(SoundDefinition.Sound.sound(new ResourceLocation(Behemoths.MODID, event), SoundDefinition.SoundType.SOUND));
        }
        this.add(mainSound, def);
    }
}
