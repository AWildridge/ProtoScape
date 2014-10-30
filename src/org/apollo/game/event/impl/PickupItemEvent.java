package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 *
 * @author Arrowzftw
 */
public final class PickupItemEvent extends Event {
    
    private int itemId;
    private int x;
    private int y;
    
    public PickupItemEvent(int itemId, int x, int y) {
        this.itemId = itemId;
        this.x = x;
        this.y = y;
    }
    
    public int getItemId() {
        return itemId;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
}