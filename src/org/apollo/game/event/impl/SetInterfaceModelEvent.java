package org.apollo.game.event.impl;

import org.apollo.game.event.Event;
/**
 * SetInterfaceModelEvent.java
 * @author The Wanderer
 */
public class SetInterfaceModelEvent extends Event {
    
    /**
     * The interface model's zoom.
     */
    private int zoom;

    /**
     * The interface id.
     */
    private int interfaceId;
    
    /**
     * The model id.
     */
    private int model;
    
    public SetInterfaceModelEvent(int interfaceId, int zoom, int model) {
        this.interfaceId = interfaceId;
        this.zoom = zoom;
        this.model = model;
    }
    
    /**
     * Gets the model's zoom.
     * @return The model's zoom.
     */
    public int getZoom() {
        return zoom;
    }
    
    /**
     * Gets the interface id.
     * @return The interface id.
     */
    public int getInterfaceId() {
        return interfaceId;
    }
    
    /**
     * Gets the model id.
     * @return The model id.
     */
    public int getModel() {
        return model;
    }
    
}
