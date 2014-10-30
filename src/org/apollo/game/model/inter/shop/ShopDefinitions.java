package org.apollo.game.model.inter.shop;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import org.apollo.game.model.Item;
import org.apollo.util.XStreamUtil;

/**
 *
 * @author D
 */
public class ShopDefinitions {

    public final static int MAX_SHOPS = 300;
    public static ShopDefinitions[] shopDefinitions = new ShopDefinitions[MAX_SHOPS];

    private int currency;
    private int shopId;
    private boolean isGeneral;
    private int[] item;
    private int[] amount = new int[100];
    private String name;
    private Item[] items = new Item[Shop.SIZE];
    public Item[] normalStock = new Item[Shop.SIZE];
    private boolean canSell;
    
    
    public int getCurrency() {
        return currency;
    }

    private int[] getItems() {
        return item;
    }

    private boolean isGeneral() {
        return isGeneral;
    }

    private int[] getAmount() {
        return amount;
    }

    public int getShopId() {
        return shopId;
    }

    private int getItemId(int index) {
        return item[index];
    }

    private int getItemAmount(int index) {
        return amount[index];
    }

    public String getName() {
        return name;
    }
    
    public boolean canSell() {
        return canSell;
    }
    
    public Item[] getItemArray() {
        return items;
    }
    
    public Item getItem()[] {
        return items;
    }
    
    public void setItemArray(Item item, int slot) {
        items[slot] = item;
    }
    
    public int normalStock(int id) {
        for(int i = 0; i < normalStock.length; i++) {
            if(getItem()[i].getId() == id) {
                return i;
            }
        }
        return -1;
    }
    
   public int getFreeSpace() {
       int count = 0;
        for(int i = 0; i < getItemArray().length; i++) {
            if(getItem()[i] != null) {
                count++;
            }
        }
        return count;
    }

    @SuppressWarnings("unchecked")
    public static void loadShops() throws FileNotFoundException {
        System.out.println("Loading shops...");
        List<ShopDefinitions> list = (List<ShopDefinitions>) 
                XStreamUtil.getXStream().fromXML(new FileInputStream("./data/ShopDefinitions.xml"));
        for (ShopDefinitions def : list) {
            
            shopDefinitions[def.getShopId()] = def;
            Item[] item = new Item[def.getItems().length];
            for(int i = 0; i < def.getItems().length; i++) {
                item[i] = new Item(def.getItemId(i), def.getItemAmount(i));
            }
            Item[] tempArray = new Item[40];
            for(int i = 0; i < item.length; i++) {
                tempArray[i] = item[i];
            }
            def.normalStock = item;
            def.items = tempArray;
            
            loadCore(def);
        }
        
        System.out.println("Loaded " + list.size() + " shop definitions");
    }
    
    
    private static void loadCore(final ShopDefinitions def) {
        Shop shop = new Shop() {

            @Override
            public int getId() {
                return def.getShopId();
            }
        };
        Shop.shop[def.getShopId()] = shop;
    }
    

}
