package org.apollo.game.model.def;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import org.apollo.game.model.WorldConstants;
import org.apollo.util.XStreamUtil;

/**
 *
 * @author Tatieiscool
 */

public class ItemBonuses {

        private static ItemBonuses[] itemBonuses = new ItemBonuses[WorldConstants.MAXIMUM_ITEM]; //Not the best way to

        public static ItemBonuses forId(int id) {
            if(itemBonuses[id] != null) {
                return itemBonuses[id];
            } else {
                return null;
            }
        }
        
        private int id;
        private int[] bonuses;


        public void setId(int id) {
                this.id = id;
        }

        public int getId() {
                return id;
        }

        public void setBonuses(int[] bonuses) {
                this.bonuses = bonuses;
        }

        public int[] getBonuses() {
                return bonuses;
        }
        
        @SuppressWarnings("unchecked")
	public static void loadItemBonuses() throws FileNotFoundException {
		System.out.println("Loading item bonuses...");
		List<ItemBonuses> list = (List<ItemBonuses>)
			XStreamUtil.getXStream().fromXML(new FileInputStream("./data/ItemBonuses.xml"));
		for (ItemBonuses def : list) {
			itemBonuses[def.getId()] = def;
		}
		System.out.println("Loaded " + list.size() + " equipment definitions");
	}
}