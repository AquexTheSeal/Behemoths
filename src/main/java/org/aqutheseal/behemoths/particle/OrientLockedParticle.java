package org.aqutheseal.behemoths.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class OrientLockedParticle extends BasicAnimatedParticle {

    protected OrientLockedParticle(ClientLevel pLevel, double pX, double pY, double pZ, SpriteSet pSprite) {
        super(pLevel, pX, pY, pZ, pSprite);
    }

    @Override
    public void render(VertexConsumer buffer, Camera camera, float ticks) {
        Vec3 cameraPos = camera.getPosition();
        float x = (float) (Mth.lerp(ticks, this.xo, this.x) - cameraPos.x());
        float y = (float) (Mth.lerp(ticks, this.yo, this.y) - cameraPos.y());
        float z = (float) (Mth.lerp(ticks, this.zo, this.z) - cameraPos.z());

        Quaternionf quaternion = new Quaternionf(0F, -1F, 1F, 0F);

        Vector3f[] vertices = {
                new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)
        };
        Vector3f[] verticesBottom = {
                new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, -1.0F, 0.0F)
        };

        float quadSize = this.getQuadSize(ticks);

        // No Z-Fighting with blocks.
        float fixedY = y + 0.01F;

        for (int i = 0; i < 4; i++) {
            Vector3f vertex = vertices[i];
            vertex.rotate(quaternion);
            vertex.mul(quadSize);
            vertex.add(x, fixedY, z);

            Vector3f vertexBottom = verticesBottom[i];
            vertexBottom.rotate(quaternion);
            vertexBottom.mul(quadSize);
            vertexBottom.add(x, fixedY - 0.01F, z);
        }

        float u0 = this.getU0();
        float u1 = this.getU1();
        float v0 = this.getV0();
        float v1 = this.getV1();
        int light = this.getLightColor(ticks);

        this.emitVertex(buffer, vertices[0], u1, v1, light);
        this.emitVertex(buffer, vertices[1], u1, v0, light);
        this.emitVertex(buffer, vertices[2], u0, v0, light);
        this.emitVertex(buffer, vertices[3], u0, v1, light);

        this.emitVertex(buffer, vertices[3], u0, v1, light);
        this.emitVertex(buffer, vertices[2], u0, v0, light);
        this.emitVertex(buffer, vertices[1], u1, v0, light);
        this.emitVertex(buffer, vertices[0], u1, v1, light);
    }

    private void emitVertex(VertexConsumer buffer, Vector3f vertex, float u, float v, int light) {
        buffer.vertex(vertex.x(), vertex.y(), vertex.z()).uv(u, v).color(rCol, gCol, bCol, alpha).uv2(light).endVertex();
    }

    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public record ShockwaveRingProvider(SpriteSet sprite) implements ParticleProvider<SimpleParticleType> {
        @Override
        public @NotNull Particle createParticle(@NotNull SimpleParticleType pType, @NotNull ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            OrientLockedParticle particle = new OrientLockedParticle(pLevel, pX, pY, pZ, this.sprite);
            particle.scale(20);
            particle.lifetime = 10 + pLevel.random.nextInt(5);
            return particle;
        }
    }
}
