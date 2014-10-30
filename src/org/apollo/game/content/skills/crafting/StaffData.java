package org.apollo.game.content.skills.crafting;

import java.util.HashMap;
import java.util.Map;

/**
 * Handles the battle staff making data
 * 
 * @author Ares_
 */
public enum StaffData {
	WATER(571, 1395, 54, 100),
	EARTH(575, 1399, 58, 112.5),
	FIRE(569, 1393, 62, 125),
	AIR(573, 1397, 66, 137.5);
	
	private static Map<Integer, StaffData> staff = new HashMap<Integer, StaffData>();
	
	public static StaffData forId(int item) { return staff.get(item); }
	
	static {
		for (StaffData s : StaffData.values()) {
			staff.put(s.orb, s);
		}
	}
	
	StaffData(int orb, int reward, int levelRequired, double experience) {
		this.orb = orb;
		this.reward = reward;
		this.levelRequired = levelRequired;
		this.experience = experience;
	}
	
	private int orb;
	
	private int reward;
	
	private int levelRequired;
	
	private double experience;
	
	public int getOrb() {
		return orb;
	}
	
	public int getReward() {
		return reward;
	}
	
	public int getLevelRequired() {
		return levelRequired;
	}
	
	public double getExperience() {
		return experience;
	}
}
