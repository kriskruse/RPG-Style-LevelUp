package dk.stravclan;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class RPGStyleLeveling implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("rpg-style-leveling");
	public long tick = 0;

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		LOGGER.info("Hello from RPG Style Leveling!");

		ServerLifecycleEvents.SERVER_STARTED.register(server -> {
			// This code will run when the server starts.

		});

		ServerTickEvents.START_SERVER_TICK.register(server -> {
			// This code will run every tick on the server.
			tick += 1;
			if (tick % 20 == 0) {
				// This code will run once every second.
				LOGGER.info("One second has passed!");
			}
			if (tick % 200 == 0) {
				// This code will run once every ten seconds.
				updatePlayers(server);
			}

		});

		ServerEntityEvents.ENTITY_LOAD.register((entity, serverWorld) -> {
			// This code will run every time an entity is loaded.
			if (entity instanceof ServerPlayerEntity) {
				// This code will run every time a player joins the server.
				LOGGER.info("Player joined: {}", entity.getName());
			}

		});



		LOGGER.info("RPG Style Leveling has been initialized!");
	}

	private void updatePlayers(MinecraftServer server) {
		List<ServerPlayerEntity> players = server.getPlayerManager().getPlayerList();
		for (ServerPlayerEntity player : players) {
			// This code will run once every ten seconds for each player.
            LOGGER.info("Player {} is online!", player.getName().getString());
		}
	}


}

