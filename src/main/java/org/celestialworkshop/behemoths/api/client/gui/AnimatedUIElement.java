package org.celestialworkshop.behemoths.api.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.util.Mth;
import org.celestialworkshop.behemoths.api.client.animation.InterpolationTypes;

import javax.annotation.Nullable;

public abstract class AnimatedUIElement<T extends Screen> extends BMUIElement<T> {

    public @Nullable AnimationState animation;

    public AnimatedUIElement(T screen) {
        super(screen);
    }

    @Override
    public void tick() {
        if (animation != null && !animation.update()) {
            animation = null;
        }
    }

    @Override
    public void setupRender(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        PoseStack pose = guiGraphics.pose();
        float currentAlpha = getAlpha(partialTick);

        pose.pushPose();
        pose.translate(getOffsetX(partialTick), getOffsetY(partialTick), 0);

        guiGraphics.setColor(1.0f, 1.0f, 1.0f, currentAlpha);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        super.setupRender(guiGraphics, mouseX, mouseY, partialTick);

        RenderSystem.disableBlend();
        guiGraphics.setColor(1.0f, 1.0f, 1.0f, 1.0f);

        pose.popPose();
    }

    public float getOffsetX(float partialTick) {
        return animation == null ? 0 : animation.getInterpolated(partialTick, animation.startX, animation.targetX);
    }

    public float getOffsetY(float partialTick) {
        return animation == null ? 0 : animation.getInterpolated(partialTick, animation.startY, animation.targetY);
    }

    public float getAlpha(float partialTick) {
        return animation == null ? 1f : animation.getInterpolated(partialTick, animation.startAlpha, animation.targetAlpha);
    }

    public static class AnimationState {
        private final InterpolationTypes type;
        private final int duration;
        private final int delay;

        public final int startX, startY;
        public final int targetX, targetY;
        public final float startAlpha, targetAlpha;

        private int elapsed;
        private int tickCounter;

        private AnimationState(Builder b) {
            this.type = b.type;
            this.duration = b.duration;
            this.delay = b.delay;
            this.startX = b.startX;
            this.startY = b.startY;
            this.targetX = b.targetX;
            this.targetY = b.targetY;
            this.startAlpha = b.startAlpha;
            this.targetAlpha = b.targetAlpha;
        }

        public boolean update() {
            tickCounter++;
            if (tickCounter <= delay) return true;

            if (elapsed < duration) {
                elapsed++;
                return true;
            }
            return false;
        }

        public float getProgress(float partialTick) {
            if (tickCounter < delay) return 0f;
            return Mth.clamp((elapsed + partialTick) / (float) duration, 0f, 1f);
        }

        public float getInterpolated(float partialTick, float start, float target) {
            float t = getProgress(partialTick);
            float easedT = InterpolationTypes.apply(type, t);
            return Mth.lerp(easedT, start, target);
        }

        public static class Builder {
            private InterpolationTypes type = InterpolationTypes.LINEAR;
            private int duration = 20, delay = 0;
            private int startX = 0, startY = 0;
            private int targetX = 0, targetY = 0;
            private float startAlpha = 1f, targetAlpha = 1f;

            public Builder type(InterpolationTypes type) { this.type = type; return this; }

            public Builder duration(int duration) { this.duration = duration; return this; }

            public Builder delay(int delay) { this.delay = delay; return this; }

            public Builder from(int x, int y) {
                this.startX = x;
                this.startY = y;
                return this;
            }

            public Builder to(int x, int y) {
                this.targetX = x;
                this.targetY = y;
                return this;
            }

            public Builder alpha(float start, float end) { this.startAlpha = start; this.targetAlpha = end; return this; }

            public Builder fadeIn() { this.startAlpha = 0f; this.targetAlpha = 1f; return this; }

            public Builder fadeOut() { this.startAlpha = 1f; this.targetAlpha = 0f; return this; }

            public AnimationState build() { return new AnimationState(this); }
        }
    }
}