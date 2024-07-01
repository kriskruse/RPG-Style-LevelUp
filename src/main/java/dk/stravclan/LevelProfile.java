package dk.stravclan;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LevelProfile {
    public static final Logger LOGGER = LoggerFactory.getLogger("rpg-style-leveling");

    private final Skill combatSkill = new CombatSkill();

    private final Map<Skill, Integer> skillLevels = new HashMap<Skill, Integer>();

    public LevelProfile(ServerPlayerEntity player){
        updateAllSkills(player);
    }

    public void updateAllSkills(ServerPlayerEntity player){
        skillLevels.replaceAll((s, v) -> s.level(player));
        for (Skill skill : skillLevels.keySet()) {
            LOGGER.info("Player has level {} in {}", skillLevels.get(skill), skill.name);
        }

    }

}
