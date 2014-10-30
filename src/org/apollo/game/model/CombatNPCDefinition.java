package org.apollo.game.model;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apollo.game.model.combat.CombatAction;
import org.apollo.game.model.combat.CombatState.AttackType;
import org.apollo.game.model.combat.CombatState.CombatStyle;
import org.apollo.game.model.combat.action.AbstractCombatAction;
import org.apollo.game.model.combat.action.MagicCombatAction;
import org.apollo.game.model.combat.action.MeleeCombatAction;
import org.apollo.game.model.combat.action.RangeCombatAction;
import org.apollo.game.model.def.ItemDefinition;
import org.apollo.util.XMLController;


/**
 * <p>Represents a type of combat NPC.</p>
 * @author Graham Edgecombe
 *
 */
public class CombatNPCDefinition {

    
  
    
    /**
     * Logger instance.
     */
    private static final Logger logger = Logger.getLogger(ItemDefinition.class.getName());
    /**
     * We shall allocate a size for the HashMap!
     */
    public static final int ALLOCATED_SIZE = 2384;
    /**
     * The <code>NPCDefinition</code> map.
     */
    private static Map<Integer, CombatNPCDefinition> definitions;

    /**
     * @return the definitions
     */
    public static Map<Integer, CombatNPCDefinition> getDefinitions() {
        return definitions;
    }

    /**
     * Gets a definition for the specified id.
     * @param id The id.
     * @return The definition.
     */
    public static CombatNPCDefinition forId(int id) {
        if (definitions.containsKey(id)) {
            return definitions.get(id);
        } else {
            return definitions.get(1);
        }
    }

 	/**
	 * Loads the item definitions.
	 * @throws IOException if an I/O error occurs.
	 * @throws IllegalStateException if the definitions have been loaded already.
	 */
	public static void init() throws IOException {
		if(definitions != null) {
			throw new IllegalStateException("NPC definitions already loaded.");
		}
		logger.info("Loading combat NPC definitions...");
		try {
			/**
			 * Load NPC definitions.
			 */
			definitions = new HashMap<Integer, CombatNPCDefinition>(ALLOCATED_SIZE);
			File file = new File("data/npcCombatDefinition.xml");
			if(file.exists()) {
				XMLController.alias("animation", Animation.class);
				XMLController.alias("NPCDefinition", CombatNPCDefinition.class);
                                XMLController.alias("entry", java.util.Map.class);
				definitions = XMLController.readXML(file);
                                System.out.println("NPC Def size = "+definitions.size());
				//definitions.put(1, new NPCDefinition(1, "Man", 2, new int[21], new int[12], Animation.create(422), Animation.create(404), 3));
				for(int i = 0; i <= definitions.size(); i++) {
					CombatNPCDefinition def = definitions.get(i);
					if(def == null) {
						continue;
					}
					switch(def.getAttackActionClassId()) {
					case 0:
						def.setCombatAction(MeleeCombatAction.getAction());
                                                break;
					case 1:
						def.setCombatAction(RangeCombatAction.getAction());
						break;
					case 2:
						def.setCombatAction(MagicCombatAction.getAction());
						break;
					case 3:
				        File[] files = new File("bin/org/hyperion/rs2/combat/npcs/impl").listFiles();
				        String packageName = "org.hyperion.rs2.combat.npcs.impl.";
				        for (File f : files) {
				            if (!f.getName().endsWith(".class")) {
				                continue;
				            }
				            String className = packageName + f.getName().substring(0, f.getName().length() - 6);
				            if (className.equals("org.hyperion.rs.combat.impl.AbstractCombatAction")) {
				                continue;
				            }
				            String requiredName = packageName + def.getName().replace(" ", "") + def.getCombatLevel();
			            	System.out.println("Req " + requiredName);
			            	System.out.println("Cur " + className);
				            if(!className.equalsIgnoreCase(requiredName)) {
				            	continue;
				            }
				            Class<?> actionClass = Class.forName(className);
				            if (actionClass.getSuperclass() == AbstractCombatAction.class) {
				            	AbstractCombatAction combatAction = (AbstractCombatAction) actionClass.newInstance();
				                try {
				                	System.out.println("Set "+combatAction);
				                	def.setCombatAction(combatAction);
				                } catch (Exception e) { 
				                	e.printStackTrace();
				                }
				            }
				        }
						break;
					}
				}
				logger.log(Level.INFO, "Loaded {0} combat NPC definitions.", definitions.size());
			} else {
				logger.info("NPC combat definitions not found!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    /**
     * The npc's id.
     */
    private int id;
    /**
     * The npc's name.
     */
    private String name;
    /**
     * The npc's combat level.
     */
    
    /*
     * NPC GwD
     * 
     */
    private String godWarsTeam;
    
    private String getGodWarsTeam() {
        return godWarsTeam;
    }
    
    private int combatLevel;
    
    private boolean agressive;
    
    public boolean getAgression() {
        return agressive;
    }
    
    /**
     * The combat cooldown delay.
     */
    private int combatCooldownDelay;
    /**
     * The npc's skill levels.
     */
    private int[] skills = new int[Skill.SKILL_LENGTH];
    /**
     * The npc's bonuses.
     */
    private int[] bonuses = new int[13];
    /**
     * The attack animation.
     */
    private Animation attack;
    /**
     * The defend animation.
     */
    private Animation defend;
    /**
     * The death animation.
     */
    private Animation death;
    /**
     * The combat action.
     */
    private CombatAction combatAction;
    /**
     * The attack action class id.
     */
    private int attackActionClassId;
    /**
     * The attack type.
     */
    private AttackType attackType;
    /**
     * The combat style.
     */
    private CombatStyle combatStyle;
    /**
     * The autocast spell id, if the attack style is magic.
     */
    private int autocastSpellId;
    /**
     * The arrow type id, if the attack style is range.
     */
    private int arrowTypeId;
    /**
     * The bow type id, if the attack style is range.
     */
    private int bowTypeId;

    /**
     * Creates the definition.
     * @param id The id.
     */
    private CombatNPCDefinition(int id, String name, int combatLevel, int combatCooldownDelay, int[] skills, int[] bonuses, Animation attack, Animation defend, Animation death, int attackActionClassId, AttackType attackType, CombatStyle combatStyle, int autocastSpellId, int arrowTypeId, int bowTypeId) {
        this.id = id;
        this.name = name;
        this.combatLevel = combatLevel;
        this.combatCooldownDelay = combatCooldownDelay;
        for (int i = 0; i < skills.length; i++) {
            this.skills[i] = skills[i];
        }
        for (int i = 0; i < bonuses.length; i++) {
            this.bonuses[i] = bonuses[i];
        }
        this.attack = attack;
        this.defend = defend;
        this.attackActionClassId = attackActionClassId;
        this.attackType = attackType;
        this.combatStyle = combatStyle;
        this.death = death;
        this.autocastSpellId = autocastSpellId;
        this.arrowTypeId = arrowTypeId;
        this.bowTypeId = bowTypeId;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the combatLevel
     */
    public int getCombatLevel() {
        return combatLevel;
    }

    /**
     * Gets the combat cooldown delay.
     * @return The combat cooldown delay.
     */
    public int getCombatCooldownDelay() {
        return combatCooldownDelay; // TODO implement
    }

    /**
     * Gets the skill levels.
     * @return The skill levels.
     */
    public int[] getSkills() {
        return this.skills;
    }

    /**
     * Gets a skill level by its index.
     * @return The skill level.
     */
    public int getSkill(int index) {
        return this.skills[index];
    }

    /**
     * Gets the bonuses.
     * @return The bonuses.
     */
    public int[] getBonuses() {
        return this.bonuses;
    }

    /**
     * Gets a bonus by its index.
     * @return The bonus.
     */
    public int getBonus(int index) {
        return this.bonuses[index];
    }

    /**
     * @return the attack
     */
    public Animation getAttack() {
        return attack;
    }

    /**
     * @return the defend
     */
    public Animation getDefend() {
        return defend;
    }

    /**
     * @return the death
     */
    public Animation getDeath() {
        return death;
    }

    /**
     * @param attackAction the attackAction to set
     */
    public void setCombatAction(CombatAction combatAction) {
        this.combatAction = combatAction;
    }

    /**
     * @return the attackAction
     */
    public CombatAction getCombatAction() {
        return combatAction;
    }

    /**
     * @return the attackActionClassName
     */
    public int getAttackActionClassId() {
        return attackActionClassId;
    }

    /**
     * @return the attackType
     */
    public AttackType getAttackType() {
        return attackType;
    }

    /**
     * @return the combatStyle
     */
    public CombatStyle getCombatStyle() {
        return combatStyle;
    }

    /**
     * @param autocastSpellId the autocastSpellId to set
     */
    public void setAutocastSpellId(int autocastSpellId) {
        this.autocastSpellId = autocastSpellId;
    }

    /**
     * @return the autocastSpellId
     */
    public int getAutocastSpellId() {
        return autocastSpellId;
    }

    /**
     * @return the arrowTypeId
     */
    public int getArrowTypeId() {
        return arrowTypeId;
    }

    /**
     * @return the bowTypeId
     */
    public int getBowTypeId() {
        return bowTypeId;
    }
}
