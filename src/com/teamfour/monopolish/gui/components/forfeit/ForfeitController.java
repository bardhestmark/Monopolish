package com.teamfour.monopolish.gui.components.forfeit;

import com.teamfour.monopolish.gui.FxUtils;
import com.teamfour.monopolish.gui.Handler;
import com.teamfour.monopolish.gui.views.game.GameController;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Controller class for forfeit view.
 * When initialized a timer will check for votes.
 * When countdown is over the game will either end or continue based on the votes.
 */
public class ForfeitController {
    // Containers for voting
    @FXML private Pane voteQuit;
    @FXML private Pane voteContinue;

    // Vote count in containers
    @FXML private Text voteCountQuit;
    @FXML private Text voteCountContinue;

    // Countdown time
    private Timer countdownTimer = new Timer();
    @FXML private Text timeValue;

    // Text showing what you voted
    @FXML private Text yourVoteText;

    // Constants
    private final int COUNTDOWN_TIME = 20;
    private final String USERNAME = Handler.getAccount().getUsername();
    private final int GAME_ID = Handler.getCurrentGameId();
    private final int NUM_OF_PLAYERS = Handler.getPlayerDAO().getPlayersInGame(GAME_ID).size();
    private final String VOTE_QUIT_MSG = "You voted to Quit";
    private final String VOTE_CONTINUE_MSG = "You voted to Continue";

    private int votesForQuit = 0;
    private int votesForContinue = 0;
    private int time = COUNTDOWN_TIME;

    @FXML public void initialize() {
        timeValue.setText(String.valueOf(COUNTDOWN_TIME));

        // User want to forfeit/quit
        voteQuit.onMouseClickedProperty().set(e -> {
            Handler.getPlayerDAO().setForfeitStatus(USERNAME, GAME_ID, 1);
            yourVoteText.setText(VOTE_QUIT_MSG);
            FxUtils.setTextColor(yourVoteText, "red");
        });

        // User want to continue the game
        voteContinue.onMouseClickedProperty().set(e -> {
            Handler.getPlayerDAO().setForfeitStatus(USERNAME, GAME_ID, 2);
            yourVoteText.setText(VOTE_CONTINUE_MSG);
            FxUtils.setTextColor(yourVoteText, "green");
        });

        // Change color on hover to show selection
        setOnHover(voteQuit);
        setOnHover(voteContinue);

        // Start refreshing method
        refresh();
    }

    /**
     * Change container style on hover,
     * will change the brightness to more easily see which container is selected
     *
     * @param container Target container for on hover
     */
    private void setOnHover(Pane container) {
        // Quit color
        String color = "#ef5350";
        String hoverColor = "#d13734";
        String backgroundRadius = " 0 0 0 15";

        // Continue color
        if (container.getId().equals("voteContinue")) {
            color = "#52d35e";
            hoverColor = "#01870e";
            backgroundRadius = "0 0 15 0";
        }

        // Final values for variables is needed in lambda functions
        String finalColor = color;
        String finalHoverColor = hoverColor;
        String finalBackgroundRadius = backgroundRadius;

        // Lighten background on hover
        container.setOnMouseEntered(e -> container.setStyle(
                "-fx-background-color: " + finalHoverColor + ";" +
                "-fx-background-radius: " + finalBackgroundRadius + ";"
        ));

        // Reset background when mouse leaves on hover
        container.setOnMouseExited(e -> container.setStyle(
                "-fx-background-color: " + finalColor + ";" +
                "-fx-background-radius: " + finalBackgroundRadius + ";"
        ));
    }

    /**
     * Refresh/update periodically to check what players vote.
     */
    private void refresh() {
        TimerTask countdownTask = new TimerTask() {
            @Override
            public void run() {
                // Get votes in forfeit
                int[] votes = Handler.getPlayerDAO().getForfeitStatus(GAME_ID);
                votesForQuit = votes[0];
                votesForContinue = votes[1];

                // Update vote counts
                voteCountQuit.setText(String.valueOf(votes[0]));
                voteCountContinue.setText(String.valueOf(votes[1]));

                // Check if every player has voted and set action accordingly
                if (votesForQuit + votesForContinue == NUM_OF_PLAYERS) {
                    checkVotes();
                    stopTimer();
                }

                // Change the time to red when the time is 5 or less.
                if (time <= 5) {
                    FxUtils.setTextColor(timeValue, "red");
                }

                // Check if time is positive
                if (time > 0) {
                    time--;
                    timeValue.setText(String.valueOf(time));
                }

                // Countdown is over, do action based on votes
                else {
                    stopTimer();
                    checkVotes();
                }
            }
        };

        // Update/refresh every second
        countdownTimer.scheduleAtFixedRate(countdownTask, 0L, 1000L);
    }

    /**
     * Stop the countdown timer
     */
    private void stopTimer() {
        countdownTimer.cancel();
        countdownTimer.purge();
    }

    /**
     * Check which option has the most votes and handle action based on the votes.
     */
    private void checkVotes() {
        if (votesForQuit > votesForContinue) GameController.gameFinished = true;
        else {
            // Hide forfeit container
            Handler.getForfeitContainer().setVisible(false);

            // Check if forfeit should be reset
            if (Handler.getGameDAO().getForfeit(GAME_ID))
                Handler.getGameDAO().setForfeit(GAME_ID, false);

            // Set forfeit check
            Handler.getPlayerDAO().setForfeitCheck(GAME_ID, USERNAME, true);
        }
    }
}
