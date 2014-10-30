package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * An event which opens a dialogue interface (which appears in the chat box).
 * @author Chris Fletcher
 */
public final class OpenInterfaceDialogueEvent extends Event {

	/**
	 * The interface id.
	 */
	private final int interfaceId;

	/**
	 * Creates a new event with the specified interface id.
	 * @param interfaceId The interface id.
	 */
	public OpenInterfaceDialogueEvent(int interfaceId) {
		this.interfaceId = interfaceId;
	}

	/**
	 * Gets the interface id.
	 * @return The interface id.
	 */
	public int getInterfaceId() {
		return interfaceId;
	}

}