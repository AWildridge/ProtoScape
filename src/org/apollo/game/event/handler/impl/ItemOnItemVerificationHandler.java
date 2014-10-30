package org.apollo.game.event.handler.impl;

import org.apollo.game.event.handler.EventHandler;
import org.apollo.game.event.handler.EventHandlerContext;
import org.apollo.game.event.impl.ItemOnItemEvent;
import org.apollo.game.model.Inventory;
import org.apollo.game.model.Item;
import org.apollo.game.model.Player;
import org.apollo.game.model.inter.bank.BankConstants;
import org.apollo.game.model.inv.SynchronizationInventoryListener;

/**
 * An {@link EventHandler} which verifies the target item in {@link ItemOnItemEvent}s.
 * @author Chris Fletcher
 */
public final class ItemOnItemVerificationHandler extends EventHandler<ItemOnItemEvent> {

	/**
	 * Gets the inventory based on the interface id.
	 * @param interfaceId The interface id.
	 * @return The proper inventory.
	 * @throws IllegalArgumentException if the interface id is not legal.
	 */
	public static Inventory interfaceToInventory(Player player, int interfaceId) {
		switch (interfaceId) {
		case SynchronizationInventoryListener.INVENTORY_ID:
		case BankConstants.SIDEBAR_INVENTORY_ID:
			return player.getInventory();

		case SynchronizationInventoryListener.EQUIPMENT_ID:
			return player.getEquipment();

		case BankConstants.BANK_INVENTORY_ID:
			return player.getBank();

		default:
			throw new IllegalArgumentException("unknown interface id: " + interfaceId);
		}
	}

	@Override
	public void handle(EventHandlerContext ctx, Player player, ItemOnItemEvent event) {
		/*
		 * Acquire the proper inventory for the interface id. This will throw an exception if the interface id
		 * is unknown, causing the handler chain to break and the event to be discarded.
		 */
		Inventory inventory = interfaceToInventory(player, event.getTargetInterfaceId());

		/*
		 * We check if the slot is in bounds; not negative and not equal to or higher than the inventory's
		 * capacity.
		 */
		int slot = event.getTargetSlot();
		if (slot < 0 || slot >= inventory.capacity()) {
			ctx.breakHandlerChain();
			return;
		}

		/*
		 * Lastly, we acquire the item at the specified slot and see if its id matches the one specified by
		 * the client.
		 */
		Item item = inventory.get(slot);
		if (item == null || item.getId() != event.getTargetId()) {
			ctx.breakHandlerChain();
			return;
		}
	}

}