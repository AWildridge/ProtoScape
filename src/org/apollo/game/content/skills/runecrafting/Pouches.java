package org.apollo.game.content.skills.runecrafting;

/**
 * Pouches.java
 * @author The Wanderer
 */
public enum Pouches {

    SMALL(5509, 5509, 1, -1, new int[3], new int[0]),
    MEDIUM(5510, 5511, 25, 270, new int[6], new int[4]),
    LARGE(5512, 5513, 50, 261, new int[9], new int[6]),
    GIANT(5514, 5515, 75, 120, new int[12], new int[9]);
    int levelReq, uses, pouch, degradedPouch;
    int[] storage, degradedStorage;

    private Pouches(int pouch, int degradedPouch, int levelReq, int uses, int[] storage, int[] degradedStorage) {
        this.levelReq = levelReq;
        this.uses = uses;
        this.storage = storage;
        this.pouch = pouch;
        this.degradedPouch = degradedPouch;
        this.degradedStorage = degradedStorage;
    }

    public int getLevelReq() {
        return levelReq;
    }

    public int getUses() {
        return uses;
    }

    public int[] getStorage() {
        return storage;
    }

    public void setStorage(int storage, int slot) {
        this.storage[slot] = storage;
    }

    public int getPouch() {
        return pouch;
    }

    public int getDegradedPouch() {
        return degradedPouch;
    }

    public int[] getDegradedStorage() {
        return degradedStorage;
    }

    public void setDegradedStorage(int degradedStorage, int slot) {
        this.degradedStorage[slot] = degradedStorage;
    }
}
