package org.apollo.game.content.quest.impl.black;

import org.apollo.game.content.quest.Quest;
import org.apollo.game.content.quest.impl.steps.Step;
import org.apollo.game.model.Player;

/**
 * The first step of black knights fortress
 *
 *
 * @author Rodrigo Molina
 */
public class FirstStep extends Step {

    	/**
    	 * The player.
    	 */
    	private Player player;
    	
    	/**
    	 * Creates a new first step.
    	 * 
	 * @param quest
	 * 	The quest whom this step belongs to.
    	 * @param player
    	 * 	The player.
    	 */
    	public FirstStep(Quest quest, Player player) {
    	    super(quest);
    	    this.player = player;
    	}

        @Override
        public void executeStep() {
            switch(getStep()) {
            
            }
        }
    
        @Override
        public String[] getText() {
            return new String[] {
        	   "",
        	   "I can start this quest by.."
            };
        }
    
        @Override
        public boolean moveOn() {
            return true;
        }

}
