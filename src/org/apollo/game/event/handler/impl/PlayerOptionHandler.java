package org.apollo.game.event.handler.impl;

import org.apollo.game.event.handler.EventHandler;
import org.apollo.game.event.handler.EventHandlerContext;
import org.apollo.game.event.impl.PlayerActionEvent;
import org.apollo.game.model.Player;
import org.apollo.game.model.Trade;
import org.apollo.game.model.World;

/**
 * Handles player options like trade and crap.
 * @author Arrowzftw
 */
public final class PlayerOptionHandler extends EventHandler<PlayerActionEvent> {

	@Override
        public void handle(EventHandlerContext ctx, Player player, PlayerActionEvent event) {
            if(event.getOption() == 3) {
                Player tradeWith = (Player) World.getWorld().getPlayerRepository().get(event.getId());
                tradeWith.sendMessage("Interact");
                if(tradeWith != null) {
                        if(tradeWith.getOfferOpponent()[1] == player) {
                            Trade.open(new Player[] {player, tradeWith});
                        } else if(tradeWith.getOfferOpponent()[1] != player) {
                            player.sendMessage("Sending trade request...");
                            tradeWith.sendMessage(player.getUndefinedName()+" :tradereq:");
                            player.setOfferOpp(1, tradeWith);
                        } else {
                            player.sendMessage("Ohter player is currently busy.");
                            ctx.breakHandlerChain();
                        }
                    }
            }   
        }

}