package org.celestialworkshop.behemoths.entities.misc;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class AutomatedAttackEntity extends Entity implements TraceableEntity {

    public LivingEntity owner;
    private AttackBehavior behavior;
    private int lifetime;
    private boolean hasStarted = false;

    public AutomatedAttackEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public void setBehavior(LivingEntity owner, int lifetime, AttackBehavior behavior) {
        this.setOwner(owner);
        this.lifetime = lifetime;
        this.behavior = behavior;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {

            if (behavior == null || this.getOwner() == null) {
                this.discard();
                return;
            }

            if (!this.hasStarted) {
                behavior.onStart(this);
                this.hasStarted = true;
            }

            behavior.onTick(this);

            if (this.tickCount >= lifetime) {
                behavior.onEnd(this);
                this.discard();
            }
        }
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
    public @Nullable LivingEntity getOwner() {
        return owner;
    }

    public void setOwner(LivingEntity entity) {
        this.owner = entity;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public interface AttackBehavior {
        default void onStart(AutomatedAttackEntity entity) {
        }

        default void onTick(AutomatedAttackEntity entity) {
        }

        default void onEnd(AutomatedAttackEntity entity) {
        }
    }
}
