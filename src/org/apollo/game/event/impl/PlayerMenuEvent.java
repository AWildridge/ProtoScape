package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * An {@link Event} for a player menu upon right click.
 * @author Lucas Beau
 * @version 0.1
 */
public final class PlayerMenuEvent extends Event {

    /**
     * The place in the menu
     */
    private final int optionNumber;

    /**
     * Checks if the menu is in the correct order
     * Follow ---> Trade
     */
    private final boolean order;

    /**
     * The text on player menu <Trade>
     */
    private final String message;

    /**
     * The player menus event
     * @param optionNumber the place in menu
     * @param order the order of options
     * @param message the message on the menu
     */
    public PlayerMenuEvent(int optionNumber, boolean order, String message) {
        this.optionNumber = optionNumber;
        this.order = order;
        this.message = message;
    }

    /**
     * The place in the menus
     * @return the place
     */
    public int getOptionNumber() {
        return optionNumber;
    }

    /**
     * The order of options
     * @return the order of the options
     */
    public boolean getOrder() {
        return order;
    }

    /**
     * The message in the menu
     * @return the message in the menu
     */
    public String getMessage() {
        return message;
    }
    
}
