package org.apollo.game.content.skills.prayer;

import org.apollo.game.GameConstants;

/**
 * The enum that handles bone data
 * 
 * @author The Wanderer
 */
public enum BoneData {
    REGULAR(526, 4.5, -1),
    WOLF(2859, 4.5, -1),
    BURNT(528, 4.5, -1),
    ZOMBIE_MONKEY(3185, 5, -1),
    REG_MONKEY(3183, 5, -1),
    BAT(530, 5.33, -1),
    BIG(532, 15, 4255),
    JOGRE(3125, 15, -1),
    NON_BEARDED_MONKEY_GUARDS(3179, 18, -1),
    BEARDED_MONKEY_GUARDS(3181, 20, -1),
    ZOGRE(4812, 22.5, 4257),
    BABY_DRAGON(534, 30, 4259),
    SKELETAL_WYVERN(6812, 50, 4261),
    DRAGON(536, 72, 4263),
    FAYRG(4830, 84, 4265),
    RAURG(4832, 96, 4267),
    DAGANNOTH(6729, 125, 4269),
    OURG(4834, 140, 4271);

    double exp;
    int id, bonemealId;

    BoneData(int id, double exp, int bonemealId) {
        this.exp = exp;
        this.id = id;
        this.bonemealId = bonemealId;
    }

    public int getId() {
        return id;
    }

    public double getExp() {
        return exp * GameConstants.EXP_MODIFIER;
    }

    public int getBonemealId() {
        return bonemealId;
    }
}
