package org.celestialworkshop.behemoths.client.clientdata;

import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;

import java.util.Map;
import java.util.UUID;

public class ClientBossBarData {
    private static final Map<UUID, Integer> BOSS_INDICES = new Object2IntArrayMap<>();

    public static void setBossIdx(UUID bossBarId, int idx) {
        BOSS_INDICES.put(bossBarId, idx);
    }

    public static int getBossIdx(UUID bossBarId) {
        return BOSS_INDICES.getOrDefault(bossBarId, -1);
    }

    public static void removeBossBar(UUID bossBarId) {
        BOSS_INDICES.remove(bossBarId);
    }
}