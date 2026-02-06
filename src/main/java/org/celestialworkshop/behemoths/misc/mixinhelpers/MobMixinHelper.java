package org.celestialworkshop.behemoths.misc.mixinhelpers;

import it.unimi.dsi.fastutil.Pair;
import org.celestialworkshop.behemoths.entities.ai.BMEntity;

public interface MobMixinHelper {

    void bm$setDamageModifier(Pair<Float, BMEntity.Operation> modifier);

    Pair<Float, BMEntity.Operation> bm$getDamageModifier();
}
