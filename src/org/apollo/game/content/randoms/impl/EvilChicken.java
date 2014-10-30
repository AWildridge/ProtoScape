package org.apollo.game.content.randoms.impl;

import org.apollo.game.content.randoms.RandomHandler.RandomEvent;
import org.apollo.game.model.Player;

/**
 *
 *
 * @author Rodrigo Molina
 */
public class EvilChicken implements RandomEvent {

        @Override
        public void activateEvent(Player player) {
    		//TODO: spawn the npc based off the players combat level.
            player.sendMessage("An evil chicken has been spawned!!!!!!!!!!");
        }
    
        @Override
        public int getRandomCount() {
    		return 40;
        }

	@Override
	public String getSkill() {
	    return "anything";
	}
}
