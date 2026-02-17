package org.celestialworkshop.behemoths.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.registries.IForgeRegistry;
import org.celestialworkshop.behemoths.Behemoths;
import org.celestialworkshop.behemoths.datagen.loot.BMBlockLoot;
import org.celestialworkshop.behemoths.datagen.loot.BMChestLoot;
import org.celestialworkshop.behemoths.datagen.loot.BMEntityLoot;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class BMLootTableProvider extends LootTableProvider {

    public BMLootTableProvider(PackOutput output) {
        super(output, Set.of(), List.of(
                new SubProviderEntry(BMEntityLoot::new, LootContextParamSets.ENTITY),
                new SubProviderEntry(BMBlockLoot::new, LootContextParamSets.BLOCK),
                new SubProviderEntry(BMChestLoot::new, LootContextParamSets.CHEST)
        ));
    }

    public static <T> Set<T> knownSet(final IForgeRegistry<T> registry) {
        return StreamSupport
                .stream(registry.spliterator(), false)
                .filter(entry -> Optional.ofNullable(registry.getKey(entry))
                        .filter(key -> key.getNamespace().equals(Behemoths.MODID))
                        .isPresent()).collect(Collectors.toSet());
    }
}
