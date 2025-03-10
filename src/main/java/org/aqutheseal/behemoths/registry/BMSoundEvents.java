package org.aqutheseal.behemoths.registry;

import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.aqutheseal.behemoths.Behemoths;

public class BMSoundEvents {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Behemoths.MODID);

    public static final RegistryObject<SoundEvent> SKY_CHARYDBIS_AMBIENT = registerRangedSoundEvent("sky_charydbis_ambient", 64);
    public static final RegistryObject<SoundEvent> SKY_CHARYDBIS_HURT = registerRangedSoundEvent("sky_charydbis_hurt", 128);
    public static final RegistryObject<SoundEvent> SKY_CHARYDBIS_DEATH = registerRangedSoundEvent("sky_charydbis_death", 128);

    public static final RegistryObject<SoundEvent> BALLISTA_START = registerSoundEvent("ballista_start");
    public static final RegistryObject<SoundEvent> BALLISTA_LOADED = registerSoundEvent("ballista_loaded");
    public static final RegistryObject<SoundEvent> BALLISTA_SHOOT = registerSoundEvent("ballista_shoot");

    private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(Behemoths.location(name)));
    }

    private static RegistryObject<SoundEvent> registerRangedSoundEvent(String name, float range) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createFixedRangeEvent(Behemoths.location(name),  range));
    }
}