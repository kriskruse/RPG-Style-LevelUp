package dk.stravclan;

import net.minecraft.server.network.ServerPlayerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class RPGLevelManager {
    public static final Logger LOGGER = LoggerFactory.getLogger(Constants.MOD_ID);
    private final Map<ServerPlayerEntity, LevelProfile> playerLevelManager = new HashMap<>();


    public RPGLevelManager(){}

    public void addPlayer(ServerPlayerEntity player){
        playerLevelManager.put(player, new LevelProfile(player));

    }

    public void removePlayer(ServerPlayerEntity player){
        playerLevelManager.remove(player);
    }

    private void updatePlayerEffects(ServerPlayerEntity player) {
        playerLevelManager.get(player).updateAllEffects(player);
    }

    public void updatePlayerLevel(ServerPlayerEntity player){
        playerLevelManager.get(player).updateAllSkills(player);
    }

    public void updatePlayers(){
        for (ServerPlayerEntity player : playerLevelManager.keySet()) {
            updatePlayerLevel(player);
            updatePlayerEffects(player);
        }
    }

    public LevelProfile getLevelProfile(ServerPlayerEntity player){
        updatePlayerLevel(player);
        updatePlayerEffects(player);
        return playerLevelManager.get(player);
    }

    public void resetPlayer(ServerPlayerEntity player){
        playerLevelManager.put(player, new LevelProfile(player));
        updatePlayerLevel(player);
        updatePlayerEffects(player);
    }

    public void changeEffectTargetPlayer(ServerPlayerEntity player, String effect, int i) {
        playerLevelManager.get(player).changeEffectTarget(effect, i);
        updatePlayerEffects(player);
    }


}
