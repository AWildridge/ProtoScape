package org.apollo.game.scheduling.impl;

import org.apollo.game.model.Player;
import org.apollo.game.model.Skill;
import org.apollo.game.scheduling.ScheduledTask;

/**
 * A {@link ScheduledTask} which restores the player's energy.
 * 
 * @author Jimmy Frix
 */
public final class EnergyRestorationTask extends ScheduledTask {

    /**
     * The player.
     */
    private final Player player;

    /**
     * Creates the skill normalization task.
     * @param character The character.
     */
    public EnergyRestorationTask(Player player) {
        super(3, false);
        this.player = player;
    }

    @Override
    public void execute() {
	if(!player.isRestoreEnergy() || player.getRunEnergy() >= 100) {
	    stop();
	}
        if (!player.isActive()) {
            stop();
        } else if (!(player.isMoving() && player.isRunning())) {
            //TODO: Prob should reduce this..
            player.setRunEnergy(player.getRunEnergy() + ((player.getSkillSet().getSkill(Skill.AGILITY).getCurrentLevel()+1 / 2) * 0.65));
        }
    }
}