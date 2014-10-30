package org.apollo.game.model.inter.shop;

import org.apollo.game.event.impl.SetInterfaceTextEvent;
import org.apollo.game.model.Item;
import org.apollo.game.model.Player;
import org.apollo.game.model.def.ItemDefinition;
import org.apollo.game.model.inter.InterfaceListener;
import org.apollo.game.model.inv.InventoryListener;
import org.apollo.game.model.inv.SynchronizationInventoryListener;

/**
 * ShopHandler.java
 * @author The Wanderer
 */
public class ShopHandler {

    public static void open(int shopId, Player player) {
        Shop shop = Shop.getShop(shopId);
        //boolean general = shop.isGeneralStore();
        player.send(new SetInterfaceTextEvent(3901, shop.getDefinitions().getName()));
        
        InventoryListener invListener = new SynchronizationInventoryListener(player, ShopConstants.PLAYER_INVENTORY_INTERFACE);
        InventoryListener shopListener = new SynchronizationInventoryListener(player, 3824);
        player.getInventory().addListener(invListener);
        player.getInventory().forceRefresh();
        InterfaceListener interListener = new ShopInterfaceListener(player, invListener, shopListener, shop);
        player.getInterfaceSet().openWindowWithSidebar(interListener, 3824, 3822);
        player.setShopId(shopId);
        shop.updateShop(player);
    }

    public static void buying(Player player, int shopId,  int slot, int id, int amount) {
        Shop shops = Shop.getShop(shopId);
        Item item = shops.getDefinitions().getItem()[slot];
        
        
        
        if (item == null) {
            return; // invalid packet, or client out of sync
        }
        
        if(amount > item.getAmount()) {
            amount = item.getAmount();
        }
        
        if(amount <= 0) {
            return;
        }
        
        Item brought = new Item(item.getId(), amount);
        
        if (item.getId() != id) {
            return; // invalid packet, or client out of sync
        }
        int transferAmount = item.getAmount();
        int cost = (int) ((double) item.getDefinition().getValue() * 1.45);
        if (transferAmount >= amount) {
            transferAmount = amount;
        } else if (transferAmount == 0) {
            return; // invalid packet, or client out of sync
        }
        int newId = item.getId();
        int newAmount = item.getAmount() - transferAmount < 0 ? 0 : item.getAmount() - transferAmount;
        ItemDefinition def = ItemDefinition.forId(newId);
        
        if (def.isStackable()) {
            if (player.getInventory().freeSlots() <= 0 && player.getInventory().getItemById(newId) == null) {
                player.sendMessage("You don't have enough inventory space to withdraw that many."); // this is the real message
            }
        } else {
            int free = player.getInventory().freeSlots();
            if (transferAmount > free) {
                player.sendMessage("You don't have enough inventory space to buy that many."); // this is the real message
                transferAmount = free;
            }
        }
        // now add it to inv
        if (player.getInventory().getCount(shops.getDefinitions().getCurrency()) >= cost) {
           
            //Remove from Sellable array or shop array
           player.getInventory().remove(new Item(shops.getDefinitions().getCurrency(), cost)); 
            if(shops.shopSoldItems.contains(id)) {
                Item array = shops.getDefinitions().getItem()[slot];
                if(newAmount <= 0) {
                    shops.getDefinitions().setItemArray(null, slot);
                    shops.shopSoldItems.remove(shops.getSlot(array));
                    shops.getDefinitions().setItemArray(null, slot);
                } else {
                    shops.getDefinitions().setItemArray(new Item(newId, newAmount), slot);
                }
            } else {
                shops.getDefinitions().setItemArray(new Item(newId, newAmount), slot);
            }
            player.getInventory().add(brought);
            shops.updateShop(player);
            
        } else {
            player.sendMessage("You don't have enough money to buy that many.");
        }
    }


	public static void selling(Player player, int slot, int id, int amount) {
            
            Item item = player.getInventory().get(slot);
            Shop shop = Shop.getShop(player.getShopId());
            
            if(!shop.getDefinitions().canSell() && shop.getDefinitions().normalStock(id) == -1) {
                player.sendMessage("You cannot sell this item to the store.");
                return;
            }
            if(shop.getDefinitions().getFreeSpace() <= 0) {
                player.sendMessage("This shop is full");
                return;
            }
       
            if (item == null) {
                player.sendMessage("This item is not in your inventory");
                return; // invalid packet, or client out of sync
            }
           if (item.getId() == 995) {
                player.sendMessage("You cannot sell this item to the store!");
                return;
            }
            if (item.getId() != id) {
                player.sendMessage("This item is not in your inventory2");
                return; // invalid packet, or client out of sync
            }
            
            int transferAmount = amount;
            if (player.getInventory().getCount(id) < amount) {
                transferAmount = player.getInventory().getCount(id);
            } else if (transferAmount == 0) {
                return; // invalid packet, or client out of sync
            }
            
            boolean noted = item.getDefinition().isNote();
            int normalId = noted ? ItemDefinition.noteToItem(item.getId()) : item.getId();
            int newInventoryAmount = transferAmount;
            Item soldItem = new Item(normalId);
            Item newItem = null;
            
            if (item.getDefinition().isStackable() || noted) {
                if(newInventoryAmount <= 0) {
                    newInventoryAmount = player.getInventory().getCount(id);
                }
                if (newInventoryAmount <= 0) {
                    return;
                } else {
                    newItem = new Item(item.getId(), newInventoryAmount);
                }
            }
            
            if(newItem == null) {
                newItem = new Item(item.getId(), newInventoryAmount);
            }
                
                    
            if(shop.shopSoldItems.contains(id)) {
                    for(int i = 0; i < shop.getDefinitions().getItemArray().length; i++) {
                        if(shop.getDefinitions().getItem()[i] != null) {
                            if(shop.getDefinitions().getItem()[i].getId() == id) {
                                Item update = new Item(normalId, transferAmount+shop.getDefinitions().getItem()[i].getAmount());
                                shop.getDefinitions().setItemArray(update, i);
                                //shop.shopSoldItems.set(shop.getSlot(item), update);
                                int cost = (soldItem.getDefinition().getValue() / 2);
                                player.getInventory().remove(id, transferAmount);
                                player.getInventory().add(new Item(995, cost * transferAmount));
                                break;
                            }
                        }
                    }                    
            } else if(!shop.shopSoldItems.contains(id) && shop.getDefinitions().normalStock(item.getId()) < 0){
                   for(int i = 0; i < 40; i++) {
                            if(shop.getDefinitions().getItem()[i] == null) {
                                    Item update = new Item(id, transferAmount);
                                    shop.getDefinitions().setItemArray(update, i);
                                    shop.shopSoldItems.add(id);
                                    int cost = (soldItem.getDefinition().getValue() / 2);
                                    player.getInventory().remove(id, transferAmount);
                                    player.getInventory().add(new Item(995, cost * transferAmount));
                                    break;
                            }
                            
                        }
                    } else if(shop.getDefinitions().normalStock(item.getId()) > 0){
                        for(int i = 0; i < shop.getDefinitions().getItemArray().length; i++) {
                           
                                if(shop.getDefinitions().getItem()[i] != null) {
                                    if(shop.getDefinitions().getItem()[i].getId() == id) {
                                        Item update = new Item(id, transferAmount+shop.getDefinitions().getItem()[i].getAmount());
                                        shop.getDefinitions().setItemArray(update, i);
                                        //shop.shopSoldItems.add(new Item (id));
                                        int cost = (soldItem.getDefinition().getValue() / 2);
                                        player.getInventory().remove(id, transferAmount);
                                        player.getInventory().add(new Item(995, cost * transferAmount));  
                                    }
                                }
                        }
                    }
                
                shop.updateShop(player);
                
    }
}
