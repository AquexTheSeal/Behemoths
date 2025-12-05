package org.celestialworkshop.behemoths.entities.ai.goals;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.RunAroundLikeCrazyGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.ForgeEventFactory;
import org.celestialworkshop.behemoths.entities.BanishingStampede;

public class StampedeRunAroundGoal extends RunAroundLikeCrazyGoal {
    
    private final BanishingStampede stampede;
    
    public StampedeRunAroundGoal(BanishingStampede stampede, double pSpeedModifier) {
        super(stampede, pSpeedModifier);
        this.stampede = stampede;
    }

    public void tick() {

        if (stampede.getPassengers().isEmpty()) {
            return;
        }

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

            stampede.makeMad();
            stampede.getAnimationManager().forceStartAnimation(BanishingStampede.THROW_RIDER_ANIMATION);

            for(int i = stampede.getPassengers().size() - 1; i >= 0; --i) {
                Entity rider = stampede.getPassengers().get(i);
                rider.stopRiding();
                rider.setDeltaMovement(stampede.getLookAngle().multiply(2, 0, 2).add(0, 1.2, 0));
                rider.hurtMarked = true;
                rider.hasImpulse = true;
            }

            stampede.level().broadcastEntityEvent(stampede, (byte)6);
        }

    }
}
