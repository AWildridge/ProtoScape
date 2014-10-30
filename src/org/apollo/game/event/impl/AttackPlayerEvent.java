package org.apollo.game.event.impl;

import org.apollo.game.event.Event;
/**
 * AttackPlayerEvent.java
 * @author The Wanderer
 */
public class AttackPlayerEvent extends Event {
    
    int index;
    
    public AttackPlayerEvent(int index) {
        this.index = index;
    }
    
    public int getIndex() {
        return index;
    }
    
}
