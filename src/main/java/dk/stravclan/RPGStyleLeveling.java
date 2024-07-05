package dk.stravclan;

import com.mojang.brigadier.arguments.FloatArgumentType;
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

    public static final Logger LOGGER = LoggerFactory.getLogger(Constants.MOD_ID);
    public long tick = 0;

    RPGPlayerLevelManager playerLevelManager = new RPGPlayerLevelManager();
    RPGUiManager uiManager = new RPGUiManager();


    @Override
    public void onInitialize() {
        LOGGER.info("Hello from RPG Style Leveling!");
        LOGGER.info("Initializing...");

        LOGGER.info("Loading settings...");
        Constants.setSkillDataMap(SettingsManager.loadSettings());

        LOGGER.info("Registering events...");
        ServerTickEvents.START_SERVER_TICK.register(server -> {
            // This code will run every tick on the server.
            // 20 tick is equal to 1 second
            tick += 1;
            if (tick % 200 == 0) {
                playerLevelManager.updatePlayers();
                //uiManager.updatePlayers();
            }
        });

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            // This code will run every time a player joins the server.
            ServerPlayerEntity player = handler.player;
            playerLevelManager.addPlayer(player);
            uiManager.addPlayer(player);
            //LOGGER.info("Player {} has joined the server!", player.getName().getString());
        });

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("rpg")
                .executes(context -> {
                            ServerPlayerEntity player = context.getSource().getPlayer();
                            RPGCommandManager.rpg(player, playerLevelManager);
                            return 1;
                        }
                )
                // "/rpg status" command
                .then(literal("status")
                        .executes(context -> {
                                    ServerPlayerEntity player = context.getSource().getPlayer();
                                    RPGCommandManager.rpg(player, playerLevelManager);
                                    return 1;
                                }
                        )
                )
                // "/rpg reset" command
                .then(literal("reset")
                        .executes(context -> {
                                    ServerPlayerEntity player = context.getSource().getPlayer();
                                    RPGCommandManager.resetCommand(player, playerLevelManager);
                                    return 1;
                                }
                        )
                )
                // "/rpg changeLevel skill amount" command
                .then(literal("changeLevel")
                        .then(argument("skill", StringArgumentType.string())
                                .then(argument("amount", IntegerArgumentType.integer()).executes(context -> {
                                    int val = RPGCommandManager.changeEffectLevel(
                                            context,
                                            playerLevelManager,
                                            StringArgumentType.getString(context, "skill"),
                                            IntegerArgumentType.getInteger(context, "amount"));
                                    RPGCommandManager.rpg(context.getSource().getPlayer(), playerLevelManager);
                                    return val;
                                })))
                )

        ));

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("rpg")
                .then(literal("setLevelOneRequired")
                        .requires(source -> source.hasPermissionLevel(2))
                        .then(argument("skill", StringArgumentType.string())
                                .then(argument("amount", FloatArgumentType.floatArg()).executes(context -> {
                                    int val = RPGCommandManager.setSkillLevelOneReq(
                                            context,
                                            StringArgumentType.getString(context, "skill"),
                                            FloatArgumentType.getFloat(context, "amount"),
                                            playerLevelManager);
                                    SettingsManager.saveSettings();
                                    return val;
                                }))
                        )
                )
                .then(literal("setModifier")
                        .requires(source -> source.hasPermissionLevel(2))
                        .then(argument("skill", StringArgumentType.string())
                                .then(argument("amount", FloatArgumentType.floatArg()).executes(context -> {
                                    int val = RPGCommandManager.setSkillLevelReqModifier(
                                            context,
                                            StringArgumentType.getString(context, "skill"),
                                            FloatArgumentType.getFloat(context, "amount"),
                                            playerLevelManager);
                                    SettingsManager.saveSettings();
                                    return val;
                                }))
                        )
                )
                .then(literal("resetAllPlayers")
                        .requires(source -> source.hasPermissionLevel(2))
                        .executes(context -> {
                            RPGCommandManager.resetAllPlayers(playerLevelManager);
                            return 1;
                        })

        ));


        LOGGER.info("RPG Style Leveling has been initialized!");
    }
}

