package org.celestialworkshop.behemoths.entities.ai.action;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.util.Mth;
import org.celestialworkshop.behemoths.api.entity.ManagedAction;
import org.celestialworkshop.behemoths.entities.SkyCharydbis;
import org.celestialworkshop.behemoths.entities.projectile.CharydbisShard;
import org.celestialworkshop.behemoths.registries.BMEntityTypes;
import org.celestialworkshop.behemoths.registries.BMSoundEvents;

import java.util.Collections;
import java.util.List;

public class CharydbisShardSummonAction extends ManagedAction<SkyCharydbis> {

    public CharydbisShardSummonAction(SkyCharydbis entity) {
        super(entity);
    }

    @Override
    public boolean canStart() {
        return entity.attackCooldown == 0 && entity.heldShards.isEmpty() && entity.getTarget() != null &&
                !entity.isSubmerged && entity.isCurrentSleepFlag(SkyCharydbis.AWAKE_FLAG);
    }

    @Override
    public boolean onTick() {
        ++timer;
        if (timer == 20) {
            entity.playSound(BMSoundEvents.CHARYDBIS_SHARD_SUMMON.get(), 3.0F, 1.0F);

            for (int j = 0; j < 3; j++) {
                for (int i = 0; i <= 360; i += 30) {
                    CharydbisShard shard = new CharydbisShard(BMEntityTypes.CHARYDBIS_SHARD.get(), entity.level());
                    shard.moveTo(entity.position());
                    shard.setOwner(entity);
                    shard.setControlled(true);
                    shard.controlledRotationOffset = i;
                    shard.controlledLayer = j;
                    shard.setStatusFlag(CharydbisShard.HOSTILE_STATUS);
                    entity.heldShards.add(shard);
                    entity.level().addFreshEntity(shard);
                }
            }

            List<CharydbisShard> copy = new ObjectArrayList<>(entity.heldShards);
            Collections.shuffle(copy);
            for (CharydbisShard shard : copy.subList(0, Math.min(5 + entity.getRandom().nextInt(5), copy.size()))) {
                shard.setStatusFlag(CharydbisShard.RETURNABLE_STATUS);
            }
        }
        return timer < 50;
    }

    @Override
    public void onStart() {
        timer = 0;
        entity.attackCooldown = Mth.ceil(300 * entity.getAttacksIntervalScale());
        entity.getAnimationManager().startAnimation(SkyCharydbis.SHARD_SUMMON_ANIMATION);
    }

    @Override
    public void onStop() {
        timer = 0;
        entity.getAnimationManager().stopAnimation(SkyCharydbis.SHARD_SUMMON_ANIMATION);
    }

    @Override
    public int getWeight() {
        return 150;
    }
}
