package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * Sends the player head to chat dialogues
 *
 *
 * @author Rodrigo Molina
 */
public class SendPlayerHeadEvent extends Event {

    	/**
    	 * The frame of the player's head to show.
    	 */
    	private int frame;
    	
    	/**
    	 * Creates a new player head event.
    	 * 
    	 * @param frame
    	 * 	the frame.
    	 */
    	public SendPlayerHeadEvent(int frame) {
    	   this.frame = frame; 
    	}
    	
    	/**
    	 * Get's the frame.
    	 * 
    	 * @return the frame.
    	 */
    	public int getFrame() {
    	    return frame;
    	}
}
