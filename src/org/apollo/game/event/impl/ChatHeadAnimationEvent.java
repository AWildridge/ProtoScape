package org.apollo.game.event.impl;

import org.apollo.game.event.Event;
import org.apollo.game.model.Animation;

/**
 * The animation of a head in a dialogue interface.
 *
 *
 * @author Rodrigo Molina
 */
public class ChatHeadAnimationEvent extends Event {

    	/**
    	 * The interface id to display the head.
    	 */
    	private int interfaceId;
    	
    	/**
    	 * The type of animation to display.
    	 */
    	private Animation anim;
    	
    	/**
    	 * Creates a new npc head animation event.
    	 * 
    	 * @param interfaceId
    	 * 	The interface id to display at
    	 * @param anim
    	 * 	the animation to animate.
    	 */
    	public ChatHeadAnimationEvent(int interfaceId, Animation anim) {
    	    this.interfaceId = interfaceId;
    	    this.anim = anim;
    	}
    	
    	public int getInterfaceId() {
    	    return interfaceId;
    	}
    	
    	public Animation getAnimation() {
    	    return anim;
    	}
}
