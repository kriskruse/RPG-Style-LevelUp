package dk.stravclan;

import net.minecraft.network.packet.s2c.play.ScoreboardDisplayS2CPacket;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.scoreboard.ScoreboardDisplaySlot;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.number.FixedNumberFormat;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.LinkedList;
import java.util.List;

public class RPGUiManager {

    Scoreboard scoreboard;
    List<ServerPlayerEntity> trackedPlayers = new LinkedList<>();

    public RPGUiManager() {
        scoreboard = new Scoreboard();
        ScoreboardCriterion criterion = ScoreboardCriterion.create("RPG Level",
                false,
                ScoreboardCriterion.RenderType.INTEGER);

        scoreboard.addObjective("RPG Level",
                criterion,
                Text.of("RPG Level"),
                ScoreboardCriterion.RenderType.INTEGER,
                true,
                new FixedNumberFormat(Text.of("Test")));
    }
    public void addPlayer(ServerPlayerEntity player) {
        trackedPlayers.add(player);
        updateUi(player);
    }

    public void updateUi(ServerPlayerEntity player) {
        displayScoreboardToPlayer(player);
    }

    public void displayScoreboardToPlayer(ServerPlayerEntity player) {
        for (ScoreboardObjective objective : scoreboard.getObjectives()) {
            if (objective.getCriterion().getName().equals("RPG Level")) {
                player.networkHandler.sendPacket(new ScoreboardDisplayS2CPacket(ScoreboardDisplaySlot.SIDEBAR, objective));
            }
        }
    }

    public void updatePlayers() {
        for (ServerPlayerEntity player : trackedPlayers) {
            updateUi(player);
        }
    }
}
