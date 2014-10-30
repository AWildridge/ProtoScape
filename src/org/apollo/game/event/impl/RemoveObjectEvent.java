package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * RemoveObjectEvent.java
 * @author The Wanderer
 */
public class RemoveObjectEvent extends Event {
    
    int type, face;
    
    public RemoveObjectEvent(int face, int type) {
        this.type = type;
        this.face = face;
    }
    
    public int getType() {
        return type;
    }
    
    public int getFace() {
        return face;
    }
}
