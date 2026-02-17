package org.celestialworkshop.behemoths.api.camera;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;
import org.celestialworkshop.behemoths.network.BMNetwork;
import org.celestialworkshop.behemoths.network.s2c.BMCameraShakePacket;

public class CameraAngleManager {

    public static void shakeArea(Level level, Vec3 center, double maxRadius, float baseIntensity, int duration, float frequency) {
        if (level instanceof ServerLevel server) {
            for (ServerPlayer player : server.players()) {
                Vec3 playerPos = player.position();
                double distance = playerPos.distanceTo(center);
                if (distance > maxRadius) {
                    continue;
                }
                double falloffFactor = 1.0 - (distance / maxRadius);
                falloffFactor = Math.max(0.0, falloffFactor);
                float adjustedIntensity = (float)(baseIntensity * falloffFactor);
                if (adjustedIntensity > 0.1f) {
                    BMNetwork.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new BMCameraShakePacket(adjustedIntensity, duration, frequency));
                }
            }
        }
    }

    public static void shakeSingle(Entity entity, float intensity, int duration, float frequency) {
        if (entity.level().isClientSide) {
            ScreenShakeHandler.shakeLocal(intensity, duration, frequency);
        } else {
            BMNetwork.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) entity), new BMCameraShakePacket(intensity, duration, frequency));
        }
    }
}
