package org.apollo.game.action.impl;

import org.apollo.game.action.Action;
import org.apollo.game.content.skills.fletching.Fletching;
import org.apollo.game.model.Animation;
import org.apollo.game.model.Item;
import org.apollo.game.model.Player;
import org.apollo.game.model.Skill;

/**
 * @author Rodrigo Molina
 */
public class FletchArrowShaftActon extends Action<Player> {

	private int amt;
	private Player player;
	
	public FletchArrowShaftActon(Player player, int amount) {
		super(2, false, player);
		this.player = player;
		this.amt = amount;
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
		if(amt > 0) {
			if(player.getInventory().contains(1511)) {
				if(player.getInventory().add(new Item(Fletching.SHAFT, 5)) == null) {
					if(amt > 1)
						player.playAnimation(Animation.FLETCHING);
					player.getSkillSet().addExperience(Skill.FLETCHING, 4);
					player.getInventory().remove(1511);
					player.sendMessage("You fletch the logs into arrow shafts.");
				} else {
					player.sendMessage("You have no more space!");
					stop();
				}
			} else {
				player.sendMessage("You ran out of logs!");
				stop();
			}
		}
		amt--;
		if(amt <= 0)
			stop();
	}

}
