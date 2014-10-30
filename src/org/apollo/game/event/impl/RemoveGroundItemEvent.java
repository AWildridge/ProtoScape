package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 *
 * @author Arrowzftw
 */
public final class RemoveGroundItemEvent extends Event {


        private int itemId;
        private int itemAmount;
    
	public RemoveGroundItemEvent(int itemId, int itemAmount) {
                this.itemId = itemId;
                this.itemAmount = itemAmount;             
	}
        
        public int getId() {
            return itemId;
        }
        
        public int getItemAmount() {
            return itemAmount;
        }

}