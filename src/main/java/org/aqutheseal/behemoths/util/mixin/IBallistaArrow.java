package org.aqutheseal.behemoths.util.mixin;

import net.minecraft.world.phys.HitResult;

import java.util.function.Consumer;

public interface IBallistaArrow {

    float behemoths$getBehemothDamageMultiplier();

    void behemoths$setBehemothDamageMultiplier(float value);

    Consumer<HitResult> behemoths$getOnCollide();

    void behemoths$setOnCollide(Consumer<HitResult> value);
}
