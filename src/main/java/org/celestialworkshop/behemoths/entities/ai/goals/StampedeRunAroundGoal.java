package org.celestialworkshop.behemoths.entities.ai.goals;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.RunAroundLikeCrazyGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.ForgeEventFactory;
import org.celestialworkshop.behemoths.entities.BanishingStampede;
import org.celestialworkshop.behemoths.utils.EntityAnimationUtils;

public class StampedeRunAroundGoal extends RunAroundLikeCrazyGoal {
    
    private final BanishingStampede stampede;
    
    public StampedeRunAroundGoal(BanishingStampede stampede, double pSpeedModifier) {
        super(stampede, pSpeedModifier);
        this.stampede = stampede;
    }

    public void tick() {
        if (!stampede.isTamed() && stampede.getRandom().nextInt(this.adjustedTickDelay(50)) == 0) {
            Entity entity = stampede.getPassengers().get(0);
            if (entity == null) {
                return;
            }

            if (entity instanceof Player) {
                int maxTemper = stampede.getMaxTemper();
                if (stampede.getMaxTemper() > 0 && stampede.getRandom().nextInt(maxTemper) < stampede.getTemper() && !ForgeEventFactory.onAnimalTame(stampede, (Player) entity)) {
                    stampede.tameWithName((Player) entity);
                    return;
                }

                stampede.modifyTemper(5);
            }

            stampede.getPassengers().forEach(rider -> {
                rider.setDeltaMovement(stampede.getLookAngle().multiply(2, 0, 2).add(0, 1, 0));
                rider.hurtMarked = true;
            });
            stampede.ejectPassengers();
            stampede.makeMad();
            EntityAnimationUtils.playAnimationS2C(stampede, BanishingStampede.THROW_RIDER_ANIMATION);
            stampede.level().broadcastEntityEvent(stampede, (byte)6);
        }

    }
}
