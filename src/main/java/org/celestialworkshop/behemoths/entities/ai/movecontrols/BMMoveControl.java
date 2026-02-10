package org.celestialworkshop.behemoths.entities.ai.movecontrols;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.NodeEvaluator;
import org.celestialworkshop.behemoths.entities.ai.BMEntity;

public class BMMoveControl<T extends Mob & BMEntity> extends MoveControl {

    protected final T entity;
    
    public BMMoveControl(T entity) {
        super(entity);
        this.entity = entity;
    }

    @Override
    public void tick() {

        if (this.operation == MoveControl.Operation.STRAFE) {
            float baseSpeed = (float) this.entity.getAttributeValue(Attributes.MOVEMENT_SPEED);
            float adjustedSpeed = (float) this.speedModifier * baseSpeed;
            float forwardMovement = this.strafeForwards;
            float sideMovement = this.strafeRight;
            float moveDistSqrd = Math.min(Mth.sqrt(forwardMovement * forwardMovement + sideMovement * sideMovement), 1.0F);

            moveDistSqrd = adjustedSpeed / moveDistSqrd;
            forwardMovement *= moveDistSqrd;
            sideMovement *= moveDistSqrd;

            float xDirMovementMod = Mth.sin((float) Math.toRadians(mob.getYRot()));
            float zDirMovementMod = Mth.cos((float) Math.toRadians(mob.getYRot()));

            float xMovementPredictionOffset = forwardMovement * zDirMovementMod - sideMovement * xDirMovementMod;
            float zMovementPredictionOffset = sideMovement * zDirMovementMod + forwardMovement * xDirMovementMod;

            if (!isWalkable(xMovementPredictionOffset, zMovementPredictionOffset)) {
                this.strafeForwards = 1.0F;
                this.strafeRight = 0.0F;
            }

            this.entity.setSpeed(adjustedSpeed);
            this.entity.setZza(strafeForwards);
            this.entity.setXxa(strafeRight);
            this.operation = MoveControl.Operation.WAIT;
        } else if (this.operation == MoveControl.Operation.MOVE_TO) {
            this.operation = MoveControl.Operation.WAIT;

            double xDelta = this.wantedX - this.entity.getX();
            double zDelta = this.wantedZ - this.entity.getZ();
            double yDelta = this.wantedY - this.entity.getY();

            double deltaSqrd = xDelta * xDelta + yDelta * yDelta + zDelta * zDelta;

            if (deltaSqrd < 2.5000003E-7D) {
                this.entity.setZza(0.0F);
                return;
            }

            float adjustedDirectionalMovementAngle = (float) (Math.toDegrees(Mth.atan2(zDelta, xDelta))) - 90.0F;

            this.entity.setYRot(rotlerp(this.entity.getYRot(), adjustedDirectionalMovementAngle, 180.0F * this.entity.getRotationFreedom()));
            this.entity.setSpeed((float) (this.speedModifier * this.entity.getAttributeValue(Attributes.MOVEMENT_SPEED)));
        } else this.entity.setZza(0.0F);
    }

    public boolean isWalkable(float relativeXOffset, float relativeZOffset) {
        PathNavigation ownerPathNav = this.entity.getNavigation();
        NodeEvaluator curNodeEvaluator = ownerPathNav.getNodeEvaluator();

        return curNodeEvaluator.getBlockPathType(this.entity.level(), Mth.floor(this.entity.getX() + relativeXOffset), this.entity.getBlockY(), Mth.floor(this.entity.getZ() + relativeZOffset)) == BlockPathTypes.WALKABLE;
    }
}

