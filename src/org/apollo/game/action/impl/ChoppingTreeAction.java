package org.apollo.game.action.impl;

import org.apollo.game.action.Action;
import org.apollo.game.content.skills.woodcutting.HatchetData;
import org.apollo.game.content.skills.woodcutting.TreeData;
import org.apollo.game.content.skills.woodcutting.Woodcutting;
import org.apollo.game.model.Animation;
import org.apollo.game.model.Player;
import org.apollo.game.model.Position;
import org.apollo.game.model.Skill;
import org.apollo.game.model.World;
import org.apollo.game.scheduling.ScheduledTask;
import org.apollo.util.Misc;

/**
 * @author The Wanderer
 * @author Rodrigo Molina
 */
public class ChoppingTreeAction extends Action<Player> {

    private Player player;
    private TreeData treeCutting;
    private HatchetData hatchetUsing;
    private int time;
    private Position objectPos;

    public ChoppingTreeAction(int time, TreeData treeCutting, HatchetData hatchetUsing, Player player, Position pos) {
        super(1, false, player);
        this.player = player;
        this.treeCutting = treeCutting;
        this.hatchetUsing = hatchetUsing;
        this.time = time;
        this.objectPos = pos;
    }

    @Override
    public Action.QueuePolicy getQueuePolicy() {
        return Action.QueuePolicy.ALWAYS;
    }

    @Override
    public Action.WalkablePolicy getWalkablePolicy() {
        return Action.WalkablePolicy.NON_WALKABLE;
    }

    private int animCount = 0;

    @Override
    public void execute() {
        if (animCount >= 0) {
            animCount++;
        }
        if (animCount >= 3) {
            player.playAnimation(hatchetUsing.getAnim());
            animCount = 0;
        }
        if (time > 0) {
            time -= Misc.random(1);
        } else if (time == 0) {
            //they've recieved a log.
            if (player.getInventory().add(treeCutting.getLogGiven())) {
                player.getSkillSet().addExperience(Skill.WOODCUTTING, treeCutting.getXpGiven());
                if (Woodcutting.checkLogs(treeCutting) || treeCutting.checkObjectIds(1278)) {//ofc change the if to something more precise.
                    player.sendNewGlobalObject(objectPos, treeCutting.getStumpId(), 0, 10);
                    player.playAnimation(Animation.STOP_ANIMATION);
                    World.getWorld().schedule(new ScheduledTask(treeCutting.getRespawnTime() + Misc.random(2), false) {

                        @Override
                        public void execute() {
                            player.sendNewGlobalObject(objectPos, treeCutting.getObjectIdUsing(), 0, 10);
                            stop();
                        }

                    });
                    player.sendMessage("This tree has run out of logs.");
                    player.getAttributeTags().remove("skilling");
                    stop();
                    return;
                }
                treeCutting.setLogsCut(treeCutting.getLogsCut() + 1);
                player.sendMessage("You chop some logs.");
            }
            time = Woodcutting.calculateChoppingTime(treeCutting, player);
        }
    }
}
