package org.apollo.game.event.handler.impl;

import org.apollo.game.event.handler.EventHandler;
import org.apollo.game.event.handler.EventHandlerContext;
import org.apollo.game.event.impl.BuildFriendsEvent;
import org.apollo.game.model.Friend.Event;
import org.apollo.game.model.Player;

public class BuildFriendsEventHandler extends EventHandler<BuildFriendsEvent> {

	@Override
	public void handle(EventHandlerContext ctx, Player player, BuildFriendsEvent event) {
		try {
			switch (event.getOpcode()) {
				case 188: // Add friend
					player.addFriend(event.getFriend(), Event.FRIEND);
					player.getFriends().refresh();
				break;
				case 215: // Delete friend
					player.deleteFriend(event.getFriend(), Event.FRIEND);
				break;
				case 133: // Add ignore
					player.addFriend(event.getFriend(), Event.IGNORE);
				break;
				case 74: // Remove ignore
					player.deleteFriend(event.getFriend(), Event.IGNORE);
				break;
			}
		} catch (Exception e) { e.printStackTrace(); }
	}

}