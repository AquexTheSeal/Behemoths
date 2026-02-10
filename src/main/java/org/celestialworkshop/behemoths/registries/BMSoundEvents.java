package org.celestialworkshop.behemoths.registries;

import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.celestialworkshop.behemoths.Behemoths;

public class BMSoundEvents {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Behemoths.MODID);

    public static final RegistryObject<SoundEvent> ARCHZOMBIE_AMBIENT = registerSoundEvent("archzombie_ambient");
    public static final RegistryObject<SoundEvent> ARCHZOMBIE_DEATH = registerSoundEvent("archzombie_death");
    public static final RegistryObject<SoundEvent> ARCHZOMBIE_HURT = registerSoundEvent("archzombie_hurt");
    public static final RegistryObject<SoundEvent> ARCHZOMBIE_SWING = registerSoundEvent("archzombie_swing");

    public static final RegistryObject<SoundEvent> STAMPEDE_HURT = registerSoundEvent("stampede_hurt");
    public static final RegistryObject<SoundEvent> STAMPEDE_CHARGE_ROAR = registerSoundEvent("stampede_charge_roar");
    public static final RegistryObject<SoundEvent> STAMPEDE_DEATH = registerSoundEvent("stampede_death");
    public static final RegistryObject<SoundEvent> STAMPEDE_CHARGE_STEP = registerSoundEvent("stampede_charge_step");
    public static final RegistryObject<SoundEvent> STAMPEDE_AMBIENT = registerSoundEvent("stampede_ambient");

    public static final RegistryObject<SoundEvent> HOLLOWBORNE_AMBIENT = registerSoundEvent("hollowborne_ambient");
    public static final RegistryObject<SoundEvent> HOLLOWBORNE_DEATH = registerSoundEvent("hollowborne_death");
    public static final RegistryObject<SoundEvent> HOLLOWBORNE_HURT = registerSoundEvent("hollowborne_hurt");

    public static final RegistryObject<SoundEvent> HOLLOWBORNE_TURRET_SHOOT = registerSoundEvent("hollowborne_turret_shoot");

    public static final RegistryObject<SoundEvent> HOLLOWCORPER_IMPACT = registerSoundEvent("hollowcorper_impact");

    public static final RegistryObject<SoundEvent> VOTING_AMBIENT = registerSoundEvent("voting_ambient");
    public static final RegistryObject<SoundEvent> VOTING_DRUM = registerSoundEvent("voting_drum");
    public static final RegistryObject<SoundEvent> VOTING_TRANSITION = registerSoundEvent("voting_transition");

    private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(Behemoths.prefix(name)));
    }

    private static RegistryObject<SoundEvent> registerRangedSoundEvent(String name, float range) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createFixedRangeEvent(Behemoths.prefix(name),  range));
    }
}
