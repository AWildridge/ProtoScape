package org.apollo.game.model.def;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.apollo.game.model.EquipmentConstants.EquipmentType;
import org.apollo.game.model.Item;
import org.apollo.util.XStreamUtil;

/**
 * Represents a type of {@link Item} which may be equipped.
 * @author Graham
 */
public final class EquipmentDefinition {

    /**
     * Logger instance.
     */
    private static final Logger logger = Logger.getLogger(EquipmentDefinition.class.getName());
    /**
     * We shall allocate a size for the HashMap!
     */
    public static final int ALLOCATED_SIZE = 2384;
    /**
     * The equipment definitions.
     */
    private static Map<Integer, EquipmentDefinition> definitions = new HashMap<Integer, EquipmentDefinition>();

    /**
     * Initialises the equipment definitions.
     * @param definitions The definitions.
     */
    public static void init(EquipmentDefinition[] definitions) {
        for (int id = 0; id < definitions.length; id++) {
            EquipmentDefinition def = definitions[id];
            if (def != null) {
                if (def.getId() != id) {
                    throw new RuntimeException("Item definition id mismatch!");
                }
                EquipmentDefinition.definitions.put(def.getId(), def);
            }
        }
    }

    /**
     * Gets an equipment definition by its id.
     * @param id The id.
     * @return {@code null} if the item is not equipment, the definition
     * otherwise.
     * @throws IndexOutOfBoundsException if the id is out of bounds.
     */
    public static EquipmentDefinition forId(int id) {
        if (id < 0 || id >= ItemDefinition.count()) {
            throw new IndexOutOfBoundsException();
        }
        return definitions.get(id);
    }
    /**
     * The item id.
     */
    private final int id;
    
    /**
     * The weapon speed
     */
    private int speed;

    /**
     * The weapon type
     */
    private WeaponStyle[] weaponStyles;
    
    /**
     * The equipment types
     */
    private EquipmentType type;
    
    /**
     * The equipment animations
     */
    private EquipmentAnimations animations;
    
    /**
     * The equipment's poison type.
     */
    private PoisonType poisonType;
    
    /**
     * The equipment's skill requirements.
     */
    private int[] skillRequirements;
    
    /**
     * The amount of special energy this item uses.
     */
    private int specialConsumption;
    
    /**
     * The amount of hits this special deals.
     */
    private int specialHits;
    
    /**
     * The slot this equipment goes into.
     */
    private int slot;
    
    /**
     * The required levels.
     */
    private int attack = 1, strength = 1, defence = 1, ranged = 1, magic = 1;

    /**
     * Gets the id.
     * @return The id.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the minimum attack level.
     * @return The minimum attack level.
     */
    public int getAttackLevel() {
        return attack;
    }

    /**
     * Gets the minimum strength level.
     * @return The minimum strength level.
     */
    public int getStrengthLevel() {
        return strength;
    }

    /**
     * Gets the minimum defence level.
     * @return The minimum defence level.
     */
    public int getDefenceLevel() {
        return defence;
    }

    /**
     * Gets the minimum ranged level.
     * @return The minimum ranged level.
     */
    public int getRangedLevel() {
        return ranged;
    }

    /**
     * Gets the minimum magic level.
     * @return The minimum magic level.
     */
    public int getMagicLevel() {
        return magic;
    }

    /**
     * Sets the target slot.
     * @param slot The target slot.
     */
    public void setSlot(int slot) {
        this.slot = slot;
    }

    /**
     * Gets the target slot.
     * @return The target slot.
     */
    public int getSlot() {
        return slot;
    }

    /**
     * Creates the equipment definition
     * @param id the item id
     * @param weaponInterface the weapon interface
     * @param weaponSpeed the weapon speed
     * @param bonuses the item bonuses
     * @param weaponType the weapon type
     * @param equipmentTypes the equipment type
     */
    public EquipmentDefinition(int id, int speed, WeaponStyle[] weaponStyles, EquipmentType type,
            EquipmentAnimations animations, int[] skillRequirements, PoisonType poisonType, int specialConsumption, int specialHits) {
        this.speed = speed;
        this.weaponStyles = weaponStyles;
        this.type = type;
        this.animations = animations;
        this.poisonType = poisonType;
        this.attack = skillRequirements[0];
        this.strength = skillRequirements[1];
        this.defence = skillRequirements[2];
        this.ranged = skillRequirements[3];
        this.magic = skillRequirements[4];
        this.specialConsumption = specialConsumption;
        this.specialHits = specialHits;
        this.id = id;
    }

    /**
     * The weapon speed
     * @return the weapon speed
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * Gets the weapon type
     * @return The weapon type
     */
    public WeaponStyle[] getWeaponStyles() {
        return weaponStyles;
    }

    /**
     * Gets the weapon type by its index.
     * @return The weapon type by its index.
     */
    public WeaponStyle getWeaponStyle(int index) {
        return weaponStyles[index];
    }

    /**
     * Gets the equipment types
     * @return The equipment types
     */
    public EquipmentType getType() {
        return type;
    }

    /**
     * Gets the equipment animation instance
     * @return the equipmentAnimation
     */
    public EquipmentAnimations getAnimation() {
        return animations;
    }

    /**
     * Gets the weapons poison type
     * @return The weapons poison type
     */
    public PoisonType getPoisonType() {
        return poisonType;
    }

    /**
     * Gets the weapons skill requirements.
     * @return The weapons skill requirements.
     */
    public int[] getSkillRequirements() {
        return skillRequirements;
    }

    /**
     * Gets the weapons skill requirements.
     * @param index The skill index.
     * @return The weapons skill requirements.
     */
    public int getSkillRequirement(int index) {
        return skillRequirements[index];
    }

    /**
     * Gets the amount of special energy this item consumes.
     * @return The amount of special energy this item consumes.
     */
    public int getSpecialConsumption() {
        return specialConsumption;
    }

    /**
     * Gets the amount of hits this special attack deals.
     * @return The amount of hits this special attack deals.
     */
    public int getSpecialHits() {
        return specialHits;
    }

    public void setSpecialConsumption(int specialConsumption) {
        this.specialConsumption = specialConsumption;
    }

    public void setSpecialHits(int specialHits) {
        this.specialHits = specialHits;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setWeaponStyle(int index, WeaponStyle style) {
        this.weaponStyles[index] = style;
    }
}
