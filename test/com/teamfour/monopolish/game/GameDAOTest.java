package com.teamfour.monopolish.game;

import com.teamfour.monopolish.database.ConnectionPool;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class GameDAOTest {
    private static GameDAO instance;

    @BeforeAll
    public static void setInstance() {
        instance = new GameDAO();
        try {
            ConnectionPool.create();
        } catch (SQLException e) {

        }

    }

    @Test
    public void testGetCurrentPlayer() {
            assertEquals(-1, instance.getCurrentPlayer(4));
            assertEquals(15, instance.getCurrentPlayer(5));
    }

    @Test
    public void testFinishGame() {
        assertTrue(instance.finishGame(4));
        assertTrue(instance.finishGame(5));
    }
}
