package org.celestialworkshop.behemoths.entities.misc;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.PartEntity;

public class BehemothMultipart<T extends Mob> extends PartEntity<Mob> {
    public final T parentMob;
    public final String name;
    public final EntityDimensions size;

    public BehemothMultipart(T parent, String name, float width, float height) {
        super(parent);
        size = EntityDimensions.scalable(width, height);
        refreshDimensions();
        parentMob = parent;
        this.name = name;
    }

    @Override
    public EntityDimensions getDimensions(Pose pPose) {
        return size;
    }

    @Override
    public InteractionResult interactAt(Player pPlayer, Vec3 pVec, InteractionHand pHand) {
        return super.interactAt(pPlayer, pVec, pHand);
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public boolean is(Entity pEntity) {
        return this == pEntity || parentMob == pEntity;
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (parentMob.getPassengers().contains(pSource.getEntity())) {
            return false;
        }
        return !isInvulnerableTo(pSource) && this.getParent().hurt(pSource, pAmount);
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {
    }

    @Override
    public boolean shouldBeSaved() {
        return false;
    }
}
