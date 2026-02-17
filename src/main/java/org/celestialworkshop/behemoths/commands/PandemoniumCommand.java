package org.celestialworkshop.behemoths.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import org.celestialworkshop.behemoths.api.pandemonium.PandemoniumCurse;
import org.celestialworkshop.behemoths.api.pandemonium.PandemoniumVoteResult;
import org.celestialworkshop.behemoths.registries.BMPandemoniumCurses;
import org.celestialworkshop.behemoths.api.pandemonium.PandemoniumVotingSystem;
import org.celestialworkshop.behemoths.world.savedata.WorldPandemoniumData;

public class PandemoniumCommand {

    private static final SuggestionProvider<CommandSourceStack> CURSE_SUGGESTIONS = (context, builder) ->
            SharedSuggestionProvider.suggestResource(
            BMPandemoniumCurses.REGISTRY.get().getKeys(),
            builder
    );

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {

        dispatcher.register(
                Commands.literal("pandemonium")
                        .then(Commands.literal("curse")
                                .then(Commands.literal("list")
                                        .executes(PandemoniumCommand::listCurses))

                                .then(Commands.literal("add")
                                        .requires(source -> source.hasPermission(2))
                                        .then(Commands.argument("curse", ResourceLocationArgument.id())
                                                .suggests(CURSE_SUGGESTIONS)
                                                .executes(PandemoniumCommand::addCurse)))

                                .then(Commands.literal("remove")
                                        .requires(source -> source.hasPermission(2))
                                        .then(Commands.argument("curse", ResourceLocationArgument.id())
                                                .suggests(CURSE_SUGGESTIONS)
                                                .executes(PandemoniumCommand::removeCurse)))

                                .then(Commands.literal("clear")
                                        .requires(source -> source.hasPermission(2))
                                        .executes(PandemoniumCommand::clearCurses))

                                .then(Commands.literal("addAll")
                                        .requires(source -> source.hasPermission(2))
                                        .executes(PandemoniumCommand::addAllCurses))
                        )

                        .then(Commands.literal("voting")
                                .then(Commands.literal("start")
                                        .requires(source -> source.hasPermission(2))
                                        .executes(PandemoniumCommand::startVoting))
                                .then(Commands.literal("stop")
                                        .requires(source -> source.hasPermission(2))
                                        .executes(PandemoniumCommand::stopVoting))
                                .then(Commands.literal("clearMarkedAdvancements")
                                        .requires(source -> source.hasPermission(2))
                                        .executes(PandemoniumCommand::clearMarkedAdvancements))
                                .then(Commands.literal("clearMarkedEntities")
                                        .requires(source -> source.hasPermission(2))
                                        .executes(PandemoniumCommand::clearMarkedEntities))
                        )
        );
    }

    private static int startVoting(CommandContext<CommandSourceStack> context) {
        ServerLevel level = context.getSource().getLevel();

        PandemoniumVoteResult result = PandemoniumVotingSystem.openPandemoniumSelection(level);
        if (result.isSuccess()) {
            context.getSource().sendSuccess(result::getMessage, true);
            return 1;
        } else {
            context.getSource().sendFailure(result.getMessage());
            return 0;
        }
    }

    private static int stopVoting(CommandContext<CommandSourceStack> context) {
        ServerLevel level = context.getSource().getLevel();

        PandemoniumVoteResult result = PandemoniumVotingSystem.endPandemoniumSelection(level);
        if (result.isSuccess()) {
            context.getSource().sendSuccess(result::getMessage, true);
            return 1;
        } else {
            context.getSource().sendFailure(result.getMessage());
            return 0;
        }
    }

    private static int clearMarkedAdvancements(CommandContext<CommandSourceStack> context) {
        ServerLevel level = context.getSource().getLevel();

        WorldPandemoniumData.get(level).clearAdvancementTriggeredVoting();
        context.getSource().sendSuccess(() -> Component.literal("Stopped voting"), true);
        return 1;
    }

    private static int clearMarkedEntities(CommandContext<CommandSourceStack> context) {
        ServerLevel level = context.getSource().getLevel();

        WorldPandemoniumData.get(level).clearEntityTriggeredVoting();
        context.getSource().sendSuccess(() -> Component.literal("Stopped voting"), true);
        return 1;
    }

    private static int addCurse(CommandContext<CommandSourceStack> context) {
        ResourceLocation id = ResourceLocationArgument.getId(context, "curse");
        PandemoniumCurse curse = BMPandemoniumCurses.REGISTRY.get().getValue(id);

        if (curse == null) {
            context.getSource().sendFailure(Component.literal("Unknown curse - " + id));
            return 0;
        }

        ServerLevel level = context.getSource().getLevel();
        WorldPandemoniumData data = WorldPandemoniumData.get(level);

        if (data.getActivePandemoniumCurses().contains(curse)) {
            context.getSource().sendFailure(Component.literal("Curse '" + curse.getDisplayName().getString() + "' is already active!"));
            return 0;
        }

        data.addActivePandemoniumCurse(curse);
        context.getSource().sendSuccess(() -> Component.literal("Added curse: " + curse.getDisplayName().getString()), true);
        return 1;
    }

    private static int addAllCurses(CommandContext<CommandSourceStack> context) {
        ServerLevel level = context.getSource().getLevel();
        WorldPandemoniumData data = WorldPandemoniumData.get(level);

        BMPandemoniumCurses.REGISTRY.get().getValues().forEach(data::addActivePandemoniumCurse);
        context.getSource().sendSuccess(() -> Component.literal("Added all curses!"), true);
        return 1;
    }

    private static int removeCurse(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ResourceLocation id = ResourceLocationArgument.getId(context, "curse");
        PandemoniumCurse curse = BMPandemoniumCurses.REGISTRY.get().getValue(id);

        if (curse == null) {
            context.getSource().sendFailure(Component.literal("Unknown curse - " + id));
            return 0;
        }

        ServerLevel level = context.getSource().getLevel();
        WorldPandemoniumData data = WorldPandemoniumData.get(level);

        if (!data.getActivePandemoniumCurses().contains(curse)) {
            context.getSource().sendFailure(Component.literal("Curse '" + curse.getDisplayName().getString() + "' is not active!"));
            return 0;
        }

        data.removeActivePandemoniumCurse(curse);
        context.getSource().sendSuccess(() -> Component.literal("Removed curse: " + curse.getDisplayName().getString()), true);
        return 1;
    }

    private static int clearCurses(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerLevel level = context.getSource().getLevel();
        WorldPandemoniumData data = WorldPandemoniumData.get(level);

        int count = data.getActivePandemoniumCurses().size();
        data.clearActivePandemoniumCurses();
        context.getSource().sendSuccess(() -> Component.literal("Cleared " + count + " active curse(s)"), true);
        return count;
    }

    private static int listCurses(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerLevel level = context.getSource().getLevel();
        WorldPandemoniumData data = WorldPandemoniumData.get(level);

        if (data.getActivePandemoniumCurses().isEmpty()) {
            context.getSource().sendSuccess(() -> Component.literal("No active curses"), false);
            return 0;
        }

        context.getSource().sendSuccess(() -> Component.literal("Active Pandemonium Curses:"), false);
        for (PandemoniumCurse curse : data.getActivePandemoniumCurses()) {
            ResourceLocation curseId = curse.getId();
            context.getSource().sendSuccess(() -> Component.literal("  - " + curse.getDisplayName().getString() + " (" + curseId + ")"), false);
        }

        return data.getActivePandemoniumCurses().size();
    }
}
