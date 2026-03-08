package org.celestialworkshop.behemoths.registries;

import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.celestialworkshop.behemoths.Behemoths;
import org.celestialworkshop.behemoths.particles.*;

public class BMParticleTypes {

    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Behemoths.MODID);

    public static final RegistryObject<ParticleType<VFXParticleData>> VFX = PARTICLE_TYPES.register("vfx", VFXParticleType::new);
    public static final RegistryObject<ParticleType<TrailParticleData>> TRAIL = PARTICLE_TYPES.register("trail", TrailParticleType::new);
}
