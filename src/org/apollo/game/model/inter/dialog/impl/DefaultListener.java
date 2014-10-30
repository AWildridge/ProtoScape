package org.apollo.game.model.inter.dialog.impl;

import org.apollo.game.model.Player;
import org.apollo.game.model.inter.dialog.DialogueListener;

/**
 * An default listener which is used to listen to dialogues that are only to be sent one time, and 
 * is not a huge back and forth chat.
 *
 *
 * @author Rodrigo Molina
 */
public class DefaultListener implements DialogueListener {
    
    	/**
    	 * The player.
    	 */
    	private Player player;
    	
    	/**
    	 * Creates a new default listener.
    	 * 
    	 * @param player
    	 * 	The player.
    	 */
    	public DefaultListener(Player player) {
    	    this.player = player;
    	}
    	
        @Override
        public void interfaceClosed() {
            	player.setDialog(null);
        }
    
        @Override
        public boolean buttonClicked(int button) {
    		return false;
        }
    
        @Override
        public void continued() {
    		player.getInterfaceSet().close();
        }

}
