package org.celestialworkshop.behemoths.client.models;

import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;
import org.celestialworkshop.behemoths.entities.ai.BMEntity;
import org.joml.Vector3f;

import java.util.List;
import java.util.function.Supplier;

public abstract class BMHierarchicalModel<E extends LivingEntity & BMEntity> extends HierarchicalModel<E> {

    private static final Vector3f ANIMATION_VECTOR_CACHE = new Vector3f();

    public void animateManager(E entity, float ageInTicks) {
        entity.getAnimationManager().getAnimationStateMap().forEach((name, pair) -> {
            Supplier<?> second = pair.second();
            if (second.get() instanceof AnimationDefinition definition) {
                this.animate(pair.first(), definition, ageInTicks);
            }
        });
    }

    public abstract List<ModelPart> parts();

    protected void animateScaled(AnimationDefinition pAnimationDefinition, float pAgeInTicks, float pSpeed, float pScale) {
        long i = (long)(pAgeInTicks * 50.0F * pSpeed);
        float f = Math.max(0.0F, pScale);
        KeyframeAnimations.animate(this, pAnimationDefinition, i, f, ANIMATION_VECTOR_CACHE);
    }
}
