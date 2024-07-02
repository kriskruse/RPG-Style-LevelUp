package dk.stravclan;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Map;

public class RPGCommandManager {

    public static void rpg(ServerPlayerEntity player, RPGLevelManager lvlManager) {
        LevelProfile profile = lvlManager.getLevelProfile(player);
        Map<Skill, Integer> skillLevels = profile.getSkillLevels();
        StringBuilder message = new StringBuilder();
        for (Skill skill : skillLevels.keySet()) {
            message.append(skill.name)
                    .append(": ")
                    .append(skillLevels.get(skill))
                    .append("    XP: ")
                    .append(Math.floor(skill.xp))
                    .append("/")
                    .append(Math.floor(skill.nextLevelReq))
                    .append("\n");
        }
        player.sendMessage(Text.of(message.toString()), false);
    }

    public static void resetCommand(ServerPlayerEntity player, RPGLevelManager lvlManager) {
        lvlManager.resetPlayer(player);
    }
}
