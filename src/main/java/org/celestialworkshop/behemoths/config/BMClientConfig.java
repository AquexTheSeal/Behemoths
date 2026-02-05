package org.celestialworkshop.behemoths.config;


import net.minecraftforge.common.ForgeConfigSpec;

public class BMClientConfig {

    public final ForgeConfigSpec.BooleanValue enableVotingMusic;
    public final ForgeConfigSpec.IntValue votingScreenParticlesMin;
    public final ForgeConfigSpec.IntValue votingScreenParticlesMax;

    public BMClientConfig(ForgeConfigSpec.Builder builder) {
        builder.push("Pandemonium Voting");

        enableVotingMusic = builder
                .comment("Should the custom music be played during Pandemonium Voting?")
                .define("Enable Voting Music", true);

        votingScreenParticlesMin = builder
                .comment("How many particles in minimum should pop up in the background of the Pandemonium Voting screens?")
                .defineInRange("Minimum Voting Screen Particles", 2, 0, Integer.MAX_VALUE);

        votingScreenParticlesMax = builder
                .comment("How many particles in maximum should pop up in the background of the Pandemonium Voting screens?")
                .defineInRange("Maximum Voting Screen Particles", 10, 0, Integer.MAX_VALUE);

        builder.pop();
    }
}
