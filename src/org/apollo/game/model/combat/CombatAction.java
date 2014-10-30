package org.apollo.game.model.combat;

import org.apollo.game.model.Character;
import org.apollo.game.model.combat.CombatState.AttackType;

/**
 * An interface that handles the logic of different types of combat, e.g.
 * ranged, magic, melee and various specials.
 * 
 * Credits to rs2-server team for formulas and a guideline
 * as how to structure it
 * @author The Wanderer
 */
public interface CombatAction {

	/**
	 * Checks if the attacker is able to hit the victim in combat.
	 * <p>
	 * Implementations should be such that
	 * <code>canHit(attacker, victim) = canHit(victim, attacker)</code>.
	 * @param attacker The attacker.
	 * @param victim The victim.
	 * @return <code>true</code> if an attack can proceed, <code>false</code>
	 * if not.
	 */
	public boolean canHit(Character attacker, Character victim);
	
	/**
	 * Makes the attacker hit the victim.
	 * @param attacker The attacker.
	 * @param victim The victim.
	 */
	public void hit(Character attacker, Character victim);
	
	/**
	 * Shows defence emotes for the victim (taking defence into account for
	 * hits should be done in the hit method).
	 * @param attacker The attacker.
	 * @param victim The victim.
	 */
	public void defend(Character attacker, Character victim);
	
	/**
	 * Gets the hit after defence calculations.
	 * @param maxHit The max hit.
	 * @param attacker The attacker.
	 * @param victim The victim.
	 * @param attackType The AttackType
	 * @param skill The skill.
	 * @param prayer The protection prayer.
	 * @return
	 */
	public int damage(int maxHit, Character attacker, Character victim, AttackType attackType, int skill, int prayer);
	
	/**
	 * Performs a special attack on the victim.
	 * @param attack The attacker.
	 * @param victim The victim.
	 */
	public boolean special(Character attacker, Character victim);
	
	/**
	 * Gets the distance required for this attack to continue.
	 * @param attacker The attacker.
	 * @return The distance requierd for this attack to continue.
	 */
	public int distance(Character attacker);

	/**
	 * Adds experience.
	 * @param attacker The attacker.
	 * @param damage The damage dealt.
	 */
	public void addExperience(Character attacker, int damage);
	
	/**
	 * Takes care of the ring of recoil effect.
	 * @param attacker The attacker.
	 * @param victim The victim.
	 * @param damage The damage dealt.
	 * @param delay The delay before the hit is visible.
	 */
	public void recoil(Character attacker, Character victim, int damage);
	
	/**
	 * Performs the Smite prayer effect.
	 * @param attacker The attacker.
	 * @param victim The victim.
	 * @param hit The hit.
	 */
	public void smite(Character attacker, Character victim, int damage);
	
}
