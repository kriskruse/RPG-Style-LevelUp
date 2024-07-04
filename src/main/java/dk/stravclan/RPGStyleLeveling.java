package dk.stravclan;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class RPGStyleLeveling implements ModInitializer {
    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger(Constants.MOD_ID);
    public long tick = 0;

    RPGLevelManager levelManager = new RPGLevelManager();
    RPGUiManager uiManager = new RPGUiManager();

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.
        LOGGER.info("Hello from RPG Style Leveling!");
        LOGGER.info("Initializing...");

        ServerTickEvents.START_SERVER_TICK.register(server -> {
            // This code will run every tick on the server.
            // 20 tick is equal to 1 second
            tick += 1;
            if (tick % 200 == 0) {
                levelManager.updatePlayers();
                //uiManager.updatePlayers();
            }
        });

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            // This code will run every time a player joins the server.
            ServerPlayerEntity player = handler.player;
            levelManager.addPlayer(player);
            uiManager.addPlayer(player);
            //LOGGER.info("Player {} has joined the server!", player.getName().getString());
        });

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(literal("rpg")
                    .executes(context -> {
                                ServerPlayerEntity player = context.getSource().getPlayer();
                                RPGCommandManager.rpg(player, levelManager);
                                return 1;
                            }
                    ));
        });

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(literal("rpg")
                    .then(literal("status")
                            .executes(context -> {
                                        ServerPlayerEntity player = context.getSource().getPlayer();
                                        RPGCommandManager.rpg(player, levelManager);
                                        return 1;
                                    }
                            )
                    ));
        });

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(literal("rpg")
                    .then(literal("reset")
                            .executes(context -> {
                                        ServerPlayerEntity player = context.getSource().getPlayer();
                                        RPGCommandManager.resetCommand(player, levelManager);
                                        return 1;
                                    }
                            )
                    ));
        });

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(literal("rpg")
                    .then(literal("changeLevel")
                            .then(argument("skill", StringArgumentType.string())
                                    .then(argument("amount", IntegerArgumentType.integer()).executes(context -> {
                                        int val =  RPGCommandManager.changeEffectLevel(
                                                context,
                                                levelManager,
                                                StringArgumentType.getString(context, "skill"),
                                                IntegerArgumentType.getInteger(context, "amount"));
                                        RPGCommandManager.rpg(context.getSource().getPlayer(), levelManager);
                                        return val;
                                    })))));
        });


        LOGGER.info("RPG Style Leveling has been initialized!");
    }
}

