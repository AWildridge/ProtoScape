package org.apollo.game.scheduling.impl;

import org.apollo.game.model.GroundItem;
import org.apollo.game.model.Item;
import org.apollo.game.model.Player;
import org.apollo.game.model.Position;
import org.apollo.game.model.World;
import org.apollo.game.scheduling.ScheduledTask;

/**
 * Register's local ground items, such as 1 body rune in varrock or something like that.
 * @author Rodrigo Molina
 */
public class SpawnGroundItemsTask extends ScheduledTask {

        /**
         * Creates a new task.
         */
        public SpawnGroundItemsTask() {
    		super(10, false);
        }
    
        private Object[][] localItems = {
        	{ new Position(3209, 3214), new Item(4151) },//position, then item
        	
        };
        
        @Override
        public void execute() {
            for(Object[] o : localItems) {
        	Position pos = (Position) o[0];
        	Item item = (Item) o[1];
        	/*
        	 * Here it asks for a controller name.  I do now know how to add a ground item
        	 * without the controller name.. it can't spawn the item per player.. ya know
        	 */
        	GroundItem ground = new GroundItem("", item, pos);
        	if(!GroundItem.getInstance().contains(ground)) {
        	    for(Player player : World.getWorld().getPlayerRepository()) {
        	//	ground.create(player, item.getId(), item.getAmount(), pos);
        		//TODO: The item cannot be spawned more than once.. why is the ground items list being reseted?
        	    }
        	}
            }
        }
    	
}
