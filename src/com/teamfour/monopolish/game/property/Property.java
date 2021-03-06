package com.teamfour.monopolish.game.property;

import com.teamfour.monopolish.gui.Handler;

import java.util.ArrayList;

/**
 * Represents the properties in a game
 *
 * @author lisawil
 * @version 1.1
 */

public abstract class Property {
    // Property type constants
    public static final int STREET = 0;
    public static final int BOAT = 1;
    public static final int TRAIN = 2;

    //Attributes
    protected final int ID;
    protected final String NAME;
    protected final String CATEGORYCOLOR;
    protected int price;
    protected int position;
    protected boolean pawned = false;
    protected String owner;

    /**
     * Constructor
     *
     * @param p_id          property id
     * @param name          name of property
     * @param price         the price of the property
     * @param position      the position the property is located on the board
     * @param categorycolor the categorycolor of the property
     * @param owner         the current owner of the property
     */
    public Property(int p_id, String name, int price, int position, String categorycolor, String owner, boolean pawned) {
        this.ID = p_id;
        this.NAME = name;
        this.price = price;
        this.position = position;
        this.CATEGORYCOLOR = categorycolor;
        this.owner = owner;
        this.pawned = pawned;
    }

    public static ArrayList<Property> getFullColorSet(int gameId, String colorHex) {
        return Handler.getPropertyDAO().getColorSet(gameId, colorHex);
    }

    @Override
    public String toString() {
        String result = "name: " + NAME + "; Price: " + price + "; Position: " + position;
        return result;
    }

    /**
     * Compares the price of a property to another property
     *
     * @param otherP
     * @return
     */
    public int compareTo(Property otherP) {
        if (otherP == null) {
            return (-2);
        }
        if (otherP.getPrice() > this.price) {
            return (-1);
        }
        if (otherP.getPrice() < this.price) {
            return (1);
        } else {
            return (0);
        }
    }

    public boolean equals(Property otherP) {
        if (otherP == null) {
            return (false);
        }
        if (this == otherP) {
            return (true);
        }
        return (otherP.getId() == this.ID);
    }

    // SETTERS & GETTERS

    public int getId() {
        return ID;
    }

    public int getPosition() {
        return position;
    }

    public String getName() {
        return NAME;
    }

    /**
     * Returns an int of what type of property this object is
     *
     * @return
     */
    public int getType() {
        if (this instanceof Street)
            return STREET;
        else if (this instanceof Boat)
            return BOAT;
        else
            return TRAIN;
    }

    public abstract String[] getAllRent();

    public int getPrice() {
        return price;
    }

    public String getCategorycolor() {
        return CATEGORYCOLOR;
    }

    public boolean isPawned() {
        return pawned;
    }

    public void setPawned(boolean pawned) {
        this.pawned = pawned;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
