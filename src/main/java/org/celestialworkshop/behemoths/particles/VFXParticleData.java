package org.celestialworkshop.behemoths.particles;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.registries.ForgeRegistries;
import org.celestialworkshop.behemoths.api.client.animation.InterpolationTypes;
import org.celestialworkshop.behemoths.registries.BMParticleTypes;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public record VFXParticleData(
        String textureName,
        int type,
        VFXInterpolationData scaleData,
        VFXInterpolationData alphaData,
        int lifetime,
        float xRot,
        float yRot,
        float zRot,
        int boundEntityId,
        float xBoundOffset,
        float yBoundOffset,
        float zBoundOffset
) implements ParticleOptions {

    @SuppressWarnings("deprecation")
    public static final Deserializer<VFXParticleData> DESERIALIZER = new Deserializer<>() {
        public @NotNull VFXParticleData fromCommand(@NotNull ParticleType<VFXParticleData> pParticleType, StringReader pReader) throws CommandSyntaxException {
            pReader.expect(' ');
            String rs = pReader.readString();
            pReader.expect(' ');
            int type = pReader.readInt();
            VFXInterpolationData scaleData = VFXInterpolationData.fromCommand(pReader);
            VFXInterpolationData alphaData = VFXInterpolationData.fromCommand(pReader);
            pReader.expect(' ');
            int age = pReader.readInt();
            pReader.expect(' ');
            float xR = pReader.readFloat();
            pReader.expect(' ');
            float yR = pReader.readFloat();
            pReader.expect(' ');
            float zR = pReader.readFloat();
            pReader.expect(' ');
            int en = pReader.readInt();
            pReader.expect(' ');
            float xO = pReader.readFloat();
            pReader.expect(' ');
            float yO = pReader.readFloat();
            pReader.expect(' ');
            float zO = pReader.readFloat();
            return new VFXParticleData(rs, type, scaleData, alphaData, age, xR, yR, zR, en, xO, yO, zO);
        }

        public @NotNull VFXParticleData fromNetwork(@NotNull ParticleType<VFXParticleData> pParticleType, FriendlyByteBuf pBuffer) {
            return new VFXParticleData(
                    pBuffer.readUtf(),
                    pBuffer.readInt(),
                    VFXInterpolationData.fromNetwork(pBuffer),
                    VFXInterpolationData.fromNetwork(pBuffer),
                    pBuffer.readInt(),
                    pBuffer.readFloat(),
                    pBuffer.readFloat(),
                    pBuffer.readFloat(),
                    pBuffer.readInt(),
                    pBuffer.readFloat(),
                    pBuffer.readFloat(),
                    pBuffer.readFloat()
            );
        }
    };

    public static final Codec<VFXParticleData> CODEC = RecordCodecBuilder.create(
            (instance) -> instance.group(
                    Codec.STRING.fieldOf("textureName").forGetter(VFXParticleData::textureName),
                    Codec.INT.fieldOf("type").forGetter(VFXParticleData::type),
                    VFXInterpolationData.CODEC.fieldOf("scaleData").forGetter(VFXParticleData::scaleData),
                    VFXInterpolationData.CODEC.fieldOf("alphaData").forGetter(VFXParticleData::alphaData),
                    Codec.INT.fieldOf("lifetime").forGetter(VFXParticleData::lifetime),
                    Codec.FLOAT.fieldOf("xRot").forGetter(VFXParticleData::xRot),
                    Codec.FLOAT.fieldOf("yRot").forGetter(VFXParticleData::yRot),
                    Codec.FLOAT.fieldOf("zRot").forGetter(VFXParticleData::zRot),
                    Codec.INT.fieldOf("boundEntityId").forGetter(VFXParticleData::boundEntityId),
                    Codec.FLOAT.fieldOf("xOff").forGetter(VFXParticleData::xBoundOffset),
                    Codec.FLOAT.fieldOf("yOff").forGetter(VFXParticleData::yBoundOffset),
                    Codec.FLOAT.fieldOf("zOff").forGetter(VFXParticleData::zBoundOffset)
            ).apply(instance, VFXParticleData::new)
    );

    @Override
    public @NotNull String writeToString() {
        return String.format(Locale.ROOT,
                "%s %s %d %s %s %d %f %f %f %d %f %f %f",
                ForgeRegistries.PARTICLE_TYPES.getKey(this.getType()),
                textureName, type, scaleData.writeToString(), alphaData.writeToString(),
                lifetime, xRot, yRot, zRot, boundEntityId, xBoundOffset, yBoundOffset, zBoundOffset
        );
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf pBuffer) {
        pBuffer.writeUtf(textureName);
        pBuffer.writeInt(type);
        scaleData.writeToNetwork(pBuffer);
        alphaData.writeToNetwork(pBuffer);
        pBuffer.writeInt(lifetime);
        pBuffer.writeFloat(xRot);
        pBuffer.writeFloat(yRot);
        pBuffer.writeFloat(zRot);
        pBuffer.writeInt(boundEntityId);
        pBuffer.writeFloat(xBoundOffset);
        pBuffer.writeFloat(yBoundOffset);
        pBuffer.writeFloat(zBoundOffset);
    }

    @Override
    public @NotNull ParticleType<?> getType() {
        return BMParticleTypes.VFX.get();
    }

    public record VFXInterpolationData(float startValue, float endValue, int interpolationType) {
        public static final Codec<VFXInterpolationData> CODEC = RecordCodecBuilder.create(
                (instance) -> instance.group(
                        Codec.FLOAT.fieldOf("startValue").forGetter(VFXInterpolationData::startValue),
                        Codec.FLOAT.fieldOf("endValue").forGetter(VFXInterpolationData::endValue),
                        Codec.INT.fieldOf("interpolationType").forGetter(VFXInterpolationData::interpolationType)
                ).apply(instance, VFXInterpolationData::new)
        );

        public void writeToNetwork(FriendlyByteBuf pBuffer) {
            pBuffer.writeFloat(startValue);
            pBuffer.writeFloat(endValue);
            pBuffer.writeInt(interpolationType);
        }

        public static VFXInterpolationData fromNetwork(FriendlyByteBuf pBuffer) {
            float start = pBuffer.readFloat();
            float end = pBuffer.readFloat();
            int interp = pBuffer.readInt();
            return new VFXInterpolationData(start, end, interp);
        }

        public static VFXInterpolationData fromCommand(StringReader pReader) throws CommandSyntaxException {
            pReader.expect(' ');
            float start = pReader.readFloat();
            pReader.expect(' ');
            float end = pReader.readFloat();
            pReader.expect(' ');
            int interp = pReader.readInt();
            return new VFXInterpolationData(start, end, interp);
        }

        public String writeToString() {
            return String.format(Locale.ROOT, "%f %f %d", startValue, endValue, interpolationType);
        }
    }

    public static class Builder {
        private String textureName;
        private VFXTypes type = VFXTypes.FLAT_LOOK;
        private int lifetime = 100;

        private float startScale = 1.0F;
        private float endScale = 1.0F;
        private InterpolationTypes scaleInterpolation = InterpolationTypes.LINEAR;

        private float startAlpha = 1.0F;
        private float endAlpha = 1.0F;
        private InterpolationTypes alphaInterpolation = InterpolationTypes.LINEAR;

        private float xRot;
        private float yRot;
        private float zRot;
        private Entity boundEntity;
        private float xOff;
        private float yOff = 0.05F;
        private float zOff;

        public Builder textureName(ResourceLocation textureName) {
            this.textureName = textureName.toString();
            return this;
        }

        public Builder type(VFXTypes type) {
            this.type = type;
            return this;
        }

        public Builder lifetime(int lifetime) {
            this.lifetime = lifetime;
            return this;
        }

        public Builder scale(float scale) {
            this.startScale = scale;
            this.endScale = scale;
            this.scaleInterpolation = InterpolationTypes.LINEAR;
            return this;
        }

        public Builder scale(float startScale, float endScale) {
            this.startScale = startScale;
            this.endScale = endScale;
            return this;
        }

        public Builder scale(float startScale, float endScale, InterpolationTypes interpolation) {
            this.startScale = startScale;
            this.endScale = endScale;
            this.scaleInterpolation = interpolation;
            return this;
        }

        public Builder scaleInterpolation(InterpolationTypes interpolation) {
            this.scaleInterpolation = interpolation;
            return this;
        }

        public Builder alpha(float alpha) {
            this.startAlpha = alpha;
            this.endAlpha = alpha;
            this.alphaInterpolation = InterpolationTypes.LINEAR;
            return this;
        }

        public Builder alpha(float startAlpha, float endAlpha) {
            this.startAlpha = startAlpha;
            this.endAlpha = endAlpha;
            return this;
        }

        public Builder alpha(float startAlpha, float endAlpha, InterpolationTypes interpolation) {
            this.startAlpha = startAlpha;
            this.endAlpha = endAlpha;
            this.alphaInterpolation = interpolation;
            return this;
        }

        public Builder alphaInterpolation(InterpolationTypes interpolation) {
            this.alphaInterpolation = interpolation;
            return this;
        }

        public Builder fadeOut() {
            this.startAlpha = 1.0F;
            this.endAlpha = 0.0F;
            return this;
        }

        public Builder fadeIn() {
            this.startAlpha = 0.0F;
            this.endAlpha = 1.0F;
            return this;
        }

        public Builder xRot(float xRot) {
            this.xRot = xRot;
            return this;
        }

        public Builder yRot(float yRot) {
            this.yRot = yRot;
            return this;
        }

        public Builder zRot(float zRot) {
            this.zRot = zRot;
            return this;
        }

        public Builder boundEntity(Entity boundEntity) {
            this.boundEntity = boundEntity;
            return this;
        }

        public Builder xBoundOffset(float xBoundOffset) {
            this.xOff = xBoundOffset;
            return this;
        }

        public Builder yBoundOffset(float yBoundOffset) {
            this.yOff = yBoundOffset;
            return this;
        }

        public Builder zBoundOffset(float zBoundOffset) {
            this.zOff = zBoundOffset;
            return this;
        }

        public VFXParticleData build() {
            VFXInterpolationData scaleData = new VFXInterpolationData(startScale, endScale, scaleInterpolation.ordinal());
            VFXInterpolationData alphaData = new VFXInterpolationData(startAlpha, endAlpha, alphaInterpolation.ordinal());

            return new VFXParticleData(
                    textureName,
                    type.ordinal(),
                    scaleData,
                    alphaData,
                    lifetime,
                    xRot,
                    yRot,
                    zRot,
                    boundEntity != null ? boundEntity.getId() : -1,
                    xOff,
                    yOff,
                    zOff
            );
        }
    }
}