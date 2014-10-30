package org.apollo.game.model.inter.shop;

import java.util.ArrayList;
import java.util.List;

import org.apollo.game.event.impl.UpdateItemsEvent;
import org.apollo.game.model.Item;
import org.apollo.game.model.Player;
import org.apollo.game.model.World;

/**
 * Shop.java
 * @author The Wanderer
 */
public abstract class Shop {
        
    public static final int PLAYER_INVENTORY_INTERFACE = 3823;
    
    public static final int SHOP_INVENTORY_INTERFACE = 3900;
    
    public static final int SIZE = 40;
    
    public static Shop[] shop = new Shop[ShopDefinitions.MAX_SHOPS];
    
    public abstract int getId();
    
    public List<Integer> shopSoldItems = new ArrayList<Integer>();
    
    public int getSlot(Item item) {
        for(int i = 0; i < shopSoldItems.size(); i++) {
            if(shopSoldItems.get(i) == item.getId()) {
                return i;
            }
        }
        return -1;
    }
    
    public int getSpace() {
        return SIZE-getDefinitions().getItemArray().length;
    }
    
  
    
    public ShopDefinitions getDefinitions() {
        return ShopDefinitions.shopDefinitions[this.getId()];
    }
    
    public static Shop getShop(int shopId) {
        for(int i = 0; i < shop.length; i++) {
            if(shop[i] != null) {
                if(shop[i].getId() == shopId) {
                    return shop[i];
                }
            }
        }
        return null;
    }
    
    public void updateShop(Player start) {
        
        Item[] items = new Item[Shop.SIZE];
        for(int i = 0; i < getDefinitions().getItemArray().length; i++) {
                items[i] = getDefinitions().getItem()[i];
        }
        
        start.send(new UpdateItemsEvent(3900, items));
        
        for (Player player : World.getWorld().getPlayerRepository()) {
            if(player.getShopId() == this.getId()) {
                player.send(new UpdateItemsEvent(3900, items));
            }
        }
    }
    
}
