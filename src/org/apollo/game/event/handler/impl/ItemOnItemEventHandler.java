package org.apollo.game.event.handler.impl;

import org.apollo.game.content.skills.crafting.Crafting;
import org.apollo.game.content.skills.crafting.LeatherData;
import org.apollo.game.content.skills.firemaking.Firemaking;
import org.apollo.game.content.skills.firemaking.Firemaking.FireData;
import org.apollo.game.content.skills.fletching.Fletching;
import org.apollo.game.content.skills.herblore.Herblore;
import org.apollo.game.event.handler.EventHandler;
import org.apollo.game.event.handler.EventHandlerContext;
import org.apollo.game.event.impl.ItemOnItemEvent;
import org.apollo.game.model.Item;
import org.apollo.game.model.Player;

/**
 * ItemOnItemEventHandler.java
 * @author The Wanderer
 */
public class ItemOnItemEventHandler extends EventHandler<ItemOnItemEvent> {
    
    private Item itemUsed;
    
    private Item usedWith;
    
    private static int NEEDLE = 1733;
    
    @Override
    public void handle(EventHandlerContext ctx, Player player, ItemOnItemEvent event) {
        itemUsed = player.getInventory().get(event.getSlot());
        usedWith = player.getInventory().get(event.getTargetSlot()); 
        
        /**
         * Fletching data
         */
        Fletching.executeArrowTipping(player, itemUsed.getId(), usedWith.getId());
        Fletching.executeDart(player, itemUsed.getId(), usedWith.getId());
        Fletching.executeBowString(player, itemUsed.getId(), usedWith.getId());
        Fletching.setupFletching(player, itemUsed.getId(), usedWith.getId());

        /**
         * Herblore Data
         */
      /*Herblore.halfPotion(player, itemUsed.getId(), usedWith.getId());
        Herblore.finishPotion(player, itemUsed.getId(), usedWith.getId());
        Herblore.grindItem(player, itemUsed.getId(), usedWith.getId());
        Herblore.executePoison(player, itemUsed.getId(), usedWith.getId());
        Herblore.executePoisonP(player, itemUsed.getId(), usedWith.getId());
        Herblore.executePoisonPP(player, itemUsed.getId(), usedWith.getId());*/

        /**
         * Crafting data
         */
        if(itemUsed.getId() == NEEDLE) {
            LeatherData hide = LeatherData.forId(usedWith.getId());
            if(hide != null && usedWith.getId() == hide.getHide()) {
                Crafting.setupInterface(player, usedWith.getId());
            }
        } else if(usedWith.getId() == NEEDLE) {
            LeatherData hide = LeatherData.forId(itemUsed.getId());
            if(hide != null && itemUsed.getId() == hide.getHide()) {
                Crafting.setupInterface(player, itemUsed.getId());
            }
        }
        Crafting.executeCut(player, itemUsed.getId(), usedWith.getId());
        Crafting.executeAmulet(player, itemUsed.getId(), usedWith.getId());
        Crafting.executeStaff(player, itemUsed.getId(), usedWith.getId());
        
        if (itemUsed.getId() == 590) {
            FireData data = FireData.forId(usedWith.getId());
            if(data != null)
            	Firemaking.lightFire(player, data, usedWith.getId());
        } else if (usedWith.getId() == 590) {
            FireData data = FireData.forId(itemUsed.getId());
            if(data != null)
            	Firemaking.lightFire(player, data, itemUsed.getId());
        }
    }  
}
