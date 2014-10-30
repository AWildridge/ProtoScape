package org.apollo.game.content.skills.slayer;

/**
 * This class encapsulates the data required for tasks for each @link Masters.
 * @author The Wanderer
 */
public enum TaskSet {
    
    TURAEL(new Monster[] {
                Monster.BATS, Monster.BEARS, Monster.COWS, Monster.DOGS, Monster.DWARVES, 
                Monster.GHOSTS, Monster.GOBLINS, Monster.ICEFIENDS, Monster.MONKIES, Monster.BIRDS, 
                Monster.SCORPIONS, Monster.SKELETONS, Monster.SPIDERS, Monster.WOLVES, Monster.ZOMBIES, 
                Monster.CRAWLING_HANDS, Monster.CAVE_BUGS, Monster.BANSHEES, Monster.CAVE_SLIMES, Monster.DESERT_LIZARDS
            }, 
            new int[][] {
                {15, 50}, {15, 50}, {15, 50}, {15, 50}, {15, 50},
                {15, 50}, {15, 50}, {10, 20}, {15, 50}, {15, 50},
                {15, 50}, {15, 50}, {15, 50}, {15, 50}, {15, 50},
                {15, 50}, {15, 50}, {15, 50}, {15, 50}, {15, 50}
            }),
    MAZCHNA(new Monster[] {
                Monster.BANSHEES, Monster.BATS, Monster.BEARS, Monster.CAVE_CRAWLERS, Monster.CAVE_SLIMES,
                Monster.COCKATRICES, Monster.CRAWLING_HANDS, Monster.CYCLOPES, Monster.DESERT_LIZARDS, Monster.DOGS,
                Monster.GHOULS, Monster.GHOSTS, Monster.HILL_GIANTS, Monster.HOBGOBLINS, Monster.ICE_WARRIORS,
                Monster.KALPHITES, Monster.MOGRES, Monster.PYREFIENDS, Monster.ROCKSLUGS, Monster.SKELETONS,
                /*Monster.VAMPYRES,*/ Monster.WALL_BEASTS, Monster.WOLVES, Monster.ZOMBIES
            },
            new int[][] {
                {40, 85}, {40, 85}, {40, 85}, {40, 85}, {40, 85},
                {40, 85}, {40, 85}, {30, 60}, {40, 70}, {40, 70},
                {40, 70}, {40, 70}, {40, 70}, {40, 70}, {40, 70},
                {40, 70}, {40, 70}, {40, 70}, {40, 70}, {40, 70},
                /*{40, 70},*/ {10, 20}, {40, 70}, {40, 70}
            }),
    VANNAKAA(new Monster[] {
                Monster.ABERRANT_SPECTRES, Monster.BANSHEES, Monster.BASILISKS, Monster.BLOODVELDS, Monster.COCKATRICES,
                Monster.CROCODILES, Monster.CYCLOPES, Monster.DUST_DEVILS, Monster.EARTH_WARRIORS, Monster.GHOULS,
                Monster.GREEN_DRAGONS, Monster.HARPIE_BUG_SWARMS, Monster.HILL_GIANTS, Monster.ICE_GIANTS, Monster.ICE_WARRIORS,
                Monster.INFERNAL_MAGES, Monster.JELLIES, Monster.KILLERWATTS, Monster.LESSER_DEMONS, Monster.MOGRES,
                Monster.MOSS_GIANTS, Monster.OGRES, Monster.OTHERWORDLY_BEINGS, Monster.PYREFIENDS, /*Monster.SHADES,*/
                Monster.SHADOW_WARRIORS, Monster.TUROTH, /*Monster.VAMPYRES,*/ Monster.WEREWOLFS
            },
            new int[][] {
                {60, 120}, {60, 120}, {60, 120}, {60, 120}, {60, 120},
                {30, 60}, {60, 120}, {60, 120}, {30, 60}, {60, 120},
                {30, 60}, {60, 120}, {60, 120}, {60, 120}, {60, 120},
                {60, 120}, {60, 120}, {60, 120}, {60, 120}, {60, 120},
                {60, 120}, {60, 120}, {60, 120}, {60, 120}, /*{60, 120},*/
                {60, 120}, {60, 120}, /*{60, 120},*/ {60, 120}
            }),
    CHAELDAR(new Monster[] {
                Monster.ABERRANT_SPECTRES, Monster.ABYSSAL_DEMONS, Monster.BANSHEES, Monster.BASILISKS, Monster.BLOODVELDS,
                Monster.BLUE_DRAGONS, Monster.BRONZE_DRAGONS, Monster.CAVE_BUGS, Monster.CAVE_CRAWLERS, Monster.CAVE_SLIMES,
                Monster.COCKATRICES, Monster.CRAWLING_HANDS, Monster.CROCODILES, Monster.DAGANNOTHS, Monster.DESERT_LIZARDS,
                Monster.LESSER_DEMONS, Monster.DUST_DEVILS, Monster.ELVES, Monster.FEVER_SPIDER, Monster.FIRE_GIANTS,
                Monster.GARGOYLES, Monster.HARPIE_BUG_SWARMS, Monster.IRON_DRAGONS, Monster.INFERNAL_MAGES, Monster.JELLIES,
                Monster.KALPHITES, Monster.KURASKS, Monster.MOGRES, Monster.NECHRYAELS, Monster.PYREFIENDS,
                Monster.ROCKSLUGS, Monster.SHADOW_WARRIORS, Monster.TROLLS, Monster.TUROTH, Monster.WALL_BEASTS
            }, 
            new int[][] {
                {110, 170}, {110, 170}, {110, 170}, {110, 170}, {110, 170},
                {110, 170}, {30, 60}, {110, 170}, {110, 170}, {110, 170},
                {110, 170}, {110, 170}, {110, 170}, {110, 170}, {110, 170},
                {110, 170}, {110, 170}, {60, 90}, {110, 170}, {110, 170},
                {110, 170}, {110, 170}, {110, 170}, {110, 170}, {110, 170},
                {110, 170}, {110, 170}, {110, 170}, {110, 170}, {110, 170},
                {110, 170}, {110, 170}, {110, 170}, {110, 170}, {110, 170},
            }),
    DURADEL(new Monster[] {
                Monster.ABERRANT_SPECTRES, Monster.ABYSSAL_DEMONS, Monster.BLOODVELDS, Monster.BLACK_DEMONS, Monster.BLACK_DRAGONS,
                Monster.DAGANNOTHS, Monster.DUST_DEVILS, Monster.FIRE_GIANTS, Monster.GARGOYLES, /*Monster.GORAKS,*/
                Monster.GREATER_DEMONS, Monster.HELLHOUNDS, Monster.IRON_DRAGONS, Monster.KALPHITES, Monster.NECHRYAELS,
                Monster.SKELETAL_WYVERNS, Monster.STEEL_DRAGONS, Monster.KURASKS
            },
            new int[][] {
                {130, 200}, {130, 200}, {130, 200}, {130, 200}, {40, 80},
                {130, 200}, {130, 200}, {130, 200}, {130, 200}, /*{40, 80},*/
                {130, 200}, {130, 200}, {40, 80}, {130, 200}, {130, 200},
                {40, 80}, {40, 80}, {130, 200}
            }),;

    Monster[] monsters;
    int[][] taskAmounts;
    
    private TaskSet(Monster[] monsters, int[][] taskAmounts) {
        this.monsters = monsters;
        this.taskAmounts = taskAmounts;
    }

    public Monster getMonsters(int index) {
        return monsters[index];
    }
    
    public Monster[] getMonsters() {
        return monsters;
    }

    public void setMonsters(Monster[] monsters) {
        this.monsters = monsters;
    }

    /**
     * 
     * @param index Monster index
     * @param secondIndex 0 is minimum assigned 1 is maximum assigned
     * @return Minimum or maximum amount assigned
     */
    public int getTaskAmountLowHigh(int index, int secondIndex) {
        return taskAmounts[index][secondIndex];
    }

    public void setTaskAmounts(int[][] taskAmounts) {
        this.taskAmounts = taskAmounts;
    }
}
