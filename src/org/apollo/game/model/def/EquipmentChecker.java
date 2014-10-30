package org.apollo.game.model.def;

import org.apollo.game.model.Player;

/**
 * Here we will check for anything else that we have missed, quest requirements and such.
 *
 *
 * @author Rodrigo Molina
 */
public class EquipmentChecker {

    	/**
    	 * Check's for any other equipment requirements.
    	 * 
    	 * @param player
    	 * 	The player.
    	 * @param itemId
    	 * 	The item id that this player wants to put on.
    	 * @return false if they cannot wear it.
    	 */
    	public static boolean checkEquipRequirements(Player player, int itemId) {
    	    switch(itemId) {
    	    case 5698:
    		//check for quest Lost City
    		break;
    	    }
    	    return true;
    	}
}
