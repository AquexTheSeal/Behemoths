package org.celestialworkshop.behemoths.entities.ai.controls.pathnav;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.FlyNodeEvaluator;
import net.minecraft.world.level.pathfinder.PathFinder;

public class TrueFlightPathNavigation extends FlyingPathNavigation {

    private static final float LARGE_ENTITY_PROXIMITY_MULTIPLIER = 2.5f;

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

//        Path path = this.getPath();
//        if (path == null || path.isDone()) {
//            return;
//        }
//
//        float proximityRadius = this.mob.getBbWidth() * LARGE_ENTITY_PROXIMITY_MULTIPLIER;
//        float verticalTolerance = this.mob.getBbHeight() * LARGE_ENTITY_PROXIMITY_MULTIPLIER;
//
//        Vec3i nextNodePos = path.getNextNodePos();
//        double dx = Math.abs(this.mob.getX() - (nextNodePos.getX() + 0.5));
//        double dy = Math.abs(this.mob.getY() - nextNodePos.getY());
//        double dz = Math.abs(this.mob.getZ() - (nextNodePos.getZ() + 0.5));
//
//        if (dx <= proximityRadius && dz <= proximityRadius && dy <= verticalTolerance) {
//            path.advance();
//        }
    }
}