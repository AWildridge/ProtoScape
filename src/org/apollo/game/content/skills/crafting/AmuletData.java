package org.apollo.game.content.skills.crafting;

import java.util.HashMap;
import java.util.Map;

/**
 * Handles all the amulet stringing data
 * 
 * @author Ares_
 */
public enum AmuletData {

	/**
	 * Regular amulets
	 */
	GOLD(1673, 1692, 8, 30),
	SAPPHIRE(1675, 1694, 24, 65),
	EMERALD(1677, 1696, 31, 70),
	RUBY(1679, 1698, 50, 85),
	DIAMOND(1681, 1700, 70, 100),
	DRAGONSTONE(1683, 1702, 80, 150),
	ONYX(6579, 6581, 90, 165),
	
	/**
	 * Silver items
	 */
	;
	
	private static Map<Integer, AmuletData> amulet = new HashMap<Integer, AmuletData>();
	
	public static AmuletData forId(int item) { return amulet.get(item); }
	
	static {
		for (AmuletData ammy : AmuletData.values()) {
			amulet.put(ammy.unStrung, ammy);
		}
	}
	
	AmuletData(int unStrung, int reward, int levelRequired, double experience) {
		this.unStrung = unStrung;
		this.reward = reward;
		this.levelRequired = levelRequired;
		this.experience = experience;
	}
	
	private int unStrung;
	
	private int reward;
	
	private int levelRequired;
	
	private double experience;
	
	public int getUnStrung() {
		return unStrung;
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
