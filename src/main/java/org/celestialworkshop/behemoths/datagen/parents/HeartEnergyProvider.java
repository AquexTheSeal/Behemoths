package org.celestialworkshop.behemoths.datagen.parents;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;
import org.celestialworkshop.behemoths.Behemoths;
import org.celestialworkshop.behemoths.api.entity.heartenergy.HeartEnergyReloadListener;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public abstract class HeartEnergyProvider implements DataProvider {

    protected final Object2IntArrayMap<ResourceLocation> data = new Object2IntArrayMap<>();
    protected final PackOutput output;
    protected final String modid;

    protected HeartEnergyProvider(PackOutput output, String modid) {
        this.output = output;
        this.modid = modid;
    }

    protected abstract void addHeartEnergyValues();

    public void addEntry(EntityType<?> entityType, int value) {
        ResourceLocation key = ForgeRegistries.ENTITY_TYPES.getKey(entityType);
        if (key != null) {
            this.data.put(key, value);
        }
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        this.addHeartEnergyValues();

        if (data.isEmpty()) {
            return CompletableFuture.allOf();
        }

        JsonObject json = new JsonObject();
        data.forEach((entityId, value) -> {
            json.addProperty(entityId.toString(), value);
        });

        Path target = output.getOutputFolder(PackOutput.Target.DATA_PACK)
                .resolve(modid)
                .resolve(HeartEnergyReloadListener.FILENAME);

        return DataProvider.saveStable(cache, json, target);
    }

    @Override
    public String getName() {
        return "Heart Energy Provider: " + modid;
    }
}
