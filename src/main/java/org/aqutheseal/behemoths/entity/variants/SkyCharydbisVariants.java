package org.aqutheseal.behemoths.entity.variants;

public enum SkyCharydbisVariants {
    BARREN("barren", 1.2F),

    LUSH("lush", 1.0F),

    NORTHERN("northern", 1.3F),

    NETHER("nether", 1.8F),

    SOUL("soul", 2F),

    VOID("void", 2.5F);

    public final String id;
    public final float hpMultiplier;

    SkyCharydbisVariants(String id, float hpMultiplier) {
        this.id = id;
        this.hpMultiplier = hpMultiplier;
    }
}
