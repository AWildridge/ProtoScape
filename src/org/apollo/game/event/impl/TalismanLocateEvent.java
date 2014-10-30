package org.apollo.game.event.impl;

import org.apollo.game.event.Event;
/**
 * TalismanLocateEvent.java
 * @author The Wanderer
 */
public class TalismanLocateEvent extends Event {
    
    /**
     * The slot the item is located in.
     */
    int slot;
    
    /**
     * The item's id.
     */
    int itemId;
    
    /**
     * The interface id.
     */
    int interfaceId;
    
    public TalismanLocateEvent(int interfaceId, int itemId, int slot) {
        this.interfaceId = interfaceId;
        this.itemId = itemId;
        this.slot = slot;
    }
    
    /**
     * Gets the interface id.
     * @return The interface id.
     */
    public int getInterface() {
        return interfaceId;
    }
    
    /**
     * Gets the item's id.
     * @return The item's id.
     */
    public int getItemId() {
        return itemId;
    }
    
    /**
     * Gets the item's slot.
     * @return The item's slot.
     */
    public int getSlot() {
        return slot;
    }
}
