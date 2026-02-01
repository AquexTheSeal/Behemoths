package org.celestialworkshop.behemoths.registries;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import org.celestialworkshop.behemoths.api.item.specialty.ItemSpecialtyCapability;

public class BMCapabilities {

    public static final Capability<ItemSpecialtyCapability> ITEM_SPECIALTY = CapabilityManager.get(new CapabilityToken<>(){});
}
