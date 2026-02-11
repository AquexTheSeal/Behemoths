package org.celestialworkshop.behemoths.config;


import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class BMCommonConfig {

    public final ForgeConfigSpec.DoubleValue pandemoniumVotingTimer;
    public final ForgeConfigSpec.BooleanValue enableResultsShowVoters;
    public final ForgeConfigSpec.IntValue maxPandemoniumCurses;
    public final ForgeConfigSpec.ConfigValue<List<? extends String>> pandemoniumVotingBlacklist;

    public final ForgeConfigSpec.ConfigValue<List<? extends String>> pandemoniumOnceAdvancements;
    public final ForgeConfigSpec.ConfigValue<List<? extends String>> pandemoniumRepeatAdvancements;
    public final ForgeConfigSpec.ConfigValue<List<? extends String>> pandemoniumOnceEntities;
    public final ForgeConfigSpec.ConfigValue<List<? extends String>> pandemoniumRepeatEntities;

    public BMCommonConfig(ForgeConfigSpec.Builder builder) {
        builder.push("Pandemonium Voting");

        {
            builder.push("Main Configuration");

            {
                pandemoniumVotingTimer = builder
                        .comment("Define how long a Pandemonium Voting session would last in seconds.")
                        .defineInRange("Pandemonium Voting Time", 120, 0, Double.MAX_VALUE);

                enableResultsShowVoters = builder
                        .comment("Should Pandemonium Voting Results display player names and what they voted for in chat?")
                        .comment("Warning: Disabling \"Display Pandemonium Voting Results in Chat\" in the client will render this config useless.")
                        .define("Include Pandemonium Voting Player Results in Chat", true);

                maxPandemoniumCurses = builder
                        .comment("How many Pandemonium Curses are allowed in a world?")
                        .comment("Note: [-1] means no curse limit.")
                        .comment("Warning: Reducing this value will NOT remove curses that are already active.")
                        .defineInRange("Maximum Pandemonium Curses", -1, -1, Integer.MAX_VALUE);

                pandemoniumVotingBlacklist = builder
                        .comment("Pandemonium Curses that shouldn't show up in the Pandemonium Voting.")
                        .comment("Format example: [\"behemoths:relentless\", \"behemoths:gravebreaker_momentum\"]")
                        .defineListAllowEmpty(
                                "Pandemonium Voting Curse Blacklist",
                                List.of(),
                                o -> o instanceof String && ResourceLocation.isValidResourceLocation((String) o)
                        );
            }

            builder.pop();

            builder.push("Voting Triggers");

            {
                this.pandemoniumOnceAdvancements = builder
                        .comment("Advancements that trigger Pandemonium Voting only once when earned by any player.")
                        .comment("Format example: [\"minecraft:story/mine_diamond\", \"minecraft:story/enter_the_nether\"]")
                        .defineListAllowEmpty(
                                "Pandemonium Voting: Once Advancements",
                                List.of(
                                        "minecraft:story/mine_diamond",
                                        "minecraft:story/enter_the_nether",
                                        "minecraft:story/enter_the_end",
                                        "minecraft:nether/netherite_armor",
                                        "minecraft:adventure/voluntary_exile",
                                        "minecraft:end/elytra"
                                ),
                                o -> o instanceof String && ResourceLocation.isValidResourceLocation((String) o)
                        );

                this.pandemoniumRepeatAdvancements = builder
                        .comment("Advancements that trigger Pandemonium Voting every time they are earned by any player.")
                        .comment("Format example: [\"minecraft:story/mine_diamond\", \"minecraft:story/enter_the_nether\"]")
                        .defineListAllowEmpty(
                                "Pandemonium Voting: Repeat Advancements",
                                List.of(
                                        "minecraft:end/kill_dragon"
                                ),
                                o -> o instanceof String && ResourceLocation.isValidResourceLocation((String) o)
                        );

                this.pandemoniumOnceEntities = builder
                        .comment("Entities that trigger Pandemonium Voting only the first time they are killed by any player.")
                        .comment("Format example: [\"minecraft:zombie\", \"minecraft:skeleton\"]")
                        .defineListAllowEmpty(
                                "Pandemonium Voting: Once Entities",
                                List.of(),
                                o -> o instanceof String && ResourceLocation.isValidResourceLocation((String) o)
                        );

                this.pandemoniumRepeatEntities = builder
                        .comment("Entities that trigger Pandemonium Voting every time they are killed by any player.")
                        .comment("Format example: [\"minecraft:zombie\", \"minecraft:skeleton\"]")
                        .defineListAllowEmpty(
                                "Pandemonium Voting: Repeat Entities",
                                List.of(),
                                o -> o instanceof String && ResourceLocation.isValidResourceLocation((String) o)
                        );
            }

            builder.pop();
        }

        builder.pop();
    }
}
