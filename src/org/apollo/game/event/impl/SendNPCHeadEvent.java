package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 *
 *
 *
 * @author Rodrigo Molina
 */
public class SendNPCHeadEvent extends Event {

    	/**
    	 * The id of this npc.
    	 */
    	private int npcId;
    	
    	/**
    	 * The interface id.
    	 */
    	private int interfaceId;
    	
    	/**
    	 * Creates a new npc head event.
    	 * 
    	 * @param npcID
    	 * 	The npc id.
    	 */
    	public SendNPCHeadEvent(int npcID, int interfaceID) {
    	    this.npcId = npcID;
    	    this.interfaceId = interfaceID;
    	}
    	
    	public int getInterfaceId() {
    	    return interfaceId;
    	}
    	
    	/**
    	 * Get's the npc id.
    	 * 
    	 * @return the id.
    	 */
    	public int getNPCId() {
    	    return npcId;
    	}
}
