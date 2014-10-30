package org.apollo.io;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apollo.game.model.Animation;
import org.apollo.game.model.EquipmentConstants;
import org.apollo.game.model.EquipmentConstants.EquipmentType;
import org.apollo.game.model.def.EquipmentAnimations;
import org.apollo.game.model.def.EquipmentDefinition;
import org.apollo.game.model.def.PoisonType;
import org.apollo.game.model.def.WeaponStyle;

/**
 * A class which parses the {@code data/equipment-[release].dat} file to
 * create an array of {@link EquipmentDefinition}s.
 * @author Graham
 */
public final class EquipmentDefinitionParser {

	/**
	 * The input stream.
	 */
	private final InputStream is;

	/**
	 * Creates the equipment definition parser.
	 * @param is The input stream.
	 */
	public EquipmentDefinitionParser(InputStream is) {
		this.is = is;
	}

	/**
	 * Parses the input stream.
	 * @return The equipment definition array.
	 * @throws IOException if an I/O error occurs.
	 */
	public EquipmentDefinition[] parse() throws IOException {
		DataInputStream dis = new DataInputStream(is);

		int count = dis.readShort() & 0xFFFF;
		EquipmentDefinition[] defs = new EquipmentDefinition[count];

		for (int id = 0; id < count; id++) {
			int slot = dis.readByte() & 0xFF;
			if (slot != 0xFF) {
				boolean twoHanded = dis.readBoolean();
				boolean fullBody = dis.readBoolean();
				boolean fullHat = dis.readBoolean();
				boolean fullMask = dis.readBoolean();
				int attack = dis.readByte() & 0xFF;
				int strength = dis.readByte() & 0xFF;
				int defence = dis.readByte() & 0xFF;
				int ranged = dis.readByte() & 0xFF;
				int magic = dis.readByte() & 0xFF;
                                int speed = dis.readByte() & 0xFF;
                                boolean special = dis.readBoolean();
                                int standAnim = dis.readShort() & 0xFFFF;
                                int runAnim = dis.readShort() & 0xFFFF;
                                int walkAnim = dis.readShort() & 0xFFFF;
                                int attackAnimOne = dis.readShort() & 0xFFFF;
                                int poison = dis.readByte() & 0xFF;
                                int specialConsumption = dis.readByte() & 0xFF;
                                int specialHits = dis.readByte() & 0xFF;

                                EquipmentAnimations animations = new EquipmentAnimations(new Animation(standAnim), new Animation(runAnim), new Animation(walkAnim), 
                                        new Animation[] {new Animation(attackAnimOne), new Animation(423), new Animation(422), new Animation(422)}, new Animation(424), 
                                        new Animation(823), new Animation(820), new Animation(821), new Animation(822));
                                EquipmentType type = EquipmentConstants.getTypeBySlot(slot);
                                PoisonType poisonType = PoisonType.NONE;
                                if(specialConsumption == 1) {
                                    specialConsumption = 0;
                                }
                                if(poison == 1) {
                                    poisonType = PoisonType.POISON;
                                } else if(poison == 2) {
                                    poisonType = PoisonType.EXTRA_POISON;
                                } else if(poison == 3) {
                                    poisonType = PoisonType.SUPER_POISON;
                                } else if(poison == 4) {
                                    poisonType = PoisonType.KARAMBWAN_PASTE_POISON;
                                }
                                if(twoHanded) {
                                    type = EquipmentType.WEAPON_2H;
                                } else if(fullBody) {
                                    type = EquipmentType.PLATEBODY;
                                } else if(fullHat) {
                                    type = EquipmentType.FULL_HELM;
                                } else if(fullMask) {
                                    type = EquipmentType.FULL_MASK;
                                }
                                WeaponStyle[] weaponStyles = new WeaponStyle[4];
                                if(special) {
                                    weaponStyles[0] = WeaponStyle.SPECIAL;
                                }
                                int[] levels = {attack, strength, defence, ranged, magic};
                                EquipmentDefinition newDef = new EquipmentDefinition(id, speed, weaponStyles, 
                                        type, animations, levels, poisonType, specialConsumption, specialHits);
				newDef.setSlot(slot);

				defs[id] = newDef;
			}
		}

		return defs;
	}

}
