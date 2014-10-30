package org.apollo.game.action.impl;

import org.apollo.game.action.Action;
import org.apollo.game.content.skills.fletching.BowData;
import org.apollo.game.model.Animation;
import org.apollo.game.model.Player;
import org.apollo.game.model.Skill;

/**
 * @author Rodrigo Molina
 */
public class FletchBowAction extends Action<Player> {

    private Player player;
    private int amount;
    private BowData bow;
    private boolean isShort;

    public FletchBowAction(Player player, int amount, BowData bow, boolean isShort) {
        super(2, false, player);
        this.player = player;
        this.amount = amount;
        this.bow = bow;
        this.isShort = isShort;
    }

    @Override
    public QueuePolicy getQueuePolicy() {
        return QueuePolicy.ALWAYS;
    }

    @Override
    public WalkablePolicy getWalkablePolicy() {
        return WalkablePolicy.NON_WALKABLE;
    }

    @Override
    public void execute() {
        if (amount > 0) {
            //prob more checks?
            int index = isShort ? 0 : 1;
            if (amount > 0) {
                player.playAnimation(Animation.FLETCHING);
            }
            player.getInventory().remove(bow.getLog());
            player.getInventory().add(bow.getUnstrungIds(index));
            player.getSkillSet().addExperience(Skill.FLETCHING, bow.getExps(index));
            player.sendMessage("You fletch the log into a bow!");
        }
        amount--;
        if (amount <= 0) {
            stop();
        }
    }
}
