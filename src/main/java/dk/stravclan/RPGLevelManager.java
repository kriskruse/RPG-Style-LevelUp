package dk.stravclan;

import net.minecraft.server.network.ServerPlayerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class RPGLevelManager {
    public static final Logger LOGGER = LoggerFactory.getLogger("rpg-style-leveling");
    private final Map<ServerPlayerEntity, LevelProfile> playerManager = new HashMap<ServerPlayerEntity, LevelProfile>();



    public RPGLevelManager(){}

    public void addPlayer(ServerPlayerEntity player){
        playerManager.put(player, new LevelProfile(player));

    }

    public void removePlayer(ServerPlayerEntity player){
        playerManager.remove(player);
    }

    public void updatePlayer(ServerPlayerEntity player){
        playerManager.get(player).updateAllSkills(player);
    }

    public void updatePlayers(){
        for (ServerPlayerEntity player : playerManager.keySet()) {
            updatePlayer(player);
            LOGGER.info("Updated player {}", player.getName().getString());
        }
    }

}
