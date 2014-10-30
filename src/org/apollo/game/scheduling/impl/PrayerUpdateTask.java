package org.apollo.game.scheduling.impl;

import org.apollo.game.model.BonusConstants;
import org.apollo.game.model.Character;
import org.apollo.game.model.Prayers.Prayer;
import org.apollo.game.model.Skill;
import org.apollo.game.scheduling.ScheduledTask;

/**
 * A tickable which drains a character's prayer.
 * @author Michael Bull
 *
 */
public class PrayerUpdateTask extends ScheduledTask {

    /**
     * The cycle time, in ticks.
     */
    public static final int CYCLE_TIME = 1;
    /**
     * The character for who we are draining the prayer.
     */
    public Character character;

    /**
     * Creates the event to cycle every 2000 milliseconds (2 seconds).
     */
    public PrayerUpdateTask(Character character) {
        super(CYCLE_TIME, false);
        this.character = character;
    }

    @Override
    public void execute() {
        double amountDrain = 0;
        for (int i = 0; i < character.getCombatState().getPrayers().length; i++) {
            if (character.getCombatState().getPrayer(i)) {
                double drain = Prayer.forId(i).getDrain();
                double bonus = 0.035 * character.getBonus(BonusConstants.PRAYER_BONUS);
                drain = drain * (1 + bonus);
                drain = 0.6 / drain;
                amountDrain += drain;
            }
        }
        Skill prayer = character.getSkillSet().getSkill(Skill.PRAYER);
        int drainedLevel = 0;
        if(prayer.getCurrentLevel() - (int) amountDrain < 0) {
            drainedLevel = 0;
        } else {
            drainedLevel = prayer.getCurrentLevel() - (int) amountDrain;
        }
        character.getSkillSet().setSkill(Skill.PRAYER, new Skill(prayer.getExperience(), drainedLevel, prayer.getMaximumLevel()));
        if (character.getSkillSet().getSkill(Skill.PRAYER).getCurrentLevel() <= 0 && !character.getCombatState().isDead()) {
            character.getCombatState().resetPrayers();
            character.sendMessage("You have run out of prayer points; you must recharge at an altar.");
        }
    }
}
