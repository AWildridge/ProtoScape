package org.apollo.game.event.impl;

import org.apollo.game.event.Event;
import org.apollo.util.NameUtil;

public class PrivateChatEvent extends Event {

	/**
	 * The friend.
	 */
	private final String friend;
	
	/**
	 * The friend.
	 */
	private final long friendlong;
	
	/**
	 * The friend's rights.
	 */
	private int friendrights;
	
	/**
	 * The message.
	 */
	private final String message;
	
	/**
	 * The compressed message.
	 */
	private final byte[] emessage;
	
	/**
	 * The message id.
	 */
	private int lastid;
	
	/**
	 * Create a new private chat event.
	 * @param uncompressed
	 * @param length
	 * @param friend
	 */
	public PrivateChatEvent(String uncompressed, byte[] length, String friend) {
		this.message = uncompressed;
		this.emessage = length;
		this.friend = friend;
		this.friendlong = NameUtil.encodeBase37(this.friend);
	}
	
	/**
	 * The friend.
	 * @return {@link String}
	 */
	public String getFriend() {
		return friend;
	}
	
	/**
	 * The message.
	 * @return {@link String}
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * The message compressed.
	 * @return {@link byte}
	 */
	public byte[] getMessageCompressed() {
		return emessage;
	}
	
	/**
	 * The friend.
	 * @return {@link long}
	 */
	public long getFriendLong() {
		return friendlong;
	}
	
	/**
	 * The friend's rights.
	 * @return {@link Integer}
	 */
	public int getFriendRights() {
		return friendrights;
	}
	
	/**
	 * Set the friends rights.
	 * @param rights
	 */
	public void setFriendRights(int rights) {
		this.friendrights = rights;
	}
	
	public int getLastId() {
		return lastid;
	}
	
	/**
	 * Private chat id.
	 * @param id
	 */
	public void setLastId(int id) {
		this.lastid = id;
	}

}