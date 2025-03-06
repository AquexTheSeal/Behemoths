package org.aqutheseal.behemoths.registry;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.aqutheseal.behemoths.Behemoths;

public class BMParticleTypes {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Behemoths.MODID);

    public static final RegistryObject<SimpleParticleType> SHOCKWAVE_RING = PARTICLE_TYPES.register("shockwave_ring", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> SHOCKWAVE_STREAK = PARTICLE_TYPES.register("shockwave_streak", () -> new SimpleParticleType(false));

}