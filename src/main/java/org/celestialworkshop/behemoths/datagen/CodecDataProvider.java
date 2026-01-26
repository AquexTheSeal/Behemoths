package org.celestialworkshop.behemoths.datagen;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import org.celestialworkshop.behemoths.Behemoths;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public abstract class CodecDataProvider<T> implements DataProvider {
    protected final PackOutput output;
    protected final String directory;
    protected final Codec<T> codec;
    private final Map<ResourceLocation, T> entries = new HashMap<>();

    public CodecDataProvider(PackOutput output, Codec<T> codec, String directory) {
        this.output = output;
        this.codec = codec;
        this.directory = directory;
    }

    protected abstract void addEntries();

    protected void add(ResourceLocation location, T data) {
        if (entries.containsKey(location)) {
            throw new IllegalStateException("Duplicate entry: " + location);
        }
        entries.put(location, data);
    }

    protected void add(String path, T data) {
        add(Behemoths.prefix(path), data);
    }

    @Override
    public CompletableFuture<?> run(CachedOutput pOutput) {
        entries.clear();
        addEntries();

        List<CompletableFuture<?>> futures = new ArrayList<>();

        for (Map.Entry<ResourceLocation, T> entry : entries.entrySet()) {
            ResourceLocation location = entry.getKey();
            T data = entry.getValue();

            Path path = this.output.getOutputFolder(PackOutput.Target.DATA_PACK)
                    .resolve(location.getNamespace())
                    .resolve(directory)
                    .resolve(location.getPath() + ".json");

            futures.add(saveEntry(pOutput, data, path, location));
        }

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }

    private CompletableFuture<?> saveEntry(CachedOutput pOutput, T data, Path path, ResourceLocation location) {
        try {
            JsonElement jsonElement = codec.encodeStart(JsonOps.INSTANCE, data)
                    .getOrThrow(false, error -> Behemoths.LOGGER.error(
                            "Failed to encode {} for {}: {}",
                            directory,
                            location,
                            error
                    ));

            return DataProvider.saveStable(pOutput, jsonElement, path);
        } catch (Exception e) {
            Behemoths.LOGGER.error("Error saving {} for {}", directory, location, e);
            return CompletableFuture.completedFuture(null);
        }
    }

    @Override
    public String getName() {
        return "Codec Data Provider: " + directory;
    }
}
