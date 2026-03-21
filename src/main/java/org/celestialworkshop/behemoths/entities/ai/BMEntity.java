package org.celestialworkshop.behemoths.entities.ai;

import it.unimi.dsi.fastutil.Pair;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.network.PacketDistributor;
import org.celestialworkshop.behemoths.api.client.animation.EntityAnimationManager;
import org.celestialworkshop.behemoths.api.entity.ActionManager;
import org.celestialworkshop.behemoths.misc.mixinhelpers.IMixinMob;
import org.celestialworkshop.behemoths.network.BMNetwork;
import org.celestialworkshop.behemoths.network.s2c.RemoveBossBarDataPacket;
import org.celestialworkshop.behemoths.network.s2c.SyncBossBarDataPacket;

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

    // MISCELLANEOUS

    default float[] getFlightAllowance() {
        return new float[]{2.0F, 2.0F};
    }

    default void addBossBarPlayer(ServerBossEvent bossEvent, ServerPlayer pServerPlayer, int bossIdx) {
        bossEvent.addPlayer(pServerPlayer);
        BMNetwork.INSTANCE.send(
                PacketDistributor.PLAYER.with(() -> pServerPlayer),
                new SyncBossBarDataPacket(bossEvent.getId(), bossIdx)
        );
    }

    default void removeBossBarPlayer(ServerBossEvent bossEvent, ServerPlayer pServerPlayer) {
        bossEvent.removePlayer(pServerPlayer);
        BMNetwork.INSTANCE.send(
                PacketDistributor.PLAYER.with(() -> pServerPlayer),
                new RemoveBossBarDataPacket(bossEvent.getId())
        );
    }


    // UTIL

    enum Operation {
        MULTIPLY,
        ADD
    }

    default Mob bmSelf() {
        return (Mob) this;
    }
}
