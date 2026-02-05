package org.celestialworkshop.behemoths.entities.ai;

import it.unimi.dsi.fastutil.Pair;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import org.celestialworkshop.behemoths.api.client.animation.EntityAnimationManager;
import org.celestialworkshop.behemoths.misc.utils.mixinhelpers.MobMixinHelper;

public interface BMEntity {

    default float getRotationFreedom() {
        return 1.0F;
    }

    // ANIMATION

    default EntityAnimationManager getAnimationManager() {
        return null;
    }

    // ATTACKING

    default void attackTarget(Entity target, float damageModifier, Operation operation) {
        if (bmSelf() instanceof MobMixinHelper mm) {
            mm.bm$setDamageModifier(Pair.of(damageModifier, operation));
            this.bmSelf().doHurtTarget(target);
            mm.bm$setDamageModifier(null);
        }
    }

    default void addAndAttackTarget(Entity target, float damageModifier) {
        this.attackTarget(target, damageModifier, Operation.ADD);
    }

    default void multiplyAndAttackTarget(Entity target, float damageModifier) {
        this.attackTarget(target, damageModifier, Operation.MULTIPLY);
    }

    enum Operation {
        MULTIPLY,
        ADD
    }

    default Mob bmSelf() {
        return (Mob) this;
    }

}
