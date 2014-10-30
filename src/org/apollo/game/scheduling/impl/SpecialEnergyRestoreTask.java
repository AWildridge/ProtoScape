package org.apollo.game.scheduling.impl;

import org.apollo.game.model.Character;
import org.apollo.game.scheduling.ScheduledTask;

/**
 * A tickable which increases a character's special energy.
 * @author Michael Bull
 *
 */
public class SpecialEnergyRestoreTask extends ScheduledTask {

    /**
     * The cycle time, in ticks.
     */
    public static final int CYCLE_TIME = 50;
    
    /**
     * The character for who we are increasing the special energy.
     */
    public Character character;

    /**
     * Creates the event to cycle every 30,000 milliseconds (30 seconds).
     */
    public SpecialEnergyRestoreTask(Character character) {
        super(CYCLE_TIME, false);
        this.character = character;
    }

    @Override
    public void execute() {
        character.getCombatState().increaseSpecial(10);
        if (character.getCombatState().getSpecialEnergy() >= 100) {
            character.specialUpdateTask = null;
            this.stop();
        }
    }
}
