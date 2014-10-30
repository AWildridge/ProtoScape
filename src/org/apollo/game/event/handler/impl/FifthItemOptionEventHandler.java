package org.apollo.game.event.handler.impl;

import org.apollo.game.event.handler.EventHandler;
import org.apollo.game.event.handler.EventHandlerContext;
import org.apollo.game.event.impl.ItemOptionEvent;
import org.apollo.game.model.GroundItem;
import org.apollo.game.model.Item;
import org.apollo.game.model.Player;

/**
 *
 * @author Arrowzftw
 */
public final class FifthItemOptionEventHandler extends EventHandler<ItemOptionEvent> {

    @Override
    public void handle(EventHandlerContext ctx, Player player, ItemOptionEvent event) {
        if (event.getOption() == 5) {
            Item item = player.getInventory().get(event.getSlot());
            if (item == null) {
                ctx.breakHandlerChain();
                return;
            }
            player.sendMessage("Dropped Item id = " + item.getId());
            player.getInventory().remove(event.getId(), item.getAmount());
            GroundItem.getInstance().create(player, item.getId(), item.getAmount(), player.getPosition());
        }
    }
}
