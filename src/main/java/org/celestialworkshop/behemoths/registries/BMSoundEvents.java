package org.celestialworkshop.behemoths.registries;

import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.celestialworkshop.behemoths.Behemoths;

public class BMSoundEvents {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Behemoths.MODID);

    public static final RegistryObject<SoundEvent> STAMPEDE_HURT = registerSoundEvent("stampede_hurt");
    public static final RegistryObject<SoundEvent> STAMPEDE_CHARGE_ROAR = registerSoundEvent("stampede_charge_roar");
    public static final RegistryObject<SoundEvent> STAMPEDE_DEATH = registerSoundEvent("stampede_death");
    public static final RegistryObject<SoundEvent> STAMPEDE_CHARGE_STEP = registerSoundEvent("stampede_charge_step");
    public static final RegistryObject<SoundEvent> STAMPEDE_AMBIENT = registerSoundEvent("stampede_ambient");

    private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(Behemoths.prefix(name)));
    }

    private static RegistryObject<SoundEvent> registerRangedSoundEvent(String name, float range) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createFixedRangeEvent(Behemoths.prefix(name),  range));
    }
}
