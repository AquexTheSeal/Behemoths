package org.celestialworkshop.behemoths.entities.ai.goals;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;
import org.celestialworkshop.behemoths.entities.Archzombie;
import org.celestialworkshop.behemoths.entities.BanishingStampede;
import org.celestialworkshop.behemoths.registries.BMPandemoniumCurses;
import org.celestialworkshop.behemoths.utils.WorldUtils;

import java.util.EnumSet;
import java.util.List;

public class ArchzombieOrbitGoal extends Goal {
    private static final double BASE_RADIUS = 6.5;
    private static final int DIRECTION_CHANGE_INTERVAL = 100;
    private static final int POKE_COOLDOWN_MIN = 80;
    private static final int POKE_DURATION = 30;
    private static final double MIN_SPACING_DISTANCE = 4.0;
    private static final double SPREAD_RADIUS = 2.0;
    private static final int SPACING_CHECK_INTERVAL = 40;

    private final Archzombie archzombie;

    private int orbitTimer;
    private int pokeTimer;
    private boolean clockwise;
    private int pathRecalcTicks;
    private int spacingCheckTimer;
    private Vec3 cachedSpacingOffset = Vec3.ZERO;
    private double cachedPandemoniumSpeed = 1.0;
    private int pandemoniumCacheTicks;

    public ArchzombieOrbitGoal(Archzombie archzombie) {
        this.archzombie = archzombie;
        this.clockwise = archzombie.getRandom().nextBoolean();
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return archzombie.getTarget() != null && archzombie.getVehicle() instanceof BanishingStampede;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        if (--pathRecalcTicks > 0) return;

        pathRecalcTicks = 2;

        LivingEntity target = archzombie.getTarget();
        if (target == null) return;

        if (--pandemoniumCacheTicks <= 0) {
            cachedPandemoniumSpeed = WorldUtils.hasPandemoniumCurse(archzombie.level(), BMPandemoniumCurses.GRAVEBREAKER_MOMENTUM.get()) ? 1.2 : 1.0;
            pandemoniumCacheTicks = 80;
        }

        orbitTimer++;
        double distance = archzombie.distanceTo(target);

        if (distance < 8) pokeTimer++;

        if (orbitTimer % DIRECTION_CHANGE_INTERVAL == 0 && archzombie.getRandom().nextFloat() < 0.3F) {
            clockwise = !clockwise;
        }

        updateSpacing();

        int pokeDuration = POKE_DURATION + (int)(POKE_DURATION * 0.5 * cachedPandemoniumSpeed);

        if (pokeTimer >= POKE_COOLDOWN_MIN) {
            double pokeSpeed = (target.getVehicle() != null ? 1.45 : 1.25) * cachedPandemoniumSpeed;
            double totalSpeed = pokeSpeed * (1 + distance * 0.015);
            archzombie.getNavigation().moveTo(target, totalSpeed);

            if (pokeTimer >= POKE_COOLDOWN_MIN + pokeDuration) {
                pokeTimer = -archzombie.getRandom().nextInt(20);
            }
            return;
        }

        double angle = orbitTimer * 0.4 * cachedPandemoniumSpeed * (clockwise ? 1 : -1);
        double radius = BASE_RADIUS + cachedSpacingOffset.length();

        Vec3 targetPos = target.position();

        double x = targetPos.x + Math.cos(angle) * radius + cachedSpacingOffset.x;
        double z = targetPos.z + Math.sin(angle) * radius + cachedSpacingOffset.z;
        double y = targetPos.y + 0.2;

        double offenseSpeed = (target.getVehicle() != null ? 1.45 : 1.25) * cachedPandemoniumSpeed;
        double totalSpeed = offenseSpeed * (1 + distance * 0.015);

        if (archzombie.distanceToSqr(x, y, z) > 3.0) {
            archzombie.getNavigation().moveTo(x, y, z, totalSpeed);
        }
    }

    private void updateSpacing() {
        if (--spacingCheckTimer > 0) return;
        spacingCheckTimer = SPACING_CHECK_INTERVAL;

        List<Archzombie> nearby = archzombie.level().getEntitiesOfClass(
                Archzombie.class,
                archzombie.getBoundingBox().inflate(12.0),
                other -> other != archzombie && other.getVehicle() instanceof BanishingStampede && other.getTarget() == archzombie.getTarget()
        );

        if (nearby.isEmpty()) {
            cachedSpacingOffset = Vec3.ZERO;
            return;
        }

        Vec3 repulsion = Vec3.ZERO;
        Vec3 myPos = archzombie.position();

        for (Archzombie other : nearby) {
            Vec3 otherPos = other.position();
            double dist = myPos.distanceTo(otherPos);

            if (dist < MIN_SPACING_DISTANCE && dist > 0.1) {
                Vec3 away = myPos.subtract(otherPos).normalize();
                double force = (MIN_SPACING_DISTANCE - dist) / MIN_SPACING_DISTANCE;
                repulsion = repulsion.add(away.scale(force));
            }
        }

        cachedSpacingOffset = repulsion.lengthSqr() > 0 ? repulsion.normalize().scale(SPREAD_RADIUS) : Vec3.ZERO;
    }
}