package org.aqutheseal.behemoths.util;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class UtilParticle {

    public static <T extends ParticleOptions> void addParticleServer(Level level, T particle, double pPosX, double pPosY, double pPosZ) {
        if (level instanceof ServerLevel server) {
            server.sendParticles(particle, pPosX, pPosY, pPosZ, 0, 0, 0, 0, 0);
        }
    }

    public static <T extends ParticleOptions> void addParticleServer(Level level, T particle, Vec3 position) {
        addParticleServer(level, particle, position.x(), position.y(), position.z());
    }
}
