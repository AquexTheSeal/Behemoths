package org.celestialworkshop.behemoths.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinition;
import net.minecraftforge.common.data.SoundDefinitionsProvider;
import net.minecraftforge.registries.RegistryObject;
import org.celestialworkshop.behemoths.Behemoths;
import org.celestialworkshop.behemoths.registries.BMSoundEvents;

public class BMSoundDefinitionsProvider extends SoundDefinitionsProvider {

    public BMSoundDefinitionsProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, Behemoths.MODID, helper);
    }

    @Override
    public void registerSounds() {
        for (BMSoundEvents.AutoGenSound sound : BMSoundEvents.AUTO_GEN_SOUNDS) {
            if (sound.variants() > 1) {
                this.variantSound(sound.sound(), sound.variants());
            } else {
                if (sound.stream()) {
                    this.music(sound.sound());
                } else {
                    this.basicSound(sound.sound());
                }
            }
        }
    }

    private void music(RegistryObject<SoundEvent> sound) {
        this.add(sound.get(), subtitledSound(sound.getId().getPath())
                .with(sound(Behemoths.prefix(sound.getId().getPath())).stream())
        );
    }

    private void basicSound(RegistryObject<SoundEvent> sound) {
        this.add(sound.get(), subtitledSound(sound.getId().getPath())
                .with(sound(Behemoths.prefix(sound.getId().getPath())))
        );
    }

    private void variantSound(RegistryObject<SoundEvent> sound, int variants) {
        SoundDefinition soundDefinition = subtitledSound(sound.getId().getPath());

        for (int i = 0; i < variants; i++) {
            soundDefinition.with(sound(Behemoths.prefix(sound.getId().getPath() + "_" + i)));
        }

        this.add(sound.get(), soundDefinition);
    }

    public static SoundDefinition subtitledSound(String path) {
        return SoundDefinition.definition().subtitle("%s.subtitle.%s".formatted(Behemoths.MODID, path));
    }
}
