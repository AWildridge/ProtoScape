package org.apollo.game.model.inter.shop;

import org.apollo.game.model.Player;
import org.apollo.game.model.inter.InterfaceListener;
import org.apollo.game.model.inv.InventoryListener;

/**
 * ShopInterfaceListener.java
 * @author The Wanderer
 */
public class ShopInterfaceListener implements InterfaceListener {
    
        /**
	 * The player.
	 */
	private final Player player;
        
        private final Shop shop;

	/**
	 * The inventory listener.
	 */
	private final InventoryListener invListener;

	/**
	 * The shop listener.
	 */
	private final InventoryListener shopListener;

	/**
	 * Creates the shop interface listener.
	 * @param player The player.
	 * @param invListener The inventory listener.
	 * @param shopListener The shop listener.
	 */
	public ShopInterfaceListener(Player player, InventoryListener invListener, InventoryListener shopListener, Shop shop) {
		this.player = player;
		this.invListener = invListener;
		this.shopListener = shopListener;
                this.shop = shop;
	}

	@Override
	public void interfaceClosed() {
		player.getInventory().removeListener(invListener);
		//shop.getStock().removeListener(shopListener);
	}
    
}
