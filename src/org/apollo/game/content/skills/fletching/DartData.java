package org.apollo.game.content.skills.fletching;

import java.util.HashMap;
import java.util.Map;

/**
 * Handles all the dart data
 * 
 * @author Ares_
 */
public enum DartData {
	BRONZE(819, 806, 1, 2.6),
	
	IRON(820, 807, 15, 3.8),
	
	STEEL(821, 808, 30, 6.3),
	
	MITHRIL(822, 809, 45, 8.8),
	
	ADAMANT(823, 810, 60, 11.3),
	
	RUNE(824, 811, 75, 13.8)
	;
	
	/**
	 * DART TIPS:
	 * 
	 * Bronze Tip: 819 Final: 806
	 * Iron Tip: 820 Final: 807
	 * Steel tip: 821 Final: 808
	 * Mith Tip: 822 Final: 809
	 * Addy Tip: 823 Final: 810
	 * Rune Tip: 824 Final: 811
	 */
	
	private static Map<Integer, DartData> darttips = new HashMap<Integer, DartData>();
	
	public static DartData forId(int item) { return darttips.get(item); }
	
	static {
		for (DartData dartTips : DartData.values()) {
			darttips.put(dartTips.dartTip, dartTips);
		}
	}
	
	DartData(int dartTip, int reward, int levelRequired, double experience) {
		this.dartTip = dartTip;
		this.reward = reward;
		this.levelRequired = levelRequired;
		this.experience = experience;
	}
	
	private int dartTip;
	
	private int reward;
	
	private int levelRequired;

	private double experience;

	public int getDartTip() {
		return dartTip;
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
