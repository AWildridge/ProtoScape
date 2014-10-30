package org.apollo.game.event.impl;

import org.apollo.game.event.Event;
import org.apollo.game.model.Item;
import org.apollo.game.model.Position;

/**
 * SendGroundItemEvent.java
 * @author The Wanderer
 */
public class SendGroundItemEvent extends Event {
    
    Item item;
    
    Position position;
    
    public SendGroundItemEvent(Item item, Position position) {
        this.item = item;
        this.position = position;
    }
    
    public Item getItem() {
        return item;
    }
    
    public Position getPosition() {
        return position;
    }
}
