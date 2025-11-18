package org.celestialworkshop.behemoths.api.animation;

import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;

public class EntityAnimationManager {

    public final Object2ObjectOpenHashMap<String, Pair<AnimationState, AnimationDefinition>> animationStateMap = new Object2ObjectOpenHashMap<>();

    public final Entity entity;

    public EntityAnimationManager(Entity entity) {
        this.entity = entity;
    }

    public AnimationState registerAnimation(String name, AnimationDefinition definition) {
        AnimationState state = new AnimationState();
        this.animationStateMap.put(name, Pair.of(state, definition));
        return state;
    }
}
