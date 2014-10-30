package org.apollo.game.action.impl;

import org.apollo.game.GameConstants;
import org.apollo.game.action.Action;
import org.apollo.game.content.skills.smithing.Smithing;
import org.apollo.game.model.Animation;
import org.apollo.game.model.Item;
import org.apollo.game.model.Player;
import org.apollo.game.model.Skill;

/**
 * SmithingAction.java
 * @author The Wanderer
 */
public class SmithingAction extends Action<Player> {
    
    Player player;
    
    int itemId;

    public SmithingAction(Player player, int itemId) {
        super(4, false, player);
        this.player = player;
        this.itemId = itemId;
    }
    int barsRequired = Smithing.barsReq;
    int amount = Smithing.amt;
    int barType = Smithing.type;
    int index = Smithing.barIndex;
    Item smithedItem = Smithing.product;

    @Override
    public void execute() {
        if (amount > 0) {
            if (player.getInventory().getCount(barType) >= barsRequired) {
                if (amount > 1) {
                    player.playAnimation(new Animation(898));
                }
                player.getInventory().remove(new Item(barType, barsRequired));
                player.getInventory().add(smithedItem);
                player.getSkillSet().addExperience(Skill.SMITHING, 12.5 * (index + 1) * barsRequired * GameConstants.EXP_MODIFIER);
                Item product = new Item(itemId);
                player.sendMessage("You successfully smith a " + product.getDefinition().getName().toLowerCase() + ".");
            } else {
                player.sendMessage("You have ran out of bars.");
                player.getAttributeTags().remove("skilling");
                stop();
            }
        }
        amount--;
        if (amount == 0) {
            player.getAttributeTags().remove("skilling");
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
