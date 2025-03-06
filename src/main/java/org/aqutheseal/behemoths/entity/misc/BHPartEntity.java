package org.aqutheseal.behemoths.entity.misc;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.PartEntity;
import org.aqutheseal.behemoths.entity.SkyCharydbis;

public class BHPartEntity<T extends Mob> extends PartEntity<Mob> {
    public final T parentMob;
    public final String name;
    public final EntityDimensions size;

    public BHPartEntity(T parent, String name, float width, float height) {
        super(parent);
        size = EntityDimensions.scalable(width, height);
        refreshDimensions();
        parentMob = parent;
        this.name = name;
    }

    @Override
    public EntityDimensions getDimensions(Pose pPose) {
        if (parentMob instanceof ISizableEntity sze) {
            return size.scale(sze.getSizeMultiplier());
        } else {
            return size;
        }
    }

    @Override
    public InteractionResult interactAt(Player pPlayer, Vec3 pVec, InteractionHand pHand) {
        if (parentMob instanceof SkyCharydbis test) {
            if (pPlayer.getMainHandItem().getItem() == Items.BEDROCK) {
                test.setSizeMultiplier(test.getSizeMultiplier() + 0.5F);
                return InteractionResult.SUCCESS;
            }
            if (pPlayer.getMainHandItem().getItem() == Items.WHITE_WOOL) {
                test.setSizeMultiplier(test.getSizeMultiplier() - 0.5F);
                return InteractionResult.SUCCESS;
            }
        }
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
        return !isInvulnerableTo(pSource) && this.getParent().hurt(pSource, pAmount);
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
}
