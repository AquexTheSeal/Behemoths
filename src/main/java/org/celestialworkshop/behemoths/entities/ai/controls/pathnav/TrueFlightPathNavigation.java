package org.celestialworkshop.behemoths.entities.ai.controls.pathnav;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.FlyNodeEvaluator;
import net.minecraft.world.level.pathfinder.PathFinder;
import org.celestialworkshop.behemoths.entities.ai.BMEntity;

public class TrueFlightPathNavigation extends FlyingPathNavigation {

    public TrueFlightPathNavigation(Mob pMob, Level pLevel) {
        super(pMob, pLevel);
    }

    @Override
    protected PathFinder createPathFinder(int pMaxVisitedNodes) {
        this.nodeEvaluator = new FlyNodeEvaluator();
        this.nodeEvaluator.setCanPassDoors(true);
        this.nodeEvaluator.setCanFloat(true);
        return new PathFinder(this.nodeEvaluator, pMaxVisitedNodes);
    }

    @Override
    protected boolean canUpdatePath() {
        return true;
    }

    @Override
    public boolean isStableDestination(BlockPos pPos) {
        return this.level.getBlockState(pPos).isAir();
    }

    @Override
    protected void followThePath() {
        super.followThePath();

        if (this.path != null && !this.path.isDone()) {
            Vec3i nextNode = this.path.getNextNodePos();

            double dX = Math.abs(this.mob.getX() - ((double)nextNode.getX() + 0.5D));
            double dY = Math.abs(this.mob.getY() - (double)nextNode.getY());
            double dZ = Math.abs(this.mob.getZ() - ((double)nextNode.getZ() + 0.5D));

            float[] acceptance = new float[]{2.0F, 2.0F};
            if (mob instanceof BMEntity bm) {
                acceptance = bm.getFlightAllowance();
            }
            double horizontalAcceptance = this.mob.getBbWidth() * acceptance[0];
            double verticalAcceptance = this.mob.getBbHeight() * acceptance[1];

            if (dX <= horizontalAcceptance && dZ <= horizontalAcceptance && dY <= verticalAcceptance) {
                this.path.advance();
            }
        }
    }
}