package org.apollo.game.content.skills.herblore;

import java.util.HashMap;
import java.util.Map;

/**
 * An enum that handles all the Poisoned weapon data
 * 
 * @author Ares_
 * @author Sillhouette
 * 	<All the ID's>
 */
public enum PoisonData {
	/*
	 * (un poisoned, poisoned, poisoned+, poisoned++)
	 */
	BRONZE_ARROW(882, 883, 5616, 5622),
	;
	
	private static Map<Integer, PoisonData> poison= new HashMap<Integer, PoisonData>();
	
	public static PoisonData pForId(int id) { return poison.get(id); }
	
	static {
		for(PoisonData p : PoisonData.values()) {
			poison.put(p.getUnPoisoned(), p);
		}	
	}
	
	/**
	 * The un poisoned weapon
	 */
	private int unPoisoned;
	
	/**
	 * The poisoned weapon reward
	 */
	private int poisonedReward;
	
	/**
	 * Medium weapon poisoned
	 */
	private int poisonedRewardP;
	
	/**
	 * Strongest weapon poisoned
	 */
	private int poisonedRewardPP;
	
	PoisonData(int unPoisoned, int poisonedReward, int poisonedRewardP, int poisonedRewardPP) {
		this.unPoisoned = unPoisoned;
		this.poisonedReward = poisonedReward;
		this.poisonedRewardP = poisonedRewardP;
		this.poisonedRewardPP = poisonedRewardPP;
	}
	
	public int getUnPoisoned() {
		return unPoisoned;
	}
	
	public int getPoisonedReward() {
		return poisonedReward;
	}
	
	public int getPoisonedRewardP() {
		return poisonedRewardP;
	}
	
	public int getPoisonedRewardPP() {
		return poisonedRewardPP;
	}
}
