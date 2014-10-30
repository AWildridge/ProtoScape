package org.apollo.game.model;

import java.text.NumberFormat;

import org.apollo.game.model.def.ItemDefinition;
import org.apollo.game.model.inter.InterfaceListener;
import org.apollo.game.model.inv.InventoryListener;
import org.apollo.game.model.inv.SynchronizationInventoryListener;



/**
 * Trading utility class.
 * @author Martin, Arrowzftw
 *
 */
public class Trade {
	
	/**
	 * The trade limit size.
	 */
	public static final int SIZE = 28;
	
	/**
	 * The PLAYER inventory interface.
	 */
	public static final int PLAYER_INVENTORY_INTERFACE = 3322;

	/**
	 * The TRADE inventory interface.
	 */
	public static final int TRADE_INVENTORY_INTERFACE = 3415;
	
	/**
	 * Opens the shop for the specified player.
	 * @param player The player to open the shop for.
	 */
	public static void open(Player[] trader) {
            if(!trader[0].getPosition().isWithinDistance(trader[1].getPosition(),3)|| !trader[1].getPosition().isWithinDistance(trader[0].getPosition(),3)){
                return;
            }
            for(Player player : trader) {
                Player player2 = null;
                if(player == trader[0])
                    player2 = trader[1];
                else
                    player2 = trader[0];
                player.setOfferOpp(0, player2);
                player.setOfferOpp(1, null);
                player.setTradeStage(1);
                player.getEventManager().sendString("Trading with: " + player2.getUndefinedName()+" who has @gre@"+player2.getInventory().freeSlots()+" free slots" ,3417);
				player.getEventManager().sendString("", 3431);
                player.getEventManager().sendString("", 3535);
                InventoryListener invListener = new SynchronizationInventoryListener(player, PLAYER_INVENTORY_INTERFACE);
                InventoryListener tradedLitsener = new SynchronizationInventoryListener(player, TRADE_INVENTORY_INTERFACE);
                player.getInventory().addListener(invListener);
                player.getOffer().addListener(tradedLitsener);
                InterfaceListener interListener = new TradeListener(player, invListener, tradedLitsener);
                player.getInterfaceSet().openWindowWithSidebar(interListener, 3323, 3321);
				player.getEventManager().sendUpdateItems(3322,player.getInventory().getItems());
				player.getEventManager().sendUpdateItems(3415,player.getOffer().getItems());
				player.getEventManager().sendUpdateItems(3416,player.getOffer().getItems());
				player.getEventManager().sendUpdateItems(3322,player.getOffer().getItems());
				player.getEventManager().sendUpdateItems(3415,player.getOffer().getItems());
				player.getEventManager().sendUpdateItems(3416,player.getOffer().getItems());
				player.getInventory().forceRefresh();
                player2.getInventory().forceRefresh();
                player.getOffer().forceRefresh();
            }
       }

        /**
         * This will decline both player's trade screens.
         * @param player Player being traded.
         */
        public static void declineTrade(Player[] trader){
            if(trader == null || trader[1] == null) return;
            boolean success = false;
            for(Player player : trader) {

                /* reset variables */
                player.setOfferOpp(0, null);
                player.setOfferOpp(1, null);

                /* give items back  or compelete trade.*/
                if(player.getTradeStage() == 3) 
                    success = true;
                    else
                player.getInventory().transfer(player.getOffer(), player.getInventory());
                player.getInventory().forceRefresh();
                /* reset the stage */
                if(success == true) {                    
                    player.getInventory().forceRefresh();
                }
                player.setTradeStage(0);
            }
                trader[0].sendMessage(success ? "The trade was successful." : "You declined the trade.");
                trader[1].sendMessage(success ? "The trade was successful." : "The other player has declined the trade.");
                
                trader[0].getInventory().transfer(trader[1].getOffer(), trader[0].getInventory());
                trader[1].getInventory().transfer(trader[0].getOffer(), trader[1].getInventory());
                trader[1].getInterfaceSet().close();
                trader[0].getInterfaceSet().close();
                
	}

        /**
	 * Deposits an item.
	 * @param player The player.
	 * @param slot The slot in the player's inventory.
	 * @param id The item id.
	 * @param amount The amount of the item to deposit.
	 */
	public static void deposit(Player player, int slot, int id, int amount) {
		boolean inventoryFiringEvents = player.getInventory().isFiringEvents();
		player.getInventory().setFiringEvents(false);
		try {
			Item item = player.getInventory().get(slot);
			if(item == null) {
				return; // invalid packet, or client out of sync
			}
			if(item.getId() != id) {
				return; // invalid packet, or client out of sync
			}
			int transferAmount = player.getInventory().getCount(id);
			if(transferAmount >= amount) {
				transferAmount = amount;
			} else if(transferAmount == 0) {
				return; // invalid packet, or client out of sync
			}
			boolean noted = item.getDefinition().isNote();
			if(item.getDefinition().isStackable() || noted) {
				int bankedId = item.getId();
				/*if(player.getOffer().freeSlots() < 1 && player.getOffer().getItemById(bankedId) == null) {
					player.sendMessage("You don't have enough space."); // this is the real message
				}*/
				// we only need to remove from one stack
				int newInventoryAmount = item.getAmount() - transferAmount;
				Item newItem;
				if(newInventoryAmount <= 0) {
					newItem = null;
				} else {
					newItem = new Item(item.getId(), newInventoryAmount);
				}
				if(player.getOffer().freeSlots() <= 0) {
                                        return;
                                } else {
                                        player.getOffer().add(bankedId, transferAmount);
					player.getInventory().set(slot, newItem);
					player.getInventory().startFiringEvents();
					player.getOffer().startFiringEvents();
				}
			} else {
				if(player.getOffer().freeSlots() < transferAmount) {
					player.sendMessage("You don't have enough space."); // this is the real message
                                        return;
                                }
                                        player.getOffer().add(item.getId(), transferAmount);
					// we need to remove multiple items
					for(int i = 0; i < transferAmount; i++) {
							player.getInventory().set(player.getInventory().getSlotById(item.getId()), null);
					}
					player.getOffer().startFiringEvents();
				
			}
		} finally {
			player.getInventory().setFiringEvents(inventoryFiringEvents);
                        if(player.getTradeStage() > 0) {
                            player.getOfferOpponent()[0].getEventManager().sendUpdateItems(3416,player.getOffer().getItems());
                            player.getOfferOpponent()[0].getEventManager().sendString(3431, "Are you sure you want to make this trade?");
                            player.getEventManager().sendString(3431, "Are you sure you want to make this trade?");
                            player.getOfferOpponent()[0].getEventManager().sendString("Trading with: " + player.getUndefinedName()+" who has @gre@"+player.getInventory().freeSlots()+" free slots" ,3417);
                        } else {
                           player.getOfferOpponent()[0].getEventManager().sendUpdateItems(6670,player.getOffer().getItems());
                        }
		}
                player.getInventory().forceRefresh();
                
        }

                /**
	 * Withdraws an item.
	 * @param player The player.
	 * @param slot The slot in the player's inventory.
	 * @param id The item id.
	 * @param amount The amount of the item to deposit.
	 */
	public static void withdraw(Player player, int slot, int id, int amount) {//needs to be fixed.
               int transferAmount = 1;
               if(player.getOffer().getCount(id) >= amount)
                   transferAmount = amount;
               else
                transferAmount = player.getOffer().getCount(id);
		ItemDefinition def = ItemDefinition.forId(id);
		if(def.isStackable()) {
			if(player.getInventory().freeSlots() <= 0) {
				player.sendMessage("You don't have enough inventory space to withdraw that many."); // this is the real message
                                return;
                        }
		} else {
			int free = player.getInventory().freeSlots();
			if(transferAmount > free) {
				player.sendMessage("You don't have enough inventory space to withdraw that many."); // this is the real message
				transferAmount = free;
			}
		}
                            
				player.getInventory().add(id, transferAmount);
				player.getOffer().remove(new Item(id, amount));
			
			if(player.getTradeStage() > 0) {
                            player.getOfferOpponent()[0].getEventManager().sendUpdateItems(3416,player.getOffer().getItems());
                            player.getOfferOpponent()[0].getEventManager().sendString(3431, "Are you sure you want to make this trade?");
                            player.getEventManager().sendString(3431, "Are you sure you want to make this trade?");
                            player.getOfferOpponent()[0].getEventManager().sendString("Trading with: " + player.getUndefinedName()+" who has @gre@"+player.getInventory().freeSlots()+" free slots" ,3417);
                        } else {
                            player.getOfferOpponent()[0].getEventManager().sendUpdateItems(6670,player.getOffer().getItems());
                        }
	}
        
        public static String formatInt(int num) {
		return NumberFormat.getInstance().format(num);
	}

        public static String listConfirmScreen(Item[] items){
		String sendTrade = "Absolutely nothing!";
		String sendAmount = "";
		int count = 0;
		for (Item item : items) {
			if(item == null)
				continue;
			if (item.getId() > 0) {
				if ((item.getAmount() >= 1000) && (item.getAmount() < 1000000)) {
					sendAmount = "@cya@" + (item.getAmount() / 1000) + "K @whi@("
							+ formatInt(item.getAmount()) + ")";
				} else if (item.getAmount() >= 1000000) {
					sendAmount = "@gre@" + (item.getAmount() / 1000000)
							+ " million @whi@(" + formatInt(item.getAmount())
							+ ")";
				} else {
					sendAmount = "" + formatInt(item.getAmount());
				}
				if (count == 0) {
					sendTrade = "";
					count = 2;
				}
				if(count == 1){
					sendTrade = sendTrade + "\\n" + item.getDefinition().getName();
				} else if(count == 2){
					sendTrade = sendTrade + " " + item.getDefinition().getName();
					count = 0;
				}
				if (item.getDefinition().isStackable()) {
					sendTrade = sendTrade + " x " + sendAmount;
				}
				sendTrade = sendTrade + "     ";
				count++;
			}
		}
		return sendTrade;
	}

        /**
         * This will open up the confirm screen.
         * @param player Player being opened for.
         */
        public static void changeToConfirmScreen(Player player, Player trading) {
            if(player == null || player.getOfferOpponent()[0] == null) return;
            player.setTradeStage(-2);
            player.getOfferOpponent()[0].setTradeStage(-2);
            player.getOfferOpponent()[0].getEventManager().sendString(3535, "Are you sure you want to make this trade?");
			player.getEventManager().sendString(3535, "Are you sure you want to make this trade?");
            String sendTrade1 = listConfirmScreen(player.getOffer().getItems());
			String sendTrade2 = listConfirmScreen(player.getOfferOpponent()[0].getOffer().getItems());
			player.getEventManager().sendString(3557,sendTrade1);
			player.getOfferOpponent()[0].getEventManager().sendString(3557,sendTrade2);
			player.getEventManager().sendString(3558,sendTrade2);
			player.getOfferOpponent()[0].getEventManager().sendString(3558,sendTrade1);
			player.getInterfaceSet().openWindowWithSidebar(3443, 3213);
            player.getEventManager().sendUpdateItems(3214,player.getInventory().getItems());
            player.getOfferOpponent()[0].getInterfaceSet().openWindowWithSidebar(3443, 3213, false);
			player.getOfferOpponent()[0].getEventManager().sendUpdateItems(3214,player.getOfferOpponent()[0].getInventory().getItems());
            player.setTradeStage(2);
            player.getOfferOpponent()[0].setTradeStage(2);
        }

        /**
         * Check if we have room
         */
        public static boolean hasRoom(Player player) {
            if(player.getInventory().freeSlots() < player.getOfferOpponent()[0].getOffer().size()){
			player.sendMessage("You don't have enough space to make this trade.");
			player.getOfferOpponent()[0].sendMessage("The other player doesn't have enough space to make this trade.");
			return false;
		}
		if(player.getOfferOpponent()[0].getInventory().freeSlots() < player.getOffer().size()){
			player.getOfferOpponent()[0].sendMessage("You don't have enough space to make this trade.");
			player.sendMessage("The other player doesn't have enough space to make this trade.");
			return false;
		}
            return true;
        }
}
