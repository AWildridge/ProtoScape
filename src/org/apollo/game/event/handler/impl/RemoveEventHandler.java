package org.apollo.game.event.handler.impl;

import org.apollo.game.content.WeaponInterfaces;
import org.apollo.game.event.handler.EventHandler;
import org.apollo.game.event.handler.EventHandlerContext;
import org.apollo.game.event.impl.ItemActionEvent;
import org.apollo.game.model.Inventory;
import org.apollo.game.model.Item;
import org.apollo.game.model.Player;
import org.apollo.game.model.inv.FullInventoryListener;
import org.apollo.game.model.inv.SynchronizationInventoryListener;

/**
 * An event handler which removes equipped items.
 * @author Graham
 * @authot zuppers
 */
public final class RemoveEventHandler extends EventHandler<ItemActionEvent> {

	@Override
        public void handle(EventHandlerContext ctx, Player player, ItemActionEvent event) {
                if (event.getOption() == 1 && event.getInterfaceId() == SynchronizationInventoryListener.EQUIPMENT_ID) {
                        Inventory inventory = player.getInventory();
                        Inventory equipment = player.getEquipment();
                        
                        if(inventory.freeSlots() <= 0) {
                            player.sendMessage(FullInventoryListener.FULL_INVENTORY_MESSAGE);
                            return;
                        }

                        int slot = event.getSlot();

                        Item item = equipment.get(slot);

                        boolean removed = true;

                        inventory.stopFiringEvents();
                        equipment.stopFiringEvents();

                        try {
                                equipment.set(slot, null);
                                Item tmp = item;
                                inventory.add(item.getId(),item.getAmount());
                                if (tmp == null) {
                                   
                                        removed = false;
                                        equipment.set(slot, tmp);
                                }
                        } finally {
                                inventory.startFiringEvents();
                                equipment.startFiringEvents();
                        }

                        if (removed) {
                                inventory.forceRefresh(); // TODO find out the specific slot that got used?
                                equipment.forceRefresh();
                        } else {
                                inventory.forceCapacityExceeded();
                        }
                        player.getBonuses().refreshBonuses();
                        WeaponInterfaces.UpdateWep(player);
                }
        }

}