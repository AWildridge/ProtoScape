package org.apollo.game.content.skills;

import org.apollo.game.GameConstants;
import org.apollo.game.action.Action;
import org.apollo.game.action.Action.QueuePolicy;
import org.apollo.game.action.Action.WalkablePolicy;
import org.apollo.game.action.DistancedAction;
import org.apollo.game.model.Animation;
import org.apollo.game.model.Item;
import org.apollo.game.model.NPC;
import org.apollo.game.model.Player;
import org.apollo.game.model.Position;
import org.apollo.game.model.Skill;
import org.apollo.game.model.World;
import org.apollo.util.Misc;

/**
 * Fishing.java
 * @author The Wanderer
 */
public class Fishing {
    
    public enum Spots {
        CATHERBY(new int[] {}, new Position[] {});
        
        int[] id;
        Position[] pos;
        
        private Spots(int[] id, Position[] pos) {
            this.id = id;
            this.pos = pos;
        }
    }
    
    public enum Fish {
        
        NET_AND_BAIT_NET(316, new Item[] {new Item(317), new Item(321)}, new int[] {1, 15}, new double[] {10.0, 40.0}, new Item[] {new Item(303)}, 621, 1),
        NET_AND_BAIT_BAIT(316, new Item[] {new Item(327), new Item(345)}, new int[] {5, 10}, new double[] {20.0, 30.0}, new Item[] {new Item(307), new Item(313)}, 622, 2),
        NET_AND_HARPOON_NET(334, new Item[] {new Item(353), new Item(407), new Item(405), new Item(401), new Item(338), new Item(363)}, new int[] {16, 16, 16, 16, 23, 46}, new double[] {20.0, 10.0, 10.0, 1.0, 45.0, 100.0}, new Item[] {new Item(305)}, 621, 1),
        NET_AND_HARPOON_HARPOON(334, new Item[] {new Item(383)}, new int[] {76}, new double[] {110.0}, new Item[] {new Item(311)}, 618, 2),
        LURE_AND_BAIT_LURE(317, new Item[] {new Item(335), new Item(331)}, new int[] {20, 30}, new double[] {50.0, 70.0}, new Item[] {new Item(307), new Item(314)}, 622, 1),
        LURE_AND_BAIT_BAIT(317, new Item[] {new Item(349)}, new int[] {25}, new double[] {60.0}, new Item[] {new Item(307), new Item(313)}, 622, 2),
        CAGE_AND_HARPOON_CAGE(312, new Item[] {new Item(377)}, new int[] {40}, new double[] {90.0}, new Item[] {new Item(301)}, 619, 1),
        CAGE_AND_HARPOON_HARPOON(312, new Item[] {new Item(359), new Item(371)}, new int[] {35, 50}, new double[] {80.0, 100.0}, new Item[] {new Item(311)}, 618, 2);
        
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
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
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
                        player.startAction(new DistancedAction(5, true, player, npc.getPosition(), 1) {
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
                        player.startAction(new DistancedAction(5, true, player, npc.getPosition(), 1) {
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
            } else {
                return;
            }
        }
    }
}
