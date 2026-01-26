package org.celestialworkshop.behemoths.datagen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.celestialworkshop.behemoths.Behemoths;

import java.util.HashMap;
import java.util.Map;

public class CodecJsonListener<T> extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private final Codec<T> codec;
    private final String directory;
    private final Map<ResourceLocation, T> data = new HashMap<>();

    public CodecJsonListener(Codec<T> codec, String directory) {
        super(GSON, directory);
        this.codec = codec;
        this.directory = directory;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> pObject, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
        this.data.clear();

        pObject.forEach((resourceLocation, jsonElement) -> {
            try {
                T decoded = codec.parse(JsonOps.INSTANCE, jsonElement)
                        .getOrThrow(false, error -> Behemoths.LOGGER.error(
                                "Failed to parse {} from {}: {}",
                                directory,
                                resourceLocation,
                                error
                        ));

                this.data.put(resourceLocation, decoded);
                Behemoths.LOGGER.debug("Loaded {} from {}", directory, resourceLocation);
            } catch (Exception e) {
                Behemoths.LOGGER.error("Error loading {} from {}", directory, resourceLocation, e);
            }
        });

        Behemoths.LOGGER.info("Loaded {} {} entries", this.data.size(), directory);
    }

    public Map<ResourceLocation, T> getData() {
        return Map.copyOf(data);
    }

    public T get(ResourceLocation location) {
        return data.get(location);
    }

    public boolean contains(ResourceLocation location) {
        return data.containsKey(location);
    }

    /**
     * Clears all loaded data.
     */
    public void clear() {
        this.data.clear();
    }
}
