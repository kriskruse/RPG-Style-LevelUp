package dk.stravclan;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class RPGCommandManager {
    public static final Logger LOGGER = LoggerFactory.getLogger(Constants.MOD_ID);

    private static final Style incrementStyle = Style.EMPTY
            .withColor(Formatting.GREEN)
            .withBold(true);

    private static final Style decrementStyle = Style.EMPTY
            .withColor(Formatting.RED)
            .withBold(true);

    public static void rpg(ServerPlayerEntity player, RPGPlayerLevelManager lvlManager) {
        LevelProfile profile = lvlManager.getLevelProfile(player);
        List<Skill> skills = profile.skills;
        MutableText message = generateMessage(skills);
        player.sendMessage(message, false);
    }

    public static void resetCommand(ServerPlayerEntity player, RPGPlayerLevelManager lvlManager) {
        lvlManager.resetPlayer(player);
    }

    public static int changeEffectLevel(CommandContext<ServerCommandSource> context, RPGPlayerLevelManager levelManager, String skillName, int i) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        if (player == null) {
            LOGGER.error("Player is null");
            return 0;
        }
        LOGGER.info("Lowering effect of {} by {} for player {}", skillName, i, player.getName().getString());
        if (Constants.skillNames.contains(skillName)) {
            levelManager.changeEffectTargetPlayer(player, skillName, i);
            return 1;
        } else {
            context.getSource().sendFeedback(() -> Text.of("That skill does not exist!"), false);
            return 0;
        }
    }

    private static MutableText generateMessage(List<Skill> skills) {
        MutableText message = Text.literal("RPG Style Leveling \n");
        for (Skill skill : skills) {
            message.append(Text.literal(skill.name + ": ").setStyle(Style.EMPTY.withColor(Formatting.AQUA)));
            message.append(Text.literal("Level:  " + skill.effectTargetLevel + " / " + skill.level));
            message.append(Text.literal(" [+]").
                    setStyle(incrementStyle.withClickEvent(
                            new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/rpg changeLevel " + skill.name + " 1"))));
            message.append(Text.literal("[-] \n").
                    setStyle(decrementStyle.withClickEvent(
                            new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/rpg changeLevel " + skill.name + " -1"))));

            message.append(Text.literal("  - XP:  ").setStyle(Style.EMPTY.withColor(Formatting.GOLD)));
            message.append(Text.literal((int) skill.xp + " / " + (int) skill.nextLevelReq));
            message.append("\n");
        }
        return message;
    }

    public static int setSkillLevelOneReq(CommandContext<ServerCommandSource> context, String skillName, float newValue, RPGPlayerLevelManager playerLevelManager){
        if (Constants.skillNames.contains(skillName)) {
            Constants.changeSkillData(skillName, "levelOneReq", newValue);
            playerLevelManager.updatePlayers();
            LOGGER.info("LevelOneReq of {} set to {} successfully", skillName, newValue);
            context.getSource().sendFeedback(() -> Text.of("LevelOneRequirement of " + skillName + " set to " + newValue), true);
            return 1;
        } else {
            context.getSource().sendFeedback(() -> Text.of("That skill does not exist!"), false);
            LOGGER.error("(set levelOneReq): Skill {} does not exist", skillName);
            return 0;
        }
    }

    public static int setSkillLevelReqModifier(CommandContext<ServerCommandSource> context, String skillName, float newValue, RPGPlayerLevelManager playerLevelManager){
        if (Constants.skillNames.contains(skillName)) {
            Constants.changeSkillData(skillName, "modifier", newValue);
            playerLevelManager.updatePlayers();
            LOGGER.info("Modifier of {} set to {} successfully", skillName, newValue);
            context.getSource().sendFeedback(() -> Text.of("Modifier of " + skillName + " set to " + newValue), true);
            return 1;
        } else {
            context.getSource().sendFeedback(() -> Text.of("That skill does not exist!"), false);
            LOGGER.error("(set modifier): Skill {} does not exist", skillName);
            return 0;
        }

    }

    public static void resetAllPlayers(CommandContext<ServerCommandSource> context, RPGPlayerLevelManager playerLevelManager) {
        for (ServerPlayerEntity player : playerLevelManager.getPlayerLevelManager().keySet()) {
            playerLevelManager.resetPlayer(player);
        }
        playerLevelManager.updatePlayers();
        context.getSource().sendFeedback(() -> Text.of("All players have been reset and updated!"), true);
    }
}
