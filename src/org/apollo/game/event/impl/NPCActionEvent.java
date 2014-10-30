package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * NPCActionEvent.java
 * @author The Wanderer
 */
public abstract class NPCActionEvent extends Event {
    
    	/**
	 * The option number (1-4).
	 */
	private final int option;

	/**
	 * The NPC's index.
	 */
	private final int index;

	/**
	 * Creates a new NPC action event.
	 * @param option The option number.
	 * @param index The index of the object.
	 */
	public NPCActionEvent(int option, int index) {
		this.option = option;
		this.index = index;
	}

	/**
	 * Gets the option number.
	 * @return The option number.
	 */
	public int getOption() {
		return option;
	}

	/**
	 * Gets the index of the NPC.
	 * @return The index of the NPC.
	 */
	public int getIndex() {
		return index;
	}
}
