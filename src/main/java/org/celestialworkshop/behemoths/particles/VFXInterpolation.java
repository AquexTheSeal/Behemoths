package org.celestialworkshop.behemoths.particles;


// TODO: Separate easing types from easings.
public enum VFXInterpolation {
    LINEAR,
    EASE_IN_SINE,
    EASE_OUT_SINE,
    EASE_IN_OUT_SINE,
    EASE_IN_QUAD,
    EASE_OUT_QUAD,
    EASE_IN_OUT_QUAD,
    EASE_IN_CUBIC,
    EASE_OUT_CUBIC,
    EASE_IN_OUT_CUBIC,
    EASE_IN_QUART,
    EASE_OUT_QUART,
    EASE_IN_OUT_QUART;

    public static float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }

    public static float apply(int ordinal, float t) {
        VFXInterpolation func = VFXInterpolation.values()[Math.min(Math.max(ordinal, 0), VFXInterpolation.values().length - 1)];
        return switch (func) {
            case EASE_IN_SINE -> (float) (1 - Math.cos((t * Math.PI) / 2));
            case EASE_OUT_SINE -> (float) Math.sin((t * Math.PI) / 2);
            case EASE_IN_OUT_SINE -> (float) (-(Math.cos(Math.PI * t) - 1) / 2);
            case EASE_IN_QUAD -> t * t;
            case EASE_OUT_QUAD -> 1 - (1 - t) * (1 - t);
            case EASE_IN_OUT_QUAD -> t < 0.5 ? 2 * t * t : 1 - (float) Math.pow(-2 * t + 2, 2) / 2;
            case EASE_IN_CUBIC -> t * t * t;
            case EASE_OUT_CUBIC -> 1 - (float) Math.pow(1 - t, 3);
            case EASE_IN_OUT_CUBIC -> t < 0.5 ? 4 * t * t * t : 1 - (float) Math.pow(-2 * t + 2, 3) / 2;
            case EASE_IN_QUART -> t * t * t * t;
            case EASE_OUT_QUART -> 1 - (float) Math.pow(1 - t, 4);
            case EASE_IN_OUT_QUART -> t < 0.5 ? 8 * t * t * t * t : 1 - (float) Math.pow(-2 * t + 2, 4) / 2;
            default -> t;
        };
    }
}
