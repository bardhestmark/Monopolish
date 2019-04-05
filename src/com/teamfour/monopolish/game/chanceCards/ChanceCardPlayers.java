package com.teamfour.monopolish.game.chanceCards;

import com.teamfour.monopolish.gui.controllers.Handler;

/**
 * Class for chance cards with transfers between players
 */
public class ChanceCardPlayers extends ChanceCard {
    private String[] usernames;

    ChanceCardPlayers(String msg, String logoPath, int amount) {
        super(msg, logoPath, amount);
    }

    /**
     * Set a list of players that should be included in the money transaction.
     * @param usernames Target players
     */
    public void setPlayers(String[] usernames) {
        // Deep copy
        this.usernames = new String[usernames.length-1];

        // Add to array if user is not current user
        for (int i = 0; i < usernames.length; i++) {
            if (!usernames[i].equals(super.getUSERNAME()))
                this.usernames[i] = usernames[i];
        }
    }

    /**
     * Transfer money from between players
     */
    public void moneyTransaction() {
        if (this.usernames == null) return;

        for (String u : this.usernames) {
            // Amount is negative, transfer from current user to player
            if (super.getAmount() < 0)
                Handler.getCurrentGame().getEntities().transferMoneyFromTo(super.getUSERNAME(), u, super.getAmount() * -1);

            // Amount is positive, transfer from player to current user
            else
                Handler.getCurrentGame().getEntities().transferMoneyFromTo(u, super.getUSERNAME(), super.getAmount());
        }
    }


}
