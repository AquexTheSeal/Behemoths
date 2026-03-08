package org.celestialworkshop.behemoths.entities.ai.controls.move;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.phys.Vec3;
import org.celestialworkshop.behemoths.entities.SkyCharydbis;

public class FlyingLookBasedMoveControl extends FlyingMoveControl {

    public SkyCharydbis entity;

    public FlyingLookBasedMoveControl(SkyCharydbis entity) {
        super(entity, 180, false);
        this.entity = entity;
    }

    @Override
    public void tick() {
        if (entity.isCurrentSleepFlag(SkyCharydbis.SLEEPING_FLAG) || entity.isCurrentSleepFlag(SkyCharydbis.WAKING_FLAG)) {
            return;
        }

        if (this.operation == Operation.MOVE_TO) {
            this.operation = Operation.WAIT;

            double dx = this.wantedX - this.mob.getX();
            double dy = this.wantedY - this.mob.getY();
            double dz = this.wantedZ - this.mob.getZ();
            double horizontalDist = Math.sqrt(dx * dx + dz * dz);

            float targetYaw = (float) (Mth.atan2(dz, dx) * Mth.RAD_TO_DEG) - 90.0F;
            float targetPitch = (float) (-(Mth.atan2(dy, horizontalDist) * Mth.RAD_TO_DEG));

            this.mob.setXRot(this.rotlerp(this.mob.getXRot(), targetPitch, 4.5F * (float) speedModifier));
            this.mob.setYRot(this.rotlerp(this.mob.getYRot(), targetYaw, 3.0F * (float) speedModifier));
            this.mob.yBodyRot = this.mob.getYRot();
            this.mob.yHeadRot = this.mob.getYRot();
        }

        this.moveBasedOnLook();
    }

    private void moveBasedOnLook() {
        this.mob.setNoGravity(true);

        checkAndFixCollision();

        double speed = this.speedModifier * this.mob.getAttributeValue(Attributes.FLYING_SPEED);
        Vec3 delta = this.mob.getDeltaMovement().add(this.mob.getLookAngle().scale(speed));

        double maxSpeed = speed * 1.5;
        if (delta.lengthSqr() > maxSpeed * maxSpeed) {
            delta = delta.normalize().scale(maxSpeed);
        }

        this.mob.setDeltaMovement(delta);
    }

    private void checkAndFixCollision() {
        if (mob.horizontalCollision || mob.verticalCollision) {
//            this.mob.setXRot(this.rotlerp(this.mob.getXRot(), mob.getXRot() + this.calculateOptimalYAdjustment() * 5, 4));
            this.mob.setYRot(this.rotlerp(this.mob.getYRot(), mob.getYRot() - 45, 10));
        }
    }

    public float calculateOptimalYAdjustment() {
        BlockPos pos = this.entity.blockPosition();
        int loopLimit = 32;
        int aboveCount = 0;
        while (aboveCount <= loopLimit && this.entity.level().isEmptyBlock(pos.above(aboveCount))) {
            aboveCount++;
        }
        int belowCount = 0;
        while (belowCount <= loopLimit && this.entity.level().isEmptyBlock(pos.below(belowCount))) {
            belowCount++;
        }
        return aboveCount >= belowCount ? -1.0F : 1.0F;
    }
}
