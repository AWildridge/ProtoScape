package org.apollo.game.content.skills.cooking;

import java.util.HashMap;

import org.apollo.game.GameConstants;

public enum MeatData {

    ANCHOVIES(321, 319, 323, 1, 30, 34, "anchovies", "some anchovies"),
    SHRIMPS(317, 315, 323, 1, 30, 33, "shrimps", "some shrimps"),
    BEEF(2132, 2142, 2146, 1, 30, 33, "meat", "a piece of beef"),
    RAT_MEAT(2134, 2142, 2146, 1, 30, 33, "meat", "a piece of rat meat"),
    BEAR_MEAT(2136, 2142, 2146, 1, 30, 33, "meat", "a piece of bear meat"),
    CHICKEN(2138, 2140, 2144, 1, 30, 33, "chicken", "some chicken"),
    KARAMBWANJI(3150, 3151, 3151, 1, 10, 20, "karambwanji", "a karambwanji"),
    RABBIT(3226, 3228, 7222, 1, 30, 33, "meat", "a rabbit"),
    SARDINE(327, 325, 369, 1, 40, 38, "sardine", "a sardine"),
    POISON_KARAMBWAN(3142, 3146, 3148, 1, 80, 20, "karambwan", "a poisoned karambwan"),
    KARAMBWAN(3146, 3144, 3148, 30, 190, 1, "karambwan", "a karambwan"),
    HERRING(345, 347, 357, 5, 50, 37, "herring", "a herring"),
    POTATO(1942, 6701, 6699, 7, 15, 42, "potato", "a potato"),
    MACKERAL(353, 355, 357, 10, 60, 45, "mackeral", "a mackeral"),
    THIN_SNAIL(3369, 3363, 3375, 12, 70, 47, "snail", "a thin snail"),
    TROUT(335, 333, 343, 15, 70, 50, "trout", "a trout"),
    LEAN_SNAIL(3371, 3365, 3375, 17, 80, 52, "snail", "a lean snail"),
    COD(341, 339, 343, 18, 75, 39, "cod", "a cod"),
    PIKE(349, 351, 343, 20, 80, 52, "pike", "a pike"),
    CRAB_MEAT(7518, 7521, 7520, 21, 100, 1, "crab meat", "some crab meat"),
    FAT_SNAIL(3373, 3367, 3375, 22, 95, 57, "snail", "a fat snail"),
    SALMON(331, 329, 343, 25, 90, 58, "salmon", "a salmon"),
    SLIMY_EEL(3379, 3381, 3383, 28, 95, 58, "eel", "a slimy eel"),
    CAVE_EEL(5005, 5003, 5006, 28, 115, 40, "eel", "a cave eel"),
    SWEETCORN(5986, 5988, 5990, 28, 104, 65, "sweetcorn", "some sweetcorn"),
    TUNA(359, 361, 367, 30, 100, 65, "tuna", "a tuna"),
    LOBSTER(377, 379, 381, 40, 120, 74, "lobster", "a lobster"),
    BASS(363, 365, 367, 43, 130, 80, "bass", "a bass"),
    SWORDFISH(371, 373, 375, 45, 140, 86, "swordfish", "a swordfish"),
    OOMLIE(2341, 2343, 2345, 50, 30, 85, "oomlie wrap", "an oomlie wrap"),
    LAVA_EEL(2148, 2149, 2149, 53, 30, 53, "eel", "a lava eel"),
    MONKFISH(7944, 7946, 7948, 62, 150, 96, "monkfish", "a monkfish"),
    SHARK(383, 385, 387, 80, 210, 100, "shark", "a shark"),
    SEA_TURTLE(395, 397, 399, 82, 211.3, 115, "sea turtle", "a sea turtle"),
    MANTA_RAY(389, 391, 393, 91, 216.3, 120, "manta ray", "a manta ray");
    private int raw;
    private int cooked;
    private int burnt;
    private int levelReq;
    private double exp;
    int burnLevel;
    private String burnMessage;
    private String cookMessage;

    MeatData(int raw, int cooked, int burnt, int levelReq, double exp, int burnLevel, String burnMessage, String cookMessage) {
        this.raw = raw;
        this.cooked = cooked;
        this.burnt = burnt;
        this.levelReq = levelReq;
        this.exp = exp;
        this.burnLevel = burnLevel;
        this.burnMessage = burnMessage;
        this.cookMessage = cookMessage;
    }

    public int getRaw() {
        return raw;
    }

    public int getCooked() {
        return cooked;
    }

    public int getBurnt() {
        return burnt;
    }

    public int getLevelReq() {
        return levelReq;
    }

    public double getExp() {
        return exp * GameConstants.EXP_MODIFIER;
    }

    public int getBurnLevel() {
        return burnLevel;
    }

    public String getBurnMessage() {
        return burnMessage;
    }

    public String getCookMessage() {
        return cookMessage;
    }
    
    public static final HashMap<Integer, MeatData> meats = new HashMap<Integer, MeatData>();

    public static MeatData forId(int itemId) {
        return meats.get(itemId);
    }

    static {
        for (MeatData meat : MeatData.values()) {
            meats.put(meat.raw, meat);
        }
    }
}
