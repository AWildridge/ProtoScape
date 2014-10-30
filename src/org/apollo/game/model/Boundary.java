package org.apollo.game.model;

/**
 * The bounds for setting certain areas such as pk zones
 * 
 * @author Sir Sean
 */
public final class Boundary {

	/**
	 * The boundary name
	 */
	private final String name;
	
	/**
	 * The bottom left location
	 */
	private final Position bottomLeft;
	
	/**
	 * The top right location
	 */
	private final Position topRight;
	
	/**
	 * Sets the boundaries in the constructor
	 * @param buttonLeft The bottom left coordinates
	 * @param topRight The top right coordinates
	 */
	public Boundary(String name, Position bottonLeft, Position topRight) {
		this.name = name;
		this.bottomLeft = bottonLeft;
		this.topRight = topRight;
	}

	/**
	 * The boundary name
	 * @return The boundary name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the button left location
	 * @return the bottom left
	 */
	public Position getBottomLeft() {
		return bottomLeft;
	}

	/**
	 * Gets the top left location
	 * @return the top right
	 */
	public Position getTopRight() {
		return topRight;
	}

	public boolean contains(Position target) {
		return target.getX() >= bottomLeft.getX() && target.getY() >= bottomLeft.getY() &&
			   target.getX() <= topRight.getX()   && target.getY() <= topRight.getY();
	}

}
