package org.apollo.game.content.cluescroll;

import org.apollo.game.model.Player;

/**
 * @author Rodrigo Molina
 */
public class ClueScrolls {

	public static final int REWARD_INTERFACE = 6960;
	
	public static final int CLUE_TEXT_INTERFACE = 6965;
	
	private Player player;
	
	private LevelOneHandler level_one;
	
	public ClueScrolls(Player player) {
		this.player = player;
		this.level_one = new LevelOneHandler(player);
	}
	
	public void handleClue(Clues level, int itemId) {
		if(level == Clues.LEVEL_ONE) {
			level_one.showInterface(itemId);
			return;
		}
	}
	
}
