package org.celestialworkshop.behemoths.entities.ai.controls.move;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.MoveControl;
import org.celestialworkshop.behemoths.entities.ai.BMEntity;

public class BMMoveControl<T extends Mob & BMEntity> extends MoveControl {

    protected final T entity;
    
    public BMMoveControl(T entity) {
        super(entity);
        this.entity = entity;
    }

    @Override
    protected float rotlerp(float pSourceAngle, float pTargetAngle, float pMaximumChange) {
        return super.rotlerp(pSourceAngle, pTargetAngle, pMaximumChange * entity.getRotationFreedom());
    }
}

