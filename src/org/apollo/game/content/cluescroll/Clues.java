package org.apollo.game.content.cluescroll;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Rodrigo Molina
 */
public enum Clues {

	
	LEVEL_ONE(new int[] {2677}, new int[] {}),
	
	
	LEVEL_TWO(new int[] {}, new int[] {}),
	
	
	LEVEL_THREE(new int[] {}, new int[] {})
	
	
	;
	
	private static Map<int[], Clues> clues = new HashMap<int[], Clues>();
	
	public static Clues check(int id) {
		for(Clues c : Clues.values()) {
			int[] ids =  c.item_ids;
			for(int i = 0; i < ids.length; i++) {
				if(ids[i] == id)
					return c;
			}
		}
		return null;
	}
	
	static {
		for(Clues c : Clues.values()) {
			clues.put(c.item_ids, c);
		}
	}
	
	Clues(int[] item_ids, int[] rewards) {
		this.setItem_ids(item_ids);
		this.setRewards(rewards);
	}
	
	private int[] item_ids;
	
	private int[] rewards;
	
	public int[] getItem_ids() {
		return item_ids;
	}
	public void setItem_ids(int[] item_ids) {
		this.item_ids = item_ids;
	}
	public int[] getRewards() {
		return rewards;
	}
	public void setRewards(int[] rewards) {
		this.rewards = rewards;
	}
}
