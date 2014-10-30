package org.apollo.game.content.skills.fishing;

import org.apollo.game.GameConstants;
import org.apollo.game.action.DistancedAction;
import org.apollo.game.model.Animation;
import org.apollo.game.model.NPC;
import org.apollo.game.model.Player;
import org.apollo.game.model.Skill;
import org.apollo.game.model.World;
import org.apollo.util.Misc;

/**
 * Fishing.java
 * @author The Wanderer
 */
public class Fishing {
    
	public static void handleFishing(int npcId, final Player player, int option, int slot) {
        for(final Fish f : Fish.values()) {
            if(npcId == f.getNpcId()) {
                if(option == f.getOption()) {
                    final NPC npc = (NPC) World.getWorld().getNPCRepository().get(slot);
                    if(player.hasAttributeTag("skilling")) {
                        return;
                    }
                    for(int i = 0; i < f.getRequiredItem().length; i++) {
                        if(!player.getInventory().contains(f.requiredItem[i].getId())) {
                            player.sendMessage("You need a " + f.requiredItem[i].getDefinition().getName().toLowerCase() + " to catch this fish."); 
                            return;
                        }
                    }
                    if(player.getSkillSet().getSkill(Skill.FISHING).getCurrentLevel() < f.levelReq[0]) {
                        player.sendMessage("You need level " + f.levelReq[0] + " Fishing to fish here.");
                        return;
                    }
                    player.getAttributeTags().add("skilling");
                    if(f.getProduced().length > 1 && player.getSkillSet().getSkill(Skill.FISHING).getCurrentLevel() >= f.levelReq[1]) {
                        player.startAction(new DistancedAction<Player>(5, true, player, npc.getPosition(), 1) {
                            @Override
                            public void executeAction() {
                                player.playAnimation(new Animation(f.getAnimId()));
                                int type = Misc.random(f.getProduced().length - 1);
                                int chance = (player.getSkillSet().getSkill(Skill.FISHING).getCurrentLevel() - f.levelReq[type]) + 10;
                                if(chance >= Misc.random(100)) {
                                    if(f.getRequiredItem().length > 1) {
                                        player.getInventory().remove(f.requiredItem[0]);
                                    }
                                    player.sendMessage("You have caught a " + f.produced[type].getDefinition().getName().toLowerCase() + "!");
                                    player.getInventory().add(f.produced[type]);
                                    player.getSkillSet().addExperience(Skill.FISHING, f.exp[type] * GameConstants.EXP_MODIFIER);
                                    if(player.getInventory().freeSlots() == 0) {
                                        player.sendMessage("You have ran out of inventory space.");
                                        player.getAttributeTags().remove("skilling");
                                        stop();
                                    }
                                }
                            }

                            @Override
                            public QueuePolicy getQueuePolicy() {
                                return QueuePolicy.NEVER;
                            }

                            @Override
                            public WalkablePolicy getWalkablePolicy() {
                                return WalkablePolicy.NON_WALKABLE;
                            }
                        });
                    } else {
                        player.startAction(new DistancedAction<Player>(5, true, player, npc.getPosition(), 1) {
                            @Override
                            public void executeAction() {
                                player.playAnimation(new Animation(f.getAnimId()));
                                int chance = (player.getSkillSet().getSkill(Skill.FISHING).getCurrentLevel() - f.levelReq[0]) + 10;
                                if(chance >= Misc.random(100)) {
                                    if(f.getRequiredItem().length > 1) {
                                        player.getInventory().remove(f.requiredItem[0]);
                                    }
                                    player.sendMessage("You have caught a " + f.produced[0].getDefinition().getName().toLowerCase() + "!");
                                    player.getInventory().add(f.produced[0]);
                                    player.getSkillSet().addExperience(Skill.FISHING, f.exp[0] * GameConstants.EXP_MODIFIER);
                                }
                                if(player.getInventory().freeSlots() == 0) {
                                    player.sendMessage("You have ran out of inventory space.");
                                    player.getAttributeTags().remove("skilling");
                                    player.stopAnimation();
                                    stop();
                                }
                           }

                            @Override
                            public QueuePolicy getQueuePolicy() {
                                return QueuePolicy.NEVER;
                            }

                            @Override
                            public WalkablePolicy getWalkablePolicy() {
                                return WalkablePolicy.NON_WALKABLE;
                            }
                        });
                    }
                }
            }
        }
    }
}
