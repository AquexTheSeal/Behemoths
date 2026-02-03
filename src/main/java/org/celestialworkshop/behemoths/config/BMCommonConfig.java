package org.celestialworkshop.behemoths.config;


import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class BMCommonConfig {

    public final ForgeConfigSpec.ConfigValue<List<? extends String>> pandemoniumOnceAdvancements;
    public final ForgeConfigSpec.ConfigValue<List<? extends String>> pandemoniumRepeatAdvancements;
    public final ForgeConfigSpec.ConfigValue<List<? extends String>> pandemoniumOnceEntities;
    public final ForgeConfigSpec.ConfigValue<List<? extends String>> pandemoniumRepeatEntities;

    public BMCommonConfig(ForgeConfigSpec.Builder builder) {
        builder.push("Main Configuration");

        this.pandemoniumOnceAdvancements = builder
                .comment("Advancements that trigger Pandemonium Voting only once.")
                .comment("Format example: [\"minecraft:story/mine_diamond\", \"minecraft:story/enter_the_nether\"]")
                .defineListAllowEmpty(
                        "Pandemonium: Once Advancements",
                        List.of(),
                        o -> o instanceof String && ResourceLocation.isValidResourceLocation((String) o)
                );

        this.pandemoniumRepeatAdvancements = builder
                .comment("Advancements that can be killed repeatedly to trigger Pandemonium Voting.")
                .comment("Format example: [\"minecraft:story/mine_diamond\", \"minecraft:story/enter_the_nether\"]")
                .defineListAllowEmpty(
                        "Pandemonium: Repeat Advancements",
                        List.of(),
                        o -> o instanceof String && ResourceLocation.isValidResourceLocation((String) o)
                );

        this.pandemoniumOnceEntities = builder
                .comment("Entities that trigger Pandemonium Voting when killed only for the first time.")
                .comment("Format example: [\"minecraft:zombie\", \"minecraft:skeleton\"]")
                .defineListAllowEmpty(
                        "Pandemonium: Once Entities",
                        List.of(),
                        o -> o instanceof String && ResourceLocation.isValidResourceLocation((String) o)
                );

        this.pandemoniumRepeatEntities = builder
                .comment("Entities that can be killed repeatedly to trigger Pandemonium Voting.")
                .comment("Format example: [\"minecraft:zombie\", \"minecraft:skeleton\"]")
                .defineListAllowEmpty(
                        "Pandemonium: Repeat Entities",
                        List.of(),
                        o -> o instanceof String && ResourceLocation.isValidResourceLocation((String) o)
                );

        builder.pop();
    }
}
