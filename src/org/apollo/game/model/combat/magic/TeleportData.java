package org.apollo.game.model.combat.magic;

import java.util.HashMap;
import java.util.Map;

import org.apollo.game.model.Character.TeleportType;

/**
 * An enum to handle all the teleporting data
 * 
 * @author Ares_
 * 
 * Date Created: 1/16/2012
 * 
 * Last Edit: 1/16/2012
 */
public enum TeleportData {
	VARROCK(3300, 3300, 1164, 100.5, TeleportType.NORMAL_TELEPORT),
        LUMBRIDGE(3222, 3218, 1167, 100.5, TeleportType.NORMAL_TELEPORT),
	;
	
	TeleportData(int destX, int destY, int actionButtonId, double experience, TeleportType type) {
		this.destX = destX;
		this.destY = destY;
		this.actionButtonId = actionButtonId;
		this.experience = experience;
		this.type = type;			
	}
		
	private static Map<Integer, TeleportData> teleports = new HashMap<Integer, TeleportData>();
		
	static {
		for(TeleportData teleport : TeleportData.values()) {
			teleports.put(teleport.getActionButtonId(), teleport);
		}
	}	
	
	public static TeleportData forId(int id) {
		return teleports.get(id);
	}	

	/**
	 * The destination.
     */
	private int destX, destY;
	
	/**
	 * The action button id
	 */
	private int actionButtonId;

	/**
	 * The type.
	 */
	private TeleportType type;
	
	/**
	 * The experience
	 */
	private double experience;

	public int getDestX() {
		return destX;
	}
	
	public int getDestY() {
		return destY;
	}

	public int getActionButtonId() {
		return actionButtonId;
	}

	public TeleportType getType() {
		return type;
	}
	
	public double getExperience() {
		return experience;
	}
}
