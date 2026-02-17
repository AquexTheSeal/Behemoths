package org.celestialworkshop.behemoths.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import org.celestialworkshop.behemoths.world.savedata.WorldPandemoniumData;
import org.jetbrains.annotations.NotNull;

public class CurseScalableLootModifier extends LootModifier {

    public static final Codec<CurseScalableLootModifier> CODEC = RecordCodecBuilder.create(
            inst -> codecStart(inst).and(
                    inst.group(
                            ForgeRegistries.ITEMS.getCodec().fieldOf("item").forGetter(m -> m.item),
                            Codec.INT.optionalFieldOf("count", 1).forGetter(m -> m.count),
                            Codec.FLOAT.optionalFieldOf("base_chance", 0.0f).forGetter(m -> m.baseChance),
                            Codec.FLOAT.fieldOf("chance_per_curse").forGetter(m -> m.chancePerCurse),
                            Codec.FLOAT.optionalFieldOf("max_chance", 1.0f).forGetter(m -> m.maxChance)
                    )
            ).apply(inst, CurseScalableLootModifier::new)
    );

    private final Item item;
    private final int count;
    private final float baseChance;
    private final float chancePerCurse;
    private final float maxChance;

    public CurseScalableLootModifier(LootItemCondition[] conditions, Item item, int count, float baseChance, float chancePerCurse, float maxChance) {
        super(conditions);
        this.item = item;
        this.count = count;
        this.baseChance = baseChance;
        this.chancePerCurse = chancePerCurse;
        this.maxChance = maxChance;
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        ServerLevel level = context.getLevel();
        WorldPandemoniumData data = WorldPandemoniumData.get(level);
        int curseCount = data.getActivePandemoniumCurses().size();

        float totalChance = Math.min(baseChance + (curseCount * chancePerCurse), maxChance);

        if (context.getRandom().nextFloat() < totalChance) {
            generatedLoot.add(new ItemStack(item, count));
        }

        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}