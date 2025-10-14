package org.millenaire;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.server.command.EnumArgument;

public class MillCommand {
    
    public static LiteralArgumentBuilder<CommandSourceStack> getCommand() {
        return Commands.literal("mill")
            .then(Commands.literal("villages")
                .executes(context -> {
                    context.getSource().sendSuccess(() -> 
                        Component.literal("Liste des villages"), false);
                    // Implémentation à compléter
                    return Command.SINGLE_SUCCESS;
                })
            )
            .then(Commands.literal("loneBuildings")
                .executes(context -> {
                    context.getSource().sendSuccess(() -> 
                        Component.literal("Bâtiments solitaires"), false);
                    return Command.SINGLE_SUCCESS;
                })
            )
            .then(Commands.literal("showBuildPoints")
                .executes(context -> {
                    context.getSource().sendSuccess(() -> 
                        Component.literal("Points de construction"), false);
                    return Command.SINGLE_SUCCESS;
                })
            )
            .executes(context -> {
                context.getSource().sendSuccess(() -> 
                    Component.literal("Usage: /mill <villages|loneBuildings|showBuildPoints>"), false);
                return Command.SINGLE_SUCCESS;
            });
    }
}