package org.aqutheseal.behemoths.capability;

import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.PacketDistributor;
import org.aqutheseal.behemoths.network.BMNetwork;
import org.aqutheseal.behemoths.network.packet.SyncFreezingPacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FreezingCapability implements INBTSerializable<CompoundTag> {
    public static final Capability<FreezingCapability> CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});

    private int freezeTime;
    private final Entity entity;

    public FreezingCapability(Entity entity) {
        this.entity = entity;
        this.freezeTime = 0;
    }

    public void setFreezeTime(int time) {
        this.freezeTime = time;
        if (!entity.level().isClientSide()) {
            BMNetwork.INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), new SyncFreezingPacket(entity.getId(), time));
        }
    }

    public int getFreezeTime() {
        return freezeTime;
    }

    public boolean isFreezing() {
        return freezeTime > 0;
    }

    public void tick() {
        if (entity == null) return;

        if (isFreezing()) {
            if (entity.isOnFire()) {
                setFreezeTime(0);
            }
            setFreezeTime(freezeTime - 1);
            entity.setDeltaMovement(entity.getDeltaMovement().scale(0.2F));
            if (entity.level().isClientSide) {
                for (int i = 0; i < 5; i++) {
                    double xa = entity.level().random.nextGaussian() * 0.05D * entity.getBbWidth();
                    double ya = entity.level().random.nextGaussian() * 0.05D * entity.getBbWidth();
                    double za = entity.level().random.nextGaussian() * 0.05D * entity.getBbWidth();
                    entity.level().addParticle(ParticleTypes.SNOWFLAKE, entity.getX(), entity.getEyeY(), entity.getZ(), xa, ya, za);
                }
            }
        }
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("FreezeTime", freezeTime);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        freezeTime = nbt.getInt("FreezeTime");
    }

    public static class Provider implements ICapabilityProvider {
        private final LazyOptional<FreezingCapability> instance;

        public Provider(Entity entity) {
            instance = LazyOptional.of(() -> new FreezingCapability(entity));
        }

        @NotNull
        @Override
        public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            return CAPABILITY.orEmpty(cap, instance);
        }
    }
}
