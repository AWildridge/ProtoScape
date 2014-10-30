package org.apollo.game.content.quest;

/**
 * The length of each quest; in terms of time.
 *
 *
 * @author Rodrigo Molina
 */
public enum QuestLength {

    	/**
    	 * This quest is very short and should take around 5-15 minutes to complete.
    	 */
    	VERY_SHORT,
    	
    	/**
    	 * This quest is short and should take around 15-25 minutes to complete.
    	 */
    	SHORT,
    	
    	/**
    	 * This quest is intermediate and should take around 25-40 minutes to complete.
    	 */
    	INTERMEDIATE,
    	
    	/**
    	 * This quest is long and should take around 40-70 minutes to complete.
    	 */
    	LONG,
    	
    	/**
    	 * This quest is very long and will take more than an hour and 30 minutes to complete.
    	 */
    	VERY_LONG,
    	
    	/**
    	 * This quest is master.  Meaning that this is the longest type of quest there is.  This <b>SHOULD</b> take
    	 * longer than 3 hours.
    	 */
    	MASTER;
}
