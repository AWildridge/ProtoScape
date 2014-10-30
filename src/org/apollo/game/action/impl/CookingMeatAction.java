package org.apollo.game.action.impl;

import org.apollo.game.action.Action;
import org.apollo.game.content.skills.cooking.Cooking;
import org.apollo.game.content.skills.cooking.MeatData;
import org.apollo.game.model.Animation;
import org.apollo.game.model.Item;
import org.apollo.game.model.Player;
import org.apollo.game.model.Skill;
import org.apollo.util.Misc;

/**
 * CookingMeatAction.java
 * @author The Wanderer
 */
public class CookingMeatAction extends Action<Player> {

    Player player;

    MeatData meat;
    
    public CookingMeatAction(Player player, MeatData meat) {
        super(4, true, player);
        this.player = player;
        this.meat = meat;
    }
    int amount = Cooking.cookAmount;

    @Override
    public void execute() {
        if (amount > 0) {
            player.playAnimation(new Animation(883));
            if (Misc.random(100) >= 60 - ((player.getSkillSet().getSkill(Skill.COOKING).getCurrentLevel() - meat.getLevelReq()) * (60 / (meat.getBurnLevel() - meat.getLevelReq())))) {
                player.getInventory().remove(new Item(meat.getRaw()));
                player.getInventory().add(new Item(meat.getCooked()));
                player.getSkillSet().addExperience(Skill.COOKING, meat.getExp());
                player.sendMessage("You successfully cook " + meat.getCookMessage() + ".");
            } else {
                player.getInventory().remove(new Item(meat.getRaw()));
                player.getInventory().add(new Item(meat.getBurnt()));
                player.sendMessage("You accidentally burn the " + meat.getBurnMessage() + ".");
            }

        }
        amount--;
        if (!player.getInventory().contains(meat.getRaw()) || amount == 0) {
            player.sendMessage("You have ran out of " + new Item(meat.getRaw()).getDefinition().getName().toLowerCase() + ".");
            player.getAttributeTags().remove("cooking");
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
