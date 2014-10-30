package org.apollo.game.event.handler.impl;

import org.apollo.game.event.handler.EventHandler;
import org.apollo.game.event.handler.EventHandlerContext;
import org.apollo.game.event.impl.ClosedInterfaceEvent;
import org.apollo.game.model.Player;
import org.apollo.game.model.Trade;

/**
 * An {@link EventHandler} for the {@link ClosedInterfaceEvent}.
 * @author Graham
 */
public final class ClosedInterfaceEventHandler extends EventHandler<ClosedInterfaceEvent> {

	@Override
	public void handle(EventHandlerContext ctx, Player player, ClosedInterfaceEvent event) {
                if(player.getTradeStage() >= 1) {
                    Trade.declineTrade(new Player[] {player, player.getOfferOpponent()[0]});
                }
		player.getInterfaceSet().interfaceClosed();
	}

}
