package org.apollo.game.sync.block;

public class ForceChatBlock extends SynchronizationBlock {

	/**
	 * The string.
	 */
	private final String text;

	/**
	 * Creates the string block.
	 * @param string The graphic.
	 */
	public ForceChatBlock(String text) {
		this.text = text;
	}

	/**
	 * Gets the string.
	 * @return The string.
	 */
	public String getText() {
		return text;
	}

}