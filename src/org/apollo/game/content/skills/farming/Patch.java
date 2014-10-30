package org.apollo.game.content.skills.farming;


/**
 * Represents each patch.
 * 
 * 
 * @author Rodrigo Molina
 */
public class Patch {

    private int objectId;
    private int configId;
    private int state;
    private int seedId;
    private double itemCount;
    private PatchInfo info;
    private boolean watered;
    private boolean supercompost;
    private boolean compost;
    private boolean diseased;
    private boolean dead;
    private int growTimer;
    private boolean isTree;
    private int treeHealth;
    /**
     * The chance will start at 10, so the patch has some sort of chance to not
     * be diseased.
     */
    private int chance = 10;

    public Patch(int configId, int objectId) {
	this.setConfigId(configId);
	this.setObjectId(objectId);
	this.setState(0);
    }

    public Patch(int configId, int objectId, int state) {
	this.setConfigId(configId);
	this.setObjectId(objectId);
	this.setState(state);
    }

    public Patch(int configId, int objectId, boolean isTree) {
	this.setConfigId(configId);
	this.setObjectId(objectId);
	this.setTree(isTree);
    }

    public boolean isDone() {
	return growTimer++ == 60;
    }

    public void reset() {
	growTimer = 0;
    }

    public void addState(int add) {
	state += add;
    }

    public int getObjectId() {
	return objectId;
    }

    public void setObjectId(int objectId) {
	this.objectId = objectId;
    }

    public int getState() {
	return state;
    }

    public void setState(int state) {
	this.state = state;
    }

    public int getSeedId() {
	return seedId;
    }

    public void setSeedId(int seedId) {
	this.seedId = seedId;
    }

    public double getItemCount() {
	return itemCount;
    }

    public void setItemCount(double d) {
	this.itemCount = d;
    }

    public boolean isWatered() {
	return watered;
    }

    public void setWatered(boolean watered) {
	this.watered = watered;
    }

    public boolean isDiseased() {
	return diseased;
    }

    public void setDiseased(boolean diseased) {
	this.diseased = diseased;
    }

    public boolean isDead() {
	return dead;
    }

    public void setDead(boolean dead) {
	this.dead = dead;
    }

    public int getConfigId() {
	return configId;
    }

    public void setConfigId(int configId) {
	this.configId = configId;
    }

    public PatchInfo getInfo() {
	return info;
    }

    public void setInfo(PatchInfo info) {
	this.info = info;
    }

    public int getChance() {
	return chance;
    }

    public void setChance(int chance) {
	this.chance = chance;
    }

    public void addChance(int v) {
	chance += v;
    }

    public boolean isSupercompost() {
	return supercompost;
    }

    public void setSupercompost(boolean supercompost) {
	this.supercompost = supercompost;
    }

    public boolean isCompost() {
	return compost;
    }

    public void setCompost(boolean compost) {
	this.compost = compost;
    }

    public boolean isTree() {
	return isTree;
    }

    public void setTree(boolean isTree) {
	this.isTree = isTree;
    }

    public int getTreeHealth() {
	return treeHealth;
    }

    public void setTreeHealth(int treeHealth) {
	this.treeHealth = treeHealth;
    }
}
