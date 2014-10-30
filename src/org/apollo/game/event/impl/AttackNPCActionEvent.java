package org.apollo.game.event.impl;

/**
 * AttackNPCActionEvent.java
 * @author The Wanderer
 */
public class AttackNPCActionEvent extends NPCActionEvent {
    
    	/**
	 * Creates the attack NPC action event.
	 * @param index The index.
	 */
	public AttackNPCActionEvent(int index) {
		super(4, index);
	}
}
