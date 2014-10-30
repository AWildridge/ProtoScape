package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * An {@link Event} which represents some sort of action on an item.
 * @author Graham
 */
public abstract class PlayerActionEvent extends Event {

	/**
	 * The option number (1-4).
	 */
	private final int option;

	
	/**
	 * The item id.
	 */
	private final int id;



	/**
	 * Creates the item action event.
	 * @param option The option number.
	 * @param interfaceId The interface id.
	 * @param id The id.
	 * @param slot The slot.
	 */
	public PlayerActionEvent(int option, int id) {
		this.option = option;
		this.id = id;
	}

	/**
	 * Gets the option number.
	 * @return The option number.
	 */
	public int getOption() {
		return option;
	}



	/**
	 * Gets the item id.
	 * @return The item id.
	 */
	public int getId() {
		return id;
	}

	

}
