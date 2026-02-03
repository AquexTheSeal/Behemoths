package org.celestialworkshop.behemoths.advancmeents;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.celestialworkshop.behemoths.registries.BMAdvancementTriggers;

public class KillBehemothTrigger extends SimpleCriterionTrigger<KillBehemothTrigger.TriggerInstance> {
    final ResourceLocation id;

    public KillBehemothTrigger(ResourceLocation pId) {
        this.id = pId;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    protected TriggerInstance createInstance(JsonObject pJson, ContextAwarePredicate pPredicate, DeserializationContext pDeserializationContext) {
        return new TriggerInstance(id, pPredicate);
    }

    public void trigger(ServerPlayer player) {
        this.trigger(player, TriggerInstance::matches);
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        public TriggerInstance(ResourceLocation id, ContextAwarePredicate player) {
            super(id, player);
        }

        public boolean matches() {
            return true;
        }

        public static TriggerInstance any() {
            return new TriggerInstance(BMAdvancementTriggers.KILL_BEHEMOTH.getId(), ContextAwarePredicate.ANY);
        }
    }
}
