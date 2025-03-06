package org.aqutheseal.behemoths.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.NotNull;

public class BasicAnimatedParticle extends TextureSheetParticle {

    private final SpriteSet sprites;

    protected BasicAnimatedParticle(ClientLevel pLevel, double pX, double pY, double pZ, SpriteSet pSprite) {
        super(pLevel, pX, pY, pZ);
        this.sprites = pSprite;
        this.hasPhysics = false;
        this.setSpriteFromAge(pSprite);
    }

    public void tick() {
        super.tick();
        this.setSpriteFromAge(this.sprites);
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public record ShockwaveStreakProvider(SpriteSet sprite) implements ParticleProvider<SimpleParticleType> {
        @Override
        public @NotNull Particle createParticle(@NotNull SimpleParticleType pType, @NotNull ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            BasicAnimatedParticle particle = new BasicAnimatedParticle(pLevel, pX, pY, pZ, this.sprite);
            particle.scale(20);
            particle.lifetime = 10 + pLevel.random.nextInt(5);
            return particle;
        }
    }
}
