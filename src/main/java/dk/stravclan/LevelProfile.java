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

    public static List<Skill> skills = new LinkedList<>();

    public LevelProfile(ServerPlayerEntity player){
        skills.add(new NaturesGraceSkill());
        skills.add(new ToughnessSkill());
        skills.add(new RunningSkill());
        //skills.add(new WalkingSkill());
        skills.add(new SwimmingSkill());
        skills.add(new JumpingSkill());
        skills.add(new CombatSkill());
        skills.add(new MiningSkill());
        skills.add(new TotalSkill());
        updateAllSkills(player);

    }

    public void updateAllSkills(ServerPlayerEntity player){
        for (Skill skill : skills) {
            skill.level(player);
        }
    }

    public void updateAllEffects(ServerPlayerEntity player) {
        for (Skill skill : skills) {
            skill.upatedEffectOnPlayer(player);
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
