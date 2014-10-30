package org.apollo.game.content.skills.runecrafting;

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
                }
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
                }
            }
        }
    }

    public static void handleTalismanTeleporting(Player player, int itemID, int objectID) {
        for(Runes r : Runes.values()) {
            if(itemID == r.getTalismanID()) {
                if(objectID == r.getMysteriousRuinsID()) {
                    player.teleport(new Position(r.getAltarX(), r.getAltarY(), player.getPosition().getHeight()));
                }
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
            }
        }
    }

    public static void handleRifts(Player player, int objectID) {
        for(Runes r : Runes.values()) {
            if(r.getRiftID() == objectID) {
                player.teleport(new Position(r.getAltarX(), r.getAltarY(), player.getPosition().getHeight()));
            }
        }
    }
}
