package org.aqutheseal.behemoths.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.entity.PartEntity;
import org.aqutheseal.behemoths.events.ModTriggers;
import org.aqutheseal.behemoths.registry.BMTags;
import org.aqutheseal.behemoths.util.mixin.IBallistaArrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(AbstractArrow.class)
public abstract class AbstractArrowMixin extends Projectile implements IBallistaArrow {

    @Shadow private double baseDamage;
    @Unique public float behemoths$behemothMultiplier = 0.0F;
    @Unique public Consumer<HitResult> behemoths$onCollide = null;

    protected AbstractArrowMixin(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "onHitEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/Vec3;length()D"))
    protected void modifyArrowDamage(EntityHitResult pResult, CallbackInfo ci) {
        boolean multipartCondition = pResult.getEntity() instanceof PartEntity<?> part && part.getParent().getType().is(BMTags.Entities.BEHEMOTHS);
        if (pResult.getEntity().getType().is(BMTags.Entities.BEHEMOTHS) || multipartCondition) {
            this.baseDamage *= this.behemoths$getBehemothDamageMultiplier();
        }
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/AbstractArrow;isCritArrow()Z"))
    protected void tick(CallbackInfo ci) {
        ModTriggers.tickBehemothArrow((AbstractArrow & IBallistaArrow) (Object) this);
    }

    @Inject(method = "onHitEntity", at = @At(value = "TAIL"))
    protected void conjureExplosionOnEntity(EntityHitResult pResult, CallbackInfo ci) {
        this.behemoths$conjureExplosion(pResult);
    }

    @Inject(method = "onHitBlock", at = @At(value = "TAIL"))
    protected void conjureExplosionOnBlock(BlockHitResult pResult, CallbackInfo ci) {
        this.behemoths$conjureExplosion(pResult);
    }

    @Unique
    private void behemoths$conjureExplosion(HitResult result) {
        if (this.behemoths$getOnCollide() != null) {
            behemoths$onCollide.accept(result);
        }
    }

    @Override
    public float behemoths$getBehemothDamageMultiplier() {
        return behemoths$behemothMultiplier;
    }

    @Override
    public void behemoths$setBehemothDamageMultiplier(float value) {
        this.behemoths$behemothMultiplier = value;
    }

    @Override
    public Consumer<HitResult> behemoths$getOnCollide() {
        return behemoths$onCollide;
    }

    @Override
    public void behemoths$setOnCollide(Consumer<HitResult> value) {
        this.behemoths$onCollide = value;
    }
}
