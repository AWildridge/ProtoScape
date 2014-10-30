package org.apollo.game.content.skills.crafting;

import java.util.HashMap;
import java.util.Map;

import org.apollo.game.GameConstants;

/**
 * All leather Data
 * 
 * @author The wanderer
 */
public enum LeatherData {	
    GREEN_DRAGON(1745, new int[] {1135, 1065, 1099}, new int[] {3, 1, 2}, new int[] {63, 57, 60}, new double[] {186.0, 62.0, 124.0}),
    BLUE_DRAGON(2505, new int[] {2499, 2487, 2493}, new int[] {3, 1, 2}, new int[] {71, 66, 68}, new double[] {210.0, 70.0, 140.0}),
    RED_DRAGON(2507, new int[] {2501, 2489, 2495}, new int[] {3, 1, 2}, new int[] {77, 73, 75}, new double[] {234.0, 78.0, 124.0}),
    BLACK_DRAGON(2509, new int[] {2503, 2491, 2497}, new int[] {3, 1, 2}, new int[] {84, 79, 82}, new double[] {258.0, 86.0, 172.0}),
    SOFT_LEATHER(1741, new int[] {1129, 1063, 1095, 1167, 1061, 1059, 1169}, new int[] {1, 1, 1, 1, 1, 1, 1}, new int[] {14, 11, 18, 9, 7, 1, 38}, new double[] {25.0, 22.0, 27.0, 18.5, 16.3, 13.8, 37.0}),
    HARD_LEATHER(1743, new int[] {1131}, new int[] {1}, new int[] {28}, new double[] {35.0}),
    SNAKESKIN(6289, new int[] {6322, 6324, 6330, 6326, 6328}, new int[] {15, 12, 8, 5, 6}, new int[] {53, 51, 47, 48, 45}, new double[] {55.0, 50.0, 35.0, 45.0, 30.0});
    
    int hide;
    int[] productIds, hideAmounts, levelReqs;
    double[] exps;
    
    private LeatherData(int hide, int[] productIds, int[] hideAmounts, int[] levelReqs, double[] exps) {
        this.hide = hide;
        this.productIds = productIds;
        this.hideAmounts = hideAmounts;
        this.levelReqs = levelReqs;
        this.exps = exps;
    }
    
    private static Map<Integer, LeatherData> hides = new HashMap<Integer, LeatherData>();
    
    public static LeatherData forId(int hide) {
        return hides.get(hide);
    }
    
    /**
     * Populates the hide map.
     */
    static {
        for(LeatherData hide : LeatherData.values()) {
            hides.put(hide.hide, hide);
        }
    }
                
    public int getHide() {
        return hide;
    }
    
    public int getProductId(int index) {
        return productIds[index];
    }
    
    public int getHideAmount(int index) {
        return hideAmounts[index];
    }
    
    public int getLevelReq(int index) {
        return levelReqs[index];
    }
    
    public double getExp(int index) {
        return exps[index] * GameConstants.EXP_MODIFIER;
    }
}
