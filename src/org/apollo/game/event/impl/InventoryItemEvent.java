package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * An {@link Event} which represents some sort of action on an item in an inventory.
 * @author Chris Fletcher
 */
public abstract class InventoryItemEvent extends Event {

	/**
	 * The option number (1-5).
	 */
	private final int option;

	/**
	 * The interface id.
	 */
	private final int interfaceId;

	/**
	 * The item id.
	 */
	private final int id;

	/**
	 * The item's slot.
	 */
	private final int slot;

	/**
	 * Creates the item action event.
	 * @param option The option number.
	 * @param interfaceId The interface id.
	 * @param id The id.
	 * @param slot The slot.
	 */
	protected InventoryItemEvent(int option, int interfaceId, int id, int slot) {
		this.option = option;
		this.interfaceId = interfaceId;
		this.id = id;
		this.slot = slot;
	}

	/**
	 * Gets the option number.
	 * @return The option number.
	 */
	public final int getOption() {
		return option;
	}

	/**
	 * Gets the interface id.
	 * @return The interface id.
	 */
	public final int getInterfaceId() {
		return interfaceId;
	}

	/**
	 * Gets the item id.
	 * @return The item id.
	 */
	public final int getId() {
		return id;
	}

	/**
	 * Gets the slot.
	 * @return The slot.
	 */
	public final int getSlot() {
		return slot;
	}

}
