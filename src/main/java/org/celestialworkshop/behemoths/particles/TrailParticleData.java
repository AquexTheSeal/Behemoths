package org.celestialworkshop.behemoths.particles;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import org.celestialworkshop.behemoths.registries.BMParticleTypes;

public record TrailParticleData(int followEntityId, int lifetime, int maxPoints, float width, float r, float g, float b, float a) implements ParticleOptions {

    @SuppressWarnings("deprecation")
    public static final Deserializer<TrailParticleData> DESERIALIZER = new Deserializer<>() {
        @Override
        public TrailParticleData fromCommand(ParticleType<TrailParticleData> type, StringReader reader)
                throws CommandSyntaxException {
            reader.expect(' ');
            int followEntityId = reader.readInt();
            reader.expect(' ');
            int lifetime = reader.readInt();
            reader.expect(' ');
            int maxPoints = reader.readInt();
            reader.expect(' ');
            float width = reader.readFloat();
            reader.expect(' ');
            float r = reader.readFloat();
            reader.expect(' ');
            float g = reader.readFloat();
            reader.expect(' ');
            float b = reader.readFloat();
            reader.expect(' ');
            float a = reader.readFloat();
            return new TrailParticleData(followEntityId, lifetime, maxPoints, width, r, g, b, a);
        }

        @Override
        public TrailParticleData fromNetwork(ParticleType<TrailParticleData> type, FriendlyByteBuf buf) {
            int followEntityId = buf.readInt();
            int lifetime = buf.readInt();
            int maxPoints = buf.readInt();
            float width = buf.readFloat();
            float r = buf.readFloat();
            float g = buf.readFloat();
            float b = buf.readFloat();
            float a = buf.readFloat();
            return new TrailParticleData(followEntityId, lifetime, maxPoints, width, r, g, b, a);
        }
    };

    public static final Codec<TrailParticleData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("followEntityId").forGetter(TrailParticleData::followEntityId),
                    Codec.INT.fieldOf("lifetime").forGetter(TrailParticleData::lifetime),
                    Codec.INT.fieldOf("maxPoints").forGetter(TrailParticleData::maxPoints),
                    Codec.FLOAT.fieldOf("width").forGetter(TrailParticleData::width),
                    Codec.FLOAT.fieldOf("r").forGetter(TrailParticleData::r),
                    Codec.FLOAT.fieldOf("g").forGetter(TrailParticleData::g),
                    Codec.FLOAT.fieldOf("b").forGetter(TrailParticleData::b),
                    Codec.FLOAT.fieldOf("a").forGetter(TrailParticleData::a)
            ).apply(instance, TrailParticleData::new)
    );

    @Override
    public ParticleType<?> getType() {
        return BMParticleTypes.TRAIL.get();
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buf) {
        buf.writeInt(followEntityId);
        buf.writeInt(lifetime);
        buf.writeInt(maxPoints);
        buf.writeFloat(width);
        buf.writeFloat(r);
        buf.writeFloat(g);
        buf.writeFloat(b);
        buf.writeFloat(a);
    }

    @Override
    public String writeToString() {
        return String.format("%d %df %df %.4f %.4f %.4f %.4f %.4f", followEntityId, lifetime, maxPoints, width, r, g, b, a);
    }
}