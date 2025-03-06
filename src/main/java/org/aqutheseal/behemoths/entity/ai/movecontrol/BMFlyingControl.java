package org.aqutheseal.behemoths.entity.ai.movecontrol;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.NodeEvaluator;
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
                this.lookAt(targetPos, 2, 2);
                Vec3 movement = mob.getDeltaMovement().add(mob.getLookAngle()).scale(speedFactor);
                if (mob.horizontalCollision) {
                    movement = movement.add(0, 0.1, 0);
                }
                Vec3 checkFront = mob.getLookAngle().scale(6);
//                if (this.isWalkable(checkFront.x(), checkFront.z())) {
//
//                }
                mob.setDeltaMovement(movement);
            }
        }
    }

    public void lookAt(Vec3 position, float pMaxYRotIncrease, float pMaxXRotIncrease) {
        mob.getLookControl().setLookAt(position.x, position.y, position.z, pMaxYRotIncrease, pMaxXRotIncrease);
    }

    private boolean isWalkable(double pRelativeX, double pRelativeZ) {
        PathNavigation pathnavigation = this.mob.getNavigation();
        NodeEvaluator nodeevaluator = pathnavigation.getNodeEvaluator();
        if (nodeevaluator.getBlockPathType(this.mob.level(), Mth.floor(this.mob.getX() + pRelativeX), this.mob.getBlockY(), Mth.floor(this.mob.getZ() + pRelativeZ)) != BlockPathTypes.WALKABLE) {
            return false;
        }
        return true;
    }
}
