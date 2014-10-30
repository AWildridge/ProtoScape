package org.apollo.game.model.inter.bank;

import org.apollo.game.model.Player;
import org.apollo.game.model.inter.EnterAmountListener;

/**
 * Listens to the enter amount interface which is sent when a player wants to enter a bank pin..
 *
 *
 * @author Rodrigo Molina
 */
public class BankPinEnterAmountListener implements EnterAmountListener {

    	/**
    	 * The player who is setting the bank pin
    	 */
    	private Player player;
    	
    	/**
    	 * Creates a new listener
    	 * 
    	 * @param player
    	 * 	the player
    	 */
    	public BankPinEnterAmountListener(Player player) {
    	    this.player = player;
    	}
    	
        @Override
        public void amountEntered(int amount) {
    		BankPin pin = new BankPin(player);
    		if(!pin.setBankPin(amount))
    		    return;
    		pin.displayInterface();
    		player.setBankPin(pin);
        }

}
