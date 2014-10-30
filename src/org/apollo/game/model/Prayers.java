package org.apollo.game.model;

import java.util.HashMap;
import java.util.Map;

import org.apollo.game.event.impl.CloseInterfaceEvent;
import org.apollo.game.event.impl.SendConfigEvent;
import org.apollo.game.scheduling.impl.PrayerUpdateTask;

public class Prayers {

	/**
	 * Represents types of prayers.
	 * @author Scu11
	 *
	 */
	public static enum Prayer {

		/**
		 * Thick skin.
		 */
		THICK_SKIN(0, "Thick Skin", 1, -1, 83, 12),

		/**
		 * Burst of Strength.
		 */
		BURST_OF_STRENGTH(1, "Burst of Strength", 4, -1, 84, 12),
		
		/**
		 * Clarity of Thought.
		 */
		CLARITY_OF_THOUGHT(2, "Clarity of Thought", 7, -1, 85, 12),
		
		/**
		 * Rock Skin.
		 */
		ROCK_SKIN(3, "Rock Skin", 10, -1, 86, 6),
		
		/**
		 * Superhuman Strength.
		 */
		SUPERHUMAN_STRENGTH(4, "Superhuman Strength", 13, -1, 87, 6),
		
		/**
		 * Improved Reflexes.
		 */
		IMPROVED_REFLEXES(5, "Improved Reflexes", 16, -1, 88, 6),
		
		/**
		 * Rapid Restore.
		 */
		RAPID_RESTORE(6, "Rapid Restore", 19, -1, 89, 26),
		
		/**
		 * Rapid Heal.
		 */
		RAPID_HEAL(7, "Rapid Heal", 22, -1, 90, 18),
		
		/**
		 * Protect Items.
		 */
		PROTECT_ITEMS(8, "Protect Items", 25, -1, 91, 18),
		
		/**
		 * Steel Skin.
		 */
		STEEL_SKIN(9, "Steel Skin", 28, -1, 92, 3),
		
		/**
		 * Ultimate Strength.
		 */
		ULTIMATE_STRENGTH(10, "Ultimate Strength", 31, -1, 93, 3),
		
		/**
		 * Incredible Reflexes.
		 */
		INCREDIBLE_REFLEXES(11, "Incredible Reflexes", 34, -1, 94, 3),
		
		/**
		 * Protect from Magic.
		 */
		PROTECT_FROM_MAGIC(12, "Protect from Magic", 37, 2, 95, 3),
		
		/**
		 * Protect from Missiles.
		 */
		PROTECT_FROM_MISSILES(13, "Protect from Missiles", 40, 1, 96, 3),
		
		/**
		 * Protect from Melee.
		 */
		PROTECT_FROM_MELEE(14, "Protect from Melee", 43, 0, 97, 3),
		
		/**
		 * Retribution.
		 */
		RETRIBUTION(15, "Retribution", 46, 3, 98, 12),
		
		/**
		 * Redemption.
		 */
		REDEMPTION(16, "Redemption", 49, 5, 99, 6),
		
		/**
		 * Smite.
		 */
		SMITE(17, "Smite", 52, 4, 100, 2);
		
		/**
		 * A map of prayer IDs.
		 */
		private static Map<Integer, Prayer> prayers = new HashMap<Integer, Prayer>();
		
		/**
		 * Gets a prayer by its ID.
		 * @param prayer The prayer id.
		 * @return The prayer, or <code>null</code> if the id is not a prayer.
		 */
		public static Prayer forId(int prayer) {
			return prayers.get(prayer);
		}
		
		/**
		 * Populates the prayer map.
		 */
		static {
			for(Prayer prayer : Prayer.values()) {
				prayers.put(prayer.id, prayer);
			}
		}

		/**
		 * The id of this prayer.
		 */
		private int id;


		/**
		 * The name of this prayer.
		 */
		private String name;
		
		/**
		 * The required level for this prayer.
		 */
		private int level;

		/**
		 * The client configuration for this prayer.
		 */
		private int config;

		/**
		 * The head icon for this prayer.
		 */
		private int icon;
		
		/**
		 * The amount of seconds it takes to drain one prayer point.
		 */
		private int drain;

		/**
		 * Creates the prayer.
		 * @param prayer The prayer id.
		 * @return 
		 */
		private Prayer(int id, String name, int level, int icon, int config, int drain) {
			this.id = id;
			this.name = name;
			this.level = level;
			this.config = config;
			this.icon = icon;
			this.drain = drain;
		}

		/**
		 * Gets the prayer id.
		 * @return The prayer id.
		 */
		public int getPrayerId() {
			return id;
		}

		/**
		 * Gets the prayer name.
		 * @return The prayer name.
		 */
		public String getPrayerName() {
			return name;
		}
		
		/**
		 * Gets the level required for this prayer.
		 * @return The level required for this prayer.
		 */
		public int getLevelRequired() {
			return level;
		}

		/**
		 * Gets the client configuration for this prayer.
		 * @return The client configuration for this prayer.
		 */
		public int getClientConfiguration() {
			return config;
		}

		/**
		 * Gets the head icon for this prayer.
		 * @return The head icon for this prayer.
		 */
		public int getHeadIcon() {
			return icon;
		}
		
		/**
		 * Gets the amount of prayer points this prayer drains every tick.
		 * @return The amount of prayer points this prayer drains every tick.
		 */
		public int getDrain() {
			return drain;
		}
	}

	/**
	 * Constants for the prayer numbers.
	 */
	public static final int THICK_SKIN = 0, BURST_OF_STRENGTH = 1, CLARITY_OF_THOUGHT = 2, ROCK_SKIN = 3,
		SUPERHUMAN_STRENGTH = 4, IMPROVED_REFLEXES = 5, RAPID_RESTORE = 6, RAPID_HEAL = 7, PROTECT_ITEMS = 8, 
		STEEL_SKIN = 9, ULTIMATE_STRENGTH = 10, INCREDIBLE_REFLEXES = 11, PROTECT_FROM_MAGIC = 12, 
		PROTECT_FROM_MISSILES = 13, PROTECT_FROM_MELEE = 14, RETRIBUTION = 15, REDEMPTION = 16, SMITE = 17;
	

	public static void activatePrayer(Player player, int id) {
		if(player.getCombatState().isDead()) {
			player.send(new SendConfigEvent(Prayer.forId(id).getClientConfiguration(), player.getCombatState().getPrayer(id) ? 1 : 0));
			return;
		}
		Prayer prayer = Prayer.forId(id);
		player.getCombatState().setPrayer(id, !player.getCombatState().getPrayer(id));
		int newId = -1;
                player.send(new CloseInterfaceEvent());
		/*if(player.getDuelRules().contains(DuelRule.NO_PRAYER)) {
			deActivatePrayer(player, id);
			player.sendMessage("You cannot use prayer in this duel.");
			return;
		}*/
		if(player.getSkillSet().getSkill(Skill.PRAYER).getMaximumLevel() < prayer.getLevelRequired()) {
			deActivatePrayer(player, id);
			player.sendMessage("You need a Prayer level of " + prayer.getLevelRequired() + " to use " + prayer.getPrayerName() + ".");
			return;
		}
		if(player.getSkillSet().getSkill(Skill.PRAYER).getCurrentLevel() <= 1) {
                    System.out.println(player.getSkillSet().getSkill(Skill.PRAYER).getCurrentLevel());
			deActivatePrayer(player, id);
			player.sendMessage("You have run out of prayer points; you must recharge at an altar.");
			return;
		}
		if(player.getCombatState().getPrayer(id)) {
			if(player.getPrayerUpdateTask() == null) {
				player.setPrayerUpdateTask(new PrayerUpdateTask(player));
				World.getWorld().schedule(player.getPrayerUpdateTask());
			}
			switch(id) {
			case 0:
				newId = 3;
				deActivatePrayer(player, newId);
				newId = 9;
				deActivatePrayer(player, newId);
			break;
			case 1:
				newId = 4;
				deActivatePrayer(player, newId);
				newId = 10;
				deActivatePrayer(player, newId);
			break;
			case 2:
				newId = 5;
				deActivatePrayer(player, newId);
				newId = 11;
				deActivatePrayer(player, newId);
			break;
			case 3:
				newId = 0;
				deActivatePrayer(player, newId);
				newId = 9;
				deActivatePrayer(player, newId);
			break;
			case 4:
				newId = 1;
				deActivatePrayer(player, newId);
				newId = 10;
				deActivatePrayer(player, newId);
			break;
			case 5:
				newId = 2;
				deActivatePrayer(player, newId);
				newId = 11;
				deActivatePrayer(player, newId);
			break;
			case 9:
				newId = 0;
				deActivatePrayer(player, newId);
				newId = 3;
				deActivatePrayer(player, newId);
			break;
			case 10:
				newId = 1;
				deActivatePrayer(player, newId);
				newId = 4;
				deActivatePrayer(player, newId);
			break;
			case 11:
				newId = 2;
				deActivatePrayer(player, newId);
				newId = 5;
				deActivatePrayer(player, newId);
			break;
			case 12:
			case 13:
			case 14:
			case 15:
			case 16:
			case 17:
				for(int i = 12; i < 18; i++) {
					if(i != id) {
						deActivatePrayer(player, i);
					} else {
						player.getCombatState().setPrayerHeadIcon(prayer.getHeadIcon());
					}
				}
				break;
			}
			for(int i = 0; i < player.getCombatState().getPrayers().length; i++) {
				Prayer prayer2 = Prayer.forId(i);
				if(player.getCombatState().getPrayer(i)) {
					player.send(new SendConfigEvent(prayer2.getClientConfiguration(), 1));
				} else {
					player.send(new SendConfigEvent(prayer2.getClientConfiguration(), 0));
				}
			}
		} else {
			switch(id) {
			case 12:
			case 13:
			case 14:
			case 15:
			case 16:
			case 17:
					player.getCombatState().setPrayerHeadIcon(-1);
				break;
			}
			boolean prayersFound = false;
			for(int i = 0; i < player.getCombatState().getPrayers().length; i++) {
				if(player.getCombatState().getPrayer(i)) {
					prayersFound = true;
					break;
				}
			}
			if(!prayersFound) {
				if(player.getPrayerUpdateTask() != null) {
					player.getPrayerUpdateTask().stop();
					player.setPrayerUpdateTask(null);
				}
			}
			player.send(new SendConfigEvent(prayer.getClientConfiguration(), 0));
		}		
	}
	
	public static void deActivatePrayer(Player player, int id) {
		player.getCombatState().setPrayer(id, false);
		player.send(new SendConfigEvent(Prayer.forId(id).getClientConfiguration(), 0));
	}
}
