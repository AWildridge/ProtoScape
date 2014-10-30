package org.apollo.game.minigames.castle.sara;

import org.apollo.game.model.Player;
import org.apollo.game.model.Position;

/**
 * The flag for sara.
 *
 *
 * @author Rodrigo Molina
 */
public class SaraFlag {

	/**
	 * The flag id of the sara flag.
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
	public SaraFlag(Position pos) {
	    this(pos, null);
	}
	
	/**
	 * Creates a new sara flag.
	 * 
	 * @param pos
	 * 	the pos
	 * @param player
	 * 	the player controlling.  can be {@code null}
	 */
	public SaraFlag(Position pos, Player player) {
	    this.setPos(pos);
	    this.setController(player);
	}
	
	public Player getController() {
	    return controller;
	}

	public void setController(Player controller) {
	    this.controller = controller;
	}

	public Position getPos() {
	    return pos;
	}

	public void setPos(Position pos) {
	    this.pos = pos;
	}

	public boolean isTaken() {
	    return taken;
	}

	public void setTaken(boolean taken) {
	    this.taken = taken;
	}

	public boolean isDropped() {
	    return dropped;
	}

	public void setDropped(boolean dropped) {
	    this.dropped = dropped;
	}
}
