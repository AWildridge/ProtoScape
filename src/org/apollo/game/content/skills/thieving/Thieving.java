package org.apollo.game.content.skills.thieving;

import org.apollo.game.action.DistancedAction;
import org.apollo.game.action.impl.PickpocketingAction;
import org.apollo.game.model.Animation;
import org.apollo.game.model.Item;
import org.apollo.game.model.NPC;
import org.apollo.game.model.Player;
import org.apollo.game.model.Position;
import org.apollo.game.model.Skill;
import org.apollo.game.model.World;
import org.apollo.game.scheduling.ScheduledTask;
import org.apollo.util.Misc;

/**
 * Thieving.java
 * 
 * @author The Wanderer
 */
public class Thieving {

    public static void handlePickpocketing(final Player player, int slot, Position loc) {
        if (player.hasAttributeTag("skilling")) {
            return;
        }
        if(player.hasAttributeTag("stunned")) {
            player.sendMessage("You are stunned!");
            return;
        }
        for (final Pickpocketing p : Pickpocketing.values()) {
            for (int i = 0; i < p.getNpcIDs().length; i++) {
                final NPC npc = (NPC) World.getWorld().getNPCRepository().get(slot);
                int npcID = npc.getDefinition().getId();
                if (p.npcIDs[i] == npcID) {

                    if (player.getSkillSet().getSkill(Skill.THIEVING).getCurrentLevel() < p.getLevelReq()) {
                        player.sendMessage("You must have a Thieving level of " + p.getLevelReq() + " to pickpocket this monster.");
                        return;
                    }
                    player.turnTo(loc);
                    player.getAttributeTags().add("skilling");
                    player.startAction(new DistancedAction<Player>(0, true, player, npc.getPosition(), 1) {

                        @Override
                        public void executeAction() {
                            if (!player.hasAttributeTag("skilling")) {
                                this.stop();
                            } else {
                                player.sendMessage("You attempt to pick the " + p.toString().toLowerCase().trim().replaceAll("_", " ") + "'s pocket...");
                                player.playAnimation(new Animation(881, 1));
                                player.startAction(new PickpocketingAction(player, npc.getPosition(), npc, p));
                            }
                            this.stop();
                        }

                        @Override
                        public QueuePolicy getQueuePolicy() {
                            throw new UnsupportedOperationException("Not supported yet.");
                        }

                        @Override
                        public WalkablePolicy getWalkablePolicy() {
                            throw new UnsupportedOperationException("Not supported yet.");
                        }
                    });

                }
            }
        }
    }

    public static void thieveStalls(final Player player, final int objectID, final Position pos) {
        for (Stalls s : Stalls.values()) {
            for (int i = 0; i < s.objects.length; i++) {
                if (s.objects[i] == objectID) {
                    if (player.getSkillSet().getSkill(Skill.THIEVING).getCurrentLevel() < s.getLevelReq()) {
                        player.sendMessage("You must have a Thieving level of " + s.getLevelReq() + " to steal from this stall.");
                        return;
                    }
                    int j = Misc.random(s.loot.length - 1);
                    player.getInventory().add(new Item(s.loot[j]));
                    player.getSkillSet().addExperience(Skill.THIEVING, s.getExp());
                    player.playAnimation(new Animation(881, 1));
                    player.sendNewObject(pos, 634, 0, 10);
                    player.sendMessage("You manage to steal some " + new Item(s.loot[j]).getDefinition().getName().toLowerCase() + " from the stall.");
                    System.out.println(pos);
                    World.getWorld().schedule(new ScheduledTask((int) (s.getTime() * 1000 / 600), false) {

                        @Override
                        public void execute() {
                            player.sendNewGlobalObject(pos, objectID, 0, 10);
                            this.stop();
                        }
                    });
                }
            }
        }
    }
}
