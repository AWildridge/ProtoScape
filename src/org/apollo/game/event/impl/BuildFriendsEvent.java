package org.apollo.game.event.impl;

import org.apollo.game.event.Event;
import org.apollo.util.NameUtil;

public final class BuildFriendsEvent extends Event {
	
	/**
	 * The opcode
	 */
	private final int opcode;

	/**
	 * The friend.
	 */
	private final String friend;
	
	/**
	 * The friend long.
	 */
	private final long longfriend;
	
	/**
	 * The chat event.
	 */
	private int chat = 0;

	/**
	 * Create a new private chat event.
	 * @param friend
	 * @param opcode
	 */
	public BuildFriendsEvent(String friend, int opcode) {
		this.opcode = opcode;
		this.friend = friend;
		this.longfriend = NameUtil.encodeBase37(friend);
	}

	/**
	 * Gets the friend.
	 * @return The friend.
	 */
	public String getFriend() {
		return friend;
	}

	/**
	 * Gets the opcode.
	 * @return The opcode.
	 */
	public int getOpcode() {
		return opcode;
	}

	/**
	 * Encode the name to a long
	 * @return {@link Long}
	 */
	public long getFriendLong() {
		return longfriend;
	}
	
	/**
	 * Get the chat id.
	 * @return {@link Integer}
	 */
	public int getChat() {
		return chat;
	}
	
	/**
	 * Set the chat id
	 * @param {@link Integer} id
	 */
	public void setChat(int id) {
		this.chat = id;
	}
	
}