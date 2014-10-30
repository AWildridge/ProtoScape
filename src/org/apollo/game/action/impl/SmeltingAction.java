package org.apollo.game.action.impl;

import org.apollo.game.GameConstants;
import org.apollo.game.action.Action;
import org.apollo.game.content.skills.smithing.Smithing;
import org.apollo.game.model.Animation;
import org.apollo.game.model.Item;
import org.apollo.game.model.Player;
import org.apollo.game.model.Skill;
import org.apollo.util.Misc;

/**
 * SmeltingAction.java
 * @author The Wanderer
 */
public class SmeltingAction extends Action<Player> {

    Player player;
    
    int index;

    public SmeltingAction(Player player, int index) {
        super(5, false, player);
        this.player = player;
        this.index = index;
    }
    int amount = Smithing.amt;
    boolean stop = false;

    @Override
    public void execute() {
        if (amount >= 1) {
            if (amount > 1) {
                player.playAnimation(new Animation(899));
            }
            player.getInventory().remove(new Item(Smithing.primaryOre[index]));
            if (Smithing.secondaryOres[index] != null) {
                player.getInventory().remove(Smithing.secondaryOres[index]);
            }
            if (index == 1) {
                int chance;
                if (player.getSkillSet().getSkill(Skill.SMITHING).getCurrentLevel() <= 45) {
                    chance = (player.getSkillSet().getSkill(Skill.SMITHING).getCurrentLevel() - Smithing.levelReq[index]) + 50;
                } else {
                    chance = 80;
                }
                if (player.getEquipment().contains(2568)) {
                    chance = 100;
                }
                if (chance >= Misc.random(100)) {
                    player.getInventory().add(Smithing.bars[index]);
                    player.getSkillSet().addExperience(Skill.SMITHING, Smithing.exp[index] * GameConstants.EXP_MODIFIER);
                    player.sendMessage("You retrieve a bar of " + Smithing.bars[index].getDefinition().getName().trim().replace(" bar", "").toLowerCase() + ".");
                } else {
                    player.sendMessage("The iron ore was too impure and failed to create a bar.");
                }
            } else {
                player.getInventory().add(Smithing.bars[index]);
                player.getSkillSet().addExperience(Skill.SMITHING, Smithing.exp[index] * GameConstants.EXP_MODIFIER);
                player.sendMessage("You retrieve a bar of " + Smithing.bars[index].getDefinition().getName().trim().replace(" bar", "").toLowerCase() + ".");
            }
            amount--;
            if (amount == 0) {
                player.getAttributeTags().remove("skilling");
                stop = true;
                stop();
            }
            if (Smithing.secondaryOres[index] != null && stop == false) {
                if (!player.getInventory().contains(Smithing.secondaryOres[index].getId()) || player.getInventory().getCount(Smithing.secondaryOres[index].getId()) < Smithing.secondaryOres[index].getAmount() || !player.getInventory().contains(Smithing.primaryOre[index])) {
                    player.sendMessage("You have ran out of the required ore.");
                    player.getAttributeTags().remove("skilling");
                    stop();
                }
            } else if (Smithing.secondaryOres[index] == null && stop == false) {
                if (!player.getInventory().contains(Smithing.primaryOre[index])) {
                    player.sendMessage("You have ran out of the required ore.");
                    player.getAttributeTags().remove("skilling");
                    stop();
                }
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
}
