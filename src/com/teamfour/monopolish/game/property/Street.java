package com.teamfour.monopolish.game.property;

/**
 * This class represents a property of type street. A street's rent level is adjusted by how many houses and hotels are on it
 */

public class Street extends Property {
    // Max levels of houses and hotels
    private final int MAX_HOUSES = 4;
    private final int MAX_HOTELS = 1;

    // Attributes
    private int[] rent = new int[9];        // Table of all rent levels, along with house and hotel prices
    private int houses;                     // Current amount of houses
    private int hotels;                     // Current amount of hotels

    /**
     * Constructor
     *
     * @param p_id          property id
     * @param name          name of property
     * @param price         the price of the property
     * @param position      the position the property is located on the board
     * @param categorycolor the category color of the property
     * @param owner         the current owner of the property
     */
    public Street(int p_id, String name, int price, int position, String categorycolor, String owner, int houses,
                  int hotels, boolean pawned) {
        super(p_id, name, price, position, categorycolor, owner, pawned);
        this.houses = houses;
        this.hotels = hotels;
        calculateRent();

        rent[7] = price;
        rent[8] = price;
    }

    /**
     * Returns the current rent for this street, based on houses, color sets and such
     * @param hasFullSet Check if the owner of this has a full color set
     * @return The current rent
     */
    public int getCurrentRent(boolean hasFullSet) {
        int currentRent = 0;

        // If owner has a full color set and no houses, just return rent times 2
        if (hasFullSet && houses == 0) {
            currentRent = rent[0] * 2;
        } else if (hotels == 0) {
            currentRent = rent[houses];
        } else {
            currentRent = rent[5];
        }

        return currentRent;
    }

    @Override
    public String[] getAllRent() {
        String[] rentString = new String[rent.length];
        for (int i = 0; i < rent.length; i++) {
            rentString[i] = "" + rent[i];
        }
        return rentString;
    }

    /**
     * Calculates rent of the different levels of house/hotel construction
     */
    private void calculateRent() {
        for (int i = 0; i < rent.length - 2; i++) {
            rent[i] = (int)(price * 0.3 * (i + 1));
        }
    }

    /**
     * Adds a house to this street
     * @return True if successful
     */
    public boolean addHouse() {
        if (houses == MAX_HOUSES)
            return false;

        houses++;
        return true;
    }

    /**
     * Adds a hotel if enough houses
     * @return True if successful
     */
    public boolean addHotel() {
        if (hotels == MAX_HOTELS || houses < MAX_HOUSES)
            return false;

        hotels++;
        return true;
    }

    // GETTERS

    public int getHousePrice() {
        return rent[7];
    }

    public int getHotelPrice() {
        return rent[8];
    }

    public int getHouses() {
        return houses;
    }

    public int getHotels() {
        return hotels;
    }

    public int getHouseAndHotels() {
        return houses + hotels;
    }
}
