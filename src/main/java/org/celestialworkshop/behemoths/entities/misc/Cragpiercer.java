package org.celestialworkshop.behemoths.entities.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.celestialworkshop.behemoths.Behemoths;
import org.celestialworkshop.behemoths.api.camera.CragpiercerCameraManager;
import org.celestialworkshop.behemoths.entities.ai.BMEntity;
import org.celestialworkshop.behemoths.network.BMNetwork;
import org.celestialworkshop.behemoths.network.s2c.CragpiercerExitPacket;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class Cragpiercer extends LivingEntity implements BMEntity {

    public static final EntityDataAccessor<Optional<UUID>> CONTROLLING_PLAYER_UUID = SynchedEntityData.defineId(Cragpiercer.class, EntityDataSerializers.OPTIONAL_UUID);

    public byte xRotInput;
    public byte yRotInput;
    public boolean shootInput;

    public int shootTime;

    public Cragpiercer(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide && this.isControlled()) {
            float speed = 2.0F;
            if (yRotInput != 0) {
                this.setYHeadRot(this.getYHeadRot() + (yRotInput * speed));
            }
            if (xRotInput != 0) {
                this.setXRot(Mth.clamp(this.getXRot() + (xRotInput * speed), -80F, 45F));
            }

            Behemoths.LOGGER.debug("Distance: " + this.distanceTo(this.getControllingPlayer()));
            if (this.distanceTo(this.getControllingPlayer()) > 4) {
                this.exitController();
            }
        }

        if (this.isControlled() && shootInput) {
            if (shootTime < 100) shootTime++;
        } else {
            shootTime = 0;
        }
    }

    public void handleServerKeyInput(byte action) {
        switch (action) {
            case 0 -> this.exitController();
            case 1 -> this.shootInput = false;
            case 2 -> this.shootInput = true;
        }
    }

    private void exitController() {
        if (this.getControllingPlayer() instanceof ServerPlayer serverPlayer) {
            BMNetwork.sendToPlayer(serverPlayer, new CragpiercerExitPacket());
        }
        this.entityData.set(CONTROLLING_PLAYER_UUID, Optional.empty());
        this.xRotInput = 0;
        this.yRotInput = 0;
        this.shootInput = false;
    }

    @Override
    public InteractionResult interact(Player pPlayer, InteractionHand pHand) {
        if (!this.isControlled()) {

            if (!this.level().isClientSide) {
                this.setControllingPlayerUUID(pPlayer.getUUID());
            } else {
                CragpiercerCameraManager.enterBallistaCamera(this);
            }

            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        return super.interact(pPlayer, pHand);
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        this.kill();
        return false;
    }

    public void kill() {
        this.remove(RemovalReason.KILLED);
        this.gameEvent(GameEvent.ENTITY_DIE);
    }

    @Override
    public void setDeltaMovement(Vec3 pDeltaMovement) {
        super.setDeltaMovement(new Vec3(0, -0.5F, 0));
    }

    public boolean isControlled() {
        return this.getControllingPlayer() != null;
    }

    public boolean isControlledBy(Player compare) {
        if (this.getControllingPlayerUUID() != null) {
            return this.getControllingPlayerUUID().equals(compare.getUUID());
        }
        return false;
    }

    public @Nullable Player getControllingPlayer() {
        UUID uuid = this.getControllingPlayerUUID();
        if (uuid != null) {
            return this.level().getPlayerByUUID(uuid);
        }
        return null;
    }

    public @Nullable UUID getControllingPlayerUUID() {
        return this.entityData.get(CONTROLLING_PLAYER_UUID).orElse(null);
    }

    public void setControllingPlayerUUID(UUID player) {
        this.entityData.set(CONTROLLING_PLAYER_UUID, Optional.of(player));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(CONTROLLING_PLAYER_UUID, Optional.empty());
    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
        this.moveTo(Vec3.atBottomCenterOf(this.blockPosition().above()));
    }

    public boolean isPushable() {
        return false;
    }

    protected void doPush(Entity pEntity) {
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return List.of();
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot pSlot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot pSlot, ItemStack pStack) {
    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    public boolean attackable() {
        return false;
    }

    public boolean isAffectedByPotions() {
        return false;
    }

    @Override
    protected void checkFallDamage(double pY, boolean pOnGround, BlockState pState, BlockPos pPos) {
    }
}
