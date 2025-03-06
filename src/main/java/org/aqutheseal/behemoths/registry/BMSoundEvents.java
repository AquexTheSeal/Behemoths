package org.aqutheseal.behemoths.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.aqutheseal.behemoths.Behemoths;

public class BMSoundEvents {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Behemoths.MODID);

    public static final RegistryObject<SoundEvent> SKY_CHARYDBIS_AMBIENT = registerSoundEvent("sky_charydbis_ambient");
    public static final RegistryObject<SoundEvent> SKY_CHARYDBIS_HURT = registerSoundEvent("sky_charydbis_hurt");
    public static final RegistryObject<SoundEvent> SKY_CHARYDBIS_DEATH = registerSoundEvent("sky_charydbis_death");

    public static final RegistryObject<SoundEvent> BALLISTA_START = registerSoundEvent("ballista_start");
    public static final RegistryObject<SoundEvent> BALLISTA_LOADED = registerSoundEvent("ballista_loaded");
    public static final RegistryObject<SoundEvent> BALLISTA_SHOOT = registerSoundEvent("ballista_shoot");

    private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Behemoths.MODID, name)));
    }
}