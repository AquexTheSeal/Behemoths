package org.celestialworkshop.behemoths.utils;

import net.minecraft.world.entity.Entity;
import org.celestialworkshop.behemoths.network.BMNetwork;
import org.celestialworkshop.behemoths.network.shared.EntityActionSharedPacket;

import java.util.Map;

public class EntityAnimationUtils {

    public static void playAnimationS2C(Entity entity, String id) {
        BMNetwork.sendToAll(new EntityActionSharedPacket(entity.getId(), EntityActionSharedPacket.Action.PLAY_ENTITY_ANIMATION, Map.of("id", id)));
    }
}
