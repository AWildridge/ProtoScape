package org.apollo.game.event.impl;

/**
 * FirstNPCActionEvent.java
 * @author The Wanderer
 */
public class FirstNPCActionEvent extends NPCActionEvent {
    
    	/**
	 * Creates the first NPC action event.
	 * @param index The index.
	 */
	public FirstNPCActionEvent(int index) {
		super(1, index);
	}
    
}
