package org.apollo.game.action.impl;

import org.apollo.game.action.Action;
import org.apollo.game.content.skills.crafting.Crafting;
import org.apollo.game.content.skills.crafting.LeatherData;
import org.apollo.game.model.Animation;
import org.apollo.game.model.Item;
import org.apollo.game.model.Player;
import org.apollo.game.model.Skill;

/**
 * LeatherCraftingAction.java
 * @author The Wanderer
 */
public class LeatherCraftingAction extends Action<Player> {

    Player player;
    
    LeatherData hides;
    
    int index;

    public LeatherCraftingAction(Player player, LeatherData hides, int index) {
        super(3, false, player);
        this.player = player;
        this.hides = hides;
        this.index = index;
    }
    int amountMade = Crafting.amt;
    int threadCount = 0;

    @Override
    public void execute() {
        if (amountMade > 0) {
            if (player.getInventory().getCount(hides.getHide()) >= hides.getHideAmount(index)) {
                if (player.getInventory().contains(1734)) {
                    if (amountMade > 1) {
                        player.playAnimation(new Animation(1249));
                    }
                    player.getInventory().remove(new Item(hides.getHide(), hides.getHideAmount(index)));
                    player.getInventory().add(new Item(hides.getProductId(index), 1));
                    player.getSkillSet().addExperience(Skill.CRAFTING, hides.getExp(index));
                } else {
                    player.sendMessage("You have ran out of thread.");
                    stop();
                }
            } else {
                player.sendMessage("You have ran out of hide.");
                stop();
            }
        }
        amountMade--;
        threadCount++;
        if (threadCount == 4) {
            threadCount = 0;
            player.getInventory().remove(new Item(1734, 1));
        }
        if (amountMade == 0) {
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
}
