package org.aqutheseal.behemoths.entity.ai.movecontrol;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;

public class ContinuousFlyingMoveControl extends MoveControl {
    private final float rotationSpeed;
    private final float minMovementThreshold;
    private boolean hasTarget = false;

    public ContinuousFlyingMoveControl(Mob pMob) {
        super(pMob);
        this.rotationSpeed = 3.0F;
        this.minMovementThreshold = 0.5F;
    }

    public void tick() {
        if (this.operation == MoveControl.Operation.MOVE_TO) {
            if (!hasTarget) {
                hasTarget = true;
            }

            double d0 = this.wantedX - this.mob.getX();
            double d1 = this.wantedY - this.mob.getY();
            double d2 = this.wantedZ - this.mob.getZ();
            double distanceSquared = d0 * d0 + d1 * d1 + d2 * d2;

            if (distanceSquared < (double)(this.minMovementThreshold * this.minMovementThreshold)) {
                this.operation = MoveControl.Operation.WAIT;
                this.mob.setYya(0.0F);
                this.mob.setZza(0.0F);
                hasTarget = false;
                return;
            }

            float targetYaw = (float)(Mth.atan2(d2, d0) * (double)(180F / (float)Math.PI)) - 90.0F;
            this.mob.setYRot(this.rotlerp(this.mob.getYRot(), targetYaw, rotationSpeed));

            float moveSpeed;
            if (this.mob.onGround()) {
                moveSpeed = (float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED));
            } else {
                moveSpeed = (float)(this.speedModifier * this.mob.getAttributeValue(Attributes.FLYING_SPEED));
            }

            this.mob.setSpeed(moveSpeed);

            double horizontalDistance = Math.sqrt(d0 * d0 + d2 * d2);
            if (Math.abs(d1) > (double) 1.0E-5F || Math.abs(horizontalDistance) > (double) 1.0E-5F) {
                float targetPitch = (float)(-(Mth.atan2(d1, horizontalDistance) * (180F / Math.PI)));
                this.mob.setXRot(this.rotlerp(this.mob.getXRot(), targetPitch, rotationSpeed));

                float verticalSpeed = d1 > 0.0D ? moveSpeed : -moveSpeed;
                this.mob.setYya(verticalSpeed * (float)Math.min(1.0, Math.abs(d1 / horizontalDistance)));
                this.mob.setZza(moveSpeed * 0.5F);
            }
        } else {
            hasTarget = false;
            this.mob.setYya(this.mob.yya * 0.9F);
            this.mob.setZza(this.mob.zza * 0.9F);
        }
    }

    @Override
    protected float rotlerp(float pCurrentRotation, float pTargetRotation, float pMaxDelta) {
        float deltaRotation = Mth.wrapDegrees(pTargetRotation - pCurrentRotation);
        float clampedDelta = Mth.clamp(deltaRotation, -pMaxDelta, pMaxDelta);
        float easingFactor = Math.min(1.0F, Math.abs(deltaRotation) / 45.0F);
        clampedDelta *= easingFactor;

        return pCurrentRotation + clampedDelta;
    }
}
