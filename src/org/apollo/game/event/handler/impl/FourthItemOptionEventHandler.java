package org.apollo.game.event.handler.impl;

import org.apollo.game.content.skills.runecrafting.RuneCrafting;
import org.apollo.game.event.handler.EventHandler;
import org.apollo.game.event.handler.EventHandlerContext;
import org.apollo.game.event.impl.ItemOptionEvent;
import org.apollo.game.model.Player;

/**
 * TalismanLocateEventHandler.java
 * @author The Wanderer
 */
public class FourthItemOptionEventHandler extends EventHandler<ItemOptionEvent> {

    @Override
    public void handle(EventHandlerContext ctx, Player player, ItemOptionEvent event) {
        if (event.getOption() == 4) {
            switch (event.getId()) {
                case 1438:
                case 1440:
                case 1442:
                case 1444:
                case 1446:
                case 1448:
                case 1452:
                case 1454:
                case 1456:
                case 1458:
                case 1462:
                    RuneCrafting.handleTalismanLocating(player, event.getId());
                    break;
            }
        }
    }
}
