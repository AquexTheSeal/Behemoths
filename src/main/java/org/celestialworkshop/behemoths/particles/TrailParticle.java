package org.celestialworkshop.behemoths.particles;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class TrailParticle extends Particle {
    public static final ParticleRenderType TRAIL = new ParticleRenderType() {
        @Override
        public void begin(BufferBuilder builder, TextureManager textureManager) {
            RenderSystem.depthMask(true);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.enableDepthTest();
            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        }

        @Override
        public void end(Tesselator tesselator) {
            tesselator.end();
        }
    };

    private final List<Vec3> points = new ArrayList<>();
    private final int followEntityId, maxPoints;
    private final float width, r, g, b, a;

    public TrailParticle(ClientLevel level, double x, double y, double z, TrailParticleData options) {
        super(level, x, y, z);
        this.lifetime = options.lifetime();
        this.hasPhysics = false;
        this.followEntityId = options.followEntityId();
        this.maxPoints = options.maxPoints();
        this.width = options.width();
        this.r = options.r();
        this.g = options.g();
        this.b = options.b();
        this.a = options.a();
    }

    @Override
    public void render(VertexConsumer buffer, Camera camera, float partialTick) {
        if (points.size() < 2) return;

        double curX = Mth.lerp(partialTick, this.xo, this.x);
        double curY = Mth.lerp(partialTick, this.yo, this.y);
        double curZ = Mth.lerp(partialTick, this.zo, this.z);
        Vec3 headPos = new Vec3(curX, curY, curZ);

        for (int i = 1; i < points.size(); i++) {
            Vec3 p1 = points.get(i - 1);
            Vec3 p2 = points.get(i);
            float trailProgress = (float) i / points.size();
            float ww = width * trailProgress;
            drawSegment(buffer, camera, p1, p2, ww, trailProgress);
        }

        Vec3 lastPoint = points.get(points.size() - 1);
        drawSegment(buffer, camera, lastPoint, headPos, 0.2f, 1.0f);
    }

    public void drawSegment(VertexConsumer vertexBuffer, Camera activeCamera, Vec3 startPoint, Vec3 endPoint, float segmentWidth, float alpha) {
        Vec3 cameraPos = activeCamera.getPosition();
        Vec3 relStart = startPoint.subtract(cameraPos);
        Vec3 relEnd = endPoint.subtract(cameraPos);

        Vec3 pathDirection = endPoint.subtract(startPoint).normalize();
        Vec3 viewVector = cameraPos.subtract(startPoint).normalize();

        Vec3 offsetVector = pathDirection.cross(viewVector).normalize().scale(segmentWidth / 2f);

        float offsetX = (float) offsetVector.x;
        float offsetY = (float) offsetVector.y;
        float offsetZ = (float) offsetVector.z;

        float finalAlpha = this.a * alpha;

        vertexBuffer.vertex(relStart.x - offsetX, relStart.y - offsetY, relStart.z - offsetZ).color(r, g, b, finalAlpha).endVertex();
        vertexBuffer.vertex(relStart.x + offsetX, relStart.y + offsetY, relStart.z + offsetZ).color(r, g, b, finalAlpha).endVertex();
        vertexBuffer.vertex(relEnd.x + offsetX, relEnd.y + offsetY, relEnd.z + offsetZ).color(r, g, b, finalAlpha).endVertex();
        vertexBuffer.vertex(relEnd.x - offsetX, relEnd.y - offsetY, relEnd.z - offsetZ).color(r, g, b, finalAlpha).endVertex();
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        if (followEntityId != -1) {
            var entity = this.level.getEntity(followEntityId);
            if (entity != null) {
                this.x = entity.getX();
                this.y = entity.getY() + entity.getBbHeight() / 2.0;
                this.z = entity.getZ();
            }
        }

        this.points.add(new Vec3(this.x, this.y, this.z));
        if (this.points.size() > maxPoints) {
            this.points.remove(0);
        }

        if (followEntityId == -1) {
            this.move(this.xd, this.yd, this.zd);
        }

        if (this.age++ >= this.lifetime) this.remove();
    }

    @Override
    public ParticleRenderType getRenderType() {
        return TRAIL;
    }

    public static class Provider implements ParticleProvider<TrailParticleData> {
        public Provider(SpriteSet pSprites) {
        }

        @Override
        public Particle createParticle(TrailParticleData options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            TrailParticle particle = new TrailParticle(level, x, y, z, options);
            particle.setParticleSpeed(xSpeed, ySpeed, zSpeed);
            return particle;
        }
    }
}