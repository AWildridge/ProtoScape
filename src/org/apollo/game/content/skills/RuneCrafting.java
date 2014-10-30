package org.apollo.game.content.skills;

import java.util.HashMap;
import java.util.Map;

import org.apollo.game.GameConstants;
import org.apollo.game.model.Animation;
import org.apollo.game.model.Graphic;
import org.apollo.game.model.Item;
import org.apollo.game.model.Player;
import org.apollo.game.model.Position;
import org.apollo.game.model.Skill;
import org.apollo.util.Misc;

/**
 * RuneCrafting.java
 * @author The Wanderer
 */
public class RuneCrafting {
    
    public enum Runes {
        AIR(556, 1436, 1, 5.0, 11, 2478, 1438, 2452, 2841, 4829, 7139),
        MIND(558, 1436, 2, 5.5, 14, 2479, 1448, 2453, 2793, 4828, 7140),
        WATER(555, 1436, 5, 6.0, 19, 2480, 1444, 2454, 2726, 4832, 7137),
        EARTH(557, 1436, 9, 6.5, 26, 2481, 1440, 2455, 2655, 4830, 7130),
        FIRE(554, 1436, 14, 7, 35, 2482, 1442, 2456, 2574, 4849, 7129),
        BODY(559, 1436, 20, 7.5, 46, 2483, 1446, 2457, 2523, 4826, 7131),
        COSMIC(564, 7936, 27, 8, 59, 2484, 1454, 2458, 2142, 4853, 7132),
        CHAOS(562, 7936, 35, 8.5, 74, 2487, 1452, 2461, 2281, 4837, 7134),
        NATURE(561, 7936, 44, 9, 91, 2486, 1462, 2460, 2400, 4835, 7133),
        LAW(563, 7936, 54, 9.5, 150, 2485, 1458, 2459, 2464, 4818, 7135),
        DEATH(560, 7936, 65, 10, 150, 2488, 1456, 2462, 2208, 4830, 7136);

        int rune, requiredEss, levelReq, multiRunes, objectID, talismanID, mysteriousRuinsID, altarX, altarY, riftID;
        double exp;

        private Runes(int rune, int requiredEss, int levelReq, double exp, int multiRunes, int objectID, int talismanID, int mysteriousRuinsID, int altarX, int altarY, int riftID) {
            this.rune = rune;
            this.requiredEss = requiredEss;
            this.levelReq = levelReq;
            this.exp = exp;
            this.multiRunes = multiRunes;
            this.objectID = objectID;
            this.talismanID = talismanID;
            this.mysteriousRuinsID = mysteriousRuinsID;
            this.altarX = altarX;
            this.altarY = altarY;
            this.riftID = riftID;
        }

        private int getRune() {
            return rune;
        }

        private int getRequiredEss() {
            return requiredEss;
        }

        private int getLevelReq() {
            return levelReq;
        }

        private double getExp() {
            return exp * GameConstants.EXP_MODIFIER;
        }

        private int getMultiRunes() {
            return multiRunes;
        }

        private int getObjectID() {
            return objectID;
        }

        private int getTalismanID() {
            return talismanID;
        }

        private int getMysteriousRuinsID() {
            return mysteriousRuinsID;
        }

        private int getAltarX() {
            return altarX;
        }

        private int getAltarY() {
            return altarY;
        }
        
        private int getRiftID() {
            return riftID;
        }
        
        private static Map<Integer, Runes> runes = new HashMap<Integer, Runes>();

        public static Runes forId(int object) {
            return runes.get(object);
	}
    }

    public enum Pouches {
        SMALL(5509, 5509, 1, -1, new int[3], new int[0]),
        MEDIUM(5510, 5511, 25, 270, new int[6], new int[4]),
        LARGE(5512, 5513, 50, 261, new int[9], new int [6]),
        GIANT(5514, 5515, 75, 120, new int[12], new int [9]);

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

        private int getLevelReq() {
            return levelReq;
        }

        private int getUses() {
            return uses;
        }

        private int[] getStorage() {
            return storage;
        }

        private void setStorage(int storage, int slot) {
            this.storage[slot] = storage;
        }

        private int getPouch() {
            return pouch;
        }

        private int getDegradedPouch() {
            return degradedPouch;
        }
        
        private int[] getDegradedStorage() {
            return degradedStorage;
        }
        
        private void setDegradedStorage(int degradedStorage, int slot) {
            this.degradedStorage[slot] = degradedStorage;
        }
    }

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
        
        private int getComboID() {
            return comboID;
        }
        
        private int getLevelReq() {
            return levelReq;
        }
        
        private int getLowTali() {
            return lowTali;
        }
        
        private int getHighTali() {
            return highTali;
        }
        
        private int getLowRune() {
            return lowRune;
        }
        
        private int getHighRune() {
            return highRune;
        }
        
        private double getlowExp() {
            return lowExp * GameConstants.EXP_MODIFIER;
        }
        
        private double getHighExp() {
            return lowExp * GameConstants.EXP_MODIFIER;
        }
        
        private int getLowAltar() {
            return lowAltar;
        }
        
        private int getHighAltar() {
            return highAltar;
        }
    }
    
    public enum Talismans {
        AIR(1438, 2984, 3291),
        EARTH(1440, 3306, 3476),
        FIRE(1442, 3312, 3253),
        WATER(1444, 3185, 3163),
        BODY(1446, 3053, 3443),
        MIND(1448, 2980, 3514),
        CHAOS(1452, 3061, 3593),
        COSMIC(1454, 2407, 4379),
        DEATH(1456, 1862, 4639),
        LAW(1458, 2858, 3379),
        NATURE(1462, 2867, 3020);
        
        int taliID, ruinsX, ruinsY;
        
        private Talismans(int taliID, int ruinsX, int ruinsY) {
            this.taliID = taliID;
            this.ruinsX = ruinsX;
            this.ruinsY = ruinsY;
        }
        
        private int getTaliID() {
            return taliID;
        }
        
        private int getRuinsX() {
            return ruinsX;
        }
        
        private int getRuinsY() {
            return ruinsY;
        }
    }
    
    public static void handleTalismanLocating(Player player, int ITEM_ID) {
        for(Talismans t : Talismans.values()) {
            if(ITEM_ID == t.getTaliID()) {
                if(player.getPosition().getX() > (t.getRuinsX() + 5) && player.getPosition().getY() > (t.getRuinsY() + 5)) {
                    player.sendMessage("The talisman pulls to the southwest.");
                }
                if(player.getPosition().getX() > (t.getRuinsX() + 5) && player.getPosition().getY() < (t.getRuinsY() + 5)) {
                    player.sendMessage("The talisman pulls to the northwest.");
                }
                if(player.getPosition().getX() > (t.getRuinsX() + 5) && (player.getPosition().getY() - t.getRuinsY()) * (player.getPosition().getY() - t.getRuinsY()) <= 25) {
                    player.sendMessage("The talisman pulls to the west.");
                }
                if(player.getPosition().getX() < (t.getRuinsX() + 5) && player.getPosition().getY() > (t.getRuinsY() + 5)) {
                    player.sendMessage("The talisman pulls to the southeast.");
                }
                if(player.getPosition().getX() < (t.getRuinsX() + 5) && player.getPosition().getY() < (t.getRuinsY() + 5)) {
                    player.sendMessage("The talisman pulls to the northeast.");
                }
                if(player.getPosition().getX() < (t.getRuinsX() + 5) && (player.getPosition().getY() - t.getRuinsY()) * (player.getPosition().getY() - t.getRuinsY()) <= 25) {
                    player.sendMessage("The talisman pulls to the east.");
                }
                if(player.getPosition().getY() < (t.getRuinsY() + 5) && (player.getPosition().getX() - t.getRuinsX()) * (player.getPosition().getX() - t.getRuinsX()) <= 25) {
                    player.sendMessage("The talisman pulls to the north.");
                }
                if(player.getPosition().getY() > (t.getRuinsY() + 5) && (player.getPosition().getX() - t.getRuinsX()) * (player.getPosition().getX() - t.getRuinsX()) <= 25) {
                    player.sendMessage("The talisman pulls to the south.");
                }
                if(player.getPosition().getHeight() != 0) {
                    player.sendMessage("The talisman is stationary.");
                }
            } else {
                return;
            }
        }
    }

    public static void checkPouchUses(Player player, int ITEM_ID) {
        for(Pouches p : Pouches.values()) {
            if(p.getPouch() == 5509) {
                player.sendMessage("Your pouch currently has unlimited uses left.");
                return;
            }
            if(p.getPouch() == ITEM_ID) {
                player.sendMessage("Your pouch currently has " + p.getUses() + " uses left.");
                return;
            }
            if(p.getDegradedPouch() == ITEM_ID) {
                player.sendMessage("Your pouch currently has 0 uses left.");
                return;
            } else {
                return;
            }
        }
    }

    public static void handleComboRunes(Player player, int ITEM_ID, int OBJECT_ID) {
        for(Combinations c : Combinations.values()) {
            if(player.getSkillSet().getSkill(Skill.RUNECRAFTING).getCurrentLevel() < c.getLevelReq()) {
                player.sendMessage("You need level " + c.getLevelReq() + " to combine these runes.");
                return;
            }
            if(c.getLowTali() == ITEM_ID) {
                if(c.getLowAltar() == OBJECT_ID) {
                    if(player.getInventory().contains(7936) && player.getInventory().contains(c.getLowRune())) {
                        for(int i = 0; i < player.getInventory().getCount(7936); i++) {
                            if(Misc.random(2) == 1) {
                                player.playAnimation(new Animation(791));
                                player.playGraphic(new Graphic(186, 0, 100));
                                for(int j = 0; j < player.getInventory().capacity(); j++) {
                                    if(player.getInventory().get(j).getId() == 7936) {
                                        player.getSkillSet().addExperience(Skill.RUNECRAFTING, c.getlowExp());
                                        player.getInventory().remove(new Item(7936), j);
                                        player.getInventory().remove(new Item(c.getLowRune()));
                                        player.getInventory().add(new Item(c.getComboID(), 1));
                                    }
                                }
                            }
                        }
                        player.getInventory().remove(new Item(c.getLowTali()));
                        player.sendMessage("You successfully combine the runes together to create a new rune!");
                    }
                }
            }
            if(c.getHighTali() == ITEM_ID) {
                if(c.getHighAltar() == OBJECT_ID) {
                    if(player.getInventory().contains(7936) && player.getInventory().contains(c.getHighRune())) {
                        for(int i = 0; i < player.getInventory().getCount(7936); i++) {
                            if(Misc.random(2) == 1) {
                                player.playAnimation(new Animation(791));
                                player.playGraphic(new Graphic(186, 0, 100));
                                for(int j = 0; j < player.getInventory().capacity(); j++) {
                                    if(player.getInventory().get(j).getId() == 7936) {
                                        player.getSkillSet().addExperience(Skill.RUNECRAFTING, c.getHighExp());
                                        player.getInventory().remove(new Item(7936), j);
                                        player.getInventory().remove(new Item(c.getHighRune()));
                                        player.getInventory().add(new Item(c.getComboID(), 1));
                                    }
                                }
                            }
                        }
                        player.getInventory().remove(new Item(c.getHighTali()));
                        player.sendMessage("You successfully combine the runes together to create a new rune!");
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }

    public static void handleRuneCrafting(Player player, int OBJECT_ID) {
        for(Runes r : Runes.values()) {
            for(Pouches p : Pouches.values()) {
                if(r.getObjectID() == OBJECT_ID) {
                    if(r.getLevelReq() > player.getSkillSet().getSkill(Skill.RUNECRAFTING).getCurrentLevel()) {
                        player.sendMessage("You must have a Runecrafting level of " + r.getLevelReq() + " to craft these runes.");
                        return;
                    }
                    for(int i = 0; i < p.getStorage().length; i++) {
                        if(p.storage[i] != 0) {
                            p.setStorage(0, i);
                            player.playAnimation(new Animation(791));
                            player.playGraphic(new Graphic(186, 0, 100));
                            player.getSkillSet().addExperience(Skill.RUNECRAFTING, r.getExp());
                            if((player.getSkillSet().getSkill(Skill.RUNECRAFTING).getCurrentLevel() / r.getMultiRunes()) >= 1) {
                                player.getInventory().add(r.getRune(), (player.getSkillSet().getSkill(Skill.RUNECRAFTING).getCurrentLevel() / r.getMultiRunes()));
                            }
                            player.getInventory().add(new Item(r.getRune(), 1));
                            if(p.getUses() > 0) {
                                p.uses -= 1;
                            }
                        }
                    }
                    for(int i = 0; i < p.getDegradedStorage().length; i++) {
                        if(p.degradedStorage[i] != 0) {
                            p.setDegradedStorage(0, i);
                            player.playAnimation(new Animation(791));
                            player.playGraphic(new Graphic(186, 0, 100));
                            player.getSkillSet().addExperience(Skill.RUNECRAFTING, r.getExp());
                            if((player.getSkillSet().getSkill(Skill.RUNECRAFTING).getCurrentLevel() / r.getMultiRunes()) >= 1) {
                                player.getInventory().add(r.getRune(), (player.getSkillSet().getSkill(Skill.RUNECRAFTING).getCurrentLevel() / r.getMultiRunes()));
                            }
                            player.getInventory().add(new Item(r.getRune(), 1));
                        }
                    }
                    if(p.getUses() < 1 && p.getUses() != -1) {
                        player.getInventory().remove(new Item(p.getPouch()));
                        player.getInventory().add(new Item(p.getDegradedPouch()));
                        p.uses = -1;
                        player.sendMessage("Your pouch has ran out of uses and needs to be recharged.");
                    }
                    if(player.getInventory().contains(r.getRequiredEss())) {
                        player.playAnimation(new Animation(791));
                        player.playGraphic(new Graphic(186, 0, 100));
                        player.getSkillSet().addExperience(Skill.RUNECRAFTING, r.getExp() * (double) (player.getInventory().getCount(r.getRequiredEss())));
                        for(int i = 0; i < player.getInventory().capacity(); i++) {
                            if(player.getInventory().get(i).getId() == r.getRequiredEss()) {
                                player.getInventory().set(i, null);
                                if((player.getSkillSet().getSkill(Skill.RUNECRAFTING).getCurrentLevel() / r.getMultiRunes()) >= 1) {
                                    player.getInventory().add(r.getRune(), (player.getSkillSet().getSkill(Skill.RUNECRAFTING).getCurrentLevel() / r.getMultiRunes()));
                                }
                                player.getInventory().add(new Item(r.getRune(), 1));
                            }
                        }
                        player.sendMessage("You bind the temple's power into the essence.");
                    }
                } else {
                    return;
                }
            }
        }
    }

    public static void handleTalismanTeleporting(Player player, int itemID, int objectID) {
        for(Runes r : Runes.values()) {
            if(itemID == r.getTalismanID()) {
                if(objectID == r.getMysteriousRuinsID()) {
                    player.teleport(new Position(r.getAltarX(), r.getAltarY(), player.getPosition().getHeight()));
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }

    public static void populatePouches(Player player, int itemID) {
        for(Pouches p : Pouches.values()) {
            if(p.getPouch() == itemID) {
                if(p.getLevelReq() > player.getSkillSet().getSkill(Skill.RUNECRAFTING).getCurrentLevel()) {
                    player.sendMessage("You need level " + p.getLevelReq() + " Runecrafting to use this pouch.");
                    return;
                }
                if(player.getInventory().contains(1436) || player.getInventory().contains(7936)) {
                    for (int i = 0; i < p.getStorage().length; i++) {
                        if(p.storage[i] == 0) {   
                            if(player.getInventory().contains(1436)) {
                                player.getInventory().remove(new Item(1436));
                                p.setStorage(1436, i);
                            } else if(player.getInventory().contains(7936)) {
                                player.getInventory().remove(new Item(7936));
                                p.setStorage(7936, i);
                            }
                        }
                    }
                }
            }
            if(p.getDegradedPouch() == itemID) {
                if(p.getLevelReq() > player.getSkillSet().getSkill(Skill.RUNECRAFTING).getCurrentLevel()) {
                    player.sendMessage("You need level " + p.getLevelReq() + " Runecrafting to use this pouch.");
                    return;
                }
                if(player.getInventory().contains(1436) || player.getInventory().contains(7936)) {
                    for (int i = 0; i < p.getDegradedStorage().length; i++) {
                        if(p.degradedStorage[i] == 0) {   
                            if(player.getInventory().contains(1436)) {
                                player.getInventory().remove(new Item(1436));
                                p.setDegradedStorage(1436, i);
                            } else if(player.getInventory().contains(7936)) {
                                player.getInventory().remove(new Item(7936));
                                p.setDegradedStorage(7936, i);
                            }
                        }
                    }
                }
            } else {
                return;
            }
        }
    }

    public static void handleEmptyingPouches(Player player, int itemID) {
        for(Pouches p : Pouches.values()) {
            if(p.getPouch() == itemID) {
                if(p.getLevelReq() > player.getSkillSet().getSkill(Skill.RUNECRAFTING).getCurrentLevel()) {
                    player.sendMessage("You need level " + p.getLevelReq() + " Runecrafting to use this pouch.");
                    return;
                }
                for(int i = 0; i < p.getStorage().length; i++) {
                    if(p.storage[i] == 0) {
                        continue;
                    }
                    if(player.getInventory().freeSlots() != -1) {
                        player.getInventory().add(new Item(p.storage[i]));
                        p.setStorage(0, i);
                    }
                }
            }
            if(p.getDegradedPouch() == itemID) {
                if(p.getLevelReq() > player.getSkillSet().getSkill(Skill.RUNECRAFTING).getCurrentLevel()) {
                    player.sendMessage("You need level " + p.getLevelReq() + " Runecrafting to use this pouch.");
                    return;
                }
                for(int i = 0; i < p.getDegradedStorage().length; i++) {
                    if(p.degradedStorage[i] == 0) {
                        continue;
                    }
                    if(player.getInventory().freeSlots() != -1) {
                        player.getInventory().add(new Item(p.degradedStorage[i]));
                        p.setDegradedStorage(0, i);
                    }
                }
            } else {
                return;
            }
        }
    }

    public static void handleRifts(Player player, int objectID) {
        for(Runes r : Runes.values()) {
            if(r.getRiftID() == objectID) {
                player.teleport(new Position(r.getAltarX(), r.getAltarY(), player.getPosition().getHeight()));
            } else {
                return;
            }
        }
    }
}
