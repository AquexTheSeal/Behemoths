package org.celestialworkshop.behemoths.entities.ai;

import it.unimi.dsi.fastutil.Pair;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import org.celestialworkshop.behemoths.api.client.animation.EntityAnimationManager;
import org.celestialworkshop.behemoths.api.entity.ActionManager;
import org.celestialworkshop.behemoths.misc.mixinhelpers.IMixinMob;

import java.util.List;

public interface BMEntity {

    default float getRotationFreedom() {
        return 1.0F;
    }

    // ANIMATION

    default EntityAnimationManager getAnimationManager() {
        return null;
    }

    default List<ActionManager> getActionManagers() {
        return List.of();
    }

    // ATTACKING

    default boolean attackTarget(Entity target, float damageModifier, Operation operation) {
        boolean flag = false;
        if (bmSelf() instanceof IMixinMob mm) {
            mm.bm$setDamageModifier(Pair.of(damageModifier, operation));
            if (target != null) {
                return this.bmSelf().doHurtTarget(target);
            }
            mm.bm$setDamageModifier(null);
        }
        return flag;
    }

    default boolean attackTargetAddition(Entity target, float damageModifier) {
        return this.attackTarget(target, damageModifier, Operation.ADD);
    }

    default boolean attackTargetMultiplication(Entity target, float damageModifier) {
        return this.attackTarget(target, damageModifier, Operation.MULTIPLY);
    }

    enum Operation {
        MULTIPLY,
        ADD
    }

    default Mob bmSelf() {
        return (Mob) this;
    }

}
