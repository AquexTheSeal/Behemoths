package org.celestialworkshop.behemoths.registries;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.celestialworkshop.behemoths.Behemoths;

import java.util.List;

public class BMSoundEvents {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Behemoths.MODID);
    public static final List<AutoGenSound> AUTO_GEN_SOUNDS = new ObjectArrayList<>();

    public static final RegistryObject<SoundEvent> ARCHZOMBIE_AMBIENT = registerSoundEvent("archzombie_ambient", 2);
    public static final RegistryObject<SoundEvent> ARCHZOMBIE_DEATH = registerSoundEvent("archzombie_death");
    public static final RegistryObject<SoundEvent> ARCHZOMBIE_HURT = registerSoundEvent("archzombie_hurt", 2);
    public static final RegistryObject<SoundEvent> ARCHZOMBIE_SWING = registerSoundEvent("archzombie_swing", 2);

    public static final RegistryObject<SoundEvent> STAMPEDE_HURT = registerSoundEvent("stampede_hurt", 3);
    public static final RegistryObject<SoundEvent> STAMPEDE_CHARGE_ROAR = registerSoundEvent("stampede_charge_roar");
    public static final RegistryObject<SoundEvent> STAMPEDE_DEATH = registerSoundEvent("stampede_death");
    public static final RegistryObject<SoundEvent> STAMPEDE_CHARGE_STEP = registerSoundEvent("stampede_charge_step");
    public static final RegistryObject<SoundEvent> STAMPEDE_AMBIENT = registerSoundEvent("stampede_ambient", 3);

    public static final RegistryObject<SoundEvent> HOLLOWBORNE_AMBIENT = registerSoundEvent("hollowborne_ambient");
    public static final RegistryObject<SoundEvent> HOLLOWBORNE_DEATH = registerSoundEvent("hollowborne_death");
    public static final RegistryObject<SoundEvent> HOLLOWBORNE_HURT = registerSoundEvent("hollowborne_hurt");
    public static final RegistryObject<SoundEvent> HOLLOWBORNE_SMASH = registerSoundEvent("hollowborne_smash");
    public static final RegistryObject<SoundEvent> HOLLOWBORNE_SMASH_START = registerSoundEvent("hollowborne_smash_start");
    public static final RegistryObject<SoundEvent> HOLLOWBORNE_STEP = registerSoundEvent("hollowborne_step");
    public static final RegistryObject<SoundEvent> HOLLOWBORNE_JUMP = registerSoundEvent("hollowborne_jump");
    public static final RegistryObject<SoundEvent> HOLLOWBORNE_JUMP_STRONG = registerSoundEvent("hollowborne_jump_strong");

    public static final RegistryObject<SoundEvent> HOLLOWBORNE_TURRET_SHOOT = registerSoundEvent("hollowborne_turret_shoot");

    public static final RegistryObject<SoundEvent> HOLLOWCORPER_IMPACT = registerSoundEvent("hollowcorper_impact");

    public static final RegistryObject<SoundEvent> PHANTASHROOM_BOUNCE = registerSoundEvent("phantashroom_bounce");

    public static final RegistryObject<SoundEvent> VOTING_AMBIENT = registerMusic("voting_ambient");
    public static final RegistryObject<SoundEvent> VOTING_TRANSITION = registerSoundEvent("voting_transition");

    private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        return registerSoundEvent(name, 1);
    }

    private static RegistryObject<SoundEvent> registerSoundEvent(String name, int variants) {
        RegistryObject<SoundEvent> object = SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(Behemoths.prefix(name)));
        AUTO_GEN_SOUNDS.add(new AutoGenSound(object, variants, false));
        return object;
    }

    private static RegistryObject<SoundEvent> registerMusic(String name) {
        RegistryObject<SoundEvent> object = SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(Behemoths.prefix(name)));
        AUTO_GEN_SOUNDS.add(new AutoGenSound(object, 1, true));
        return object;
    }

    public record AutoGenSound(RegistryObject<SoundEvent> sound, int variants, boolean stream) {
    }
}
