package org.apollo.game.content;

import org.apollo.game.event.impl.ObjectActionEvent;
import org.apollo.game.model.Animation;
import org.apollo.game.model.Player;
import org.apollo.game.model.World;
import org.apollo.game.scheduling.ScheduledTask;

/**
 * A class to handle miscellanous object events.
 *
 *
 * @author Rodrigo Molina
 */
public class ObjectMisc {

    	/**
    	 * Pick's an object from a field.
    	 * 
    	 * @param player
    	 * 	The player picking.
    	 * @param event
    	 * 	The event of the picking.
    	 * @param itemId
    	 * 	The item id to give when picking.
    	 */
    	public static void pickObject(final Player player, ObjectActionEvent event, int itemId) {
           	if(player.hasAttributeTag("picking"))
        	    return;
        	player.playAnimation(Animation.BURY);
        	player.getInventory().add(itemId);
        	player.getAttributeTags().add("picking");
        	player.getWalkingQueue().addStep(event.getPosition());
        	World.getWorld().schedule(new ScheduledTask(2, false) {

		    @Override
		    public void execute() {
			player.getAttributeTags().remove("picking");
                	/*World.getWorld().schedule(new ScheduledTask(20, false) {

			    @Override
			    public void execute() {
				player.sendNewObject(event.getPosition(), 312, 1, 10);
				stop();
			    }
                	  
                	});*/
                	stop();
		    }
        	  
        	});
    	}
    	
}
