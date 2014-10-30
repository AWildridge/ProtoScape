package org.apollo.game.content.skills.herblore;

import java.util.HashMap;
import java.util.Map;

/**
 * The finished product of HerbloreVariables
 * 
 * @author Ares_
 */
public enum FinishedPotion {
	ATTACK(121, 221, HerbloreVariables.GUAM_UNF, 3, 25),
	ANTIPOISON(175, 235, HerbloreVariables.MARRENTILL_UNF, 5, 37.5),
	RELICYMSBALM(4844, HerbloreVariables.SNAKEWEED, HerbloreVariables.ROGUESPURSE_UNF, 8, 40),
	STRENGTH(116, 225, HerbloreVariables.TARROMIN_UNF, 12, 50),
	STATRESTORE(127, 223, HerbloreVariables.HARRALANDER_UNF, 22, 62.6),
	BLAMISHOIL(1582, 1581, HerbloreVariables.HARRALANDER_UNF, 25, 80),
	ENERGY(3010, 1975, HerbloreVariables.HARRALANDER_UNF, 26, 67.6),
	DEFENCE(133, 239, HerbloreVariables.RANARR_UNF, 30, 75),
	AGILITY(3034, 2152, HerbloreVariables.TOADFLAX_UNF, 34, 80),
	COMBAT(9741, 9736, HerbloreVariables.HARRALANDER_UNF, 36, 84),
	PRAY(139, 231, HerbloreVariables.RANARR_UNF, 38, 87.5),
	SUPERATTACK(145, 221, HerbloreVariables.IRIT_UNF, 45, 100),
	SUPERANTIPOISON(181,  235, HerbloreVariables.IRIT_UNF, 48, 106.3),
	FISHING(151, 231, HerbloreVariables.AVANTOE_UNF, 50, 112.5),
	SUPERENERGY(3018, 2970, HerbloreVariables.AVANTOE_UNF, 52, 117.5),
	SUPERSTRENGTH(157, 225, HerbloreVariables.KWUARM_UNF, 55, 125),
	WEAPONPOISON(187, 241, HerbloreVariables.KWUARM_UNF,  60, 137.5),
	SUPERRESTORE(3025, 223, HerbloreVariables.SNAPDRAONG_UNF, 63, 142.5),
	SUPERDEFENCE(163, 239, HerbloreVariables.CADANTINE_UNF, 66, 150),
	ANTIPOISONPLUS(5945, 6049, HerbloreVariables.ANTIPOISONPLUS_UNF, 68, 155),
	ANTIFIREBREATH(2454, 241, HerbloreVariables.LANTADYME_UNF, 69, 157.5),
	RANGING(169, 245, HerbloreVariables.DWARF_UNF, 72, 162.5),
	WEAPONPOISNPLUS(5937, 223, HerbloreVariables.WEAPONPOISONPLUS_UNF, 73, 165),
	MAGIC(3042, 3138, HerbloreVariables.LANTADYME_UNF, 76, 172.5),
	ZAMORAKBREW(189, 247, HerbloreVariables.TORSTOL_UNF, 78, 175),
	ANTIPOISONPLUSPLUS(5954, 6051, HerbloreVariables.ANTIPOISONPLUSPLUS_UNF, 79, 177.5),
	SARADOMINBREW(6687, 6693, HerbloreVariables.TOADFLAX_UNF, 81, 180),
	WEAPONPOISONPLUSPLUS(5954, 6018, HerbloreVariables.WEAPONPOISONPLUSPLUS_UNF, 82, 190);
	
	private int ingredient;
	
	private int potion; 
	
	private int finishedPotion;
	
	private int level;
	
	private double exp;
	
	private static Map<Integer, FinishedPotion> finList = new HashMap<Integer, FinishedPotion>();
	
	FinishedPotion(int finishedPotion, int ingredient, int potion, int level, double exp) {
		this.finishedPotion = finishedPotion;
		this.ingredient = ingredient;
		this.potion = potion;
		this.level = level;
		this.exp = exp;
	}
	
	static {
		for(FinishedPotion fin : FinishedPotion.values()) {
			finList.put(fin.getPot(), fin);
		}	
	}
	
	public static FinishedPotion finForId(int id) {
		return finList.get(id);
	}
	
	public int getIngredient() {
		return ingredient;
	}

	public int getPot() {
		return potion;
	}

	public int getFinPot() {
		return finishedPotion;
	}
	
	public int getLevel() {
		return level;
	}
	
	public double getExp() {
		return exp;
	}
}
