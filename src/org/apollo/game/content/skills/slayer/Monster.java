package org.apollo.game.content.skills.slayer;

/**
 * Encapsulates data regarding different monsters that can be assigned as assignments. This includes
 * the experience gained and the level to kill. DOES NOT INCLUDE TASK AMOUNTS (varies per master!!!)
 * @author The Wanderer
 */
public enum Monster {
    BATS(new int[] {412, 78}, new double[] {8, 32}, 1),
    BEARS(new int[] {105, 106, 1195}, new double[] {25, 27, 35}, 1),
    COWS(new int[] {81, 397, 955}, new double[] {8, 8, 8}, 1),
    CROCODILES(new int[] {1993}, new double[] {63}, 1),
    CYCLOPES(new int[] {116}, new double[] {75}, 1),
    DAGANNOTHS(new int[] {2887, 2888, 1338, 1339, 1340, 1341, 1342, 2881, 2882, 2883}, new double[] {86, 100, 70, 70, 120, 120, 120, 255, 255, 334}, 1),
    BLACK_DEMONS(new int[] {84}, new double[] {157}, 1),
    GREATER_DEMONS(new int[] {83}, new double[] {87}, 1),
    LESSER_DEMONS(new int[] {82}, new double[] {79}, 1),
    DOGS(new int[] {98, 99, 1593}, new double[] {49, 49, 62}, 1),
    BLACK_DRAGONS(new int[] {50, 54, 3376}, new double[] {258, 199, 80}, 1),
    BLUE_DRAGONS(new int[] {55, 52}, new double[] {108, 50}, 1),
    RED_DRAGONS(new int[] {53, 1589}, new double[] {143, 50}, 1),
    GREEN_DRAGONS(new int[] {941}, new double[] {75}, 1),
    BRONZE_DRAGONS(new int[] {1590}, new double[] {125}, 1),
    IRON_DRAGONS(new int[] {1591}, new double[] {173}, 1),
    STEEL_DRAGONS(new int[] {1592}, new double[] {220}, 1),
    DWARVES(new int[] {118, 119, 120, 121}, new double[] {16, 61, 26, 16}, 1),
    EARTH_WARRIORS(new int[] {124}, new double[] {54}, 1),
    ELVES(new int[] {1183, 1184}, new double[] {105, 108}, 1),
    GHOSTS(new int[] {103, 104}, new double[] {25, 25}, 1),
    GHOULS(new int[] {1218}, new double[] {50}, 1),
    FIRE_GIANTS(new int[] {110}, new double[] {111}, 1),
    HILL_GIANTS(new int[] {117}, new double[] {35}, 1),
    ICE_GIANTS(new int[] {111}, new double[] {70}, 1),
    MOSS_GIANTS(new int[] {112, 1681}, new double[] {60, 120}, 1),
    GOBLINS(new int[] {100, 101, 102, 298, 299, 444}, new double[] {13, 13, 13, 13, 13, 13}, 1),
    //GORAKS(new int[] {}, new double[] {112}, 1),
    HELLHOUNDS(new int[] {49}, new double[] {116}, 1),
    HOBGOBLINS(new int[] {122, 123}, new double[] {29, 49}, 1),
    ICE_WARRIORS(new int[] {125}, new double[] {59}, 1),
    ICEFIENDS(new int[] {3406}, new double[] {15}, 1),
    KALPHITES(new int[] {1153, 1156, 1154, 1155, 1157}, new double[] {40, 40, 90, 170, 170}, 1),
    MONKIES(new int[] {132}, new double[] {6}, 1),
    OGRES(new int[] {114, 115}, new double[] {60, 60}, 1),
    OTHERWORDLY_BEINGS(new int[] {126}, new double[] {66}, 1),
    BIRDS(new int[] {41, 951}, new double[] {3, 3}, 1),
    RATS(new int[] {47}, new double[] {2}, 1),
    SCORPIONS(new int[] {107}, new double[] {37}, 1),
    //SHADES(new int[] {}, new double[] {}, 1),
    SHADOW_WARRIORS(new int[] {158}, new double[] {67}, 1),
    SKELETONS(new int[] {90, 91, 92}, new double[] {29, 17, 59}, 1),
    SPIDERS(new int[] {61, 59, 60}, new double[] {2, 50, 50}, 1),
    TZHAAR(new int[] {2610, 2604}, new double[] {140, 125}, 1), //TODO: Add Hur and Mej
    TROLLS(new int[] {1115, 1116, 1117}, new double[] {150}, 1),
    //VAMPYRES(new int[] {}, new double[] {}, 1),
    WEREWOLFS(new int[] {1024, 1025, 1026, 1027, 1028, 1029, 1030, 1031, 1032, 1033, 1034, 1035}, 
            new double[] {60, 60, 60, 60, 60, 60, 100, 100, 100, 100, 100, 100}, 1),
    WOLVES(new int[] {96, 97, 95}, new double[] {34, 34, 70}, 1),
    ZOMBIES(new int[] {73, 74, 75, 76}, new double[] {30, 30, 53, 50}, 1),
    CRAWLING_HANDS(new int[] {1648, 1649, 1650, 1651, 1652, 1653, 1654, 1655, 1656, 1657}, 
            new double[] {16, 16, 16, 16, 16, 19, 19, 19, 19, 19}, 5),
    CAVE_BUGS(new int[] {1832}, new double[] {5}, 7),
    CAVE_CRAWLERS(new int[] {1600, 1601, 1602, 1603}, new double[] {22, 22, 22, 22}, 10),
    BANSHEES(new int[] {1612}, new double[] {22}, 15),
    CAVE_SLIMES(new int[] {1831}, new double[] {25}, 17),
    ROCKSLUGS(new int[] {1622, 1623}, new double[] {27}, 20),
    DESERT_LIZARDS(new int[] {2804, 2805, 2806, 2807, 2808}, new double[] {25, 25, 25, 15, 15}, 22),
    COCKATRICES(new int[] {1620, 1621}, new double[] {37, 37}, 25),
    PYREFIENDS(new int[] {1633, 1634, 1635, 1636}, new double[] {45, 45, 45, 45}, 30),
    MOGRES(new int[] {2801}, new double[] {48}, 32),
    HARPIE_BUG_SWARMS(new int[] {3153}, new double[] {25}, 33),
    WALL_BEASTS(new int[] {1827}, new double[] {105}, 35),
    KILLERWATTS(new int[] {3201, 3202}, new double[] {51, 51}, 37),
    BASILISKS(new int[] {1616, 1617}, new double[] {75}, 40),
    FEVER_SPIDER(new int[] {2850}, new double[] {40}, 42),
    INFERNAL_MAGES(new int[] {1643, 1644, 1645, 1646, 1647}, new double[] {60, 60, 60, 60, 60}, 45),
    BLOODVELDS(new int[] {1618, 1619}, new double[] {120, 120}, 50),
    JELLIES(new int[] {1637, 1638, 1639, 1640, 1641, 1642}, new double[] {75, 75, 75, 75, 75, 75}, 52),
    TUROTH(new int[] {1626, 1627, 1628, 1629, 1630, 1631, 1632}, 
            new double[] {77, 79, 77, 77, 76, 79, 81}, 55),
    ABERRANT_SPECTRES(new int[] {1604, 1605, 1606, 1607}, new double[] {90, 90, 90, 90}, 60),
    DUST_DEVILS(new int[] {1624}, new double[] {105}, 65),
    KURASKS(new int[] {1608, 1609}, new double[] {97, 97}, 70),
    SKELETAL_WYVERNS(new int[] {3068, 3069, 3070, 3071}, new double[] {210, 210, 210, 210}, 72),
    GARGOYLES(new int[] {1610, 1611}, new double[] {105, 105}, 75),
    NECHRYAELS(new int[] {1613}, new double[] {105}, 80),
    ABYSSAL_DEMONS(new int[] {1615}, new double[] {150}, 85);
    
    int[] npcIds;
    double[] exp;
    int levelReq;
    
    private Monster(int[] npcIds, double[] exp, int levelReq) {
        this.npcIds = npcIds;
        this.exp = exp;
        this.levelReq = levelReq;
    }

    public double getExp(int index) {
        return exp[index];
    }

    public int getLevelReq() {
        return levelReq;
    }

    public int[] getNpcIds() {
        return npcIds;
    }
}
