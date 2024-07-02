package dk.stravclan;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class LevelProfile {
    public static final Logger LOGGER = LoggerFactory.getLogger("rpg-style-leveling");

    private final Map<Skill, Integer> skillLevels = new HashMap<>();

    public LevelProfile(ServerPlayerEntity player){
        Skill combatSkill = new CombatSkill();
        skillLevels.put(combatSkill, combatSkill.level(player));
        Skill swimmingSkill = new SwimmingSkill();
        skillLevels.put(swimmingSkill, swimmingSkill.level(player));
        Skill runningSkill = new RunningSkill();
        skillLevels.put(runningSkill, runningSkill.level(player));
        Skill walkingSkill = new WalkingSkill();
        skillLevels.put(walkingSkill, walkingSkill.level(player));
        Skill miningSkill = new MiningSkill();
        skillLevels.put(miningSkill, miningSkill.level(player));
        Skill jumpingSkill = new JumpingSkill();
        skillLevels.put(jumpingSkill, jumpingSkill.level(player));
        Skill naturesGraceSkill = new NaturesGraceSkill();
        skillLevels.put(naturesGraceSkill, naturesGraceSkill.level(player));
        Skill toughnessSkill = new ToughnessSkill();
        skillLevels.put(toughnessSkill, toughnessSkill.level(player));


        updateAllSkills(player);
    }

    public void updateAllSkills(ServerPlayerEntity player){
        skillLevels.replaceAll((s, v) -> s.level(player));
    }

    public void updateAllEffects(ServerPlayerEntity player) {
        for (Skill skill : skillLevels.keySet()) {
            updateEffect(player, skill);
        }
    }

    private void updateEffect(@NotNull ServerPlayerEntity player, Skill skill) {
        int amplifier = skillLevels.get(skill);
        RegistryEntry<StatusEffect> effect = Constants.skillEffects.get(skill.name);
        if (amplifier < 1) {
            player.removeStatusEffect(effect);
            return;
        }
        // -1 duration means infinite, amplifier - 1 because level 1 effect is amplifier 0
        StatusEffectInstance effectInstance = new StatusEffectInstance(effect, -1, amplifier - 1); // -1 duration means infinite
        try {
            player.setStatusEffect(effectInstance, player);
        } catch (Exception e) {
            LOGGER.error("Failed to add effect {} to player {}", effect, player.getName().getString());
            LOGGER.error(e.getMessage());
        }
    }

    public Map<Skill, Integer> getSkillLevels(){
        return skillLevels;
    }

}
