package org.apollo.game.content.skills.fishing;

import org.apollo.game.model.Item;

/**
 * Fish.java
 * @author The Wanderer
 */
public enum Fish {

    NET_AND_BAIT_NET(316, new Item[]{new Item(317), new Item(321)}, new int[]{1, 15}, new double[]{10.0, 40.0}, new Item[]{new Item(303)}, 621, 1),
    NET_AND_BAIT_BAIT(316, new Item[]{new Item(327), new Item(345)}, new int[]{5, 10}, new double[]{20.0, 30.0}, new Item[]{new Item(307), new Item(313)}, 622, 2),
    NET_AND_HARPOON_NET(334, new Item[]{new Item(353), new Item(407), new Item(405), new Item(401), new Item(338), new Item(363)}, new int[]{16, 16, 16, 16, 23, 46}, new double[]{20.0, 10.0, 10.0, 1.0, 45.0, 100.0}, new Item[]{new Item(305)}, 621, 1),
    NET_AND_HARPOON_HARPOON(334, new Item[]{new Item(383)}, new int[]{76}, new double[]{110.0}, new Item[]{new Item(311)}, 618, 2),
    LURE_AND_BAIT_LURE(317, new Item[]{new Item(335), new Item(331)}, new int[]{20, 30}, new double[]{50.0, 70.0}, new Item[]{new Item(307), new Item(314)}, 622, 1),
    LURE_AND_BAIT_BAIT(317, new Item[]{new Item(349)}, new int[]{25}, new double[]{60.0}, new Item[]{new Item(307), new Item(313)}, 622, 2),
    CAGE_AND_HARPOON_CAGE(312, new Item[]{new Item(377)}, new int[]{40}, new double[]{90.0}, new Item[]{new Item(301)}, 619, 1),
    CAGE_AND_HARPOON_HARPOON(312, new Item[]{new Item(359), new Item(371)}, new int[]{35, 50}, new double[]{80.0, 100.0}, new Item[]{new Item(311)}, 618, 2);
    int[] levelReq;
    Item[] requiredItem, produced;
    int npcId, animId, option;
    double[] exp;

    private Fish(int npcId, Item[] produced, int[] levelReq, double[] exp, Item[] requiredItem, int animId, int option) {
        this.npcId = npcId;
        this.produced = produced;
        this.levelReq = levelReq;
        this.exp = exp;
        this.requiredItem = requiredItem;
        this.animId = animId;
        this.option = option;
    }

    public int[] getLevelReq() {
        return levelReq;
    }

    public Item[] getRequiredItem() {
        return requiredItem;
    }

    public Item[] getProduced() {
        return produced;
    }

    public int getNpcId() {
        return npcId;
    }

    public int getAnimId() {
        return animId;
    }

    public double[] getExp() {
        return exp;
    }

    public int getOption() {
        return option;
    }
}
