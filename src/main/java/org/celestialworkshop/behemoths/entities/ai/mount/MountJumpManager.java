package org.celestialworkshop.behemoths.entities.ai.mount;

import net.minecraft.world.entity.LivingEntity;
import org.celestialworkshop.behemoths.network.BMNetwork;
import org.celestialworkshop.behemoths.network.shared.QueueJumpPacket;

public class MountJumpManager<T extends LivingEntity & CustomJumpingMount> {

    public final T entity;

    public boolean queuedJump = false;
    public int queuedJumpPower = 0;
    public int jumpPower = 0;
    public int jumpCooldown = 0;

    public MountJumpManager(T entity) {
        this.entity = entity;
    }

    public void entityTick() {
        if (jumpCooldown > 0) {
            jumpCooldown--;
        }

        if (queuedJump && entity.getJumpCondition() && jumpCooldown == 0) {
            jumpCooldown = entity.getJumpCooldown(queuedJumpPower);
            entity.performJump(queuedJumpPower);
            queuedJump = false;
            queuedJumpPower = 0;
        }
    }

    public void jumpableClientTick(boolean jumping) {
        if (jumping && jumpCooldown == 0) {
            if (jumpPower < entity.getMaximumJumpPower()) {
                jumpPower++;
            }
        } else if (jumpPower > 0) {
            BMNetwork.sendToServer(new QueueJumpPacket(entity.getId(), jumpPower));
        }
    }

    public float getJumpScale() {
        return Math.min(this.jumpPower / (float) entity.getMaximumJumpPower(), 1.0F);
    }
}
