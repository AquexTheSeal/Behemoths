package org.celestialworkshop.behemoths.entities.ai;

import it.unimi.dsi.fastutil.Pair;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import org.celestialworkshop.behemoths.network.BMNetwork;
import org.celestialworkshop.behemoths.network.s2c.ManageAnimationStatePacket;
import org.celestialworkshop.behemoths.utils.mixinhelpers.MobMixinHelper;

import java.util.Map;

public interface BMEntity {

    // ANIMATION

    Map<String, AnimationState> getAnimationStateMap();

    default AnimationState createAnimationState(String name) {
        AnimationState state = new AnimationState();
        this.getAnimationStateMap().put(name, state);
        return state;
    }

    default void manageAnimationState(String animationId, ManageAnimationStatePacket.Action action) {
        BMNetwork.sendToAll(new ManageAnimationStatePacket(this.bmSelf().getId(), animationId, action));
    }

    default void startAnimation(String animationId) {
        this.manageAnimationState(animationId, ManageAnimationStatePacket.Action.START);
    }

    default void stopAnimation(String animationId) {
        this.manageAnimationState(animationId, ManageAnimationStatePacket.Action.STOP);
    }

    default float getRotationFreedom() {
        return 1.0F;
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
