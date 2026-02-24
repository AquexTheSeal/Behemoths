package org.celestialworkshop.behemoths.entities.ai.controls.look;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.LookControl;

public class NoXRotResetLookControl extends LookControl {

    public NoXRotResetLookControl(Mob pMob) {
        super(pMob);
    }

    @Override
    protected boolean resetXRotOnTick() {
        return false;
    }

    public void tick() {
        super.tick();

//        this.getYRotD().ifPresent((rot) -> {
//            this.mob.yHeadRot = this.rotateTowards(this.mob.yHeadRot, rot, this.yMaxRotSpeed);
//        });
//        this.getXRotD().ifPresent((rot) -> {
//            this.mob.setXRot(this.rotateTowards(this.mob.getXRot(), rot, this.xMaxRotAngle));
//        });
//
//        this.clampHeadRotationToBody();
    }
}
