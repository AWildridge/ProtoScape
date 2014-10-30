/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apollo.game.content.skills.herblore;

import org.apollo.game.model.Item;
import org.apollo.game.model.Player;

/**
 *
 * @author D
 */
public class PotionCombine {
    
    
    public static void combinePotions(Player player, Item item, Item usedWithItem) {
        
        int dose1;
        int dose2;
        
        String itemName = item.getDefinition().getName();
        String secondItemName = item.getDefinition().getName();
        
        if(itemName.contains("(4)")) {
            dose1 = 4;
        } else if(itemName.contains("(3)")) {
            dose1 = 3;
        } else if(itemName.contains("(2)")) {
            dose1 = 2;
        } else if(itemName.contains("(1)")) {
            dose1 = 1;
        }
        
        if(secondItemName.contains("(4)")) {
            dose2 = 4;
        } else if(secondItemName.contains("(3)")) {
            dose2 = 3;
        } else if(secondItemName.contains("(2)")) {
            dose2 = 2;
        } else if(secondItemName.contains("(1)")) {
            dose2 = 1;
        }
        
        
        String potName = item.getDefinition().getName().replaceAll(("(4)"), "").replaceAll(("(3)"), "").replaceAll(("(2)"), "").replaceAll(("(1)"), "");
        String secondPotName = usedWithItem.getDefinition().getName().replaceAll(("(4)"), "").replaceAll(("(3)"), "").replaceAll(("(2)"), "").replaceAll(("(1)"), "");
        
        
        
    }
    
    public int getNewDose(int dose1, int dose2) {
        
        return -1;
    }
    
}
