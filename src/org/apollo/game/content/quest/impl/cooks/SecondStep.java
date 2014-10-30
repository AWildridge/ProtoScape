package org.apollo.game.content.quest.impl.cooks;

import org.apollo.game.content.quest.Quest;
import org.apollo.game.content.quest.impl.steps.Step;
import org.apollo.game.model.Player;

/**
 *
 *
 *
 * @author Rodrigo Molina
 */
public class SecondStep extends Step {

    	
    	/**
    	 * The player..
    	 */
    	private Player player;
    	
    	/**
    	 * Creates a new second step.
    	 * 
    	 * @param quest
    	 * 	The quest of the step.
    	 * @param player
    	 * 	The player doing this step.
    	 */
        public SecondStep(Quest quest, Player player) {
    		super(quest);
    		this.player = player;
        }
    
        private boolean hopperSet;
        
        @Override
        public void executeStep() {
		switch(getStep()) {
		case -1:
		    increaseStep();
		case 0:
		    hopperSet = true;
		    increaseStep();
		    break;
		case 1:
		    if(hopperSet) {
			//play the animation..
			//and set the flour bin
			//3166, 3306, 0 = 1781 set it to have flour
		    }
		    increaseStep();
		    break;
		case 2://Empty flour bin
		    if(player.getInventory().contains(1931)) {
			player.getInventory().remove(1931);
			player.getInventory().add(1933);
			this.increaseStep();
		    }
		    break;
		case 3:
		    //now they have come back to talk to the Cook.
		    if(player.getInventory().contains(1944) && player.getInventory().contains(1933) && player.getInventory().contains(1927)) {
			
		    } else {
			//go back kid.
		    }
		    break;
		}
        }
    
        @Override
        public String[] getText() {
            return new String[] {
            		"",
            		"@str@Talk to the Cook inside the Lumbridge Castle.",
                	"",
                	"The cook said that he needs to bake a cake for the Dukes'",
                	"birthday or else he would get sacked.",
                	"",
                	"You now need to grab ingredients that are needed",
                	"to bake a cake for the cook.",
                	"",
                	"The ingredients needed are:",
                	(player.getInventory().contains(1944) ? "@gre@" : "@red@")+"Egg",
                	(player.getInventory().contains(1933) ? "@gre@" : "@red@")+"Extra fine flour",
                	(player.getInventory().contains(1927) ? "@gre@" : "@red@")+"Top-quality milk",
                	"",
                	"Look around Lumbridge for these ingredients.  Some will",
                	"take some more looking around than others."
            };
        }
        @Override
        public boolean moveOn() {
    		return true;
        }

}
