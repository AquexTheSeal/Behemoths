package org.aqutheseal.behemoths.entity.ai.movecontrol;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class BMFlyingControl extends MoveControl {

    private final Mob mob;

    public BMFlyingControl(Mob mob) {
        super(mob);
        this.mob = mob;
    }

    public void tick() {
        float speed = (float) this.getSpeedModifier();
        double speedFactor = mob.getAttributeValue(Attributes.FLYING_SPEED) * speed;

        if (mob.isVehicle() && mob.getFirstPassenger() instanceof Player passenger) {
            this.lookAt(passenger.position().add(passenger.getLookAngle().scale(6)), 10, 10);
            mob.setDeltaMovement(mob.getDeltaMovement().add(mob.getLookAngle()).scale(speedFactor * 1.25F));
        } else {
            Vec3 targetPos = new Vec3(wantedX, wantedY, wantedZ);
            if (!mob.isDeadOrDying()) {
                Vec3 movement = mob.getDeltaMovement().add(mob.getLookAngle()).scale(speedFactor);
                if (mob.horizontalCollision) {
                    mob.setXRot(Mth.clamp(mob.getXRot() + calculateOptimalYAdjustment() * 10, -80, 80));
                    mob.yHeadRot = Mth.wrapDegrees(mob.yHeadRot - 15);
                } else {
                    this.lookAt(targetPos, 5, 3);
                }
                if (mob.verticalCollision && !mob.onGround()) {
                    mob.setXRot(Mth.clamp(mob.getXRot() + calculateOptimalYAdjustment() * 15, -80, 80));
                } else if (mob.onGround()) {
                    mob.setXRot(Mth.clamp(mob.getXRot() - 15, -80, 80));
                } else {
                    this.lookAt(targetPos, 5, 3);
                }
                mob.setDeltaMovement(movement);
            }
        }
    }

    public float calculateOptimalYAdjustment() {
        BlockPos pos = mob.blockPosition();
        int loopLimit = 64;
        int aboveCount = 0;
        while (aboveCount <= loopLimit && mob.level().isEmptyBlock(pos.above(aboveCount))) {
            aboveCount++;
        }
        int belowCount = 0;
        while (belowCount <= loopLimit && mob.level().isEmptyBlock(pos.below(belowCount))) {
            belowCount++;
        }
        return aboveCount >= belowCount ? -1.0F : 1.0F;
    }

    public void lookAt(Vec3 position, float pMaxYRotIncrease, float pMaxXRotIncrease) {
        mob.getLookControl().setLookAt(position.x, position.y, position.z, pMaxYRotIncrease, pMaxXRotIncrease);
    }
}
