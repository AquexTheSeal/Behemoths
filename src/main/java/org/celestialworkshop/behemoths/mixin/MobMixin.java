package org.celestialworkshop.behemoths.mixin;

import it.unimi.dsi.fastutil.Pair;
import net.minecraft.world.entity.Mob;
import org.celestialworkshop.behemoths.entities.ai.BMEntity;
import org.celestialworkshop.behemoths.misc.utils.mixinhelpers.MobMixinHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Mob.class)
public class MobMixin implements MobMixinHelper {

    @Unique private Pair<Float, BMEntity.Operation> bm$damageModifier;

    @ModifyVariable(
            method = "doHurtTarget",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z",
                    shift = At.Shift.BEFORE
            ),
            ordinal = 0
    )
    private float bm$modifyDamage(float damage) {
        float newDamage = damage;
        if (this.bm$getDamageModifier() != null) {
            float modifier = this.bm$getDamageModifier().first();
            BMEntity.Operation operation = this.bm$getDamageModifier().second();

            switch (operation) {
                case ADD -> newDamage += modifier;
                case MULTIPLY -> newDamage *= modifier;
            }
        }
        return newDamage;
    }

    @Override
    public void bm$setDamageModifier(Pair<Float, BMEntity.Operation> modifier) {
        this.bm$damageModifier = modifier;
    }

    @Override
    public Pair<Float, BMEntity.Operation> bm$getDamageModifier() {
        return this.bm$damageModifier;
    }
}
