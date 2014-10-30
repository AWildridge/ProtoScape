package org.apollo.game.model;

import org.apollo.game.model.inter.InterfaceListener;
import org.apollo.game.model.inv.InventoryListener;

public class TradeListener implements InterfaceListener {
    
        /**
	 * The player.
	 */
	private final Player player;

	/**
	 * The inventory listener.
	 */
	private final InventoryListener invListener;

	/**
	 * The shop listener.
	 */
	private final InventoryListener tradedLitsener;

	/**
	 * Creates the shop interface listener.
	 * @param player The player.
	 * @param invListener The inventory listener.
	 * @param shopListener The shop listener.
	 */
	public TradeListener(Player player, InventoryListener invListener, InventoryListener shopListener) {
		this.player = player;
		this.invListener = invListener;
		this.tradedLitsener = shopListener;
	}

	@Override
	public void interfaceClosed() {
                Trade.declineTrade(new Player[] {player, player.getOfferOpponent()[0]}); //- resets trade.
		player.getInventory().removeListener(invListener);
		player.getOffer().removeListener(tradedLitsener);
	}
    
}
