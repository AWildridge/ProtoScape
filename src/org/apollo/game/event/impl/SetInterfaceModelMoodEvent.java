package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * An event which is sent to the client to make a character model on an interface play a certain animation.
 * @author Chris Fletcher
 */
public final class SetInterfaceModelMoodEvent extends Event {

	/**
	 * The interface id.
	 */
	private final int interfaceId;

	/**
	 * The model's mood id.
	 */
	private final int mood;

	/**
	 * Creates a new set interface NPC model's mood event.
	 * @param interfaceId The interface id.
	 * @param mood The model's mood id.
	 */
	public SetInterfaceModelMoodEvent(int interfaceId, int mood) {
		this.interfaceId = interfaceId;
		this.mood = mood;
	}

	/**
	 * Gets the interface id.
	 * @return The interface id.
	 */
	public int getInterfaceId() {
		return interfaceId;
	}

	/**
	 * Gets the model's mood id.
	 * @return The model's mood id.
	 */
	public int getMood() {
		return mood;
	}

}