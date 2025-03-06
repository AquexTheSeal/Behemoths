package org.aqutheseal.behemoths.util;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;
import org.apache.logging.log4j.util.TriConsumer;
import org.aqutheseal.behemoths.network.BMNetwork;
import org.aqutheseal.behemoths.network.packet.ScreenShakePacket;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class UtilLevel {

    public static void shakeScreensOnSurroundings(Level level, Vec3 position, float range, float maxIntensity, Predicate<Player> immune) {
        if (!level.isClientSide()) {
            AABB box = AABB.ofSize(position, range * 2, range * 2, range * 2);
            for (ServerPlayer player : level.getEntitiesOfClass(ServerPlayer.class, box).stream().filter(immune.negate()).toList()) {
                double distance = player.position().distanceTo(position);
                float intensity = calculateShakeIntensity(distance, range, maxIntensity);

                ScreenShakePacket packet = new ScreenShakePacket(intensity);
                BMNetwork.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), packet);
            }
        }
    }

    private static float calculateShakeIntensity(double distance, float range, float maxIntensity) {
        if (distance >= range) return 0;
        return (float) (maxIntensity * (1 - (distance / range)));
    }

    public static void loopThroughLivingArea(Entity exception, AABB boundingBox, Consumer<LivingEntity> action) {
        List<LivingEntity> targets = exception.level().getEntitiesOfClass(LivingEntity.class, boundingBox).stream().filter(e -> e != exception).toList();
        for (LivingEntity target : targets) {
            action.accept(target);
        }
    }

    public static void loopThroughBlockArea(AABB boundingBox, Consumer<BlockPos> action) {
        int mX = Mth.floor(boundingBox.minX);
        int mY = Mth.floor(boundingBox.minY);
        int mZ = Mth.floor(boundingBox.minZ);
        int xX = Mth.floor(boundingBox.maxX);
        int xY = Mth.floor(boundingBox.maxY);
        int xZ = Mth.floor(boundingBox.maxZ);

        for(int xi = mX; xi <= xX; ++xi) {
            for (int yi = mY; yi <= xY; ++yi) {
                for (int zi = mZ; zi <= xZ; ++zi) {
                    BlockPos blockpos = new BlockPos(xi, yi, zi);
                    action.accept(blockpos);
                }
            }
        }
    }

    public static void loopThroughSpherePos(double radius, TriConsumer<Double, Double, Double> action) {
        for (double x = -radius; x <= radius; x++) {
            for (double y = -radius; y <= radius; y++) {
                for (double z = -radius; z <= radius; z++) {
                    if (x * x + y * y + z * z <= radius * radius) {
                        action.accept(x, y, z);
                    }
                }
            }
        }
    }
}
