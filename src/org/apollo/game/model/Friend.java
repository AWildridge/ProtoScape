package org.apollo.game.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apollo.game.event.impl.BuildFriendsEvent;

/**
 * Holds the friends
 * @author Steve
 */

public final class Friend {
	
	/**
	 * Hold the friends.
	 */
	private Map<String, Event> friends = new HashMap<String, Event>();
	
	/**
	 * Hold the friend capacity.
	 */
	private final int capacity;
	
	/**
	 * Hold the chat id.
	 */
	private int lastchat;
	
	/**
	 * The friend size.
	 */
	private int size = 0;
	
	/**
	 * Holds the player.
	 */
	private final Player player;

	/**
	 * Are we loaded
	 */
	private boolean loaded = false;
	
	/**
	 * Event of input.
	 */
	public enum Event {
		/**
		 * Adding a user.
		 */
		FRIEND,
		/**
		 * Ignoring a user.
		 */
		IGNORE
	}
	
	/**
	 * Are we loaded
	 * @return {@link Boolean}
	 */
	public boolean loaded() {
		return loaded;
	}
	
	/**
	 * Set the load
	 * @param loaded
	 */
	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}
	
	/**
	 * TODO: Make this do something
	 */
	public Friend(Player player) {
		this.player = player;
		this.capacity = player.isMembers() ? 200 : 100;
	}
	
	/**
	 * Add a user.
	 * 
	 * @param who The user
	 * @param what Friend or ignore
	 * @throws Exception 
	 */
	public void add(String who, Event what, boolean loader) throws Exception {
		if (loader) {
			friends.put(who,  what);
		} else {
			if (size > capacity) {
				throw new Exception("Friends list full.");
			} else {
				if (what == Event.FRIEND) {
					if (friends.get(who) != null) {
						if (friends.get(who).equals(Event.IGNORE)) {
							throw new Exception("Friend is already ignored.");
						} else if (friends.get(who).equals(Event.FRIEND)) {
							throw new Exception("Friend is already added.");
						} else {
							friends.put(who, what);
						}
					} else {
						friends.put(who, what);
					}
				} else if (what == Event.IGNORE) {
					if (friends.get(who) != null) {
						if (friends.get(who).equals(Event.FRIEND)) {
							throw new Exception("Friend is already added.");
						} else if (friends.get(who).equals(Event.IGNORE)) {
							throw new Exception("Friend is already ignored.");
						} else {
							friends.put(who, what);
						}
					} else {
						friends.put(who, what);	
					}
				}
			}
		}
		this.size = friends.size();
	}
	
	/**
	 * Delete a user.
	 * @param who The user.
	 * @param what Friend or ignore.
	 * @throws Exception
	 */
	public void delete(String who, Event what) throws Exception {
		if (what == Event.FRIEND) {
			if (friends.get(who) != null) {
				if (friends.get(who).equals(Event.IGNORE)) {
					throw new Exception("Friend is ignored.");
				} else if (!friends.get(who).equals(Event.FRIEND)) {
					throw new Exception("Friend is not added.");
				} else {
					friends.remove(who);
				}
			} else {
				friends.remove(who);
			}
		} else if (what == Event.IGNORE) {
			if (friends.get(who) != null) {
				if (friends.get(who).equals(Event.FRIEND)) {
					throw new Exception("Friend is added.");
				} else if (!friends.get(who).equals(Event.IGNORE)) {
					throw new Exception("Friend is not ignored.");
				} else {
					friends.remove(who);
				}
			} else {
				friends.remove(who);
			}
		}
		this.size = friends.size();
	}
	
	/**
	 * Get the friend capacity.
	 * @return {@link Capacity}
	 */
	public int capacity() {
		return capacity;
	}
	
	/**
	 * Get the friends size.
	 * @return {@link Integer}
	 */
	public int size() {
		return size;
	}
	
	/**
	 * Gets the friends list.
	 * @return {@link Map<String, Friends>}
	 */
	public Map<String, Event> getFriends() {
		return this.friends;
	}
	
	/**
	 * Get the value for the event.
	 * @param what The event.
	 * @return {@link Integer}
	 * @throws Exception
	 */
	public int getValue(Event what) throws Exception {
		if (what == Event.FRIEND) {
			return 1;
		} else if (what == Event.IGNORE) {
			return 2;
		} else {
			throw new Exception("Invalid event.");
		}
	}
	
	/**
	 * Get the event for the value.
	 * @param what The event.
	 * @return {@link Event}
	 * @throws Exception
	 */
	public Event getValue(int what) throws Exception {
		if (what == 1) {
			return Event.FRIEND;
		} else if (what == 2) {
			return Event.IGNORE;
		} else {
			throw new Exception("Invalid event.");
		}
	}
	
	/**
	 * Manually refresh the specified player.
	 * @param player
	 * @throws Exception
	 */
	public void refresh(String player) throws Exception {
		for (Entry<String, Event> entry : friends.entrySet()) {
			if (entry.getKey().equals(player)) {
				boolean online = World.getWorld().isPlayerOnline(player);
				BuildFriendsEvent make = new BuildFriendsEvent(player, getValue(entry.getValue()));
				if (online)
					make.setChat(1);
				else
					make.setChat(0);
				this.player.send(make);
			}
		}
	}
	
	/**
	 * Refresh the friend list.
	 * TODO: Finish
	 * @throws Exception 
	 */
	public void refresh() throws Exception {
		for (Entry<String, Event> entry : friends.entrySet()) {
			boolean online = World.getWorld().isPlayerOnline(entry.getKey());
			BuildFriendsEvent make = new BuildFriendsEvent(entry.getKey(), getValue(entry.getValue()));
			if (online) {
				make.setChat(1);
			} else {
				make.setChat(0);
			}
			player.send(make);
		}
	}
	
	/**
	 * Get the chat id
	 * @return {@link Integer}
	 */
	public int getLastId() {
		return lastchat++;
	}

}