package org.apollo.game.event.handler.impl;

import org.apollo.game.content.skills.runecrafting.RuneCrafting;
import org.apollo.game.event.handler.EventHandler;
import org.apollo.game.event.handler.EventHandlerContext;
import org.apollo.game.event.impl.ItemOptionEvent;
import org.apollo.game.model.Player;

/**
 * AlternateItemOptionTwoEventHandler.java
 * @author The Wanderer
 */
public class ThirdItemOptionEventHandler extends EventHandler<ItemOptionEvent> {

    @Override
    public void handle(EventHandlerContext ctx, Player player, ItemOptionEvent event) {
        if (event.getOption() == 3) {
            switch (event.getId()) {
                case 5509:
                case 5510:
                case 5511:
                case 5512:
                case 5513:
                case 5514:
                case 5515:
                    RuneCrafting.checkPouchUses(player, event.getId());
                    break;
            }
        }
    }
}
