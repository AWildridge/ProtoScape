package org.apollo.game.content.skills.runecrafting;

/**
 * Talismans.java
 * @author The Wanderer
 */
public enum Talismans {

    AIR(1438, 2984, 3291),
    EARTH(1440, 3306, 3476),
    FIRE(1442, 3312, 3253),
    WATER(1444, 3185, 3163),
    BODY(1446, 3053, 3443),
    MIND(1448, 2980, 3514),
    CHAOS(1452, 3061, 3593),
    COSMIC(1454, 2407, 4379),
    DEATH(1456, 1862, 4639),
    LAW(1458, 2858, 3379),
    NATURE(1462, 2867, 3020);
    int taliID, ruinsX, ruinsY;

    private Talismans(int taliID, int ruinsX, int ruinsY) {
        this.taliID = taliID;
        this.ruinsX = ruinsX;
        this.ruinsY = ruinsY;
    }

    public int getTaliID() {
        return taliID;
    }

    public int getRuinsX() {
        return ruinsX;
    }

    public int getRuinsY() {
        return ruinsY;
    }
}
