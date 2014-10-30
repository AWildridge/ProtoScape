package org.apollo.game.event.impl;

/**
 * The first {@link ItemActionEvent}.
 * @author Graham
 */
public final class SecondPlayerActionEvent extends PlayerActionEvent {

	/**
	 * Creates the first item action event.
	 * @param interfaceId The interface id.
	 * @param id The item id.
	 * @param slot The item slot.
	 */
	public SecondPlayerActionEvent(int slot) {
		super(2, slot);
	}

}
