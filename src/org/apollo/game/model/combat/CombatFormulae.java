package org.apollo.game.model.combat;

import org.apollo.game.model.BonusConstants;
import org.apollo.game.model.Character;
import org.apollo.game.model.EquipmentConstants;
import org.apollo.game.model.Item;
import org.apollo.game.model.NPC;
import org.apollo.game.model.Player;
import org.apollo.game.model.Prayers;
import org.apollo.game.model.Skill;
import org.apollo.game.model.combat.CombatState.CombatStyle;
import org.apollo.game.model.combat.action.MagicCombatAction;
import org.apollo.game.model.combat.action.MeleeCombatAction;
import org.apollo.game.model.combat.action.RangeCombatAction;
import org.apollo.game.model.combat.action.RangeCombatAction.BowType;

/**
 * A utility class which contains combat-related formulae.
 * @author Scu11
 * @author Graham Edgecombe
 *
 */
public final class CombatFormulae {
	
	/**
	 * Default private constructor.
	 */
	private CombatFormulae() {
		
	}

	/**
	 * Calculates a character's melee max hit.
	 */
	public static int calculateMeleeMaxHit(Character character, boolean special) {
                if(character instanceof NPC) {
                    
                }
		int maxHit = 0;
		double specialMultiplier = 1;
		double prayerMultiplier = 1;
		double otherBonusMultiplier = 1; //TODO: void melee = 1.2, slayer helm = 1.15, salve amulet = 1.15, salve amulet(e) = 1.2
		int strengthLevel = character.getSkillSet().getSkill(Skill.STRENGTH).getCurrentLevel();
		int combatStyleBonus = 0;
		
		if(character.getCombatState().getPrayer(Prayers.BURST_OF_STRENGTH)) {
			prayerMultiplier = 1.05;
		} else if(character.getCombatState().getPrayer(Prayers.SUPERHUMAN_STRENGTH)) {
			prayerMultiplier = 1.1;
		} else if(character.getCombatState().getPrayer(Prayers.ULTIMATE_STRENGTH)) {
			prayerMultiplier = 1.15;
		}
		
		switch(character.getCombatState().getCombatStyle()) {
		case AGGRESSIVE_1:
		case AGGRESSIVE_2:
			combatStyleBonus = 3;
			break;
		case CONTROLLED_1:
		case CONTROLLED_2:
		case CONTROLLED_3:
			combatStyleBonus = 1;
			break;
		}
		
		if(fullVoidMelee(character)) {
			otherBonusMultiplier = 1.15;
		}
		
		int effectiveStrengthDamage = (int) Math.floor((strengthLevel * prayerMultiplier * otherBonusMultiplier) + combatStyleBonus);
		double baseDamage = 1.3 + (effectiveStrengthDamage / 10) + (character.getBonus(BonusConstants.STRENGTH_BONUS) / 80) + ((effectiveStrengthDamage * character.getBonus(BonusConstants.STRENGTH_BONUS)) / 640);
		
		if(special) {
			if(character.getEquipment().get(EquipmentConstants.WEAPON) != null) {
				switch(character.getEquipment().get(EquipmentConstants.WEAPON).getId()) {
				case 3101:
                                    specialMultiplier = 1.1;
                                    break;
				case 3204:
				case 5698:
					specialMultiplier = 1.1;
					break;
				case 1305:
					specialMultiplier = 1.25;
					break;
				case 1434:
					specialMultiplier = 1.45;
					break;
				}
			}
		}
		
		maxHit = (int) Math.floor(baseDamage * specialMultiplier);
		
		if(fullDharok(character)) {
			int hpLost = character.getSkillSet().getSkill(Skill.HITPOINTS).getMaximumLevel() - character.getSkillSet().getSkill(Skill.HITPOINTS).getCurrentLevel();
			maxHit += hpLost / 2;
		}		
		return maxHit;
	}

	/**
	 * Calculates a character's range max hit.
	 */
	public static int calculateRangeMaxHit(Character character, boolean special) {
		int maxHit = 0;
		double specialMultiplier = 1;
		double prayerMultiplier = 1;
		double otherBonusMultiplier = 1;	
		int rangedStrength = character.getBonus(BonusConstants.RANGE_ATTACK_BONUS);
		Item weapon = character.getEquipment().get(EquipmentConstants.WEAPON);
		BowType bow = BowType.forId(weapon != null ? weapon.getId() : -1);
		
		if(bow != null && bow == BowType.CRYSTAL_BOW) {
			/**
			 * Crystal Bow does not use arrows, so we don't use the arrows range strength bonus.
			 */
			rangedStrength = character.getEquipment().get(EquipmentConstants.WEAPON).getItemBonuses().getBonuses()[BonusConstants.RANGE_ATTACK_BONUS];
		}
		
		int rangeLevel = character.getSkillSet().getSkill(Skill.RANGED).getCurrentLevel();
		int combatStyleBonus = 0;
		
		//TODO: Add new prayers: sharp eye, hawk eye, eagle eye
		
		switch(character.getCombatState().getCombatStyle()) {
		case ACCURATE:
			combatStyleBonus = 3;
			break;
		}
		
		if(fullVoidRange(character)) {
			otherBonusMultiplier = 1.1;
		}
		
		int effectiveRangeDamage = (int) ((rangeLevel * prayerMultiplier * otherBonusMultiplier) + combatStyleBonus);
		double baseDamage = 1.3 + (effectiveRangeDamage / 10) + (rangedStrength / 80) + ((effectiveRangeDamage * rangedStrength) / 640);
		
		if(special) {
			if(character.getEquipment().get(EquipmentConstants.ARROWS) != null) {
				switch(character.getEquipment().get(EquipmentConstants.ARROWS).getId()) {
				case 11212:
					specialMultiplier = 1.5;
					break;
				case 9243:
					specialMultiplier = 1.15;
					break;
				case 9244:
					specialMultiplier = 1.45;
					break;
				case 9245:
					specialMultiplier = 1.15;
					break;
				case 9236:
					specialMultiplier = 1.25;
					break;
				case 882:
				case 884:
				case 886:
				case 888:
				case 890:
				case 892:
					if(character.getEquipment().get(EquipmentConstants.WEAPON) != null && character.getEquipment().get(EquipmentConstants.WEAPON).getId() == 11235) {
						specialMultiplier = 1.3;
					}
					break;
				}
			}
		}
		
		maxHit = (int) (baseDamage * specialMultiplier);
		
		return maxHit;
	}

	public static boolean fullVoidMelee(Character character) {
		return character.getEquipment() != null
				&& character.getEquipment().contains(8839)
				&& character.getEquipment().contains(8840)
				&& character.getEquipment().contains(8842)
				&& character.getEquipment().contains(11665);
	}

	public static boolean fullVoidRange(Character character) {
		return character.getEquipment() != null
				&& character.getEquipment().contains(8839)
				&& character.getEquipment().contains(8840)
				&& character.getEquipment().contains(11664)
				&& character.getEquipment().contains(8097);
	}

	public static boolean fullVoidMage(Character character) {
		return character.getEquipment() != null
				&& character.getEquipment().contains(8839)
				&& character.getEquipment().contains(8840)
				&& character.getEquipment().contains(11663)
				&& character.getEquipment().contains(8098);
	}

	public static boolean fullGuthan(Character character) {
		return character.getEquipment() != null
				&& character.getEquipment().contains(4724)
				&& character.getEquipment().contains(4726)
				&& character.getEquipment().contains(4728)
				&& character.getEquipment().contains(4730);
	}

	public static boolean fullTorag(Character character) {
		return character.getEquipment() != null
				&& character.getEquipment().contains(4745)
				&& character.getEquipment().contains(4747)
				&& character.getEquipment().contains(4749)
				&& character.getEquipment().contains(4751);
	}

	public static boolean fullKaril(Character character) {
		return character.getEquipment() != null
				&& character.getEquipment().contains(4732)
				&& character.getEquipment().contains(4734)
				&& character.getEquipment().contains(4736)
				&& character.getEquipment().contains(4738);
	}

	public static boolean fullAhrim(Character character) {
		return character.getEquipment() != null
				&& character.getEquipment().contains(4708)
				&& character.getEquipment().contains(4710)
				&& character.getEquipment().contains(4712)
				&& character.getEquipment().contains(4714);
	}

	public static boolean fullDharok(Character character) {
		return character.getEquipment() != null
				&& character.getEquipment().contains(4716)
				&& character.getEquipment().contains(4718)
				&& character.getEquipment().contains(4720)
				&& character.getEquipment().contains(4722);
	}

	public static boolean fullVerac(Character character) {
		return character.getEquipment() != null
				&& character.getEquipment().contains(4753)
				&& character.getEquipment().contains(4755)
				&& character.getEquipment().contains(4757)
				&& character.getEquipment().contains(4759);
	}

	/**
	 * Get the attackers' weapon speed.
	 * 
	 * @param player The player for whose weapon we are getting the speed value.
	 * @return A <code>long</code>-type value of the weapon speed.
	 */
	public static int getCombatCooldownDelay(Character character) {
		int extra = 0;
		if(getActiveCombatAction(character) == RangeCombatAction.getAction()) {
			if(character.getCombatState().getCombatStyle() != CombatStyle.AGGRESSIVE_1) {
				/**
				 * If we are ranging and are not on rapid, combat speed is increased by 1 cycle
				 */
				extra = 1;
			}
		}
		return (character.getEquipment().get(3) != null && character.getEquipment().get(3).getEquipmentDefinition() != null) ? character.getEquipment().get(3).getEquipmentDefinition().getSpeed() + extra : 4;
	}

	public static CombatAction getActiveCombatAction(Character character) {
		if(character.getDefaultCombatAction() != null) {
                    if(character instanceof Player) {
			return character.getDefaultCombatAction();
                    } else {
                        NPC n = (NPC) character;
                        return n.getCombatDef().getCombatAction();
                    }
		}
		if(character.getCombatState().getQueuedSpell() != null || (character.getAutocastSpell() != null && character.getCombatState().getCombatStyle() == CombatStyle.AUTOCAST)) {
			return MagicCombatAction.getAction();
		}
		Item weapon = character.getEquipment().get(EquipmentConstants.WEAPON);
		if(weapon != null) {
			if(RangeCombatAction.BowType.forId(weapon.getId()) != null) {
				return RangeCombatAction.getAction();				
			} else if(RangeCombatAction.KnifeType.forId(weapon.getId()) != null) {
				return RangeCombatAction.getAction();				
			} else if(RangeCombatAction.DartType.forId(weapon.getId()) != null) {
				return RangeCombatAction.getAction();				
			} else if(RangeCombatAction.ThrownaxeType.forId(weapon.getId()) != null) {
				return RangeCombatAction.getAction();				
			} else if(RangeCombatAction.JavelinType.forId(weapon.getId()) != null) {
				return RangeCombatAction.getAction();				
			} else if(character.getEquipment().get(EquipmentConstants.WEAPON).getId() == 6522) {
				return RangeCombatAction.getAction();				
			}
		}
		return MeleeCombatAction.getAction();
	}

}
