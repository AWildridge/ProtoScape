package org.apollo.game.content.skills.herblore;

import java.util.HashMap;
import java.util.Map;

/**
 * Enum that handles all the item grinding data
 * 
 * @author Ares_
 */
public enum ItemGrinding {

	/**
	 * Chocolate dust
	 */
	CHOCOLATE_DUST(1973, 1975, 2),
	
	/**
	 * Unicorn dust
	 */
	UNICORN_HORN_DUST(237, 235, 2),
	
	/**
	 * Scale dust
	 */
	DRAGON_SCALE_DUST(243, 241, 2),
	
	/**
	 * Nest dust
	 */
	BIRD_NEST_DUST(5075, 6693, 2);
	
	private int unground;
	
	private int grinded;
	
	private double experience;
	
	private static Map<Integer, ItemGrinding> grind = new HashMap<Integer, ItemGrinding>();
	
	ItemGrinding(int unground, int grinded, double experience) {
		this.unground = unground;
		this.grinded = grinded;
		this.experience = experience;
	}
	
	public static ItemGrinding grindForId(int id) { return grind.get(id); }
	
	static {
		for(ItemGrinding item : ItemGrinding.values()) {
			grind.put(item.getUngrounded(), item);
		}	
	}
	
	public int getUngrounded() {
		return unground;
	}
	
	public int getGrinded() {
		return grinded;
	}
	
	public double getExperience() {
		return experience;
	}
}
