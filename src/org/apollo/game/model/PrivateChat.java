package org.apollo.game.model;

import java.util.ArrayDeque;
import java.util.Queue;

import org.apollo.game.event.impl.PrivateChatEvent;
import org.apollo.game.event.impl.PrivateChatLoadedEvent;
import org.apollo.util.NameUtil;

public class PrivateChat {
	
	/**
	 * The instance.
	 */
	private static final PrivateChat instance = new PrivateChat();
	
	/**
	 * The events.
	 */
	private Queue<Player> events = new ArrayDeque<Player>();
	
	/**
	 * The current world.
	 */
	private final World world = World.getWorld();
	
	/**
	 * PM System.
	 */
	private int chatid = 1;
	
	/**
	 * Login event.
	 * @param player
	 */
	public void login(Player player) {
		events.add(player);
	}
	
	/**
	 * Logout event.
	 * @param player
	 */
	public void logout(Player player) {
		events.add(player);
	}
	
	/**
	 * Send the events to outstream.
	 * @throws Exception
	 */
	public void dispatch() {
		if (!events.isEmpty()) {
			for (Player login : events) {
				if (login != null) {
					if (!login.getFriends().loaded() && login.isActive()) {
						forceRefresh(login);
						login.send(new PrivateChatLoadedEvent(2));
						login.getFriends().setLoaded(true);
					}
					sendStatus(login.getUndefinedName());
				}
			}
			events.clear();
		}
	}
	
	/**
	 * Force a private chat refresh ONLOGIN
	 * @param player
	 */
	public void forceRefresh(Player player) {
		try {
			player.getFriends().refresh();
		} catch (Exception e) { }
	}
	
	/**
	 * Send the pm status.
	 * @param player
	 * @throws Exception
	 */
	private void sendStatus(String player) {
		for (Player all : world.getPlayerRepository()) {
			if (all.getFriends().getFriends().containsKey(player)) {
				try {
					all.getFriends().refresh(player);
				} catch (Exception e) { }
			}
		}
	}
	
	/**
	 * Send a private message.
	 * @param sender
	 * @param event
	 */
	public void sendPrivateMessage(Player sender, PrivateChatEvent event) {
		Player friend = (Player) World.getWorld().getPlayerRepository().get(NameUtil.encodeBase37(event.getFriend()));
		if (friend != null) {
			event.setFriendRights(sender.getPrivilegeLevel().toInteger());
			event.setLastId(chatid++);
			friend.send(event);
		}
	}
	
	/**
	 * Return the instance
	 * @return {@link PrivateChat}
	 */
	public static PrivateChat getInstance() {
		return instance;
	}

}