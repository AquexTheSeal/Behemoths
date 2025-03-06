package org.aqutheseal.behemoths.events;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.phys.Vec3;
import org.aqutheseal.behemoths.util.UtilParticle;
import org.aqutheseal.behemoths.util.mixin.IBallistaArrow;

public class ModTriggers {

    public static <T extends AbstractArrow & IBallistaArrow> void tickBehemothArrow(T arrow) {
        if (arrow.behemoths$getOnCollide() != null) {
            Vec3 start = arrow.position();
            Vec3 end = start.add(arrow.getDeltaMovement());

            for (int i = 0; i < 5; i++) {
                double t = i / 4.0;
                Vec3 particlePos = start.lerp(end, t);
                UtilParticle.addParticleServer(arrow.level(), ParticleTypes.CAMPFIRE_COSY_SMOKE, particlePos);
            }
        }
    }
}
