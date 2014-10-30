package org.apollo.game.content.quest.impl;

import org.apollo.game.content.quest.Quest;
import org.apollo.game.content.quest.QuestDifficulty;
import org.apollo.game.content.quest.QuestGeneral;
import org.apollo.game.content.quest.QuestLength;
import org.apollo.game.content.quest.impl.black.FirstStep;
import org.apollo.game.content.quest.impl.steps.Step;
import org.apollo.game.model.Player;

/**
 * The black nights fortress.
 *
 *
 * @author Rodrigo Molina
 */
public class BlackKnightsFortress extends QuestGeneral implements Quest {
    
    	/**
    	 * The points given upon finishing quest.
    	 */
    	private final int POINTS_GIVEN = 2;
    	
    	/**
    	 * The max steps of this quest.
    	 */
    	private final int MAX_STEPS = 5;
    	
    	/**
    	 * The player doing this quest.
    	 */
    	private Player player;
    	
    	/**
    	 * Creates a new quest.
    	 * 
    	 * @param player
    	 * 	The player.
    	 */
    	public BlackKnightsFortress(Player player) {
    	    this.player = player;
    	}
    	
        @Override
        public void giveReward() {
            
        }

        @Override
        public Quest fillSteps() {
            steps = new Step[MAX_STEPS];
            steps[0] = new FirstStep(this, player);
            return this;
        }
    
        @Override
        public int pointsGiven() {
            return POINTS_GIVEN;
        }
    
        @Override
        public QuestDifficulty getDifficulty() {
            return QuestDifficulty.EASY;
        }
    
        @Override
        public QuestLength getLength() {
            return QuestLength.SHORT;
        }

	@Override
	public String getQuestName() {
		return "Black Knights Fortress";
	}

}
