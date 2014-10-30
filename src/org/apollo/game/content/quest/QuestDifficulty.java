package org.apollo.game.content.quest;

/**
 * The difficulty of this quest; in terms of storyline.
 *
 *
 * @author Rodrigo Molina
 */
public enum QuestDifficulty {

    	/**
    	 * This quest is very easy and should not be too hard to figure out.
    	 */
    	VERY_EASY,
    	
    	/**
    	 * This quest is easy and should not be that hard to figure out.
    	 */
    	EASY,
    	
    	/**
    	 * This quest is intermediate and should be a bit hard to figure out.
    	 */
    	INTERMEDIATE,
    	
    	/**
    	 * This quest is hard and should be hard to figure out the steps and what to get.
    	 */
    	HARD,
    	
    	/**
    	 * This quest is very hard, meaning that this quest will be difficult to figure out.
    	 */
    	VERY_HARD,
    	
    	/**
    	 * This quest is master; this quest will be very difficult to figure out the steps and to figure out the 
    	 * storyline.  This quest is the hardest type of quest to figure out.
    	 */
    	MASTER;
}
