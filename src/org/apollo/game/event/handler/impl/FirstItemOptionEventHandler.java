package org.apollo.game.event.handler.impl;

import org.apollo.game.content.Consumables;
import org.apollo.game.content.cluescroll.Clues;
import org.apollo.game.content.skills.prayer.Prayer;
import org.apollo.game.content.skills.runecrafting.RuneCrafting;
import org.apollo.game.event.handler.EventHandler;
import org.apollo.game.event.handler.EventHandlerContext;
import org.apollo.game.event.impl.ItemOptionEvent;
import org.apollo.game.model.Item;
import org.apollo.game.model.Player;

/**
 * ConsumeItemEventHandler.java
 *
 * @author The Wanderer
 */
public class FirstItemOptionEventHandler extends EventHandler<ItemOptionEvent> {

    @Override
    public void handle(EventHandlerContext ctx, Player player, ItemOptionEvent event) {
        if (event.getOption() == 1) {
            Clues clue = Clues.check(event.getId());
            if (clue != null) {
                player.getClues().handleClue(clue, event.getId());
                return;
            }
            switch (event.getId()) {
                //Burying bones
                case 526:
                case 528:
                case 530:
                case 532:
                case 534:
                case 536:
                case 2859:
                case 3125:
                case 3179:
                case 3181:
                case 3183:
                case 3185:
                case 4812:
                case 4830:
                case 4832:
                case 4834:
                case 6729:
                case 6812:
                    Prayer.handleBurying(player, new Item(event.getId()), event.getSlot());
                    break;

                // Pouch Populating
                case 5509:
                case 5510:
                case 5511:
                case 5512:
                case 5513:
                case 5514:
                case 5515:
                    RuneCrafting.populatePouches(player, event.getId());
                    break;

                // Eating
                case 319:
                case 315:
                case 2140:
                case 2142:
                case 325:
                case 2309:
                case 347:
                case 355:
                case 333:
                case 339:
                case 351:
                case 329:
                case 361:
                case 1891:
                case 1893:
                case 1895:
                case 379:
                case 365:
                case 373:
                case 6705:
                case 7946:
                case 2297:
                case 2299:
                case 385:
                case 397:
                case 2301:
                case 2303:
                case 7060:
                case 391:
                    Consumables.handleEating(player, new Item(event.getId()), event.getSlot(), 0);
                    break;

                //Potion Drinking
                case 2428:
                case 121:
                case 123:
                case 125:
                case 113:
                case 115:
                case 117:
                case 119:
                case 2432:
                case 133:
                case 135:
                case 137:
                case 3032:
                case 3034:
                case 3036:
                case 3038:
                case 2434:
                case 139:
                case 141:
                case 143:
                case 2436:
                case 145:
                case 147:
                case 149:
                case 2438:
                case 151:
                case 153:
                case 155:
                case 2440:
                case 157:
                case 159:
                case 161:
                case 2442:
                case 163:
                case 165:
                case 167:
                case 2444:
                case 169:
                case 171:
                case 173:
                case 3040:
                case 3042:
                case 3044:
                case 3046:
                    Consumables.handlePotions(player, event.getId());
                    break;
            }
        }
    }
}
