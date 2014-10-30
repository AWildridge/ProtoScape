package org.apollo.game.content.skills.fletching;

import java.util.HashMap;
import java.util.Map;

import org.apollo.game.GameConstants;

/**
 * Handles the data for making arrows
 * 
 * @author Ares_
 */
public enum ArrowData {
	BRONZE(39, 882, 1, 1.3),
	
	IRON(40, 884, 15, 2.5),
	
	STEEL(41, 886, 30, 5.0),
	
	MITHRIL(42, 888, 45, 7.5),
	
	ADAMANT(43, 890, 60, 10.0),
	
	RUNE(44, 892, 75, 12.5)
	;
	
	private static Map<Integer, ArrowData> arrowtips = new HashMap<Integer, ArrowData>();
	
	public static ArrowData forId(int item) { return arrowtips.get(item); }
	
	static {
		for (ArrowData arrowtip : ArrowData.values()) {
			arrowtips.put(arrowtip.tipId, arrowtip);
		}
	}
	
	ArrowData(int tipId, int reward, int levelRequired, double experience) {
		this.tipId = tipId;
		this.reward = reward;
		this.levelRequired = levelRequired;
		this.experience = experience;
	}
	
	private int tipId;
	
	private int reward;
	
	private int levelRequired;

	private double experience;

	public int getTipId() {
		return tipId;
	}

	public int getReward() {
		return reward;
	}

	public int getLevelRequired() {
		return levelRequired;
	}

	public double getExperience() {
		return experience * GameConstants.EXP_MODIFIER;
	}
}
