package com.teamfour.monopolish.game;

/**
 * Represents the tile data in the board. Each tile on the board is represented by
 * an Id to tell what kind of tile it is.
 *
 * @author      eirikhem
 * @version     1.4
 */

public class Board {
    // Static variables
    public static final int START = 0;
    public static final int PROPERTY = 1;
    public static final int GO_TO_JAIL = 2;
    public static final int FREE_PARKING = 3;
    public static final int JAIL = 4;
    public static final int CHANCE = 5;
    public static final int COMMUNITY_TAX = 6;

    // Attributes
    private int[] tiles = {0, 1, 6, 1, 1, 1, 5, 1, 1,
                            4, 1, 1, 1, 1, 1, 5, 1, 1,
                            3, 1, 5, 1, 1, 1, 1, 1, 1,
                            2, 1, 1, 1, 1, 5, 1, 1, 1};

    /**
     * Returns the tile type at the specified position
     * @param position Position integer
     * @return Tile type ID at this position
     */
    public int getTileType(int position) {
        return tiles[position];
    }
}