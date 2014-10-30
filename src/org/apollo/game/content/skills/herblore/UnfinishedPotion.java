package org.apollo.game.content.skills.herblore;

import java.util.HashMap;
import java.util.Map;

/**
 * Handles the unfinished potion making
 * 
 * @author Ares_
 */
public enum UnfinishedPotion {
	ATTACK(HerbloreVariables.GUAM, HerbloreVariables.GUAM_UNF, 3),
	ANTIPOISON(HerbloreVariables.MARRENTILL, HerbloreVariables.MARRENTILL_UNF, 5),
	RECLICYMSBALM(HerbloreVariables.ROGUEPURSE, HerbloreVariables.ROGUESPURSE_UNF, 8),
	STRENGTH(HerbloreVariables.TARROMIN, HerbloreVariables.TARROMIN_UNF, 12),
	STATRESTORE(HerbloreVariables.HARRALANDER, HerbloreVariables.HARRALANDER_UNF, 22),
	BLAMISHOIL(HerbloreVariables.HARRALANDER, HerbloreVariables.HARRALANDER_UNF, 25),
	ENERGY(HerbloreVariables.HARRALANDER, HerbloreVariables.HARRALANDER_UNF, 26),
	DEFENCE(HerbloreVariables.RANARR, HerbloreVariables.RANARR_UNF, 30),
	AGILITY(HerbloreVariables.TOADFLAX, HerbloreVariables.TOADFLAX_UNF, 34),
	COMBAT(HerbloreVariables.HARRALANDER, HerbloreVariables.HARRALANDER_UNF, 36),
	PRAY(HerbloreVariables.RANARR, HerbloreVariables.RANARR_UNF, 38),
	SUPERATTACK(HerbloreVariables.IRIT, HerbloreVariables.IRIT_UNF, 45),
	SUPERANTIPOISON(HerbloreVariables.IRIT, HerbloreVariables.IRIT_UNF, 48),
	FISHING(HerbloreVariables.AVANTOE, HerbloreVariables.AVANTOE_UNF, 50),
	SUPERENERGY(HerbloreVariables.AVANTOE, HerbloreVariables.AVANTOE_UNF, 52),
	SUPERSTRENGTH(HerbloreVariables.KWUARM, HerbloreVariables.KWUARM_UNF, 55),
	WEAPONPOISON(HerbloreVariables.KWUARM, HerbloreVariables.KWUARM_UNF,  60),
	SUPERRESTORE(HerbloreVariables.SNAPDRAGON, HerbloreVariables.SNAPDRAONG_UNF, 63),
	SUPERDEFENCE(HerbloreVariables.CADANTINE, HerbloreVariables.CADANTINE_UNF, 66),
	SUPERANTIPOISONPLUSPLUS(HerbloreVariables.TOADFLAX, HerbloreVariables.ANTIPOISONPLUS_UNF, 68),
	ANTIFIREBREATH(HerbloreVariables.LANTADYME, HerbloreVariables.LANTADYME_UNF, 68),
	RANGING(HerbloreVariables.DWARFWEED, HerbloreVariables.DWARF_UNF, 72),
	WEAPONPOISNPLUS(6016, HerbloreVariables.WEAPONPOISONPLUS_UNF, 73),
	MAGIC(HerbloreVariables.LANTADYME, HerbloreVariables.LANTADYME_UNF, 76),
	ZAMORAKBREW(HerbloreVariables.TORSTOL, HerbloreVariables.TORSTOL_UNF, 78),
	ANTIPOISONPLUSPLUS(HerbloreVariables.IRIT, HerbloreVariables.ANTIPOISONPLUSPLUS_UNF, 79),
	SARADOMINBREW(HerbloreVariables.TOADFLAX, HerbloreVariables.TOADFLAX_UNF, 81),
	WEAPONPOISONPLUSPLUS(2398, HerbloreVariables.WEAPONPOISONPLUSPLUS_UNF, 82)
	;
	
	/**
	 * The herb you use
	 */
	private int herb;
	
	/**
	 * The potion you will get
	 */
	private int potion;
	
	/**
	 * The level you need to create the potion
	 */
	private int level;
	
	/**
	 * Maps the unfinished potion
	 */
	private static Map<Integer, UnfinishedPotion> unfList = new HashMap<Integer, UnfinishedPotion>();
	
	UnfinishedPotion(int herb, int potion, int level) {
		this.herb = herb;
		this.potion = potion;
		this.level = level;
	}
	
	public static UnfinishedPotion unFinForId(int id) { return unfList.get(id); }
	
	/**
	 * Creates the potion map
	 */
	static {
		for(UnfinishedPotion unf : UnfinishedPotion.values()) {
			unfList.put(unf.getHerb(), unf);
		}
	}
	
	/**
	 * Gets the herb Id
	 * 
	 * @return
	 * 	the herb Id
	 */
	public int getHerb() {
		return herb;
	}
	
	/**
	 * Gets the potion Id
	 * 
	 * @return
	 * 	the potion Id
	 */
	public int getPotion() {
		return potion;
	}
	
	/**
	 * Gets the level
	 * 
	 * @return
	 * 	the level
	 */
	public int getLevel() {
		return level;
	}
}
