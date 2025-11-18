package org.celestialworkshop.behemoths.particles;

import java.util.function.BiFunction;

public enum VFXFade {
    NONE((age, lifetime) -> 1.0f),

    FADE_OUT((age, lifetime) -> {
        float progress = Math.min(age / lifetime, 1.0f);
        return 1.0f - progress;
    }),

    FADE_IN((age, lifetime) -> Math.min(age / lifetime, 1.0f)),

    FADE_IN_OUT((age, lifetime) -> {
        float progress = Math.min(age / lifetime, 1.0f);
        if (progress < 0.5f) {
            return progress * 2.0f;
        }
        return 2.0f - (progress * 2.0f);
    }),

    FADE_OUT_SMOOTH((age, lifetime) -> {
        float progress = Math.min(age / lifetime, 1.0f);
        return (float) Math.pow(1.0f - progress, 2);
    }),

    FADE_IN_SMOOTH((age, lifetime) -> {
        float progress = Math.min(age / lifetime, 1.0f);
        return 1.0f - (float) Math.pow(1.0f - progress, 2);
    }),

    FADE_OUT_LATE((age, lifetime) -> {
        float progress = Math.min(age / lifetime, 1.0f);
        if (progress < 0.75f) {
            return 1.0f;
        }
        return 1.0f - ((progress - 0.75f) * 4.0f);
    }),

    FADE_IN_EARLY((age, lifetime) -> {
        float progress = Math.min(age / lifetime, 1.0f);
        if (progress < 0.25f) {
            return progress * 4.0f;
        }
        return 1.0f;
    }),

    FLICKER((age, lifetime) -> {
        float progress = age / lifetime;
        float flicker = (float) Math.sin(progress * Math.PI * 8);
        return 0.7f + 0.3f * Math.abs(flicker);
    }),

    FADE_IN_QUICK_OUT_SLOW((age, lifetime) -> {
        float progress = Math.min(age / lifetime, 1.0f);
        if (progress < 0.1f) {
            return progress * 10.0f;
        }
        float fadeProgress = (progress - 0.1f) / 0.9f;
        return 1.0f - (float) Math.pow(fadeProgress, 0.5);
    });

    private final BiFunction<Float, Integer, Float> fadeFunction;

    VFXFade(BiFunction<Float, Integer, Float> fadeFunction) {
        this.fadeFunction = fadeFunction;
    }

    public float getAlpha(float age, int lifetime) {
        if (lifetime <= 0) return 1.0f;
        float alpha = fadeFunction.apply(age, lifetime);
        return Math.max(0.0f, Math.min(1.0f, alpha));
    }
}
