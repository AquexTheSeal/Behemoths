package org.aqutheseal.behemoths.entity.ai.lookcontrol;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.LookControl;

public class SmoothFlyingLookControl extends LookControl {
    private final int maxYRotFromCenter;

    public SmoothFlyingLookControl(Mob pMob, int pMaxYRotFromCenter) {
        super(pMob);
        this.maxYRotFromCenter = pMaxYRotFromCenter;
    }

    @Override
    protected boolean resetXRotOnTick() {
        return false;
    }

    public void tick() {
       super.tick();
       mob.setYRot(this.mob.yHeadRot);
    }
}
