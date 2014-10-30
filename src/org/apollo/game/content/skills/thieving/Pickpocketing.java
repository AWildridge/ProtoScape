package org.apollo.game.content.skills.thieving;

import org.apollo.game.GameConstants;

/**
 * Pickpocketing.java
 * @author The Wanderer
 */
public enum Pickpocketing {

    MAN(new int[]{1, 2, 3, 4, 5, 6}, 1, 8, new int[]{995}, new int[][]{{3, 3}}, 1),
    FARMER(new int[]{7, 1757}, 10, 14.5, new int[]{995, 5318}, new int[][]{{3, 3}, {1, 1}}, 1),
    FEMALE_HAM(new int[]{1715}, 15, 18.5, 
               new int[]{995, 590, 1511, 1617, 1619, 1621, 1623, 321, 1739, 2138, 4298, 4300, 4302, 4304, 4306, 4308, 4310, 1267, 249, 251, 253, 685, 686, 687, 688, 689, 690},
               new int[][]{{7, 12}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}}, 2),
    MALE_HAM(new int[]{1714}, 20, 22.2, 
             new int[]{995, 590, 1511, 1617, 1619, 1621, 1623, 321, 2138, 4298, 4300, 4302, 4304, 4306, 4308, 4310, 1267, 249, 251, 253, 685, 686, 687, 688, 689, 690},
             new int[][]{{10, 15}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}}, 3),
    HAM_GUARD(new int[]{1710, 1711, 1712}, 23, 22.2, 
             new int[]{995, 1739, 249, 251, 253, 1129, 1511, 1733, 1734, 590, 1617, 1619, 1621, 1623, 321, 2138, 4298, 4300, 4302, 4304, 4306, 4308, 4310, 882, 884, 886, 1349, 1351, 1353, 1203, 1205, 1207, 1265, 1267, 1269, 685, 686, 687, 688, 689, 690},
             new int[][]{{12, 17}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}}, 3),
    WARRIOR(new int[]{15, 18}, 25, 26, new int[]{995}, new int[][]{{18, 18}}, 2),
    ROGUE(new int[]{187}, 32, 36.5, new int[]{995, 995, 995, 556, 1523, 1219, 1993, 2357}, new int[][]{{25, 25}, {40, 40}, {45, 45}, {8, 8}, {1, 1}, {1, 1}, {1, 1}, {1, 1}}, 2),
    MASTER_FARMER(new int[]{2234, 2235, 3299}, 38, 43,
                  new int[]{5096, 5097, 5098, 5099, 5100, 5101, 5102, 5103, 5104, 5105, 5106, 5291, 5292, 5293, 5294, 5295, 5296, 5297, 5298, 5299, 5300, 5301, 5302, 5303, 5304, 5305, 5306, 5307, 5308, 5309, 5310, 5311, 5318, 5319, 5320, 5321, 5322, 5323, 5324},
                  new int[][]{{1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}}, 3),
    GAURD(new int[]{9, 10}, 40, 46.8, new int[]{995}, new int[][]{{30, 30}}, 2),
    FREMENNIK(new int[]{1305, 1306, 1307, 1308, 1309, 1310, 1311, 1312, 1313, 1314}, 45, 65, new int[]{995}, new int[][]{{40, 40}}, 2),
    BANDIT_41(new int[]{1883, 1884}, 45, 65, new int[]{995}, new int[][]{{40, 40}}, 5),
    KNIGHT(new int[]{23}, 55, 84.3, new int[]{995}, new int[][]{{50, 50}}, 3),
    BANDIT_56(new int[]{1880, 1881}, 55, 84.3, new int[]{995}, new int[][]{{50, 50}}, 5),
    WATCHMAN(new int[]{34}, 65, 137.5, new int[]{995, 2309}, new int[][]{{60, 60}, {1, 1}}, 3),
    MENAPHITE(new int[]{1904, 1905}, 65, 137.5, new int[]{995}, new int[][]{{60, 60}}, 5),
    PALADIN(new int[]{20}, 70, 151.8, new int[]{995, 995, 995, 995, 562}, new int[][]{{80, 80}, {80, 80}, {80, 80}, {80, 80}, {2, 2}}, 3),
    GNOME(new int[]{66, 67, 68, 159, 160, 161, 168, 169}, 75, 198.3, new int[]{995, 444, 557, 2150, 2162, 569}, new int[][]{{200, 400}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}}, 1),
    HERO(new int[]{21}, 80, 273.3, new int[]{995, 560, 565, 1993, 1601, 444, 569}, new int[][]{{100, 300}, {2, 2}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1},}, 4),
    ELVES(new int[]{2363, 2364, 2365, 2366, 2367}, 85, 353.3, new int[]{995, 561, 560, 569, 444, 1993, 1601, 237,}, new int[][]{{250, 350}, {3, 3}, {2, 2}, {1, 1}, {1, 1}, {1, 1}}, 5);
    public int[] npcIDs, items;
    public int[][] quantities;
    public int levelReq, damage;
    public double exp;

    /**
     * Checks for the enum which has this npc id.
     * 
     * @param npcId
     * 	The npc id pickpocketed.
     * @return the npc id
     */
    public static Pickpocketing forId(int npcId) {
	for(Pickpocketing p : Pickpocketing.values()) {
	    for(int a = 0; a < p.npcIDs.length; a++) {
		if(p.npcIDs[a] == npcId) {
		    return p;
		}
	    }
	}
	return null;
    }
    
    private Pickpocketing(int[] npcIDs, int levelReq, double exp, int[] items, int[][] quantities, int damage) {
        this.npcIDs = npcIDs;
        this.levelReq = levelReq;
        this.exp = exp;
        this.items = items;
        this.quantities = quantities;
        this.damage = damage;
    }

    public int[] getNpcIDs() {
        return npcIDs;
    }

    public int[] getItems() {
        return items;
    }

    public int[][] getQuantities() {
        return quantities;
    }

    public int getLevelReq() {
        return levelReq;
    }

    public int getDamage() {
        return damage;
    }

    public double getExp() {
        return exp * GameConstants.EXP_MODIFIER;
    }
}
