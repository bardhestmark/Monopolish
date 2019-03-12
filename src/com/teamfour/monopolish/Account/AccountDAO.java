package com.teamfour.monopolish.Account;

import com.teamfour.monopolish.database.ConnectionPool;
import com.teamfour.monopolish.database.DataAccessObject;

import java.sql.*;

/**
 * Handles database communication towards the 'Account' table in the database.
 *
 * @author      Eirik Hemstad
 * @version     1.0
 */

public class AccountDAO extends DataAccessObject {
    /**
     * Inserts an account object into the account table
     * @param account Object to insert
     * @return True if the operation was successful, false if this user already exists
     * @throws SQLException
     */
    public boolean insertAccount(Account account, String password) throws SQLException {
        int count = 0;
        try {
            getConnection();
            cStmt = connection.prepareCall("{call account_insert_user(?, ?, ?)}");

            cStmt.setString(1, account.getUsername());
            cStmt.setString(2, account.getEmail());
            cStmt.setString(3, password);
            cStmt.setDate(4, Date.valueOf(account.getRegDate()));
            //cStmt.setInt(4, account.getHighscore());

            count = cStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            releaseConnection();
        }

        return (count > 0);
    }

    /**
     * Attempts to reset the password
     * @param username
     * @param currentPassword
     * @param newPassword
     * @return 1 if successful, 0 if no user, -1 if wrong password
     */
    public int resetPassword(String username, String currentPassword, String newPassword) {
        int status = 0;
        try {
            getConnection();
            cStmt = connection.prepareCall("{call reset_password(?, ?, ?, ?)}");

            cStmt.setString(1, username);
            cStmt.setString(2, currentPassword);
            cStmt.setString(3, newPassword);
            cStmt.registerOutParameter(4, Types.BIT);

            cStmt.executeUpdate();

            status = cStmt.getInt(4);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            releaseConnection();
        }

        return status;
    }

    /**
     * Finds a user based on its credentials
     * @param username Username of the account
     * @param password Password of the acount
     * @return Null if credentials are wrong
     */
    public Account getAccountByCredentials(String username, String password) throws SQLException {
        ResultSet rs = null;
        try {
            getConnection();
            cStmt = connection.prepareCall("{call account_validate_user(?, ?)}");

            cStmt.setString(1, username);
            cStmt.setString(2, password);

            rs = cStmt.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            releaseConnection();
        }

        if (rs == null && rs.next() == false) {
            return null;
        }

        return new Account(rs.getString(1), rs.getString(2), rs.getDate(3).toLocalDate(),
                            rs.getInt(4));
    }
}
