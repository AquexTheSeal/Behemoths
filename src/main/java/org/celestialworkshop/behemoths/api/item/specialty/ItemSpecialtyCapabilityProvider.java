package org.celestialworkshop.behemoths.api.item.specialty;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.celestialworkshop.behemoths.registries.BMCapabilities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemSpecialtyCapabilityProvider implements ICapabilitySerializable<CompoundTag> {

    private final ItemSpecialtyCapability handler = new ItemSpecialtyCapability();
    private final LazyOptional<ItemSpecialtyCapability> lazyHandler = LazyOptional.of(() -> handler);

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return BMCapabilities.ITEM_SPECIALTY.orEmpty(cap, lazyHandler);
    }

    @Override
    public CompoundTag serializeNBT() {
        return handler.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        handler.deserializeNBT(nbt);
    }

    public void invalidate() {
        lazyHandler.invalidate();
    }
}
