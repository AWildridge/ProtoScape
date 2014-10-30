package org.apollo.game.event.impl;

import org.apollo.game.event.Event;
/**
 * ItemOnObjectActionEvent.java
 * @author The Wanderer
 */
public class ItemOnObjectActionEvent extends Event {
    
    /**
     * The interface id.
     */
    private final int interfaceId;
    
    /**
     * The slot.
     */
    private final int slot;
    
    /**
     * The item's id.
     */
    private final int id;
    
    /**
     * The object's id.
     */
    private final int objectId;
    
    /**
     * The object's x coordinate.
     */
    private final int objectX;
    
    /**
     * The object's y coordinate.
     */
    private final int objectY;
    
    /**
     * Creates the fifth item action event.
     * @param interfaceId The interface id.
     * @param id The item id.
     * @param slot The item slot.
     */
    public ItemOnObjectActionEvent(int interfaceId, int id, int slot, int objectId, int objectX, int objectY) {
    	this.interfaceId = interfaceId;
        this.slot = slot;
        this.id = id;
        this.objectId = objectId;
        this.objectX = objectX;
        this.objectY = objectY;
    }
    
    /**
     * Gets the item's id.
     * @return The item's id.
     */
    public int getId() {
        return id;
    }
    
    /**
     * Gets the interface.
     * @return The interface.
     */
    public int getInterface() {
        return interfaceId;
    }
    
    /**
     * Gets the slot the item is in.
     * @return The slot.
     */
    public int getSlot() {
        return slot;
    }
    
    /**
     * Gets the object's id.
     * @return The object's id.
     */
    public int getObjectId() {
        return objectId;
    }
    
    /**
     * Gets the object's x coordinate.
     * @return The object's x coordinate.
     */
    public int getObjectX() {
        return objectX;
    }
    
    /**
     * Gets the object's y coordinate.
     * @return The object's y coordinate.
     */
    public int getObjectY() {
        return objectY;
    }
}
