package org.apollo.game.model.combat;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apollo.game.model.Character;

/**
 * Maintains a count of the total damage and last hit time of attackers for the
 * current character.
 * 
 * Credits to rs2-server team for formulas and a guideline
 * as how to structure it
 * @author The Wanderer
 */
public final class DamageMap {
	
	/**
	 * The time after which the total hit expires (10 minutes).
	 */
	private static final long HIT_EXPIRE_TIME = 600000;
	
	/**
	 * The total damage map.
	 */
	private Map<Character, TotalDamage> totalDamages = new HashMap<Character, TotalDamage>();
	
	/**
	 * Resets the map (e.g. if the entity dies).
	 */
	public void reset() {
		totalDamages.clear();
	}
	
	/**
	 * Gets the highest damage dealer to this entity.
	 * @return The highest damage dealer to this entity.
	 */
	public Character highestDamage() {
		int highestDealt = 0;
		Character killer = null;
		for(Character character : totalDamages.keySet()) {
			if(totalDamages.get(character).getDamage() > highestDealt) {
				highestDealt = totalDamages.get(character).getDamage();
				killer = character;
			}
		}
		return killer;
	}
	
	/**
	 * Removes entities that have been destroyed from the map, or entries where
	 * the player has not hit for 10 minutes.
	 */
	public void removeInvalidEntries() {
		// copy the map OR face concurrent modification exceptions ;)
		Set<Character> characters = new HashSet<Character>();
		characters.addAll(totalDamages.keySet());
		for(Character e : characters) {
			if(e.isDestroyed()) {
				totalDamages.remove(e);
			} else {
				TotalDamage d = totalDamages.get(e);
				long delta = System.currentTimeMillis() - d.getLastHitTime();
				if(delta >= HIT_EXPIRE_TIME) {
					totalDamages.remove(e);
				}
			}
		}
	}
	
	/**
	 * Increments the total damage dealt by an attacker.
	 * @param attacker
	 * @param amount
	 */
	public void incrementTotalDamage(Character attacker, int amount) {
		TotalDamage totalDamage = totalDamages.get(attacker);
		if(totalDamage == null) {
			totalDamage = new TotalDamage();
			totalDamages.put(attacker, totalDamage);
		}
		totalDamage.increment(amount);
	}
	
}
