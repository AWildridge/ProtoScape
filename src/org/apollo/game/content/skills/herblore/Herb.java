package org.apollo.game.content.skills.herblore;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * Contains various data related to cleaning/identifying herbs; please
 * note that on RuneScape you originally "identified" the herbs, but it
 * was changed because of massive scamming (people sold guams for ranarr
 * price and so forth). This update happened on 10th of September 2007,
 * so I suggest 317 servers to have identification, and NOT cleaning.
 *
 * Also note that some of the herbs didn't exist back in 2007, and is
 * therefore not in the 317 cache.
 *
 * Credits to the following sites for level requirement, experience
 * and herb item ID's;
 *
 * http://itemdb.biz/
 * http://runescape.wikia.com/wiki/Calculators/Herbs
 * http://runescape.wikia.com/wiki/Dungeoneering/Herblore
 * http://services.runescape.com/m=rswiki/en/Herblore_-_The_Herbs
 * http://pastebin.com/WXK7gCPd
 *
 * @author Nouish <admin@nouish.com>
 *
 */

public enum Herb {	
	GUAM(199, 249, 3, 20),
	MARRENTILL(201, 251, 5, 38),
	TARROMIN(203, 253, 11, 50),
	HARRALANDER(205, 255, 20, 63),
	RANARR(207, 257, 25, 75),
	TOADFLAX(3049, 2998, 30, 80),
	IRIT(209, 259, 40, 88),
	WERGALI(14836, 14854, 41, 95),
	AVANTOE(211, 261, 48, 100),
	KWUARM(213, 263, 54, 113),
	SNAPDRAGON(3051, 3000, 59, 118),
	CADANTINE(215, 265, 65, 125),
	LANTADYME(2485, 2481, 67, 131),
	DWARFWEED(217, 267, 70, 138),
	SPIRITWEED(12174, 12172, 35, 78),
	TORSTOL(219, 269, 75, 150),
	FELLSTALK(21626, 21625, 91, 168),

	/**
	 * Daemonheim herbs
	 */
	SAGEWORT(17494, 17512, 3, 21),
	VALERIAN(17496, 17514, 4, 32),
	ALOE(17498, 17516, 8, 40),
	WORMWOOD(17500, 17518, 34, 72),
	MAGEBANE(17502, 17520, 37, 77),
	FEATHERFOIL(17504, 17522, 41, 86),
	WINTERSGRIP(17506, 17524, 67, 127),
	LYCOPUS(17508, 17526, 70, 131),
	BUCKTHORN(17510, 17510, 74, 138),
	ERZILLE(19984, 19989, 54, 100),
	UGUNE(19986, 19991, 56, 115),
	ARGWAY(19985, 19990, 57, 116),
	SHENGO(19987, 19992, 58, 117),
	SAMADEN(19988, 19993, 59, 117);

	/**
	 * Construct a new {@link Herb} object with the given parameters.
	 * These are all required to successfully lose the grimy herb,
	 * obtain the clean version and then obtain some experience if
	 * your Herblore level is high enough.
	 *
	 * @param unidentifiedId
	 *		The ID of the unidentified/grimy version of the herb.
	 * @param identifiedId
	 *		The ID of the identified/clean version of the herb.
	 * @param levelRequirement
	 *		The level required to identify/clean the herb.
	 * @param experience
	 *		The experience obtained by identifying/cleaning the herb.
	 */
	private Herb(int unidentifiedId, int identifiedId, int levelRequirement, double experience) {
		this.unidentifiedId = (short) unidentifiedId;
		this.identifiedId = (short) identifiedId;
		this.levelRequirement = (short) levelRequirement;
		this.experience = experience;
	}

	/**
	 * The unidentified (grimy) ID of the herb.
	 */
	private final short unidentifiedId;

	/**
	 * The identified (clean) ID of the herb.
	 */
	private final short identifiedId;

	/**
	 * The level required to identify/clean the herb.
	 */
	private final short levelRequirement;

	/**
	 * The experience obtained by identifying/cleaning the herb.
	 */
	private final double experience;

	/**
	 * Returns the represented herbs' unidentified/grimy ID.
	 *
	 * @return
	 *		The unidentified/grimy ID.
	 */
	public final short getUnidentifiedID() {
		return this.unidentifiedId;
	}

	/**
	 * Returns the represented herbs' identified/clean ID.
	 *
	 * @return
	 *		The identified/clean ID.
	 */
	public final short getIdentifiedID() {
		return this.identifiedId;
	}

	/**
	 * Returns the level required to identify/clean the represented
	 * herb.
	 *
	 * @return
	 *		The level required to identify/clean the represented
	 *		herb.
	 */
	public final short getLevelRequirement() {
		return this.levelRequirement;
	}

	/**
	 * Returns the experience obtained by identifying/cleaning the
	 * represented herb.
	 *
	 * @return
	 *		The experience obtained by the represented herb.
	 */
	public final double getExperience() {
		return this.experience;
	}

	/**
	 * A {@link Map} of the {@link Herb}s in this enum.
	 */
	private static final Map<Short, Herb> herbs = new HashMap<Short, Herb>();

	/**
	 * Returns the {@link Herb} for an unidentified ID.
	 *
	 * @param unidentifiedId
	 *		The ID of the unidentified/grimy version of the herb.
	 * @return
	 *		The {@link Herb} object which has an unidentified/grimy
	 *		version which equals {@link unidentifiedId}.
	 */
	public static Herb forId(int unidentifiedId) {
		return herbs.get((short) unidentifiedId);
	}

	/**
	 * We'd like to fill in the values in the {@link herbs} {@link HashMap} to
	 * make it easier to find back the values (instead of iterating over the values()
	 * checking for an matching ID).
	 */
	static {
		for (Herb herb : Herb.values()) {
			herbs.put(herb.getUnidentifiedID(), herb);
		}
	}
}
