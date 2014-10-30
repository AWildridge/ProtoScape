package org.apollo.game.event.impl;

import org.apollo.game.event.Event;
import org.apollo.game.model.Animation;

/**
 *
 *
 *
 * @author Rodrigo Molina
 */
public class ObjectAnimationEvent extends Event {

    	private int tileObjectType;
    	
    	private int orientation;
    	
    	private Animation anim;
    	
    	public ObjectAnimationEvent(int tileObjectType, int orientation, Animation anim) {
    	    this.tileObjectType = tileObjectType;
    	    this.orientation = orientation;
    	    this.anim = anim;
    	}
    	
    	public int getTileObjectType() {
    	    return tileObjectType;
    	}
    	
    	public int getOrientation() {
    	    return orientation;
    	}
    	
    	public Animation getAnim() {
    	    return anim;
    	}
}
