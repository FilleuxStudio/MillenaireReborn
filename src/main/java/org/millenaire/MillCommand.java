package org.millenaire;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import org.millenaire.common.blocks.MillBlocks;
import org.millenaire.common.blocks.StoredPosition;
import org.millenaire.common.building.BuildingTypes;

import java.util.Map;

public class MillCommand {
    
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("mill")
            .then(Commands.literal("village")
                .executes(MillCommand::listVillages))
            .then(Commands.literal("loneBuildings")
                .executes(MillCommand::listLoneBuildings))
            .then(Commands.literal("showBuildPoints")
                .executes(MillCommand::toggleBuildPoints))
            .executes(MillCommand::showUsage);
    }
    
    private static int showUsage(CommandContext<CommandSourceStack> context) {
        context.getSource().sendSuccess(() -> 
            Component.literal("Usage: /mill <villages|loneBuildings|showBuildPoints>"), false);
        return Command.SINGLE_SUCCESS;
    }
    
    private static int listVillages(CommandContext<CommandSourceStack> context) {
        // Spit out direction and distance to all villages
        
        // Test code - remove before command use
        for(Map.Entry<String, ?> entry : BuildingTypes.getCache().entrySet()) {
            context.getSource().sendSuccess(() -> 
                Component.literal(entry.getKey() + " - " + entry.getValue()), false);
        }
        
        return Command.SINGLE_SUCCESS;
    }
    
    private static int listLoneBuildings(CommandContext<CommandSourceStack> context) {
        // Spit out Distance and direction to all lone buildings
        context.getSource().sendSuccess(() -> 
            Component.literal("Lone buildings command not yet implemented"), false);
        return Command.SINGLE_SUCCESS;
    }
    
    private static int toggleBuildPoints(CommandContext<CommandSourceStack> context) {
        StoredPosition storedPos = (StoredPosition) MillBlocks.storedPosition;
        if (storedPos.getShowParticles()) {
            storedPos.setShowParticles(false);
            context.getSource().sendSuccess(() -> 
                Component.literal("Build points visualization disabled"), false);
        } else {
            storedPos.setShowParticles(true);
            context.getSource().sendSuccess(() -> 
                Component.literal("Build points visualization enabled"), false);
        }
        return Command.SINGLE_SUCCESS;
    }
    
    public static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(register());
    }
}