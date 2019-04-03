package com.teamfour.monopolish.game.entities;

import com.teamfour.monopolish.game.entities.player.*;
import com.teamfour.monopolish.game.property.Property;
import com.teamfour.monopolish.game.property.Street;
import com.teamfour.monopolish.gui.controllers.Handler;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * This class manages all Entities in the game session. Entities are classified by players and the bank object.
 * This class uses its arraylist of players and the bank object to handle the communication between these objects,
 * as well as acting as an abstraction layer between all the objects and the rest of the application
 *
 * @author      eirikhem
 * @version     1.4
 */

public class EntityManager {
    // Attributes
    int gameId;                                         // ID for the game that contains this manager
    ArrayList<Player> players = new ArrayList<>();      // All players in the game
    PlayerDAO playerDAO = new PlayerDAO();              // Player database connection
    Bank bank;                                          // Bank object for handling available money and properties

    /**
     * Constructor
     * @param gameId
     */
    public EntityManager(int gameId) {
        this.gameId = gameId;
        this.bank = new Bank(gameId);
    }

    /**
     * Gets a player object based on their username
     * @param username The player's username
     * @return Player object
     */
    public Player getPlayer(String username) {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getUsername().equals(username)) {
                return (players.get(i));
            }
        }
        return (null);
    }

    /**
     *
     * @param pos
     * @return
     */
    public Player getPlayerByPosition(int pos){
        for(int i = 0; i<players.size(); i++){
            if(players.get(i).getPosition() == pos){
                return(players.get(i));
            }
        }
        return(null);
    }

    public int getPlayerPosition(String username){
        Player temp = getPlayer(username);
        return(temp.getPosition());
    }

    /**
     * Removes a player from this entitymanager
     * @param username
     */
    public void removePlayer(String username){
        playerDAO.removePlayer(gameId, username);
        Player temp = getPlayer(username);
        players.remove(temp);
    }

    /**
     * Returns a property object placed on the specified position
     * @param position Position
     * @return Property object
     */
    public Property getPropertyAtPosition(int position) {
        Property prop = null;
        for (Player p : players) {
            prop = p.getPropertyAtPosition(position);
            if (prop != null) {
                return prop;
            }
        }

        return bank.getPropertyAtPosition(position);
    }

//      * Transfers money from the bank to the specified username
//     * @param username Username of player
//     * @param amount Amount to be transferred
//     * @return True if successful
//     */
//    public boolean transferMoneyFromBank(String username, int amount) {
//        Player player = getPlayer(username);
//        if (bank.getMoney() < amount) {
//            return false;
//        }
//
//        return bank.transferMoney(player, amount);
//    }/**


    public void transferMoneyBank(String username, int amount) {
        Player player = getPlayer(username);
        bank.transferMoney(player, amount);
    }

    /**
     * Grabs a house from the bank to specified property
     * @param owner Owner of the property
     * @param id Id of the property
     * @return 1 if successful,
     *         -1 if not enough money,
     *         -2 if property was not found
     */
    public int transferHouseToProperty(String owner, int id) {
        Player ownerPlayer = getPlayer(owner);
        for (Property p : ownerPlayer.getProperties()) {
            if (p.getId() == id && p instanceof Street) {
                if (ownerPlayer.getMoney() >= ((Street) p).getHousePrice()) {
                    ((Street)p).addHouse();
                    bank.getHouses(1);
                    return 1;
                } else {
                    return -1;
                }
            }
        }

        return -2;
    }

    /**
     * Grabs a hotel from the bank to specified property
     * @param owner Owner of the property
     * @param id Id of the property
     * @return 1 if successful,
     *         -1 if not enough money,
     *         -2 if property was not found
     *         -3 if not enough houses to buy hotel
     */
    public boolean transferHotelToProperty(String owner, int id) {
        Player ownerPlayer = getPlayer(owner);
        for (Property p : ownerPlayer.getProperties()) {
            if (p.getId() == id && p instanceof Street) {
                if (ownerPlayer.getMoney() >= ((Street) p).getHotelPrice()) {
                     if (((Street)p).addHotel()) {
                         bank.getHotels(1);
                         return true;
                     } else {
                         return false;
                     }
                } else {
                    return false;
                }
            }
        }

        return false;
    }

//    /**
//     * Transfers money from the specified player to the bank
//     * @param username Player's username
//     * @param amount Amount to be transferred
//     * @return True if successful,
//     *         false if no money
//     */
//    public boolean transferMoneyToBank(String username, int amount) {
//        Player player = getPlayer(username);
//        if (player.getMoney() < amount) {
//            return false;
//        }
//
//        return player.transferMoney(bank, amount);
//    }

    public boolean distributeStartMoney(int amount) {
        for (Player p : players) {
            p.setMoney(0);
            bank.transferMoney(p, amount);
        }

        return true;
    }

    public boolean transferMoneyFromTo(String from, String to, int amount) {
        boolean result = false;
        Player fromPlayer = getPlayer(from);
        Player toPlayer = getPlayer(to);

        if (fromPlayer == null || toPlayer == null)
            return false;

        if (fromPlayer.getMoney() < amount) {
            amount = fromPlayer.getMoney();
        }

        return fromPlayer.transferMoney(toPlayer, amount);
    }

    public String getOwnerAtProperty(int position) {
        return getPropertyAtPosition(position).getOwner();
    }

    public void transactProperty(Entity receiver, Property property) {
        Entity propertyOwner;
        String owner = property.getOwner();
        if (owner.equals("") || owner == null) {
            propertyOwner = bank;
        } else {
            propertyOwner = getPlayer(property.getOwner());
        }

        if (receiver instanceof Player)
            property.setOwner(((Player) receiver).getUsername());
        else
            property.setOwner("");

        // Transfer properties
        receiver.getProperties().add(property);
        propertyOwner.getProperties().remove(property);

        // Transfer money
        receiver.transferMoney(propertyOwner, property.getPrice());
    }

    /**
     * Retrieves all player data from the database, to update the current game
     * after an opponent's round
     */
    public void updateFromDatabase() throws SQLException {
        int moneyInGame = 0;
        players.clear();
        players = playerDAO.getPlayersInGame(gameId);
        for (Player p : players) {
            p.updatePropertiesFromDatabase(gameId);
            moneyInGame += p.getMoney();
        }

        // Update bank
        bank.updatePropertiesFromDatabase(gameId);
        bank.setMoney(Bank.MAX_GAME_MONEY - moneyInGame);
    }

    /**
     * Writes all player updates to the database
     */
    public void updateToDatabase() throws SQLException {
        for (Player p : players) {
            playerDAO.updatePlayer(p, gameId);
            p.updatePropertiesToDatabase(gameId);
        }
    }

    /**
     * Returns the current player
     * @return You object
     */
    public Player getYou() {
        for(Player p : players) {
            if (p.getUsername().equals(Handler.getAccount().getUsername())) {
                return p;
            }
        }

        return null;
    }

    /**
     * Cycles through all players and updates their bankruptcy according to their value
     */
    public void updateBankruptcy() {
        for (Player p : players) {
            if (p.getMoney() <= 0) {
                p.setBankrupt(true);
            }
        }
    }

    /**
     * Check if there's a winner
     * @return The winner username
     */
    public String findWinner() {
        String result = null;
        // Count the number of not bankrupt players
        int notBankrupt = 0;
        for (Player p : players) {
            if (!p.isBankrupt()) {
                notBankrupt++;
                // If only one player is not bankrupt, this will be the winner
                result = p.getUsername();
            }
        }

        // If there are more or less than one not bankrupt, no winner was found
        if (notBankrupt == 1) {
            return result;
        } else {
            return null;
        }
    }

    /**
     * Generates a string array with the turn order of the players, based on their Player ID's
     * @return String array with the play order
     */
    public String[] generateTurnOrder() {
        String[] turns = new String[players.size()];
        for (int i = 0; i < turns.length; i++) {
            turns[i] = players.get(i).getUsername();
        }

        return turns;
    }

    /**
     * Returns a string presentation of all the entities
     * @return
     */
    public String toString() {
        String result = "Entities: \n";
        for (Player p : players) {
            result += "Name: " + p.getUsername() + "; " + p.toString();
        }

        result += bank.toString();

        return result;
    }

    // GETTERS & SETTERS

    public ArrayList<Player> getPlayers() {
        return (players);
    }
}