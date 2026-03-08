package org.celestialworkshop.behemoths.particles;

import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleType;

public class TrailParticleType extends ParticleType<TrailParticleData> {

    public TrailParticleType() {
        super(true, TrailParticleData.DESERIALIZER);
    }

    @Override
    public Codec<TrailParticleData> codec() {
        return TrailParticleData.CODEC;
    }
}