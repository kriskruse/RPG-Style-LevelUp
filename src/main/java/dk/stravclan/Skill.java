package dk.stravclan;

import net.minecraft.block.Block;
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


    public Skill(String name, float levelOneReq, float levelReqModifier) {
        this.name = name;
        this.levelOneReq = levelOneReq;
        this.levelReqModifier = levelReqModifier;
    }

    public int level(ServerPlayerEntity player) {
        int level = 0;
        long xp = xp(player);
        float levelReq = levelOneReq;

        while (xp > levelReq) {
            levelReq *= levelReqModifier;
            level ++;
        }
        return level;
    }
    public abstract long xp(@NotNull ServerPlayerEntity player);

}

class CombatSkill extends Skill {
    public CombatSkill() {
        super(Constants.combatSkillName, Constants.combatSkillLevelOneReq, Constants.combatSkillLevelReqModifier);
    }
    public long xp(@NotNull ServerPlayerEntity player) {
        long xp = (long) (player.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(Stats.DAMAGE_DEALT))
                + player.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(Stats.DAMAGE_BLOCKED_BY_SHIELD))
                + player.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(Stats.TARGET_HIT)));
        LOGGER.info("Player has {} xp in combat", xp);
        return xp;
    }
}

class SwimmingSkill extends Skill {
    public SwimmingSkill() {
        super(Constants.swimmingSkillName, Constants.swimmingSkillLevelOneReq, Constants.swimmingSkillLevelReqModifier);
    }

    public long xp(@NotNull ServerPlayerEntity player) {
        long xp = (long) (player.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(Stats.SWIM_ONE_CM))) / 100;
        LOGGER.info("Player has {} xp in Swimming", xp);
        return xp;
    }
}

class WalkingSkill extends Skill {
    public WalkingSkill() {
        super(Constants.walkingSkillName, Constants.walkingSkillLevelOneReq, Constants.walkingSkillLevelReqModifier);
    }

    public long xp(@NotNull ServerPlayerEntity player) {
        long xp = (long) (player.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(Stats.WALK_ONE_CM))) / 100;
        LOGGER.info("Player has {} xp in Walking", xp);
        return xp;
    }
}

class RunningSkill extends Skill {
    public RunningSkill() {
        super(Constants.runningSkillName, Constants.runningSkillLevelOneReq, Constants.runningSkillLevelReqModifier);
    }

    public long xp(@NotNull ServerPlayerEntity player) {
        long xp = (long) (player.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(Stats.SPRINT_ONE_CM))) / 100;
        LOGGER.info("Player has {} xp in Running", xp);
        return xp;
    }
}

class MiningSkill extends Skill {
    public MiningSkill() {
        super(Constants.miningSkillName, Constants.miningSkillLevelOneReq, Constants.miningSkillLevelReqModifier);
    }

    public long xp(@NotNull ServerPlayerEntity player) {
        long xp = 0;
        for (Block block : Constants.miningXp.keySet()) {
            xp += (long) (player.getStatHandler().getStat(Stats.MINED.getOrCreateStat(block)) * Constants.miningXp.get(block));
        }
        LOGGER.info("Player has {} xp in Mining", xp);
        return xp;
    }
}


class JumpingSkill extends Skill {
    public JumpingSkill() {
        super(Constants.jumpingSkillName, Constants.jumpingSkillLevelOneReq, Constants.jumpingSkillLevelReqModifier);
    }

    public long xp(@NotNull ServerPlayerEntity player) {
        long xp = player.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(Stats.JUMP));
        LOGGER.info("Player has {} xp in Jumping", xp);
        return xp;
    }
}

class NaturesGraceSkill extends Skill {
    public NaturesGraceSkill() {
        super(Constants.naturesGraceSkillName, Constants.naturesGraceSkillLevelOneReq, Constants.naturesGraceSkillLevelReqModifier);
    }

    public long xp(@NotNull ServerPlayerEntity player) {
        long xp = 0;
        for (Block block : Constants.naturesGraceXp.keySet()) {
            xp += (long) (player.getStatHandler().getStat(Stats.MINED.getOrCreateStat(block)) * Constants.naturesGraceXp.get(block));
        }
        LOGGER.info("Player has {} xp in Natures Grace", xp);
        return xp;
    }
}

class ToughnessSkill extends Skill {
    public ToughnessSkill() {
        super(Constants.ToughnessSkillName, Constants.ToughnessSkillLevelOneReq, Constants.ToughnessSkillLevelReqModifier);
    }

    public long xp(@NotNull ServerPlayerEntity player) {
        long xp = player.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(Stats.DAMAGE_TAKEN));
        LOGGER.info("Player has {} xp in Toughness", xp);
        return xp;
    }
}