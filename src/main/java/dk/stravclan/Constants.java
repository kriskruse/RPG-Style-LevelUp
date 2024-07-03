package dk.stravclan;

import net.minecraft.block.Block;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.List;
import java.util.Map;

import static java.util.Map.entry;
import static net.minecraft.block.Blocks.*;
import static net.minecraft.entity.effect.StatusEffects.*;

public class Constants {
    public static final String runningSkillName = "Running";
    public static final int runningSkillLevelOneReq = 15000;
    public static final float runningSkillLevelReqModifier = 2f;
    public static final String swimmingSkillName = "Swimming";
    public static final int swimmingSkillLevelOneReq = 1500;
    public static final float swimmingSkillLevelReqModifier = 2f;
    public static final String combatSkillName = "Combat";
    public static final int combatSkillLevelOneReq = 15000;
    public static final float combatSkillLevelReqModifier = 2.5f;
    public static final String miningSkillName = "Mining";
    public static final int miningSkillLevelOneReq = 5000;
    public static final float miningSkillLevelReqModifier = 2f;
    public static final String walkingSkillName = "Walking";
    public static final int walkingSkillLevelOneReq = 15000;
    public static final float walkingSkillLevelReqModifier = 2f;
    public static final String jumpingSkillName = "Jumping";
    public static final int jumpingSkillLevelOneReq = 15000;
    public static final float jumpingSkillLevelReqModifier = 2f;
    public static final String naturesGraceSkillName = "Natures Grace";
    public static final int naturesGraceSkillLevelOneReq = 1000;
    public static final float naturesGraceSkillLevelReqModifier = 2f;
    public static final String ToughnessSkillName = "Toughness";
    public static final int ToughnessSkillLevelOneReq = 10000;
    public static final float ToughnessSkillLevelReqModifier = 1.5F;

    public static final List<String> skillNames = List.of(
            combatSkillName,
            swimmingSkillName,
            runningSkillName,
            walkingSkillName,
            miningSkillName,
            jumpingSkillName,
            naturesGraceSkillName,
            ToughnessSkillName
    );


    public static final Map<Block, Float> miningXp = Map.ofEntries(
            entry(NETHERRACK, 0.01f),
            entry(STONE, 0.1f),
            entry(ANDESITE, 0.1f),
            entry(DIORITE, 0.1f),
            entry(GRANITE, 0.1f),
            entry(COPPER_ORE, 1f),
            entry(COAL_ORE, 1f),
            entry(IRON_ORE, 2f),
            entry(NETHER_QUARTZ_ORE, 2f),
            entry(REDSTONE_ORE, 2f),
            entry(GOLD_ORE, 3f),
            entry(LAPIS_ORE, 3f),
            entry(DIAMOND_ORE, 4f),
            entry(EMERALD_ORE, 4f),
            entry(NETHER_GOLD_ORE, 3f),
            entry(ANCIENT_DEBRIS, 5f)
    );

    public static final Map<String, RegistryEntry<StatusEffect>> skillEffects = Map.ofEntries(
            entry(combatSkillName, STRENGTH),
            entry(swimmingSkillName, DOLPHINS_GRACE),
            entry(runningSkillName, SPEED),
            entry(walkingSkillName, SATURATION),
            entry(miningSkillName, HASTE),
            entry(jumpingSkillName, JUMP_BOOST),
            entry(naturesGraceSkillName, LUCK),
            entry(ToughnessSkillName, RESISTANCE)
    );

    public static final Map<Block, Float> naturesGraceXp = Map.ofEntries(
            entry(GRASS_BLOCK, 0.1f),
            entry(DANDELION, 1f),
            entry(POPPY, 1f),
            entry(BLUE_ORCHID, 1f),
            entry(ALLIUM, 1f),
            entry(AZURE_BLUET, 1f),
            entry(RED_TULIP, 1f),
            entry(ORANGE_TULIP, 1f),
            entry(WHITE_TULIP, 1f),
            entry(PINK_TULIP, 1f),
            entry(OXEYE_DAISY, 1f),
            entry(CORNFLOWER, 1f),
            entry(LILY_OF_THE_VALLEY, 1f),
            entry(WITHER_ROSE, 3f),
            entry(SUNFLOWER, 1f),
            entry(LILAC, 1f),
            entry(ROSE_BUSH, 1f),
            entry(PEONY, 1f),
            entry(TALL_GRASS, 0.1f),
            entry(LARGE_FERN, 0.1f),
            entry(TORCHFLOWER, 1f)
    );
}
