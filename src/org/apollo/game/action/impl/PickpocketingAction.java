package org.apollo.game.action.impl;

import org.apollo.game.action.DistancedAction;
import org.apollo.game.content.skills.thieving.Pickpocketing;
import org.apollo.game.event.impl.DamageEvent;
import org.apollo.game.model.Animation;
import org.apollo.game.model.Graphic;
import org.apollo.game.model.Item;
import org.apollo.game.model.NPC;
import org.apollo.game.model.Player;
import org.apollo.game.model.Position;
import org.apollo.game.model.Skill;
import org.apollo.game.model.World;
import org.apollo.game.scheduling.ScheduledTask;
import org.apollo.util.Misc;

/**
 * PickpocketingAction.java
 * @author The Wanderer
 */
public class PickpocketingAction extends DistancedAction<Player> {

    Player player;
    
    NPC npc;
    
    Position pos;
    
    Pickpocketing p;

    public PickpocketingAction(Player player, Position pos, NPC npc, Pickpocketing p) {
        super(1, false, player, pos, 1);
        this.pos = pos;
        this.player = player;
        this.npc = npc;
        this.p = p;
    }

    @Override
    public void executeAction() {
        if (Misc.random(((player.getSkillSet().getSkill(Skill.THIEVING).getCurrentLevel() - p.getLevelReq()) + 1) / 2) != 1) {
            int j = Misc.random(p.items.length - 1);
            player.getInventory().add(new Item(p.items[j], (Misc.random(p.quantities[j][1] - p.quantities[j][0]) + p.quantities[j][0])));
            player.sendMessage("You pick the " + p.toString().toLowerCase().trim().replaceAll("_", " ") + "'s pocket.");
            player.getSkillSet().addExperience(Skill.THIEVING, p.getExp());
            player.getAttributeTags().remove("skilling");
            this.stop();
        } else {
            player.getAttributeTags().add("stunned");
            player.sendMessage("You fail to pick the " + p.toString().toLowerCase().trim().replaceAll("_", " ") + "'s pocket.");
            player.playGraphic(new Graphic(80, 0, 100));
            player.playAnimation(player.getDefendAnimation());
            npc.turnTo(player.getPosition());
            npc.playAnimation(new Animation(422));
            player.getWalkingQueue().clear();
            Skill Hitpoints = player.getSkillSet().getSkill(Skill.HITPOINTS);
            player.damageCharacter(new DamageEvent(p.getDamage(), Hitpoints.getCurrentLevel(), Hitpoints.getMaximumLevel()));
            npc.forceChat("What do you think you are doing?!");
            player.getAttributeTags().remove("skilling");
            World.getWorld().schedule(new ScheduledTask(11, false) {

                @Override
                public void execute() {
                    player.getAttributeTags().remove("stunned");
                    this.stop();
                }
            });
            this.stop();
        }
    }

    @Override
    public QueuePolicy getQueuePolicy() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public WalkablePolicy getWalkablePolicy() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
