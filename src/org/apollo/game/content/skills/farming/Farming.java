package org.apollo.game.content.skills.farming;

import org.apollo.game.action.Action;
import org.apollo.game.event.impl.SendConfigEvent;
import org.apollo.game.model.Animation;
import org.apollo.game.model.Item;
import org.apollo.game.model.Player;
import org.apollo.game.model.Skill;
import org.apollo.game.model.World;
import org.apollo.game.scheduling.ScheduledTask;
import org.apollo.util.Misc;

/**
 * Farming.java
 * @author The Wanderer
 * @see SaveFarming void, Modified static would never work for all players
 */
@SuppressWarnings("unused")
public class Farming {
    
    private Player player;
    public Farming(Player player) {
        this.player = player;
    }
    
    private int herbConfigState = 0;
    private int SEED_DIBBER = 5343;
    private int RAKE = 5341;
    private int TROWEL = 5325;
    private int WEEDS = 6055;
    private int COMPOST = 6032;
    private int SUPER_COMPOST = 6034;
    private int PLANT_CURE = 6036;
    private int SPADE = 952;
    private boolean isComposted;
    private boolean isSuperComposted;
    private boolean isDiseased;
    public int chanceDiseased = 30; //30%
    public boolean isDead;
    public static final int MAX_PATCHES = 20;
    private int amount;
    private int type;

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void cleanPatch(int itemId, int objectId) {
        if (itemId == RAKE) {
            player.playAnimation(new Animation(2273));
            player.startAction(new Action(3, false, player) {

                @Override
                public void execute() {
                    if (herbConfigState >= 3 || player.getInventory().freeSlots() <= 0) {
                        stop();
                    } else {
                        herbConfigState++;
                        player.playAnimation(new Animation(2273));
                        player.send(new SendConfigEvent(515, herbConfigState));
                        player.getInventory().add(WEEDS);
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
    
    
    //TODO
    public void saveFarmingStates(int state, int config) {
        for(int i = 0; i < MAX_PATCHES; i++) {
            if(player.farmingConfig[i] < 0) {
                player.farmingConfig[i] = config;
                player.farmingState[i] = state;
            }
        }
    }
    
    //TODO
    public void loadOnLogin() {
        for(int i = 0; i < MAX_PATCHES; i++) {
            if(player.farmingConfig[i] >= 0) {
                player.send(new SendConfigEvent(player.farmingConfig[i], player.farmingState[i]));
                this.herbConfigState = player.farmingState[i];
                player.send(new SendConfigEvent(player.farmingConfig[i], player.farmingState[i]));
            }
        }
    }

    public void addCompost(int itemId, int objectId) {
        if (herbConfigState == 3 && isComposted == false) {
            if (itemId == COMPOST) {
                //player.playAnimation(Animation.YES);
                chanceDiseased += 10;
                player.getInventory().remove(COMPOST);
                player.sendMessage("You fertilize the patch with compost!");
                player.getInventory().add(1925);
                isComposted = true;
            } else if (itemId == SUPER_COMPOST) {
                chanceDiseased += 30;
                player.getInventory().remove(SUPER_COMPOST);
                player.sendMessage("You fertilize the patch with super compost!");
                player.getInventory().add(1925);
                isSuperComposted = true;
            }
        }
    }

    public void clearPatch(int itemId, int objectId) {
        if (isDead == true) {
            if (itemId == SPADE) {
                player.send(new SendConfigEvent(515, 3));
                isComposted = false;
                isDiseased = false;
                chanceDiseased = 30; //30%
                isDead = false;
                herbConfigState = 3;
                player.sendMessage("You clear the patch of the unwanted, dead plants.");
            }
        }
    }

    public void diseaseCuring(int itemId, int objectId) {
        if (itemId == PLANT_CURE) {
            if (isDiseased == true) {
                player.getInventory().remove(PLANT_CURE);
                player.getInventory().add(229);
                isDiseased = false;
                player.send(new SendConfigEvent(515, herbConfigState + 1));
                System.out.println(herbConfigState);
                player.sendMessage("You cure the plant of all diseases!");
            }
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	public void herbHarvesting(int objectId) {
        if (herbConfigState >= 8) {
            if (isSuperComposted == true) {
                amount = Misc.random(20) + 5;
            } else {
                amount = Misc.random(15) + 3;
            }
            player.startAction(new Action(3, false, player) {
                int harvestAmount = amount;
                @Override
                public void execute() {
                    for (HerbData herb : HerbData.values()) {
                        if (herb.getSeedId() == type) {
                            if (harvestAmount > 0) {
                                if (player.getInventory().freeSlots() > 0) {
                                    player.getInventory().add(herb.getHarvestId());
                                    player.sendMessage("You harvest a " + new Item(herb.getHarvestId()).getDefinition().getName() + ".");
                                    player.getSkillSet().addExperience(Skill.FARMING, herb.getHarvestExp());
                                } else {
                                    player.sendMessage("You have ran out of space in your inventory.");
                                    stop();
                                }
                            } else {
                                player.send(new SendConfigEvent(515, 3));
                                stop();
                            }
                            harvestAmount--;
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
            isComposted = false;
            isDiseased = false;
            chanceDiseased = 30; //30%
            isDead = false;
            herbConfigState = 3;
        }
    }
    
    public void handleFarmingEvent(int itemId, int objectId) {
        World.getWorld().schedule(new ScheduledTask(5, false) {

                        @Override
                        public void execute() {
                            if (isDiseased == true) {
                                player.send(new SendConfigEvent(515, herbConfigState + 166));
                                isDead = true;
                                isDiseased = false;
                            } else if (herbConfigState >= 8 || isDead == true) {
                                stop();
                            } else if (chanceDiseased < Misc.random(100) && herbConfigState > 3 && herbConfigState != 7) {
                                player.send(new SendConfigEvent(515, herbConfigState + 124));
                                isDiseased = true;
                            } else {
                                player.send(new SendConfigEvent(515, herbConfigState + 1));
                                herbConfigState++;
                            }
                        }
        });
    }

    public void handleHerbFarming(int itemId, int objectId) {
        for (HerbData herb : HerbData.values()) {
            if (herb.getSeedId() == itemId) {
                if (herbConfigState == 3) {
                    type = itemId;
                    if (!player.getInventory().contains(herb.getSeedId())) {
                        return; //client out of sync
                    }
                    if (player.getSkillSet().getSkill(Skill.FARMING).getCurrentLevel() < herb.getLevelReq()) {
                        player.sendMessage("You need level " + herb.getLevelReq() + " Farming to grow this herb.");
                        return;
                    }
                    if (!player.getInventory().contains(SEED_DIBBER)) {
                        player.sendMessage("You need a seed dibber to plant seeds here.");
                        return;
                    }
                    player.sendMessage("You plant some seeds.");
                    player.playAnimation(new Animation(2291));
                    player.getInventory().remove(herb.getSeedId());
                    player.getSkillSet().addExperience(Skill.HERBLORE, herb.plantExp);
                    handleFarmingEvent(itemId, objectId);

                }
            }
        }
    }
}
