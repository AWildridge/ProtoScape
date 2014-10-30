package org.apollo.game.event.impl;

import org.apollo.game.event.Event;
/**
 * OpenChatInterfaceEvent.java
 * @author The Wanderer
 */
public class OpenChatInterfaceEvent extends Event {
       
        /**
	 * The interface id.
	 */
	private final int id;

	/**
	 * Creates the event with the specified interface id.
	 * @param id The interface id.
	 */
	public OpenChatInterfaceEvent(int id) {
		this.id = id;
	}

	/**
	 * Gets the interface id.
	 * @return The interface id.
	 */
	public int getId() {
		return id;
	}
}
