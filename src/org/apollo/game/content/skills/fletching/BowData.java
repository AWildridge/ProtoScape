package org.apollo.game.content.skills.fletching;

import java.util.HashMap;
import java.util.Map;

/**
 * An enum to handle all the bow data
 * 
 * @since 1/4/2011
 * 	<Last update: 1/17/2012>
 * 
 * @author Ares_
 * @author The Wanderer (restructured enum)
 */
public enum BowData {
    
        NORMAL(1511, new int[] {5, 10}, new double[] {10.0, 20.0}, new int[] {50, 48}, new int[] {841, 839}),
        OAK(1521, new int[] {20, 25}, new double[] {33.0, 50.0}, new int[] {54, 56}, new int[] {843, 845}),
        WILLOW(1519, new int[] {35, 40}, new double[] {66.5, 83.0}, new int[] {60, 58}, new int[] {849, 851}),
        MAPLE(1517, new int[] {50, 55}, new double[] {100.0, 116.5}, new int[] {64, 62}, new int[] {853, 851}),
        YEW(1515, new int[] {65, 70}, new double[] {135.0, 150.0}, new int[] {68, 66}, new int[] {857, 855}),
        MAGIC(1513, new int[] {50, 85}, new double[] {166.5, 183.0}, new int[] {72, 70}, new int[] {861, 859});
	
	private static Map<Integer, BowData> bows = new HashMap<Integer, BowData>();

	public static BowData forId(int item) { return bows.get(item); }

	static {
		for (BowData bow : BowData.values()) {
			bows.put(bow.log, bow);
		}
	}
        
        int log;
        int[] levelReqs, unstrungIds, rewards;
        double[] exps;
	
	BowData(int log, int[] levelReqs, double[] exps, int[] unstrungIds, int[] rewards) {
            this.log = log;
            this.levelReqs = levelReqs;
            this.exps = exps;
            this.unstrungIds = unstrungIds;
            this.rewards = rewards;
	}
        
        public int getLog() {
            return log;
        }
        
        public int getLevelReqs(int index) {
            return levelReqs[index];
        }
        
        public int[] getLevelReqs() {
            return levelReqs;
        }
        
        public int getUnstrungIds(int index) {
            return unstrungIds[index];
        }
        
        public int[] getUnstrungIds() {
            return unstrungIds;
        }
        
        public int getRewards(int index) {
            return rewards[index];
        }
        
        public int[] getRewards() {
            return rewards;
        }
        
        public double getExps(int index) {
            return exps[index];
        }
        
        public double[] getExps() {
            return exps;
        }
	
}
