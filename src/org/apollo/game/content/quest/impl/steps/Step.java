package org.apollo.game.content.quest.impl.steps;

import org.apollo.game.content.quest.Quest;

/**
 * Represents one single step in a quest.
 *
 * Each step can consist of three things.. a dialogue (kind of a chatter or a long conversation..), or maybe
 * some other type of fight?
 *
 * @author Rodrigo Molina
 */
public abstract class Step {

    	/**
    	 * The quest of this step.
    	 */
    	private Quest quest;
    	
    	/**
    	 * Executes the step that this player is in.
    	 */
    	public abstract void executeStep();
    	
    	/**
    	 * Writes the text to this interface.
    	 * 
    	 * @return A {@link String} array of texts to write to the interface.
    	 */
    	public abstract String[] getText();
    	
    	/**
    	 * The current step in each step that this player is in.
    	 * 
    	 * This is to stop us from making around a MILLION steps.
    	 */
    	private int step = 0;
    	
    	/**
    	 * Creates a new step..
    	 * 
    	 * @param quest
    	 */
    	public Step(Quest quest) {
    	    this.quest = quest;
    	}
    	
    	/**
    	 * Checks if the player can move on to the next step or not by checking the items they have or the levels.
    	 * 
    	 * @return true if they have finished everything and can move on to the next step.
    	 */
    	public abstract boolean moveOn();

    	/**
    	 * Get's the step value.
    	 * 
    	 * @return the step.
    	 */
	public int getStep() {
	    return step;
	}

	/**
	 * Increases the inner step of this step.
	 */
	public void increaseStep() {
	    step++;
	}

	/**
	 * Reset's the current step.
	 */
	public void resetStep() {
	    step = -2;
	}
	
	public Quest getQuest() {
	    return quest;
	}
}
