package com.teamfour.monopolish.game.property;

/**
 * This class represents a property of type boat. A boat's rent level is adjusted by what the player got on the dice
 *
 * @author      eirikhem
 */

public class Train extends Property {
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
    public Train(int p_id, String name, int price, int position, String categorycolor, String owner, boolean pawned) {
        super(p_id, name, price, position, categorycolor, owner, pawned);
    }

    public int getRent(int trainsOwned, int diceFactor) {
        if (trainsOwned == 1) {
            return diceFactor * 80;
        } else {
            return diceFactor * 200;
        }
    }

    public String[] getAllRent() {
        String[] rent = new String[2];
        rent[0] = "Dice times 80";
        rent[1] = "Dice times 200";

        return rent;
    }
}
