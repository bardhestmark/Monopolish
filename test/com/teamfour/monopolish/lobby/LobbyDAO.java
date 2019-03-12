package com.teamfour.monopolish.lobby;

import com.teamfour.monopolish.database.DataAccessObject;

import java.sql.SQLException;
import java.sql.Types;

/**
 * Handles all database communication to the Lobby table in the database
 *
 * @author      Eirik Hemstad
 * @version     1.0
 */

public class LobbyDAO extends DataAccessObject {

    /**
     * Makes the user join the first available lobby (with less than 4 players). If none were found,
     * create a new one
     * @param userId Userid
     * @return The lobby id
     * @throws SQLException
     */
    public int insertLobby(int userId) throws SQLException {
        getConnection();
        cStmt = connection.prepareCall("{call lobby_insert(?, ?)}");

        cStmt.setInt(1, userId);
        cStmt.registerOutParameter(2, Types.INTEGER);

        int roomId = -1;
        if (cStmt.execute())
            roomId = cStmt.getInt(2);

        return roomId;
    }

    /**
     * Sets the specified users 'ready' status
     * @param roomId Lobby id
     * @param userId Account id
     * @param ready Ready or not?
     * @return True if successful
     * @throws SQLException
     */
    public boolean setReady(int roomId, int userId, boolean ready) throws SQLException {
        getConnection();
        cStmt = connection.prepareCall("{call lobby_set_ready(?, ?, ?)}");

        cStmt.setInt(1, roomId);
        cStmt.setInt(2, userId);
        cStmt.setBoolean(3, ready);

        int count = cStmt.executeUpdate();

        return (count > 0);
    }

    /**
     * Deletes an entire lobby session
     * @param roomId Id of the lobby session
     * @return True if successful
     * @throws SQLException
     */
    public boolean deleteLobby(int roomId) throws SQLException {
        getConnection();
        cStmt = connection.prepareCall("{call lobby_delete(?)}");

        cStmt.setInt(1, roomId);

        int count = cStmt.executeUpdate();

        return (count > 0);
    }

    /**
     * Deletes the specified user from the specified lobby
     * @param roomId Lobby id
     * @param userId Account id
     * @return True if successful
     * @throws SQLException
     */
    public boolean deleteUserFromLobby(int roomId, int userId) throws SQLException {
        getConnection();
        cStmt = connection.prepareCall("{call lobby_delete_user(?, ?)}");

        cStmt.setInt(1, roomId);
        cStmt.setInt(2, userId);

        int count = cStmt.executeUpdate();

        return (count > 0);
    }
}
