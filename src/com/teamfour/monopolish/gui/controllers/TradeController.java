package com.teamfour.monopolish.gui.controllers;

import com.teamfour.monopolish.game.entities.EntityManager;
import com.teamfour.monopolish.game.property.Property;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;

import java.sql.SQLException;
import java.util.ArrayList;

public class TradeController {
    // Trading elements:
    @FXML private FlowPane youroffer, askfor, yourproperties, opponentsproperties;
    @FXML private Button offermoneyok, requestmoneyok, clearyou, clearopponent, canceltrade, proposeTradeBtn;
    @FXML private Label tradeusername, yourtrademoney, requestedtrademoney, invalidinput, invalidinput2;
    @FXML private TextField offeredmoney, requestedmoney;

    // Username of the opponent you want to trade with
    private final String TRADE_USERNAME = Handler.getSendUsername();
    private final String YOU = Handler.getCurrentGame().getEntities().getYou().getUsername();
    private final EntityManager entity = Handler.getCurrentGame().getEntities();

    /**
     * Draws a trading screen, letting you choose what to trade
     */
    @FXML public void initialize() {
        //just some cosmetics
        tradeusername.setText("Trading with: " + TRADE_USERNAME);

        //Lists of the properties available
        ArrayList<Pane> yourCards = new ArrayList<>();
        ArrayList<Pane> opponentsCards = new ArrayList<>();

        //Lists for the cards on offer,
        // because its less work than getting the cards out of the flowPane(they get converted to nodes)
        ArrayList<Pane> offeredCards = new ArrayList<>();
        ArrayList<Pane> requestedCards = new ArrayList<>();

        for (Property property : Handler.getCurrentGame().getEntities().getYou().getProperties()){
            yourCards.add(GameControllerDrawFx.createPropertyCard(property));
        }

        for (Property property : Handler.getCurrentGame().getEntities().getPlayer(TRADE_USERNAME).getProperties()){
            opponentsCards.add(GameControllerDrawFx.createPropertyCard(property));
        }

        //setting onclick for the cards so they change pane when clicked upon,
        // also adding them to the onoffer Arraylists
        for (Pane card : yourCards) {
            card.setOnMouseClicked(event -> {
                try {
                    youroffer.getChildren().add(card);
                    offeredCards.add(card);
                }
                catch (IllegalArgumentException e) {
                    yourproperties.getChildren().add(card);
                    offeredCards.remove(card);
                }
            });
        }
        for (Pane card : opponentsCards) {
            card.setOnMouseClicked(event -> {
                try {
                    askfor.getChildren().add(card);
                    requestedCards.add(card);
                }
                catch (IllegalArgumentException e) {
                    opponentsproperties.getChildren().add(card);
                    requestedCards.remove(card);
                }
            });
        }

        //setting buttons for clearing all offers
        clearyou.setOnAction(event -> {
            yourtrademoney.setText(null);
            youroffer.getChildren().clear();
            offeredCards.clear();
            yourproperties.getChildren().clear();
            yourproperties.getChildren().addAll(yourCards);
        });
        clearopponent.setOnAction(event -> {
            requestedtrademoney.setText(null);
            askfor.getChildren().clear();
            requestedCards.clear();
            opponentsproperties.getChildren().clear();
            opponentsproperties.getChildren().addAll(opponentsCards);
        });

        //setting buttons for offering money
        offermoneyok.setOnAction(event -> {
            String input = offeredmoney.getText();
            try {
                int check = Integer.parseInt(input);
                if (check > Handler.getCurrentGame().getEntities().getYou().getMoney()) {
                    throw new IllegalArgumentException("Not enough money");
                }
                invalidinput.setVisible(false);
                yourtrademoney.setText(input);
            } catch (NumberFormatException e) {
                invalidinput.setText("Invalid input");
                invalidinput.setVisible(true);
            } catch (IllegalArgumentException e) {
                invalidinput.setText(e.getMessage());
                invalidinput.setVisible(true);
            }
        });
        requestmoneyok.setOnAction(event -> {
            String input = requestedmoney.getText();
            try {
                int check = Integer.parseInt(input);
                if(check > Handler.getCurrentGame().getEntities().getPlayer(TRADE_USERNAME).getMoney()) {
                    throw new IllegalArgumentException("Not enough money");
                }
                invalidinput2.setVisible(false);
                requestedtrademoney.setText(input);
            } catch (NumberFormatException e) {
                invalidinput2.setText("Invalid input");
                invalidinput2.setVisible(true);
            } catch (IllegalArgumentException e) {
                invalidinput2.setText(e.getMessage());
                invalidinput2.setVisible(true);
            }
        });

        //adding properties into panes for showing
        yourproperties.getChildren().addAll(yourCards);
        opponentsproperties.getChildren().addAll(opponentsCards);

        proposeTradeBtn.setOnAction(event -> {
            Handler.getCurrentGame().getEntities().updateFromDatabase();
            ArrayList<String> offeredPropertiesNameList = new ArrayList<>();
            ArrayList<String> requestedPropertiesNameList = new ArrayList<>();

            ArrayList<Object> finaloffer = new ArrayList<>();

            for (Pane p : offeredCards) {
                offeredPropertiesNameList.add(p.getId());
            }
            for (Pane p : requestedCards) {
                requestedPropertiesNameList.add(p.getId());
            }

            int offeredmoney = Integer.parseInt(yourtrademoney.getText());
            int requestedmoney = Integer.parseInt(requestedtrademoney.getText());


            ArrayList<Property> offeredProperties = new ArrayList<>();
            ArrayList<Property> requestedProperties = new ArrayList<>();

            ArrayList<Property> offeredPropertiesNew = new ArrayList<>();
            ArrayList<Property> requestedPropertiesNew = new ArrayList<>();

            offeredProperties.addAll(entity.getYou().getProperties());
            requestedProperties.addAll(entity.getPlayer(TRADE_USERNAME).getProperties());

            offeredProperties.trimToSize();
            requestedProperties.trimToSize();

            int index = 0;
            for (int i = 0; i < offeredPropertiesNameList.size(); i++) {
                System.out.println("offered props: ");
                if (offeredProperties.get(i).getName().equalsIgnoreCase(offeredPropertiesNameList.get(i))) {
                    offeredPropertiesNew.add(offeredProperties.get(i));
                    System.out.println("Adding prop at: "+i);
                    index++;
                }
            }
            offeredPropertiesNew.trimToSize();
            int index2 = 0;
            for (int i = 0; i < requestedPropertiesNameList.size(); i++) {
                System.out.println("requested props: ");
                if (requestedProperties.get(i).getName().equalsIgnoreCase(requestedPropertiesNameList.get(i))) {
                    requestedPropertiesNew.add(requestedProperties.get(i));
                    System.out.println("Adding prop at: " + i);
                    index2++;
                }
            }
            requestedPropertiesNew.trimToSize();

            // add offered properties:
            System.out.println("sending trade to entitymanager............");

            entity.tradeFromTo(entity.getYou(), entity.getPlayer(TRADE_USERNAME),
                    offeredmoney, requestedmoney, offeredPropertiesNew, requestedPropertiesNew);

            entity.updateToDatabase();
            entity.updateFromDatabase();
            if (entity.isTrade(YOU)) {
                System.out.println("Found trades for you!");
            }

        });

        canceltrade.setOnAction(event -> {
            Pane container = Handler.getSendContainer();
            if (container != null) container.setVisible(false);
        });
    }
}
