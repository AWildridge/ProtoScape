package org.apollo.game.content.skills.crafting;

import java.util.HashMap;
import java.util.Map;

/**
 * A enum that contains all the Gem Data
 * 
 * @author Ares_
 */
public enum GemData {

	SAPPHIRE(1623, 1607, 20, 888, 50.0),
	EMERALD(1621, 1605, 27, 889, 67.0),
	RUBY(1619, 1603, 34, 887, 85.0),
	DIAMOND(1617, 1601, 43, 886, 107.5),
	DRAGONSTONE(1631, 1615, 55, 885, 137.5),
	ONYX(6571, 6573, 67, 2717, 168)
	;
	
	private static Map<Integer, GemData> gems = new HashMap<Integer, GemData>();
	
	public static GemData forId(int item) { return gems.get(item); }

	static {
		for (GemData gem : GemData.values()) {
			gems.put(gem.uncut, gem);
		}
	}
	
	GemData(int uncut, int reward, int levelRequired, int animation, double experience) {
		this.uncut = uncut;
		this.reward = reward;
		this.levelRequired = levelRequired;
		this.animation = animation;
		this.experience = experience;
	}
	
	/**
	 * The uncut id
	 */
	private int uncut;
	
	/**
	 * The reward <cut gem>
	 */
	private int reward;
	
	/**
	 * The level you need
	 */
	private int levelRequired;
	
	/**
	 * The experience granted
	 */
	private double experience;
	
	/**
	 * The animation you do
	 */
	private int animation;
	
	/**
	 * Gets the uncut id
	 * 
	 * @return
	 *  the uncut id
	 */
	public int getUnCut() {
		return uncut;
	}
	
	/**
	 * The rewarded cut gem
	 * 
	 * @return
	 *  the gem thats cut
	 */
	public int getReward() {
		return reward;
	}
	
	/**
	 * The animation id
	 * 
	 * @return
	 *  the animation
	 */
	public int getAnimation() {
		return animation;
	}
	
	/**
	 * The level required
	 * 
	 * @return
	 *  the level you need
	 */
	public int getLevelRequired() {
		return levelRequired;
	}
	
	/**
	 * The experience gained
	 * 
	 * @return
	 *  the experience
	 */
	public double experience() {
		return experience;
	}
}
