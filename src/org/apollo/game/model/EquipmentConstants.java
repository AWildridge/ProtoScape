package org.apollo.game.model;

/**
 * Contains equipment-related constants.
 * @author Graham
 */
public final class EquipmentConstants {

    /**
     * The hat slot.
     */
    public static final int HAT = 0;
    /**
     * The cape slot.
     */
    public static final int CAPE = 1;
    /**
     * The amulet slot.
     */
    public static final int AMULET = 2;
    /**
     * The weapon slot.
     */
    public static final int WEAPON = 3;
    /**
     * The chest slot.
     */
    public static final int CHEST = 4;
    /**
     * The shield slot.
     */
    public static final int SHIELD = 5;
    /**
     * The legs slot.
     */
    public static final int LEGS = 7;
    /**
     * The hands slot.
     */
    public static final int HANDS = 9;
    /**
     * The feet slot.
     */
    public static final int FEET = 10;
    /**
     * The ring slot.
     */
    public static final int RING = 12;
    /**
     * The arrows slot.
     */
    public static final int ARROWS = 13;

    /**
     * Default private constructor to prevent instantiation;
     */
    private EquipmentConstants() {
    }

    /**
     * Equipment type enum.
     * @author Lothy
     * @author Miss Silabsoft
     *
     */
    public enum EquipmentType {

        /**
         * Item is a cape
         */
        CAPE("Cape", EquipmentConstants.CAPE),
        /**
         * Item is a pair of boots
         */
        BOOTS("Boots", EquipmentConstants.FEET),
        /**
         * Item is a pair of gloves
         */
        GLOVES("Gloves", EquipmentConstants.HANDS),
        /**
         * Item is a shield
         */
        SHIELD("Shield", EquipmentConstants.SHIELD),
        /**
         * Item is a hat
         */
        HAT("Hat", EquipmentConstants.HAT),
        /**
         * Item is an amulet
         */
        AMULET("Amulet", EquipmentConstants.AMULET),
        /**
         * Item is a set of arrows
         */
        ARROWS("Arrows", EquipmentConstants.ARROWS),
        /**
         * Item is a ring
         */
        RING("Ring", EquipmentConstants.RING),
        /**
         * Item is a normal body with no sleeves
         */
        BODY("Body", EquipmentConstants.CHEST),
        /**
         * Item is legs
         */
        LEGS("Legs", EquipmentConstants.LEGS),
        /**
         * Item is a platebody
         */
        PLATEBODY("Platebody", EquipmentConstants.CHEST),
        /**
         * Item covers over hair
         */
        FULL_HELM("Full helm", EquipmentConstants.HAT),
        /**
         * Item covers over head fully
         */
        FULL_MASK("Full mask", EquipmentConstants.HAT),
        /**
         * Item is a weapon
         */
        WEAPON("Weapon", EquipmentConstants.WEAPON),
        /**
         * Item is a weapon 2 handed
         */
        WEAPON_2H("Two-handed weapon", EquipmentConstants.WEAPON);
        /**
         * The description.
         */
        private String description;
        /**
         * The slot.
         */
        private int slot;

        /**
         * Creates the equipment type.
         * @param description The description.
         * @param slot The slot.
         */
        private EquipmentType(String description, int slot) {
            this.description = description;
            this.slot = slot;
        }

        /**
         * Gets the description.
         * @return The description.
         */
        public String getDescription() {
            return description;
        }

        /**
         * Gets the slot.
         * @return The slot.
         */
        public int getSlot() {
            return slot;
        }
    }

    /**
     * Gets an equipment type.
     * @param item The item.
     * @return The equipment type.
     */
    public static EquipmentType getType(Item item) {
        if (item.getEquipmentDefinition() != null) {
            return item.getEquipmentDefinition().getType();
        } else {
            return null;
        }
    }

    public static EquipmentType getTypeBySlot(int slot) {
        for (EquipmentType type : EquipmentType.values()) {
            if (slot == type.getSlot()) {
                return type;
            } else {
                return null;
            }
        }
        return null;
    }
}
