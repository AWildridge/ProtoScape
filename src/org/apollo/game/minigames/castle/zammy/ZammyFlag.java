package org.apollo.game.minigames.castle.zammy;

import org.apollo.game.model.Player;
import org.apollo.game.model.Position;

/**
 * The flag of the zammy team.
 *
 *
 * @author Rodrigo Molina
 */
public class ZammyFlag {

    	/**
    	 * The flag id of the zammy flag.
    	 */
    	public static final int FLAG_ID = 1;
    	
    	/**
    	 * The player who is holding this flag.
    	 */
    	private Player controller;
    	
    	/**
    	 * The position of this flag.
    	 */
    	private Position pos;
    	
    	/**
    	 * The flag for if the flag is taken or not.
    	 */
    	private boolean taken;
    	
    	/**
    	 * The flag if the flag is dropped.
    	 */
    	private boolean dropped;
    	
    	/**
    	 * Creates a new flag.
    	 * 
    	 * @param pos
    	 * 	the position of this flag.
    	 */
    	public ZammyFlag(Position pos) {
    	    this(pos, null);
    	}
    	
    	/**
    	 * Creates a new zammy flag.
    	 * 
    	 * @param pos
    	 * 	the pos
    	 * @param player
    	 * 	the player controlling.  can be {@code null}
    	 */
    	public ZammyFlag(Position pos, Player player) {
    	    this.setPos(pos);
    	    this.setController(player);
    	}
    	
    	public Player getController() {
    	    return controller;
    	}
    	
    	public Position getPosition() {
    	    return getPos();
    	}

	public boolean isTaken() {
	    return taken;
	}

	public void setTaken(boolean taken) {
	    this.taken = taken;
	}

	public Position getPos() {
	    return pos;
	}

	public void setPos(Position pos) {
	    this.pos = pos;
	}

	public void setController(Player controller) {
	    this.controller = controller;
	}

	public boolean isDropped() {
	    return dropped;
	}

	public void setDropped(boolean dropped) {
	    this.dropped = dropped;
	}
}
