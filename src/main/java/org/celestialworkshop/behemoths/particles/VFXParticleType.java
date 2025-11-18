package org.celestialworkshop.behemoths.particles;

import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleType;

public class VFXParticleType extends ParticleType<VFXParticleData> {

    public VFXParticleType(boolean pOverrideLimiter) {
        super(pOverrideLimiter, VFXParticleData.DESERIALIZER);
    }

    @Override
    public Codec<VFXParticleData> codec() {
        return VFXParticleData.CODEC;
    }
}