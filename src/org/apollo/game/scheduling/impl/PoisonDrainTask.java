package org.apollo.game.scheduling.impl;

import org.apollo.game.event.impl.DamageEvent;
import org.apollo.game.model.Character;
import org.apollo.game.model.Skill;
import org.apollo.game.scheduling.ScheduledTask;

/**
 * An event which depletes and deals poison damage.
 * @author Michael Bull
 *
 */
public class PoisonDrainTask extends ScheduledTask {

	/**
	 * The cycle time, in ticks.
	 */
	public static final int CYCLE_TIME = 30;
	
	/**
	 * The character for who we are poisoning.
	 */
	public Character character;
	
	/**
	 * The character who poisoned the victim.
	 */
	public Character attacker;
	
	/**
	 * Creates the event to cycle every 18,000 milliseconds (18 seconds).
	 */
	public PoisonDrainTask(Character character, Character attacker) {
		super(CYCLE_TIME, false);
		this.character = character;
		this.attacker = attacker;
	}
	
	/**
	 * One damage is dealt 4 times before decreasing.
	 */
	private int drainAmount = 4;

	@Override
	public void execute() {
		int dmg = character.getCombatState().getPoisonDamage();
		if(dmg > character.getSkillSet().getSkill(Skill.HITPOINTS).getCurrentLevel()) {
			dmg = character.getSkillSet().getSkill(Skill.HITPOINTS).getCurrentLevel();
		}
                DamageEvent damage = new DamageEvent(dmg, DamageEvent.HitType.POISON_HIT, character.getHP(), character.getMaxHP());
		character.damageCharacter(damage);
                character.inflictDamage(damage, attacker);
                drainAmount--;
		if(drainAmount == 0) {
			character.getCombatState().decreasePoisonDamage(1);
			drainAmount = 4;
		}
	}

}
