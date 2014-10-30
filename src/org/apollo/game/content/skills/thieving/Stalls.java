package org.apollo.game.content.skills.thieving;

import org.apollo.game.GameConstants;

/**
 * Stalls.java
 * @author The Wanderer
 */
public enum Stalls {

    VEGETABLE(new int[]{4706, 4708}, 2, 10, 10, new int[]{1957, 1965, 1550, 1942, 1982}),
    CAKE(new int[]{2561, 630, 6163}, 5, 16, 2.5, new int[]{1891, 2309, 1901}),
    GENERAL(new int[]{4876}, 5, 16, 10, new int[]{590, 2347, 1931}),
    CRAFTING(new int[]{4874, 6166}, 5, 16, 10, new int[]{1755, 1597, 1592}),
    FOOD(new int[]{4875}, 5, 16, 10, new int[]{1963}),
    TEA(new int[]{635, 6574}, 5, 16, 2.5, new int[]{1978}),
    ROCK_CAKE(new int[]{2793}, 15, 10, 5, new int[]{2379}),
    SILK(new int[]{629, 2560, 6568}, 20, 24, 5, new int[]{950}),
    WINE(new int[]{14011}, 22, 27, 15, new int[]{1987, 1935, 7919, 1993, 1937}),
    SEED(new int[]{7053}, 27, 10, 5, new int[]{5318, 5319, 5324, 5322, 5320, 5323, 5321, 5305, 5306, 5307, 5308, 5309, 5310, 5311, 5097}),
    FUR(new int[]{632, 2563, 4278, 6571}, 35, 36, 15, new int[]{958}),
    FISH(new int[]{4705, 4707, 4277}, 42, 42, 15, new int[]{359, 331, 359, 331, 359, 331, 359, 331, 377}),
    SILVER(new int[]{628, 2565, 6164}, 50, 54, 30, new int[]{442}),
    MAGIC(new int[]{4877}, 65, 100, 60, new int[]{554, 555, 556, 557}),
    SPICE(new int[]{633, 2564, 6572}, 65, 81.3, 80, new int[]{2007}),
    SCIMITAR(new int[]{4878}, 65, 100, 60, new int[]{1323}),
    GEM(new int[]{631, 2562, 6162, 6570}, 75, 160, 180, new int[]{1617, 1619, 1621, 1623});
    int levelReq;
    int[] objects, loot;
    double exp, time;

    private Stalls(int[] objects, int levelReq, double exp, double time, int[] loot) {
        this.objects = objects;
        this.levelReq = levelReq;
        this.exp = exp;
        this.time = time;
        this.loot = loot;
    }

    public int getLevelReq() {
        return levelReq;
    }

    public int[] getObjects() {
        return objects;
    }

    public int[] getLoot() {
        return loot;
    }

    public double getExp() {
        return exp * GameConstants.EXP_MODIFIER;
    }

    public double getTime() {
        return time;
    }
}