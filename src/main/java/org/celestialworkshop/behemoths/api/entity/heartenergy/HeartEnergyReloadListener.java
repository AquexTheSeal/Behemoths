package org.celestialworkshop.behemoths.api.entity.heartenergy;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import org.celestialworkshop.behemoths.Behemoths;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Reader;
import java.util.Map;

public class HeartEnergyReloadListener extends SimplePreparableReloadListener<Object2IntArrayMap<ResourceLocation>> {

    public static final Object2IntArrayMap<ResourceLocation> VALUES = new Object2IntArrayMap<>();
    public static final String FILENAME = "behemoths_heart_energy.json";

    @Override
    @NotNull
    protected Object2IntArrayMap<ResourceLocation> prepare(ResourceManager resourceManager, ProfilerFiller profiler) {
        VALUES.clear();

        for (String namespace : resourceManager.getNamespaces()) {
            for (Resource resource : resourceManager.getResourceStack(ResourceLocation.fromNamespaceAndPath(namespace, FILENAME))) {
                try (Reader reader = resource.openAsReader()) {
                    JsonObject json = GsonHelper.parse(reader);

                    for (Map.Entry<String, JsonElement> e : json.entrySet()) {
                        ResourceLocation entityId;
                        try {
                            entityId = ResourceLocation.parse(e.getKey());
                        } catch (IllegalArgumentException ex) {
                            Behemoths.LOGGER.warn("Invalid entity ID {} for Heart Energy in namespace {}, skipping", e.getKey(), namespace);
                            continue;
                        }

                        int value = GsonHelper.convertToInt(e.getValue(), e.getKey());

                        if (VALUES.containsKey(entityId)) {
                            Behemoths.LOGGER.warn("Heart energy value for {} overridden by {}", entityId, namespace);
                        }

                        VALUES.put(entityId, value);
                    }

                } catch (IllegalArgumentException | IOException | JsonParseException ex) {
                    Behemoths.LOGGER.error("Failed to load heart energy values from namespace {}", namespace, ex);
                }
            }
        }

        Behemoths.LOGGER.info("Loaded {} heart energy values.", VALUES.size());

        return VALUES;
    }

    @Override
    protected void apply(Object2IntArrayMap<ResourceLocation> pObject, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
    }
}

