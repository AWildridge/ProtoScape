package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * An event sent when the client clicks a button.
 * @author Graham
 */
public final class SetSidebarEvent extends Event {

	/**
	 * The interface id.
	 */
	private final int interfaceId;
        private final int menuId;

	/**
	 * Creates the button event.
	 * @param interfaceId The interface id.
	 */
	public SetSidebarEvent(int menuId, int interfaceId) {
                this.menuId = menuId;
		this.interfaceId = interfaceId;
	}

	/**
	 * Gets the interface id.
	 * @return The interface id.
	 */
	public int getInterfaceId() {
		return interfaceId;
	}
        
        public int getMenuId() {
		return menuId;
	}

}
