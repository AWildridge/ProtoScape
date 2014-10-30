package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * An event which changes the state of a hidden interface component (i.e./e.g. the special attack bar on the
 * weapon tab).
 * @author Chris Fletcher
 */
public final class SetInterfaceComponentEvent extends Event {

	/**
	 * Visible flag.
	 */
	private final boolean visible;

	/**
	 * The component id.
	 */
	private final int component;

	/**
	 * Creates the interface component state event.
	 * @param component The compononent id.
	 * @param visible The flag for showing or hiding the component.
	 */
	public SetInterfaceComponentEvent(int component, boolean visible) {
		this.component = component;
		this.visible = visible;
	}

	/**
	 * Gets the visible flag.
	 * @return {@code true} if the component has been set to visible, {@code false} if not.
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * Gets the id of the interface component.
	 * @return The component id.
	 */
	public int getComponentId() {
		return component;
	}

}
