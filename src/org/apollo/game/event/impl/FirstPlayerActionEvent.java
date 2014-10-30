package org.apollo.game.event.impl;

/**
 * The first {@link ItemActionEvent}.
 * @author Graham
 */
public final class FirstPlayerActionEvent extends PlayerActionEvent {

	/**
	 * Creates the first item action event.
	 * @param interfaceId The interface id.
	 * @param id The item id.
	 * @param slot The item slot.
	 */
	public FirstPlayerActionEvent(int slot) {
		super(1, slot);
	}

}
