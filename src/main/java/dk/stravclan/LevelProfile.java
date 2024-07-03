package dk.stravclan;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

public class LevelProfile {
    public static final Logger LOGGER = LoggerFactory.getLogger("rpg-style-leveling");

    public List<Skill> skills = new LinkedList<>();

    public LevelProfile(ServerPlayerEntity player){
        skills.add(new NaturesGraceSkill());
        skills.add(new ToughnessSkill());
        skills.add(new RunningSkill());
        skills.add(new WalkingSkill());
        skills.add(new SwimmingSkill());
        skills.add(new JumpingSkill());
        skills.add(new CombatSkill());
        skills.add(new MiningSkill());
        updateAllSkills(player);
    }

    public void updateAllSkills(ServerPlayerEntity player){
        for (Skill skill : skills) {
            skill.level(player);
        }
    }

    public void updateAllEffects(ServerPlayerEntity player) {
        for (Skill skill : skills) {
            updateEffect(player, skill);
        }
    }

    private void updateEffect(@NotNull ServerPlayerEntity player, Skill skill) {
        int amplifier = skill.effectTargetLevel;
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

    public void changeEffectTarget(String skillName, int amount){
        Skill skill = findSkillWithName(skillName);
        if (skill == null) {
            LOGGER.error("INCREMENT-EFFECT Could not find skill with name {}", skillName);
            return;
        }
        skill.changeEffectTargetLevel(amount);
    }

    private Skill findSkillWithName(String skillName) {
        for (Skill skill : skills) {
            if (skill.name.equals(skillName)) {
                return skill;
            }
        }
        LOGGER.error("Could not find skill with name {}", skillName);
        return null;
    }


    public List<Skill> getSkillLevels(){
        return skills;
    }

}
