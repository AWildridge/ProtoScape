package org.apollo.game.model.inter.dialog.impl;

import org.apollo.game.model.Animation;
import org.apollo.game.model.Player;
import org.apollo.game.model.Position;
import org.apollo.game.model.inter.dialog.DialogueListener;

/**
 * Listens to the dialogues that are sent when climbing ladders.
 *
 *
 * @author Rodrigo Molina
 */
public class LadderListener implements DialogueListener {

    	/**
    	 * The player.
    	 */
    	private Player player;
    	
    	/**
    	 * new ladder listener.
    	 * 
    	 * @param player
    	 *	the player.
    	 */
    	public LadderListener(Player player) {
    	    this.player = player;
    	}
    	
        @Override
        public void interfaceClosed() {
            player.setDialog(null);
        }
    
        @Override
        public boolean buttonClicked(int button) {
            switch(button) {
            case 2461://up
        	player.teleport(new Position(player.getPosition().getX(), player.getPosition().getY(), player.getHeight() + 1));
        	player.playAnimation(Animation.create(828));
        	break;
            case 2462://down
        	player.teleport(new Position(player.getPosition().getX(), player.getPosition().getY(), player.getHeight() - 1));
        	player.playAnimation(Animation.create(828));
        	break;
            }
    		return false;
        }
    
        @Override
        public void continued() {
        }

}
