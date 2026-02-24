package org.celestialworkshop.behemoths.particles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.celestialworkshop.behemoths.api.client.animation.InterpolationTypes;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class VFXParticle extends TextureSheetParticle {
    public static final int FRAME_DETECTION_LIMIT = 30;

    private static final ConcurrentHashMap<ResourceLocation, Integer> FRAME_COUNT_CACHE = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<ResourceLocation, Integer> START_INDEX_CACHE = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, ResourceLocation[]> TEXTURE_PATH_CACHE = new ConcurrentHashMap<>();

    public final VFXParticleData data;
    public final ResourceLocation texture;
    public final int detectedFrames;
    public final int beginAnimationIndex;

    private final ResourceLocation[] cachedTexturePaths;

    public int currentFrame;

    protected VFXParticle(ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed, VFXParticleData data) {
        super(pLevel, pX, pY, pZ);
        this.data = data;

        this.lifetime = data.lifetime();
        this.texture = ResourceLocation.tryParse(data.textureName());
        this.beginAnimationIndex = this.scanAnimationIndex();
        this.detectedFrames = this.scanFrames();
        this.cachedTexturePaths = this.precomputeTexturePaths();

        this.quadSize = data.scaleData().startValue();
        this.alpha = data.alphaData().startValue();

        this.setSize(this.quadSize * 2, this.quadSize * 2);
        this.moveToBoundEntity();

        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        this.xd = pXSpeed;
        this.yd = pYSpeed;
        this.zd = pZSpeed;
    }

    private int scanAnimationIndex() {
        return START_INDEX_CACHE.computeIfAbsent(texture, tex -> {
            ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
            ResourceLocation baseTexture = ResourceLocation.fromNamespaceAndPath(tex.getNamespace(), "textures/vfx/" + tex.getPath() + ".png");

            if (resourceManager.getResource(baseTexture).isPresent()) {
                return 0;
            }

            ResourceLocation frame0 = ResourceLocation.fromNamespaceAndPath(tex.getNamespace(), String.format("textures/vfx/%s/%s_0.png", tex.getPath(), tex.getPath()));
            boolean hasFrame0 = resourceManager.getResource(frame0).isPresent();

            ResourceLocation frame1 = ResourceLocation.fromNamespaceAndPath(tex.getNamespace(), String.format("textures/vfx/%s/%s_1.png", tex.getPath(), tex.getPath()));
            boolean hasFrame1 = resourceManager.getResource(frame1).isPresent();

            if (!hasFrame0 && hasFrame1) {
                return 1;
            } else if (hasFrame0) {
                return 0;
            } else {
                throw new IllegalStateException("Texture index 0 or 1 does not exist for Behemoths VFX Particle.");
            }
        });
    }

    private int scanFrames() {
        return FRAME_COUNT_CACHE.computeIfAbsent(texture, tex -> {
            ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
            ResourceLocation baseTexture = ResourceLocation.fromNamespaceAndPath(tex.getNamespace(), "textures/vfx/" + tex.getPath() + ".png");

            if (resourceManager.getResource(baseTexture).isPresent()) {
                return 0;
            }

            int low = beginAnimationIndex;
            int high = FRAME_DETECTION_LIMIT;
            int lastFound = beginAnimationIndex;

            String startPath = String.format("textures/vfx/%s/%s_%d.png", tex.getPath(), tex.getPath(), beginAnimationIndex);
            ResourceLocation startFrame = ResourceLocation.fromNamespaceAndPath(tex.getNamespace(), startPath);
            if (!resourceManager.getResource(startFrame).isPresent()) {
                return 0;
            }

            while (low <= high) {
                int mid = (low + high) / 2;
                String path = String.format("textures/vfx/%s/%s_%d.png", tex.getPath(), tex.getPath(), mid);
                ResourceLocation frameTexture = ResourceLocation.fromNamespaceAndPath(tex.getNamespace(), path);

                if (resourceManager.getResource(frameTexture).isPresent()) {
                    lastFound = mid;
                    low = mid + 1;
                } else {
                    high = mid - 1;
                }
            }

            return (lastFound - beginAnimationIndex) + 1;
        });
    }

    private ResourceLocation[] precomputeTexturePaths() {
        if (detectedFrames == 0) {
            return new ResourceLocation[]{
                    ResourceLocation.fromNamespaceAndPath(texture.getNamespace(), "textures/vfx/" + texture.getPath() + ".png")
            };
        }

        String cacheKey = texture.toString() + "_" + beginAnimationIndex;
        return TEXTURE_PATH_CACHE.computeIfAbsent(cacheKey, key -> {
            ResourceLocation[] paths = new ResourceLocation[detectedFrames];
            for (int i = 0; i < detectedFrames; i++) {
                int frameIndex = beginAnimationIndex + i;
                String path = String.format("textures/vfx/%s/%s_%d.png", texture.getPath(), texture.getPath(), frameIndex);
                paths[i] = ResourceLocation.fromNamespaceAndPath(texture.getNamespace(), path);
            }
            return paths;
        });
    }

    public ResourceLocation getCurrentTexture() {
        return cachedTexturePaths[detectedFrames == 0 ? 0 : currentFrame];
    }

    @Override
    public void tick() {

        super.tick();

        this.moveToBoundEntity();
        this.updateTextureAnimation();
    }

    @Override
    public void render(VertexConsumer pBuffer, Camera pRenderInfo, float pPartialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        MultiBufferSource.BufferSource bufferSource = minecraft.renderBuffers().bufferSource();
        Vec3 vec3 = pRenderInfo.getPosition();

        float progress = Math.min((this.age + pPartialTicks) / (float) this.lifetime, 1.0f);

        VFXParticleData.VFXInterpolationData scaleData = data.scaleData();
        float scaleDelta = InterpolationTypes.apply(scaleData.interpolationType(), progress);
        this.quadSize = Mth.lerp(scaleDelta, scaleData.startValue(), scaleData.endValue());

        VFXParticleData.VFXInterpolationData alphaData = data.alphaData();
        float alphaDelta = InterpolationTypes.apply(alphaData.interpolationType(), progress);
        this.alpha = Mth.lerp(alphaDelta, alphaData.startValue(), alphaData.endValue());

        float x = (float)(Mth.lerp(pPartialTicks, this.xo, this.x) - vec3.x());
        float y = (float)(Mth.lerp(pPartialTicks, this.yo, this.y) - vec3.y());
        float z = (float)(Mth.lerp(pPartialTicks, this.zo, this.z) - vec3.z());

        PoseStack pose = new PoseStack();
        pose.pushPose();
        pose.translate(x, y, z);
        this.renderBasedOnTypings(pose, pRenderInfo, bufferSource, pPartialTicks, this.quadSize);
        pose.popPose();
        bufferSource.endBatch();
    }

    public void renderBasedOnTypings(PoseStack pose, Camera renderInfo, MultiBufferSource.BufferSource bufferSource, float pPartialTicks, float size) {
        switch (this.getVFXType()) {
            case FLAT_LOOK -> {
                pose.mulPose(renderInfo.rotation());
                pose.mulPose(Axis.ZP.rotationDegrees(data.zRot()));
                this.renderQuadWithBackface(pose, bufferSource, size, RenderType.entityTranslucent(this.getCurrentTexture()));
            }
            case FLAT -> {
                this.applyDataRotations(pose);
                pose.mulPose(Axis.XP.rotationDegrees(90));
                this.renderQuadWithBackface(pose, bufferSource, size, RenderType.entityTranslucent(this.getCurrentTexture()));
            }
            case WALLS, WALLS_INTERSECTED -> {
                this.applyDataRotations(pose);
                float wallDistance = size * (this.getVFXType() == VFXTypes.WALLS_INTERSECTED ? 0.5f : 1f);
                List<Pair<Integer, Vec3>> i = List.of(
                        Pair.of(0, new Vec3(0, 0, wallDistance)),
                        Pair.of(90, new Vec3(wallDistance, 0, 0)),
                        Pair.of(180, new Vec3(0, 0, -wallDistance)),
                        Pair.of(270, new Vec3(-wallDistance, 0, 0))
                );
                for (Pair<Integer, Vec3> pair : i) {
                    pose.pushPose();
                    pose.translate(pair.getSecond().x, pair.getSecond().y, pair.getSecond().z);
                    pose.mulPose(Axis.YP.rotationDegrees(pair.getFirst()));
                    this.renderQuadWithBackface(pose, bufferSource, size, RenderType.entityTranslucent(this.getCurrentTexture()));
                    pose.popPose();
                }
            }
            case CROSS -> {
                this.applyDataRotations(pose);
                this.renderQuadWithBackface(pose, bufferSource, size, RenderType.entityTranslucent(this.getCurrentTexture()));
                pose.mulPose(Axis.YP.rotationDegrees(90));
                this.renderQuadWithBackface(pose, bufferSource, size, RenderType.entityTranslucent(this.getCurrentTexture()));
            }
        }
    }

    private VFXTypes getVFXType() {
        int ordinal = data.type();
        VFXTypes[] values = VFXTypes.values();
        return values[Math.min(Math.max(ordinal, 0), values.length - 1)];
    }

    private void renderQuadWithBackface(PoseStack pose, MultiBufferSource.BufferSource bufferSource, float size, RenderType renderType) {
        pose.pushPose();
        pose.translate(0, size * 1.5F, 0);
        pose.scale(-size, -size, size);
        {
            pose.pushPose();
            pose.scale(-1, -1, 1);
            pose.translate(0.0F, -1.5F, 0.0F);
            Matrix4f poseMatrix = pose.last().pose();
            VertexConsumer vertexConsumer = bufferSource.getBuffer(renderType);

            int overlay = OverlayTexture.NO_OVERLAY;
            int light = LightTexture.FULL_BRIGHT;

            float currentAlpha = this.alpha;

            vertexConsumer.vertex(poseMatrix, -1.0F, -1.0F, 0.0F).color(1.0F, 1.0F, 1.0F, currentAlpha).uv(0.0F, 1.0F).overlayCoords(overlay).uv2(light).normal(0F, 1.0F, 0F).endVertex();
            vertexConsumer.vertex(poseMatrix, 1.0F, -1.0F, 0.0F).color(1.0F, 1.0F, 1.0F, currentAlpha).uv(1.0F, 1.0F).overlayCoords(overlay).uv2(light).normal(0F, 1.0F, 0F).endVertex();
            vertexConsumer.vertex(poseMatrix, 1.0F, 1.0F, 0.0F).color(1.0F, 1.0F, 1.0F, currentAlpha).uv(1.0F, 0.0F).overlayCoords(overlay).uv2(light).normal(0F, 1.0F, 0F).endVertex();
            vertexConsumer.vertex(poseMatrix, -1.0F, 1.0F, 0.0F).color(1.0F, 1.0F, 1.0F, currentAlpha).uv(0.0F, 0.0F).overlayCoords(overlay).uv2(light).normal(0F, 1.0F, 0F).endVertex();

            pose.popPose();
        }
        pose.popPose();
    }

    public void updateTextureAnimation() {
        if (detectedFrames > 0) {
            float progress = (float) this.age / (float) this.lifetime;
            this.currentFrame = Math.min((int) (progress * detectedFrames), detectedFrames - 1);
        }
    }

    private void moveToBoundEntity() {
        Entity boundTo = level.getEntity(data.boundEntityId());
        if (boundTo != null) {
            this.setPos(
                    boundTo.getX() + data.xBoundOffset(),
                    boundTo.getY() + data.yBoundOffset(),
                    boundTo.getZ() + data.zBoundOffset()
            );
        }
    }

    private void applyDataRotations(PoseStack pose) {
        pose.mulPose(Axis.XP.rotationDegrees(data.xRot()));
        pose.mulPose(Axis.YP.rotationDegrees(data.yRot()));
        pose.mulPose(Axis.ZP.rotationDegrees(data.zRot()));
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.CUSTOM;
    }

    public static void clearCaches() {
        FRAME_COUNT_CACHE.clear();
        START_INDEX_CACHE.clear();
        TEXTURE_PATH_CACHE.clear();
    }

    public static class Provider implements ParticleProvider<VFXParticleData> {
        public Provider(SpriteSet pSprites) {
        }
        @Override
        public Particle createParticle(VFXParticleData pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            return new VFXParticle(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed, pType);
        }
    }
}