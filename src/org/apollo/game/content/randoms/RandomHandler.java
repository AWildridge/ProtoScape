package org.apollo.game.content.randoms;

import java.util.ArrayList;
import java.util.List;

import org.apollo.game.content.randoms.impl.EvilChicken;
import org.apollo.game.model.Player;
import org.apollo.util.Misc;

/**
 * An random event is anything, from Dr.Jekyl to Evil Chicken, to Evil Spirit (Tree), etc, generated when
 * performing certain actions.
 *
 * todo; could be revised into something much simpler
 *
 * @author Rodrigo Molina
 */
public class RandomHandler {

    	/**
    	 * In here, we will check if an random event is possible or needs to be called
    	 * and activated.
    	 * 
    	 * @param player
    	 * 	The player doing the action
    	 * @param skill
    	 * 	True if the action is skill-generated or not.
    	 */
    	public static void doAction(Player player, String skill) {
    	    if(generateRandom(player)) {
    		RandomEvent random = null;
    		for(RandomEvent rand : events) {
    		    if(rand.getSkill().equals("anything") && Misc.random(10) == 0) {
    			random = rand;
    			break;
    		    }
    		    if(rand.getSkill().equalsIgnoreCase(skill)) {
    			//now choose an event.
    			int r = Misc.random(rand.getRandomCount());
    			if(r == 0) {
    			    random = rand;
    			    break;
    			}
    		    } else if(!skill.equals(rand.getSkill())) {
    			//ignore the skill activated events if the player is not performing an skill action.
    			continue;
    		    } else {
    			//don't know, guess do the event?
    		    }
    		}
    		if(random != null) {
    		    random.activateEvent(player);
    		}
    	    } else {
    		System.out.println("NO RANDOM FOR THIS GUY!!");
    	    }
    	}
    	
    	/**
    	 * The list of random events.
    	 */
    	private static List<RandomEvent> events = new ArrayList<RandomEvent>();
    	
    	static {
    	    events.add(new EvilChicken());
    	}
    	
    	/**
    	 * Checks if the player is allowed and able to activate an random event.
    	 * 
    	 * @param player
    	 * 	The player.
    	 * @return true if it is time to start a random.
    	 */
    	private static boolean generateRandom(Player player) {
    	    //do checks.
    	    if(!player.isActive())
    		return false;
    	    if(player.isInCombat())
    		return false;
    	    int r = Misc.random(80);
    	    if(r == 0) 
    		return true;
    	    return false;
    	}
    	
    	/**
    	 * This interface represents an single random event.
    	 *
    	 * 
    	 * @author Rodrigo Molina
    	 */
    	public interface RandomEvent {
    	    /**
    	     * Activates the random event, by spawning an certain npc, or doing something else.
    	     * 
    	     * @param player
    	     * 		The player who needs to get raped by the event!
    	     */
    	    public void activateEvent(Player player);
    	    
    	    /**
    	     * Get's the random count in which the event is to be performed.
    	     * 
    	     * @return the integer amount of the count.
    	     */
    	    public int getRandomCount();
    	    
    	    /**
    	     * This will return the string of the skill that activates this random.
    	     * 
    	     * @return the skill..
    	     */
    	    public String getSkill();
    	}
}
