package dk.stravclan;

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
    public static final Logger LOGGER = LoggerFactory.getLogger(Constants.MOD_ID);

    public String name;
    public float levelOneReq;
    public float levelReqModifier;
    public float nextLevelReq;
    public long xp;
    public int level;
    public int effectTargetLevel;


    public Skill(String name) {
        this.name = name;
        this.levelOneReq = Constants.skillDataMap.get(name).get("levelOneReq");
        this.levelReqModifier = Constants.skillDataMap.get(name).get("modifier");
        this.level = 0;
        this.xp = 0;
        this.nextLevelReq = levelOneReq;
    }

    public void level(ServerPlayerEntity player, LevelProfile levelProfile) {
        xp = calculateXP(player, levelProfile);
        int oldLevel = level;
        nextLevelReq = (float) (levelOneReq * Math.pow(level + 1, levelReqModifier));

        while (xp > nextLevelReq) {
            level++;
            this.nextLevelReq = (float) (levelOneReq * Math.pow(level + 1, levelReqModifier));
        }

        // update effect target level if the level has changed and the user has not manually changed it
        if (oldLevel == effectTargetLevel) {
            effectTargetLevel = level;
        }
    }

    public void changeEffectTargetLevel(int i) {
        if (i > 0) {
            effectTargetLevel = Math.min(level, effectTargetLevel + i);
        } else if (i < 0) {
            effectTargetLevel = Math.max(0, effectTargetLevel + i);
        } else {
            LOGGER.error("Effect target level change was 0");
        }
    }

    public void updateEffectOnPlayer(@NotNull ServerPlayerEntity player) {
        RegistryEntry<StatusEffect> effect = Constants.skillEffects.get(name);
        if (effectTargetLevel < 1) {
            player.removeStatusEffect(effect);
            return;
        }
        StatusEffectInstance effectInstance = new StatusEffectInstance(
                effect, -1, effectTargetLevel - 1, false, false, false);
        try {
            player.setStatusEffect(effectInstance, player);
        } catch (Exception e) {
            LOGGER.error("Failed to add effect {} to player {}", effect, player.getName().getString());

        }
    }

    public void updateRequirements() {
        levelOneReq = Constants.skillDataMap.get(name).get("levelOneReq");
        levelReqModifier = Constants.skillDataMap.get(name).get("modifier");
    }

    public abstract long calculateXP(@NotNull ServerPlayerEntity player, LevelProfile levelProfile);


}


class CombatSkill extends Skill {
    public CombatSkill() {
        super(Constants.combatSkillName);
    }

    public long calculateXP(@NotNull ServerPlayerEntity player, LevelProfile levelProfile) {
        return (player.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(Stats.DAMAGE_DEALT))
                + player.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(Stats.DAMAGE_BLOCKED_BY_SHIELD))
                + player.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(Stats.TARGET_HIT))) / 100;

    }
}

class SwimmingSkill extends Skill {
    public SwimmingSkill() {
        super(Constants.swimmingSkillName);
    }

    public long calculateXP(@NotNull ServerPlayerEntity player, LevelProfile levelProfile) {
        return (long) (player.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(Stats.SWIM_ONE_CM))) / 100;

    }
}

class WalkingSkill extends Skill {
    public WalkingSkill() {
        super(Constants.walkingSkillName);
    }

    public long calculateXP(@NotNull ServerPlayerEntity player, LevelProfile levelProfile) {
        return (long) (player.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(Stats.WALK_ONE_CM))) / 100;
    }
}

class RunningSkill extends Skill {
    public RunningSkill() {
        super(Constants.runningSkillName);
    }

    public long calculateXP(@NotNull ServerPlayerEntity player, LevelProfile levelProfile) {
        return (long) (player.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(Stats.SPRINT_ONE_CM))) / 100;
    }
}

class MiningSkill extends Skill {
    public MiningSkill() {
        super(Constants.miningSkillName);
    }

    public long calculateXP(@NotNull ServerPlayerEntity player, LevelProfile levelProfile) {
        long xp = 0;
        for (Block block : Constants.miningXp.keySet()) {
            xp += (long) (player.getStatHandler().getStat(Stats.MINED.getOrCreateStat(block)) * Constants.miningXp.get(block));
        }
        return xp;
    }
}


class JumpingSkill extends Skill {
    public JumpingSkill() {
        super(Constants.jumpingSkillName);
    }

    public long calculateXP(@NotNull ServerPlayerEntity player, LevelProfile levelProfile) {
        return player.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(Stats.JUMP));
    }
}

class NaturesGraceSkill extends Skill {
    public NaturesGraceSkill() {
        super(Constants.naturesGraceSkillName);
    }

    public long calculateXP(@NotNull ServerPlayerEntity player, LevelProfile levelProfile) {
        long xp = 0;
        for (Block block : Constants.naturesGraceXp.keySet()) {
            xp += (long) (player.getStatHandler().getStat(Stats.MINED.getOrCreateStat(block)) * Constants.naturesGraceXp.get(block));
        }
        return xp;
    }
}

class ToughnessSkill extends Skill {
    public ToughnessSkill() {
        super(Constants.toughnessSkillName);
    }

    public long calculateXP(@NotNull ServerPlayerEntity player, LevelProfile levelProfile) {
        return player.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(Stats.DAMAGE_TAKEN)) / 100;
    }
}

class TotalSkill extends Skill {
    public TotalSkill() {
        super("Total");
    }


    public long calculateXP(@NotNull ServerPlayerEntity player, LevelProfile levelProfile) {
        long xp = 0;
        for (Skill skill : levelProfile.skills) {
            if (skill.name.equals("Total")) {
                continue;
            }
            xp += skill.level;
        }
        return xp;
    }


    public void level(ServerPlayerEntity player, LevelProfile levelProfile) {
        level = (int) calculateXP(player, levelProfile);

    }

    @Override
    public void updateEffectOnPlayer(@NotNull ServerPlayerEntity player) {
        RegistryEntry<StatusEffect> effect = Constants.skillEffects.get(name);
        if (level < 1) {
            player.removeStatusEffect(effect);
            return;
        }
        StatusEffectInstance effectInstance = new StatusEffectInstance(
                effect, -1, (Math.floorDiv(level, 5)) - 1, false, false, false);
        try {
            player.setStatusEffect(effectInstance, player);
        } catch (Exception e) {
            LOGGER.error("Failed to add TotalLevel skills effect {} to player {}", effect, player.getName().getString());

        }
    }
}
