package org.apollo.game.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apollo.game.model.obj.StaticObject;
import org.apollo.game.model.region.RegionRepository;

/**
 * Represents a tile.
 * @author Graham Edgecombe
 *
 */
public class Tile {
	
	/**
	 * A bitmask which determines which directions can be traversed.
	 */
	private final int traversalMask;

	/**
	 * A list of items in this region.
	 */
	private List<GroundItem> items = new ArrayList<GroundItem>();
	
	/**
	 * The game object on this tile.
	 */
	private StaticObject[] gameObjects = new StaticObject[23]; 
	
	/**
	 * Creates the tile with a traversal mask.
	 * @param traversalMask The traversal mask to set.
	 */
	public Tile(int traversalMask) {
		this.traversalMask = traversalMask;
	}
	
	/**
	 * Gets the traversal bitmask.
	 * @return The traversal bitmask.
	 */
	public int getTraversalMask() {
		return traversalMask;
	}
	
	/**
	 * Checks if northern traversal is permitted.
	 * @return True if so, false if not.
	 */
	public boolean isNorthernTraversalPermitted() {
		return (traversalMask & RegionRepository.NORTH_TRAVERSAL_PERMITTED) > 0;
	}
	
	/**
	 * Checks if eastern traversal is permitted.
	 * @return True if so, false if not.
	 */
	public boolean isEasternTraversalPermitted() {
		return (traversalMask & RegionRepository.EAST_TRAVERSAL_PERMITTED) > 0;
	}
	
	/**
	 * Checks if southern traversal is permitted.
	 * @return True if so, false if not.
	 */
	public boolean isSouthernTraversalPermitted() {
		return (traversalMask & RegionRepository.SOUTH_TRAVERSAL_PERMITTED) > 0;
	}
	
	/**
	 * Checks if western traversal is permitted.
	 * @return True if so, false if not.
	 */
	public boolean isWesternTraversalPermitted() {
		return (traversalMask & RegionRepository.WEST_TRAVERSAL_PERMITTED) > 0;
	}
	
	/**
	 * Gets the list of items.
	 * @return The list of items.
	 */
	public Collection<GroundItem> getGroundItems() {
		return items;
	}

	/**
	 * @return the gameObject
	 */
	public StaticObject getStaticObject(int type) {
		return gameObjects[type];
	}

	/**
	 * @param gameObject the gameObject to set
	 */
	public void setStaticObject(int type, StaticObject gameObject) {
		gameObjects[type] = gameObject;
	}
}
