package org.celestialworkshop.behemoths.entities.misc;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.celestialworkshop.behemoths.api.client.animation.EntityAnimationManager;
import org.celestialworkshop.behemoths.api.entity.ActionManager;
import org.celestialworkshop.behemoths.client.animations.PhantashroomGluttonAnimations;
import org.celestialworkshop.behemoths.entities.SkyCharydbis;
import org.celestialworkshop.behemoths.entities.ai.BMEntity;
import org.celestialworkshop.behemoths.entities.ai.action.PhantashroomGluttonOpenAction;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PhantashroomGlutton extends LivingEntity implements BMEntity, OwnableEntity {

    public final EntityAnimationManager animationManager = new EntityAnimationManager(this);
    public final ActionManager<PhantashroomGlutton> idleManager = new ActionManager<>(this);

    public static final String OPEN_ANIMATION = "open_animation";
    public static final String CLOSE_ANIMATION = "close_animation";

    public Optional<UUID> ownerUUID = Optional.empty();
    public List<Phantom> summonedPhantoms = new ObjectArrayList<>();
    public int openTimer;

    public PhantashroomGlutton(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.animationManager.registerAnimation(OPEN_ANIMATION, () -> PhantashroomGluttonAnimations.OPEN);
        this.animationManager.registerAnimation(CLOSE_ANIMATION, () -> PhantashroomGluttonAnimations.CLOSE);

        this.idleManager.addAction(new PhantashroomGluttonOpenAction(this));

        openTimer = -500 - getRandom().nextInt(500);
    }

    @Override
    public EntityAnimationManager getAnimationManager() {
        return animationManager;
    }

    @Override
    public List<ActionManager> getActionManagers() {
        return List.of(idleManager);
    }

    @Override
    public void tick() {
        if (this.getOwner() != null) {
            if (this.getOwner().isDeadOrDying() || this.getOwner().isRemoved() || (this.getOwner() instanceof SkyCharydbis ch && ch.isSleeping())) {
                this.kill();
            }
        }

        this.summonedPhantoms.removeIf(p -> p.isRemoved() || p.isDeadOrDying());

        super.tick();
        this.setXRot(0);
        this.setYRot(0);
        this.setYBodyRot(0);

        double centerX = Math.floor(this.getX()) + 0.5;
        double centerZ = Math.floor(this.getZ()) + 0.5;
        this.moveTo(centerX, this.getY(), centerZ, this.getYRot(), this.getXRot());
        this.setDeltaMovement(0, this.getDeltaMovement().y, 0);

        if (!level().isClientSide && this.canOpen()) {
            openTimer++;
        }
    }

    public boolean canOpen() {
        if (this.getOwner() instanceof SkyCharydbis sc) {
            return sc.isCurrentSleepFlag(SkyCharydbis.AWAKE_FLAG) && sc.getTarget() != null;
        }
        return true;
    }

    public void onMouthOpened() {
        if (this.summonedPhantoms.size() <= 3) {
            Phantom phantom = EntityType.PHANTOM.spawn((ServerLevel) level(), blockPosition().above(), MobSpawnType.MOB_SUMMONED);
            if (phantom != null) {
                phantom.setDeltaMovement(0, 2, 0);
                this.summonedPhantoms.add(phantom);
            }
        }
    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
        this.animationManager.startAnimation(CLOSE_ANIMATION);
    }

    @Override
    public void knockback(double pStrength, double pX, double pZ) {
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    protected void tickDeath() {
        if (!this.level().isClientSide() && !this.isRemoved()) {
            this.level().broadcastEntityEvent(this, (byte)60);
            this.remove(Entity.RemovalReason.KILLED);
        }
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return List.of(ItemStack.EMPTY);
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

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        this.ownerUUID.ifPresent(id -> pCompound.putUUID("owner", id));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (pCompound.contains("owner")) {
            this.ownerUUID = Optional.of(pCompound.getUUID("owner"));
        }
    }

    @Override
    public @Nullable UUID getOwnerUUID() {
        return this.ownerUUID.orElse(null);
    }

    public @Nullable LivingEntity getOwner() {
        if (level() instanceof ServerLevel server && this.getOwnerUUID() != null) {
            Entity fromUUID = server.getEntity(this.getOwnerUUID());
            if (fromUUID instanceof LivingEntity living) {
                return living;
            }
        }
        return null;
    }

    public void setOwner(@Nullable LivingEntity owner) {
        if (owner != null) {
            this.ownerUUID = Optional.of(owner.getUUID());
        }
    }
}
