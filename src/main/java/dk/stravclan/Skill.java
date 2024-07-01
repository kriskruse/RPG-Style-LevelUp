package dk.stravclan;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.ServerStatHandler;
import net.minecraft.stat.Stats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


abstract class Skill {
    public static final Logger LOGGER = LoggerFactory.getLogger("rpg-style-leveling");

    public String name;
    public float levelOneReq;
    public float levelReqModifier;


    public Skill(String name, float levelReq, float levelReqModifier) {
        this.name = name;
        this.levelOneReq = levelReq;
        this.levelReqModifier = levelReqModifier;
    }

    public abstract int level(ServerPlayerEntity player);
    public abstract float xp(ServerPlayerEntity player);


}

class CombatSkill extends Skill {
    public CombatSkill() {
        super("Combat", 10000, 2.1f);
    }

    public int level(ServerPlayerEntity player) {
        int level = 0;
        float xp = xp(player);
        float levelReq = levelOneReq;

        while (xp > levelReq) {
            levelReq *= levelReqModifier;
            level ++;
        }
        return level;
    }

    public float xp(ServerPlayerEntity player) {
        float xp = player.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(Stats.DAMAGE_DEALT))
                + player.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(Stats.DAMAGE_BLOCKED_BY_SHIELD))
                + player.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(Stats.TARGET_HIT));
        LOGGER.info("Player has {} xp in combat", xp);
        return xp;
    }

}
