package com.teamfour.monopolish.game.entities.player;

import com.teamfour.monopolish.database.ConnectionPool;
import com.teamfour.monopolish.database.DataAccessObject;
import java.sql.SQLException;
import java.util.ArrayList;

public class PlayerDAO extends DataAccessObject {

    public ArrayList<Player> createPlayers(int game_id, String[] usernames){
        ArrayList<Player> players = null;
        try {
            connection = ConnectionPool.getMainConnectionPool().getConnection();
            cStmt = connection.prepareCall("{call player_create(?, ?)}");
            for(int i = 0; i<usernames.length; i++) {
                cStmt.setInt(1, game_id);
                cStmt.setString(2, usernames[i]);

                players.add(new Player(cStmt.executeQuery().getString("username")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            releaseConnection();
        }
        return(null);
    }

    public void removePlayer(int game_id, String username){
        try {
            connection = ConnectionPool.getMainConnectionPool().getConnection();
            cStmt = connection.prepareCall("{call player_remove(?, ?)}");

            for(int i = 0; i<10; i++) {
                cStmt.setString(1, username);
                cStmt.setInt(2, game_id);
                cStmt.executeQuery();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            releaseConnection();
        }
    }

    public void updatePlayer(Player player, int game_id){
        try {
            connection = ConnectionPool.getMainConnectionPool().getConnection();
            cStmt = connection.prepareCall("{call player_update(?, ?, ?)}");

            for(int i = 0; i<10; i++) {
                cStmt.setString(1, player.getUsername());
                cStmt.setInt(2, game_id);
                cStmt.setInt(3, player.getPosition());
                cStmt.setInt(4, player.getMoney());

                cStmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            releaseConnection();
        }
    }

    public void endGame(int game_id){
        try {
            connection = ConnectionPool.getMainConnectionPool().getConnection();
            cStmt = connection.prepareCall("{call player_clean(?, ?, ?)}");

            for(int i = 0; i<10; i++) {

                cStmt.setInt(1, game_id);
                cStmt.executeQuery();

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            releaseConnection();
        }

    }
}