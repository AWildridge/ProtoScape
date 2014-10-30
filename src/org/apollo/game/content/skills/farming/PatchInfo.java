package org.apollo.game.content.skills.farming;

import org.apollo.game.content.skills.farming.FarmingConfigs.AlottmentConfigs;
import org.apollo.game.content.skills.farming.FarmingConfigs.FlowerConfigs;
import org.apollo.game.content.skills.farming.FarmingConfigs.HerbConfigs;
import org.apollo.game.content.skills.farming.FarmingConfigs.TreeConfigs;
import org.apollo.util.Misc;


/**
 * The information of each patch.
 * 
 * 
 * @author Rodrigo Molina
 */
public enum PatchInfo {

    /*
     * Alottment patches.
     */
    POTATOE(new int[] { 8550, 8551, 8552, 8553, 8554, 8555 }, 5318, 1, 1942, 5, 20, AlottmentConfigs.POTATOE_SEED, Misc.minutesToSeconds(7 / 4),9.5),
    ONION(new int[] { 8550, 8551, 8552, 8553, 8554, 8555 }, 5319, 5, 1957, 10, 30, AlottmentConfigs.ONION_SEED, Misc .minutesToSeconds(7 / 4), 10.5),
    CABBAGE(new int[] { 8550,8551, 8552, 8553, 8554, 8555 }, 5324, 7, 1965, 15, 40, AlottmentConfigs.CABBAGE_SEED, Misc.minutesToSeconds(7 / 4), 11.5), 
    TOMATOE(new int[] { 8550, 8551, 8552, 8553, 8554, 8555 }, 5322, 12, 1982, 20, 50, AlottmentConfigs.TOMATOE, Misc.minutesToSeconds(7 / 4), 14),
    SWEETCORN( new int[] { 8550, 8551, 8552, 8553, 8554, 8555 }, 5320, 20, 5986,  30, 60, AlottmentConfigs.SWEETCORN, Misc.minutesToSeconds(11 / 4), 19),
    STRAWBERRY(new int[] { 8550, 8551, 8552, 8553, 8554, 8555 }, 5323, 31, 5504, 40, 70, AlottmentConfigs.STRAWBERRY, Misc.minutesToSeconds(11 / 4), 29), 
    WATERMELON(new int[] { 8550, 8551, 8552, 8553, 8554, 8555 }, 5321, 47, 5982, 50, 80, AlottmentConfigs.WATERMELON, Misc.minutesToSeconds(14 / 4), 54.5),

    /*
     * Flower patches.
     */
    MARIGOLD(new int[] { 7847, 7848, 7849 }, 5096, 2, 6010, 5, 10,FlowerConfigs.MARIGOLD, Misc.minutesToSeconds(17.5 / 4), 47),
    ROSEMARY( new int[] { 7847, 7848, 7849 }, 5097, 11, 6014, 8, 15, FlowerConfigs.ROSEMARY, Misc.minutesToSeconds(17.5 / 4), 66.5),
    NASTURTIUM(new int[] { 7847, 7848, 7849 }, 5098, 24, 6012, 10, 18,FlowerConfigs.NASTURTIUM, Misc.minutesToSeconds(17.5 / 4), 111), 
    WOAD( new int[] { 7847, 7848, 7849 }, 5099, 25, 5738, 12, 23,FlowerConfigs.WOAD_PLANT, Misc.minutesToSeconds(17.5 / 4), 115.5), 
    LIMPWURT(new int[] { 7847, 7848, 7849 }, 5100, 26, 226, 17, 30, FlowerConfigs.LIMPWURT_PLANT, Misc.minutesToSeconds(17.5 / 4), 120), 
    WHITE_LILY(new int[] { 7847, 7848, 7849 }, -1, 52, -1, 50, 90, FlowerConfigs.WHITE_LILY, Misc.minutesToSeconds(120 / 4), 250),
    // dont know item id for lily

    /*
     * Herb patches.
     */
    GUAM(new int[] { 8150, 8151, 8152 }, 5291, 9, 249, 10, 20, HerbConfigs.GUAM, Misc.minutesToSeconds(70 / 4), 12.5),
    MARRENTILL(new int[] { 8150, 8151, 8152 }, 5292, 14, 251, 13, 28,HerbConfigs.MARRENTILL, Misc.minutesToSeconds(70 / 4), 15), 
    TARROMIN(new int[] { 8150, 8151, 8152 }, 5293, 19, 253, 18, 35, HerbConfigs.TARROMIN, Misc.minutesToSeconds(70 / 4), 18),
    HARRLANDER(new int[] { 8150, 8151, 8152 }, 5294, 24, 255, 22, 40, HerbConfigs.HARRALANDER, Misc.minutesToSeconds(70 / 4), 24), 
    RANARR(new int[] { 8150, 8151, 8152 }, 5295, 32, 257, 23, 48, HerbConfigs.RANARR, Misc.minutesToSeconds(70 / 4), 30.5), 
    TOADFLAX(new int[] { 8150, 8151, 8152 }, 5296, 38, 2998, 29, 58, HerbConfigs.TOADFLAX, Misc.minutesToSeconds(70 / 4), 38.5),
    IRIT(new int[] { 8150, 8151, 8152 }, 5297, 44, 259, 32, 61, HerbConfigs.IRIT, Misc.minutesToSeconds(70 / 4), 48.5), 
    AVANTOE(new int[] { 8150, 8151, 8152 }, 5298, 50, 261, 38, 68, HerbConfigs.AVANTOE, Misc.minutesToSeconds(70 / 4), 61.5),
    KWUARM(new int[] { 8150, 8151, 8152 }, 5299, 56, 263, 48, 75, HerbConfigs.KWUARM, Misc.minutesToSeconds(70 / 4), 78), 
    SNAPDRAGON(new int[] { 8150, 8151, 8152 }, 5300, 62, 3000, 60, 86, HerbConfigs.SNAPDRAGON, Misc.minutesToSeconds(70 / 4), 98.5), 
    CADANTINE(new int[] { 8150, 8151, 8152 }, 5301, 67, 265, 65, 95,HerbConfigs.CADANTINE, Misc.minutesToSeconds(7 / 40), 120),
    LANTADYME(new int[] { 8150, 8151, 8152 }, 5302, 73, 2481, 76, 115, HerbConfigs.LANTADYME, Misc.minutesToSeconds(70 / 4), 151.5),
    DWARFWEED(new int[] { 8150, 8151, 8152 }, 5303, 79, 267, 80, 130, HerbConfigs.DWARF_WEED, Misc.minutesToSeconds(70 / 4), 192), 
    TORSTOL(new int[] { 8150, 8151, 8152 }, 5304, 85, 269, 90, 160, HerbConfigs.TORSTOL, Misc.minutesToSeconds(70 / 4), 224.5),

    /*
     * Tree patches.
     */
    OAK(new int[] { 8388, 8389, 8390, 8391 }, 5358, 15, 1521, 15, 40, TreeConfigs.OAK_TREE, Misc.minutesToSeconds(140 / 4), 467.3), 
    WILLOW(new int[] { 8388, 8389, 8390, 8391 }, 5359, 30, 1519, 18, 60,TreeConfigs.WILLOW_TREE, Misc.minutesToSeconds(220 / 4), 1456.3),
    MAPLE(new int[] { 8388, 8389, 8390, 8391 }, 5360, 45, 1517, 25, 75,TreeConfigs.MAPLE_TREE, Misc.minutesToSeconds(300 / 4), 3403.4),
    YEW(new int[] { 8388, 8389, 8390, 8391 }, 5361, 60, 1515, 30, 90, TreeConfigs.YEW_TREE, Misc.minutesToSeconds(380 / 4), 7069.9),
    MAGIC(new int[] { 8388, 8389, 8390, 8391 }, 5362, 75, 1513, 45, 120, TreeConfigs.MAGIC_TREE, Misc.minutesToSeconds(480 / 4), 13768.3),

    ;
    /**
     * I used an array because there are multiple object ids to which each type
     * of patch can be used for.
     */
    private int objectIds[];
    private int seedId;
    private int levelNeeded;
    private int harvestedItem;
    private int patchState;
    private int xpGivenPlanted;
    private int xpGivenHarvested;
    private double harvestCount;
    private int time;// in seconds, each cycle
    private int treeHealth;

    public boolean check(int object) {
	for (int a = 0; a < objectIds.length; a++) {
	    if (objectIds[a] == object)
		return false;
	}
	return true;
    }

    public static PatchInfo forId(int seedId) {
	for (PatchInfo pi : PatchInfo.values()) {
	    if (pi != null) {
		if (pi.getSeedId() == seedId)
		    return pi;
	    }
	}
	return null;
    }

    /**
     * Creates a new patch.
     * 
     * @param objectId
     *            The array of objects.
     * @param seedId
     *            The seed id.
     * @param levelNeeded
     *            The level needed.
     * @param itemback
     *            The item given back.
     * @param xpGiven
     *            The xp given.
     * @param xpGivenHarvested
     *            The xp given when harvested.
     * @param patchState
     *            The state of the patch.
     * @param time
     *            The time it takes to grow in each grow cycle.
     * @param harvestCount
     *            The count of items that can be harvested from this patch.
     */
    PatchInfo(int objectId[], int seedId, int levelNeeded, int itemback, int xpGiven, int xpGivenHarvested, int patchState, int time,
	    double harvestCount) {
	this.setObjectIds(objectId);
	this.setSeedId(seedId);
	this.setLevelNeeded(levelNeeded);
	this.setXpGivenPlanted(xpGiven);
	this.setHarvestedItem(itemback);
	this.setXpGivenHarvested(xpGivenHarvested);
	this.setPatchState(patchState);
	this.setTime(time);
	this.setHarvestCount(harvestCount);
    }

    /**
     * Creates a new patch.
     * 
     * @param objectId
     *            The array of objects.
     * @param seedId
     *            The seed id.
     * @param levelNeeded
     *            The level needed.
     * @param itemback
     *            The item given back.
     * @param xpGiven
     *            The xp given.
     * @param xpGivenHarvested
     *            The xp given when harvested.
     * @param patchState
     *            The state of the patch.
     * @param time
     *            The time it takes to grow in each grow cycle.
     * @param harvestCount
     *            The count of items that can be harvested from this patch.
     * @param treeHealth
     *            The health of the tree.
     */
    PatchInfo(int objectId[], int seedId, int levelNeeded, int itemback, int xpGiven, int xpGivenHarvested, int patchState, int time, double harvestCount, int treeHealth) {
	this.setObjectIds(objectId);
	this.setSeedId(seedId);
	this.setLevelNeeded(levelNeeded);
	this.setXpGivenPlanted(xpGiven);
	this.setHarvestedItem(itemback);
	this.setXpGivenHarvested(xpGivenHarvested);
	this.setPatchState(patchState);
	this.setTime(time);
	this.setHarvestCount(harvestCount);
	this.setTreeHealth(treeHealth);
    }

    public int getLevelNeeded() {
	return levelNeeded;
    }

    public void setLevelNeeded(int levelNeeded) {
	this.levelNeeded = levelNeeded;
    }

    public int getPatchState() {
	return patchState;
    }

    public void setPatchState(int patchState) {
	this.patchState = patchState;
    }

    public int getSeedId() {
	return seedId;
    }

    public void setSeedId(int seedId) {
	this.seedId = seedId;
    }

    public int[] getObjectIds() {
	return objectIds;
    }

    public void setObjectIds(int objectIds[]) {
	this.objectIds = objectIds;
    }

    public int getTime() {
	return time;
    }

    public void setTime(int time) {
	this.time = time;
    }

    public int getHarvestedItem() {
	return harvestedItem;
    }

    public void setHarvestedItem(int harvestedItem) {
	this.harvestedItem = harvestedItem;
    }

    public int getXpGivenPlanted() {
	return xpGivenPlanted /** SkillHandler.FARMING_MULTIPLIER*/;
    }

    public void setXpGivenPlanted(int xpGiven) {
	this.xpGivenPlanted = xpGiven;
    }

    public int getXpGivenHarvested() {
	return xpGivenHarvested /** SkillHandler.FARMING_MULTIPLIER*/;
    }

    public void setXpGivenHarvested(int xpGivenHarvested) {
	this.xpGivenHarvested = xpGivenHarvested;
    }

    public double getHarvestCount() {
	return harvestCount;
    }

    public void setHarvestCount(double harvestCount) {
	this.harvestCount = harvestCount;
    }

    public int getTreeHealth() {
	return treeHealth;
    }

    public void setTreeHealth(int treeHealth) {
	this.treeHealth = treeHealth;
    }
}
