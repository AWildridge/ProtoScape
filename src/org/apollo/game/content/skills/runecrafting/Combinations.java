package org.apollo.game.content.skills.runecrafting;

import org.apollo.game.GameConstants;

/**
 * Combinations.java
 * @author The Wanderer
 */
public enum Combinations {

    MIST(4695, 6, 1444, 1438, 555, 556, 8.0, 8.5, 2478, 2480),
    DUST(4696, 10, 1440, 1438, 557, 556, 8.3, 9.0, 2478, 2481),
    MUD(4698, 13, 1440, 1444, 557, 555, 9.3, 9.5, 2480, 2481),
    SMOKE(4697, 15, 1442, 1438, 554, 556, 8.5, 9.5, 2478, 2482),
    STEAM(4694, 19, 1442, 1444, 554, 555, 9.5, 10.0, 2480, 2482),
    LAVA(4699, 23, 1442, 1440, 554, 557, 10.0, 10.5, 2481, 2482);
    int comboID, levelReq, lowTali, highTali, lowRune, highRune, lowAltar, highAltar;
    double lowExp, highExp;

    private Combinations(int comboID, int levelReq, int lowTali, int highTali, int lowRune,
            int highRune, double lowExp, double highExp, int lowAltar, int highAltar) {
        this.comboID = comboID;
        this.levelReq = levelReq;
        this.lowTali = lowTali;
        this.highTali = highTali;
        this.lowRune = lowRune;
        this.highRune = highRune;
        this.lowExp = lowExp;
        this.highExp = highExp;
        this.lowAltar = lowAltar;
        this.highAltar = highAltar;
    }

    public int getComboID() {
        return comboID;
    }

    public int getLevelReq() {
        return levelReq;
    }

    public int getLowTali() {
        return lowTali;
    }

    public int getHighTali() {
        return highTali;
    }

    public int getLowRune() {
        return lowRune;
    }

    public int getHighRune() {
        return highRune;
    }

    public double getlowExp() {
        return lowExp * GameConstants.EXP_MODIFIER;
    }

    public double getHighExp() {
        return lowExp * GameConstants.EXP_MODIFIER;
    }

    public int getLowAltar() {
        return lowAltar;
    }

    public int getHighAltar() {
        return highAltar;
    }
}
