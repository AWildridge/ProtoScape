package org.apollo.game.model.def;

/**
 * NPCDefinition.java
 * @author The Wanderer
 */
public class NPCDefinition {

    private int id;
    private String name;
    private String desc;
    private int combatLevel;
    private int size;
    private int hp;
    private int maxHp;
    private boolean isAttackable;
    private String[] actions;
    private int walkAnim;
    private int standAnim;
    private int retreatAnim;
    private int turnRightAnim;
    private int turnLeftAnim;

    public NPCDefinition(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public int getCombatLevel() {
        return combatLevel;
    }

    public boolean isAttackable() {
        return isAttackable;
    }

    public int getSize() {
        return size;
    }

    public String[] getActions() {
        return actions;
    }

    public int getWalkAnim() {
        return walkAnim;
    }

    public int getHp() {
        return hp;
    }

    public int getMaxHp() {
        return maxHp;
    }
    
    public void setHp(int hp) {
        this.hp = hp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public void setRetreatAnim(int retreatAnim) {
        this.retreatAnim = retreatAnim;
    }

    public void setStandAnim(int standAnim) {
        this.standAnim = standAnim;
    }

    public void setTurnLeftAnim(int turnLeftAnim) {
        this.turnLeftAnim = turnLeftAnim;
    }

    public void setTurnRightAnim(int turnRightAnim) {
        this.turnRightAnim = turnRightAnim;
    }

    public void setWalkAnim(int walkAnim) {
        this.walkAnim = walkAnim;
    }

    public int getRetreatAnim() {
        return retreatAnim;
    }

    public int getStandAnim() {
        return standAnim;
    }

    public int getTurnLeftAnim() {
        return turnLeftAnim;
    }

    public int getTurnRightAnim() {
        return turnRightAnim;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setCombatLevel(int combatLevel) {
        this.combatLevel = combatLevel;
    }

    public void setAttackable(boolean isAttackable) {
        this.isAttackable = isAttackable;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setActions(String[] actions) {
        this.actions = actions;
    }
    private static NPCDefinition[] definitions;

    /**
     * Gets an npc definition by its id.
     * @param id The id.
     * @return The definition.
     */
    public static NPCDefinition forId(int id) {
        return definitions[id];
    }

    public static void init(NPCDefinition[] definitions) {
        NPCDefinition.definitions = definitions;
        for (int id = 0; id < definitions.length; id++) {
            NPCDefinition def = definitions[id];
            if (def.getId() != id) {
                throw new RuntimeException("NPC definition id mismatch!");
            }
        }
    }
}
