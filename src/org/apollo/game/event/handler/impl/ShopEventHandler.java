package org.apollo.game.event.handler.impl;

import org.apollo.game.event.handler.EventHandler;
import org.apollo.game.event.handler.EventHandlerContext;
import org.apollo.game.event.impl.ItemActionEvent;
import org.apollo.game.model.Item;
import org.apollo.game.model.Player;
import org.apollo.game.model.def.ItemDefinition;
import org.apollo.game.model.inter.shop.Shop;
import org.apollo.game.model.inter.shop.ShopHandler;

/**
 * ShopEventHandler.java
 * @author The Wanderer
 */
public class ShopEventHandler extends EventHandler<ItemActionEvent> {

    @Override
    public void handle(EventHandlerContext ctx, Player player, ItemActionEvent event) {
        
        if (event.getInterfaceId() == 3823) {
            selling(player, event);
            ctx.breakHandlerChain();
        } else if (event.getInterfaceId() == 3900) {
            buying(ctx, player, event);
            ctx.breakHandlerChain();
        }
    }

    /**
     * Handles a withdraw action.
     * @param ctx The event handler context.
     * @param player The player.
     * @param event The event.
     */
    private void buying(EventHandlerContext ctx, Player player, ItemActionEvent event) {
        
        switch (event.getOption()) {
            case 1:
                if (event.getSlot() >= 0 && event.getSlot() < Shop.SIZE) {
                    Item item = new Item(event.getId());
                    int cost = (int) ((double) item.getDefinition().getValue() * 1.45);
                    String end;
                    if (cost == 1) {
                        end = ".";
                    } else {
                        end = "s.";
                    }
                    player.sendMessage("This item currently costs " + cost + " gold piece" + end);
                    break;
                }
                break;

            case 2:
                
                if (event.getSlot() >= 0 && event.getSlot() < Shop.SIZE) {
                    ShopHandler.buying(player, player.shopId, event.getSlot(), event.getId(), 1);
                }
                break;

            case 3:
                if (event.getSlot() >= 0 && event.getSlot() < Shop.SIZE) {
                    ShopHandler.buying(player, player.shopId, event.getSlot(), event.getId(), 5);
                }
                break;

            case 4:
                if (event.getSlot() >= 0 && event.getSlot() < Shop.SIZE) {
                   ShopHandler.buying(player, player.shopId, event.getSlot(), event.getId(), 10);
                }
                break;
        }
    }

    /**
     * Handles a selling action.
     * @param ctx The event handler context.
     * @param player The player.
     * @param event The event.
     */
    private void selling(Player player, ItemActionEvent event) {
        
        switch (event.getOption()) {
            case 1:
                if (event.getSlot() >= 0 && event.getSlot() < 28) {
                    Item item = new Item(event.getId());
                    int value = item.getDefinition().getValue() / 2;
                    String end;
                    if (value == 1) {
                        end = ".";
                    } else {
                        end = "s.";
                    }
                    if (item.getDefinition().isNote()) {
                        Item newItem = new Item(ItemDefinition.noteToItem(event.getId()));
                        int newValue = newItem.getDefinition().getValue() / 2;
                        if (newValue == 1) {
                            end = ".";
                        } else {
                            end = "s.";
                        }
                        player.sendMessage("This item will sell for " + newValue + " gold piece" + end);
                        break;
                    } else if (value >= 0) {
                        player.sendMessage("This item will sell for " + value + " gold piece" + end);
                        break;
                    }
                }
                break;
                
            case 2:
                
                if(event.getSlot() >= 0 && event.getSlot() < Shop.SIZE) {
                        ShopHandler.selling(player, event.getSlot(), event.getId(), 1);
                    }
            break;
                
            case 3:
                if(event.getSlot() >= 0 && event.getSlot() < Shop.SIZE) {
                        ShopHandler.selling(player, event.getSlot(), event.getId(), 5);
                    }
                    break;
                
            case 4:
                if(event.getSlot() >= 0 && event.getSlot() < Shop.SIZE) {
                        ShopHandler.selling(player, event.getSlot(), event.getId(), 10);
                    }
                    break;
        }
    }
}
