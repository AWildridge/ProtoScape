package org.apollo.game.content.skills.runecrafting;

import java.util.HashMap;
import java.util.Map;

import org.apollo.game.GameConstants;

/**
 * Runes.java
 * @author The Wanderer
 */
public enum Runes {

    AIR(556, 1436, 1, 5.0, 11, 2478, 1438, 2452, 2841, 4829, 7139),
    MIND(558, 1436, 2, 5.5, 14, 2479, 1448, 2453, 2793, 4828, 7140),
    WATER(555, 1436, 5, 6.0, 19, 2480, 1444, 2454, 2726, 4832, 7137),
    EARTH(557, 1436, 9, 6.5, 26, 2481, 1440, 2455, 2655, 4830, 7130),
    FIRE(554, 1436, 14, 7, 35, 2482, 1442, 2456, 2574, 4849, 7129),
    BODY(559, 1436, 20, 7.5, 46, 2483, 1446, 2457, 2523, 4826, 7131),
    COSMIC(564, 7936, 27, 8, 59, 2484, 1454, 2458, 2142, 4853, 7132),
    CHAOS(562, 7936, 35, 8.5, 74, 2487, 1452, 2461, 2281, 4837, 7134),
    NATURE(561, 7936, 44, 9, 91, 2486, 1462, 2460, 2400, 4835, 7133),
    LAW(563, 7936, 54, 9.5, 150, 2485, 1458, 2459, 2464, 4818, 7135),
    DEATH(560, 7936, 65, 10, 150, 2488, 1456, 2462, 2208, 4830, 7136);
    int rune, requiredEss, levelReq, multiRunes, objectID, talismanID, mysteriousRuinsID, altarX, altarY, riftID;
    double exp;

    private Runes(int rune, int requiredEss, int levelReq, double exp, int multiRunes, int objectID, int talismanID, int mysteriousRuinsID, int altarX, int altarY, int riftID) {
        this.rune = rune;
        this.requiredEss = requiredEss;
        this.levelReq = levelReq;
        this.exp = exp;
        this.multiRunes = multiRunes;
        this.objectID = objectID;
        this.talismanID = talismanID;
        this.mysteriousRuinsID = mysteriousRuinsID;
        this.altarX = altarX;
        this.altarY = altarY;
        this.riftID = riftID;
    }

    public int getRune() {
        return rune;
    }

    public int getRequiredEss() {
        return requiredEss;
    }

    public int getLevelReq() {
        return levelReq;
    }

    public double getExp() {
        return exp * GameConstants.EXP_MODIFIER;
    }

    public int getMultiRunes() {
        return multiRunes;
    }

    public int getObjectID() {
        return objectID;
    }

    public int getTalismanID() {
        return talismanID;
    }

    public int getMysteriousRuinsID() {
        return mysteriousRuinsID;
    }

    public int getAltarX() {
        return altarX;
    }

    public int getAltarY() {
        return altarY;
    }

    public int getRiftID() {
        return riftID;
    }
    public static Map<Integer, Runes> runes = new HashMap<Integer, Runes>();

    public static Runes forId(int object) {
        return runes.get(object);
    }
}
