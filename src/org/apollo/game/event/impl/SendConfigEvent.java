package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * SendConfigEvent.java
 * @author The Wanderer
 */
public class SendConfigEvent extends Event {
    
    int configId;
    
    int state;
    
    public SendConfigEvent(int configId, int state) {
        this.configId = configId;
        this.state = state;
    }
    
    public int getConfigId() {
        return configId;
    }
    
    public int getState() {
        return state;
    }
}
