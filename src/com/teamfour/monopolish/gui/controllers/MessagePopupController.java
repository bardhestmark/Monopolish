package com.teamfour.monopolish.gui.controllers;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.Timer;
import java.util.TimerTask;

public class MessagePopupController {
    private static @FXML Pane container;
    private static @FXML Text textElement;
    private static final int ANIMATION_DURATION = 200;
    private static final int COUNTDOWN_TIME = 8;
    private static int time = COUNTDOWN_TIME;

    public static void setup(Pane container, Text textElement) {
        MessagePopupController.container = container;
        MessagePopupController.textElement = textElement;
    }

    public static void show (String msg) {
        // Set text element
        textElement.setText(msg);

        // Slide and fade container in
        animateMovement(true);

        // Hide container on click
        container.setOnMouseClicked(e -> animateMovement(false));

        // Start timer
        Timer timer = new Timer();

        // Countdown to zero seconds
        TimerTask countdown = new TimerTask() {
            @Override
            public void run() {
                if (time > 0) {
                    time--;
                } else {
                    timer.cancel();
                    timer.purge();
                }
            }
        };

        timer.scheduleAtFixedRate(countdown, 0L, 1000L);
    }

    private static void animateMovement(boolean show) {
        int translateY = 100;
        double opacityFrom = 1.0;
        double opacityTo = 0.0;

        // Reverse animation
        if (show) {
            translateY *= -1;
            opacityFrom = 0.0;
            opacityTo = 1.0;
        }

        // Setup translate- and fade-transition
        TranslateTransition tt = new TranslateTransition(Duration.millis(ANIMATION_DURATION), container);
        tt.setByY(translateY);

        // Fade message in and out
        FadeTransition ft = new FadeTransition(Duration.millis(ANIMATION_DURATION), container);
        ft.setFromValue(opacityFrom);
        ft.setToValue(opacityTo);

        // Show both animations at the same time
        ParallelTransition pt = new ParallelTransition(tt, ft);

        // Play transition
        pt.play();
    }
}