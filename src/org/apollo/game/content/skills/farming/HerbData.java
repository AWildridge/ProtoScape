package org.apollo.game.content.skills.farming;

import org.apollo.game.GameConstants;

/**
 * HerbData.java
 * @author The Wanderer
 */
public enum HerbData {
    GUAM(5291, 9, 11.0, 12.5, 249),
    MARRENTILL(5292, 14, 13.5, 15.0, 251),
    TARROMIN(5293, 19, 16.0, 18.0, 253),
    HARRALANDER(5294, 26, 21.5, 24.0, 255),
    RANARR(5295, 32, 27.0, 30.5, 257),
    TOADFLAX(5296, 38, 34.0, 38.5, 2998),
    IRIT(5297, 44, 43.0, 48.5, 259),
    AVANTOE(5298, 50, 54.5, 61.5, 261),
    KWUARM(5299, 56, 69.0, 78.0, 263),
    SNAPDRAGON(5300, 62, 87.5, 98.5, 3000),
    CADANTINE(5301, 67, 106.5, 120.0, 265),
    LANTADYME(5302, 73, 134.5, 151.5, 2481),
    DWARF_WEED(5303, 79, 170.5, 192.0, 267),
    TORSTOL(5304, 85, 199.5, 224.5, 269);
    
    double plantExp, harvestExp;
    int levelReq, seedId, harvestId;
    
    public static int CYCLE_TIME = 8000; //ticks
    
    //public static Animation HARVEST = new Animation();
    
    private HerbData(int seedId, int levelReq, double plantExp, double harvestExp, int harvestId) {
        this.seedId = seedId;
        this.levelReq = levelReq;
        this.plantExp = plantExp;
        this.harvestExp = harvestExp;
        this.harvestId = harvestId;
    }
    
    public int getSeedId() {
        return seedId;
    }
    
    public int getLevelReq() {
        return levelReq;
    }
    
    public double getPlantExp() {
        return plantExp * GameConstants.EXP_MODIFIER;
    }
    
    public double getHarvestExp() {
        return harvestExp * GameConstants.EXP_MODIFIER;
    }
    
    public int getHarvestId() {
        return harvestId;
    }
}
