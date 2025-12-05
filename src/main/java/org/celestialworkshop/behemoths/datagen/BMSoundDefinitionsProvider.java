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
        this.variantSound(BMSoundEvents.ARCHZOMBIE_SWING, 2);

        this.variantSound(BMSoundEvents.STAMPEDE_HURT, 3);
        this.basicSound(BMSoundEvents.STAMPEDE_CHARGE_ROAR);
        this.basicSound(BMSoundEvents.STAMPEDE_DEATH);
        this.basicSound(BMSoundEvents.STAMPEDE_CHARGE_STEP);
        this.variantSound(BMSoundEvents.STAMPEDE_AMBIENT, 3);
    }

    private void basicSound(RegistryObject<SoundEvent> sound) {
        this.add(sound.get(), subtitledSound(sound.getId().getPath())
                .with(sound(Behemoths.prefix(sound.getId().getPath())).attenuationDistance(32))
        );
    }

    private void variantSound(RegistryObject<SoundEvent> sound, int variants) {
        SoundDefinition soundDefinition = subtitledSound(sound.getId().getPath());

        for (int i = 0; i < variants; i++) {
            soundDefinition.with(sound(Behemoths.prefix(sound.getId().getPath() + "_" + i)).attenuationDistance(32));
        }

        this.add(sound.get(), soundDefinition);
    }

    public static SoundDefinition subtitledSound(String path) {
        return SoundDefinition.definition().subtitle("%s.subtitle.%s".formatted(Behemoths.MODID, path));
    }
}
