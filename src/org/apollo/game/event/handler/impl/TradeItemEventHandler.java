package org.apollo.game.event.handler.impl;

import org.apollo.game.event.handler.EventHandler;
import org.apollo.game.event.handler.EventHandlerContext;
import org.apollo.game.event.impl.ItemActionEvent;
import org.apollo.game.model.Player;
import org.apollo.game.model.Trade;

/**
 * ShopEventHandler.java
 * @author The Wanderer
 */
public class TradeItemEventHandler extends EventHandler<ItemActionEvent> {

    public int getOptionAmount(int option) {
        switch(option) {
            default:
                return 1;
            case 1:
                return 1;
            case 2:
                return 5;
            case 3:
                return 10;
            case 4:
                return 2147000000;
                
                
        }
    }
    @Override
    public void handle(EventHandlerContext ctx, Player player, ItemActionEvent event) {
        int interfaceId = event.getInterfaceId();
        if (player.getTradeStage() == 1) {
            if(interfaceId == 3322) {
                Trade.deposit(player, event.getSlot(), event.getId(), getOptionAmount(event.getOption()));
                ctx.breakHandlerChain();
            } else if(interfaceId == 3415) {
                Trade.withdraw(player, event.getSlot(), event.getId(), getOptionAmount(event.getOption()));
            }
        }
        
    }
}
