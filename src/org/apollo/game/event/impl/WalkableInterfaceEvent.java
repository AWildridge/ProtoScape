package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * An interface that is walkable.  Meaning you can walk around while it is open.
 *
 *
 * @author Rodrigo Molina
 */
public class WalkableInterfaceEvent extends Event {

    	/**
    	 * The ID of the interfac.e
    	 */
    	private final int interfaceId;
    	
    	/**
    	 * Creates a new walkable interface event.
    	 * 
    	 * @param interfaceId
    	 * 	the id.
    	 */
    	public WalkableInterfaceEvent(int interfaceId) {
    	    this.interfaceId = interfaceId;
    	}
    	
    	/**
    	 * Get's the interface id.
    	 * 
    	 * @return the id
    	 */
    	public int getInterfaceId() {
    	    return interfaceId;
    	}
}
