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
        this.variantSound(BMSoundEvents.ARCHZOMBIE_AMBIENT, 2);
        this.basicSound(BMSoundEvents.ARCHZOMBIE_DEATH);
        this.variantSound(BMSoundEvents.ARCHZOMBIE_HURT, 2);
        this.variantSound(BMSoundEvents.ARCHZOMBIE_SWING, 2);

        this.variantSound(BMSoundEvents.STAMPEDE_HURT, 3);
        this.basicSound(BMSoundEvents.STAMPEDE_CHARGE_ROAR);
        this.basicSound(BMSoundEvents.STAMPEDE_DEATH);
        this.basicSound(BMSoundEvents.STAMPEDE_CHARGE_STEP);
        this.variantSound(BMSoundEvents.STAMPEDE_AMBIENT, 3);

        this.basicSound(BMSoundEvents.HOLLOWBORNE_AMBIENT);
        this.basicSound(BMSoundEvents.HOLLOWBORNE_DEATH);
        this.basicSound(BMSoundEvents.HOLLOWBORNE_HURT);
        this.basicSound(BMSoundEvents.HOLLOWBORNE_SMASH);
        this.basicSound(BMSoundEvents.HOLLOWBORNE_SMASH_START);
        this.basicSound(BMSoundEvents.HOLLOWBORNE_STEP);
        this.basicSound(BMSoundEvents.HOLLOWBORNE_JUMP);
        this.basicSound(BMSoundEvents.HOLLOWBORNE_JUMP_STRONG);

        this.basicSound(BMSoundEvents.HOLLOWBORNE_TURRET_SHOOT);

        this.basicSound(BMSoundEvents.HOLLOWCORPER_IMPACT);

        this.music(BMSoundEvents.VOTING_AMBIENT);
        this.basicSound(BMSoundEvents.VOTING_DRUM);
        this.basicSound(BMSoundEvents.VOTING_TRANSITION);
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
