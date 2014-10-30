package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * SendNewObjectEvent.java
 * @author The Wanderer
 */
public class CreateObjectEvent extends Event {
    
    int id, type, face;
    
    public CreateObjectEvent(int id, int face, int type) {
        this.id = id;
        this.type = type;
        this.face = face;
    }
    
    public int getId() {
        return id;
    }
    
    public int getType() {
        return type;
    }
    
    public int getFace() {
        return face;
    }
}
