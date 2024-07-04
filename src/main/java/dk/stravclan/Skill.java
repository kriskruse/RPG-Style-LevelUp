package dk.stravclan;

import net.fabricmc.fabric.api.screenhandler.v1.FabricScreenHandlerFactory;
import net.minecraft.block.Block;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract class Skill {
    public static final Logger LOGGER = LoggerFactory.getLogger("rpg-style-leveling");

    public String name;
    public float levelOneReq;
    public float levelReqModifier;
    public float nextLevelReq;
    public long xp;
    public int level;
    public int effectTargetLevel;


    public Skill(String name, float levelOneReq, float levelReqModifier) {
        this.name = name;
        this.levelOneReq = levelOneReq;
        this.levelReqModifier = levelReqModifier;
        this.level = 0;
        this.xp = 0;
        this.nextLevelReq = levelOneReq;
    }

    public void level(ServerPlayerEntity player) {
        xp = calculateXP(player);
        int oldLevel = level;
        nextLevelReq = (float) (levelOneReq * Math.pow(level + 1, levelReqModifier));

        while (xp > nextLevelReq) {
            level ++;
            this.nextLevelReq = (float) (levelOneReq * Math.pow(level + 1, levelReqModifier));
        }

        // update effect target level if the level has changed and the user has not manually changed it
        if (oldLevel == effectTargetLevel) {
            effectTargetLevel = level;
        }
    }

    public void changeEffectTargetLevel(int i) {
        if (i > 0){
            effectTargetLevel = Math.min(level, effectTargetLevel + i);
        }else if (i < 0){
            effectTargetLevel = Math.max(0, effectTargetLevel + i);
        }else {
            LOGGER.error("Effect target level change was 0");
        }
    }

    public void upatedEffectOnPlayer(@NotNull ServerPlayerEntity player) {
        RegistryEntry<StatusEffect> effect = Constants.skillEffects.get(name);
        if (effectTargetLevel < 1) {
            player.removeStatusEffect(effect);
            return;
        }
        StatusEffectInstance effectInstance = new StatusEffectInstance(effect, -1, effectTargetLevel - 1);
        try {
            player.setStatusEffect(effectInstance, player);
        } catch (Exception e) {
            LOGGER.error("Failed to add effect {} to player {}", effect, player.getName().getString());

        };
    }

    public abstract long calculateXP(@NotNull ServerPlayerEntity player);

}


class CombatSkill extends Skill {
    public CombatSkill() {
        super(Constants.combatSkillName, Constants.combatSkillLevelOneReq, Constants.combatSkillLevelReqModifier);
    }
    public long calculateXP(@NotNull ServerPlayerEntity player) {
        return (player.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(Stats.DAMAGE_DEALT))
                + player.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(Stats.DAMAGE_BLOCKED_BY_SHIELD))
                + player.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(Stats.TARGET_HIT))) / 100;

    }
}

class SwimmingSkill extends Skill {
    public SwimmingSkill() {
        super(Constants.swimmingSkillName, Constants.swimmingSkillLevelOneReq, Constants.swimmingSkillLevelReqModifier);
    }

    public long calculateXP(@NotNull ServerPlayerEntity player) {
        return (long) (player.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(Stats.SWIM_ONE_CM))) / 100;

    }
}

class WalkingSkill extends Skill {
    public WalkingSkill() {
        super(Constants.walkingSkillName, Constants.walkingSkillLevelOneReq, Constants.walkingSkillLevelReqModifier);
    }

    public long calculateXP(@NotNull ServerPlayerEntity player) {
        return (long) (player.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(Stats.WALK_ONE_CM))) / 100;
    }
}

class RunningSkill extends Skill {
    public RunningSkill() {
        super(Constants.runningSkillName, Constants.runningSkillLevelOneReq, Constants.runningSkillLevelReqModifier);
    }

    public long calculateXP(@NotNull ServerPlayerEntity player) {
        return (long) (player.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(Stats.SPRINT_ONE_CM))) / 100;
    }
}

class MiningSkill extends Skill {
    public MiningSkill() {
        super(Constants.miningSkillName, Constants.miningSkillLevelOneReq, Constants.miningSkillLevelReqModifier);
    }

    public long calculateXP(@NotNull ServerPlayerEntity player) {
        long xp = 0;
        for (Block block : Constants.miningXp.keySet()) {
            xp += (long) (player.getStatHandler().getStat(Stats.MINED.getOrCreateStat(block)) * Constants.miningXp.get(block));
        }
        return xp;
    }
}


class JumpingSkill extends Skill {
    public JumpingSkill() {
        super(Constants.jumpingSkillName, Constants.jumpingSkillLevelOneReq, Constants.jumpingSkillLevelReqModifier);
    }

    public long calculateXP(@NotNull ServerPlayerEntity player) {
        return player.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(Stats.JUMP));
    }
}

class NaturesGraceSkill extends Skill {
    public NaturesGraceSkill() {
        super(Constants.naturesGraceSkillName, Constants.naturesGraceSkillLevelOneReq, Constants.naturesGraceSkillLevelReqModifier);
    }

    public long calculateXP(@NotNull ServerPlayerEntity player) {
        long xp = 0;
        for (Block block : Constants.naturesGraceXp.keySet()) {
            xp += (long) (player.getStatHandler().getStat(Stats.MINED.getOrCreateStat(block)) * Constants.naturesGraceXp.get(block));
        }
        return xp;
    }
}

class ToughnessSkill extends Skill {
    public ToughnessSkill() {
        super(Constants.toughnessSkillName, Constants.toughnessSkillLevelOneReq, Constants.toughnessSkillLevelReqModifier);
    }

    public long calculateXP(@NotNull ServerPlayerEntity player) {
        return player.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(Stats.DAMAGE_TAKEN)) / 100;
    }
}

class TotalSkill extends Skill {
    public TotalSkill() {
        super("Total", 1, 1);
    }

    public long calculateXP(@NotNull ServerPlayerEntity player) {
        long xp = 0;
        for (Skill skill : LevelProfile.skills) {
            xp += skill.level;
        }
        return xp;
    }

    @Override
    public void upatedEffectOnPlayer(ServerPlayerEntity player){
        RegistryEntry<StatusEffect> effect = Constants.skillEffects.get(name);
        if (level < 1) {
            player.removeStatusEffect(effect);
            return;
        }
        StatusEffectInstance effectInstance = new StatusEffectInstance(effect, -1, (Math.floorDiv(level,5)) - 1);
        try {
            player.setStatusEffect(effectInstance, player);
        } catch (Exception e) {
            LOGGER.error("Failed to add TotalLevel skills effect {} to player {}", effect, player.getName().getString());

        };
    }
    }
}