package org.apollo.game.event.handler.impl;

import org.apollo.game.event.handler.EventHandler;
import org.apollo.game.event.handler.EventHandlerContext;
import org.apollo.game.event.impl.PrivateChatEvent;
import org.apollo.game.model.Player;
import org.apollo.game.model.PrivateChat;

public final class PrivateChatEventHandler extends EventHandler<PrivateChatEvent> {

	@Override
	public void handle(EventHandlerContext ctx, Player player, PrivateChatEvent event) {
		PrivateChat.getInstance().sendPrivateMessage(player, event);
	}

}