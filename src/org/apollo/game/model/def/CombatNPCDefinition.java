package org.apollo.game.model.def;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apollo.game.model.Animation;
import org.apollo.game.model.Graphic;
import org.apollo.game.model.NPCDrop;
import org.apollo.game.model.combat.CombatAction;
import org.apollo.game.model.combat.CombatState.AttackType;
import org.apollo.game.model.combat.CombatState.CombatStyle;
import org.apollo.game.model.combat.action.AbstractCombatAction;
import org.apollo.game.model.combat.action.MagicCombatAction;
import org.apollo.game.model.combat.action.MagicCombatAction.Spell;
import org.apollo.game.model.combat.action.MeleeCombatAction;
import org.apollo.game.model.combat.action.RangeCombatAction;
import org.apollo.util.Misc;
import org.apollo.util.XMLController;
import org.apollo.util.XStreamUtil;

/**
 * <p>Represents a type of combat NPC.</p>
 * 
 * @author Graham Edgecombe
 */
public class CombatNPCDefinition {

    /**
     * Logger instance.
     */
    private static final Logger logger = Logger.getLogger(CombatNPCDefinition.class.getName());
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
        return definitions.get(id);
    }

    @SuppressWarnings({ "unchecked", "null" })
    public static void initRSWikiDump() throws IOException {
        logger.info("Loading RSWiki Dump NPC combat definitions...");
        try {
            List<CombatNPCDefinition> list = (List<CombatNPCDefinition>) XStreamUtil.getXStream().fromXML(new FileInputStream("./data/rswikiNPCDefs.xml"));
            for (CombatNPCDefinition def : list) {
                if (def == null) {
                    definitions.put(def.getId(), new CombatNPCDefinition(1, 1, null, new int[]{}, false, new Animation(-1),
                            new Animation(-1), new Animation(-1), AttackType.CRUSH, CombatStyle.ACCURATE, Spell.BIND, new NPCDrop[]{},
                            new NPCDrop[]{}, 1, CombatActionType.MELEE));
                    continue;
                }
                //also this makes no sense.. setting the same..
                def.setAggressive(def.isAggressive());
                def.setPoisonous(def.isPoisonous());
                def.setImmuneToPoison(def.isImmuneToPoison());
                def.setCombatCooldownDelay(def.getCombatCooldownDelay());
                def.setMaxHit(def.getMaxHit());
            }
            logger.info("Loaded " + definitions.size() + " rswiki dumps.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the npc combat definitions.
     * @throws IOException if an I/O error occurs.
     * @throws IllegalStateException if the definitions have been loaded already.
     */
    public static void init() throws IOException {
        if (definitions != null) {
            throw new IllegalStateException("NPC definitions already loaded.");
        }
        logger.info("Loading combat NPC definitions...");
        try {
            /**
             * Load NPC definitions.
             */
            definitions = new HashMap<Integer, CombatNPCDefinition>();
            File file = new File("data/npcCombatDefinition.xml");
            if (file.exists()) {
                definitions = XMLController.readXML(file);
                for (int i : definitions.keySet()) {
                    CombatNPCDefinition def = definitions.get(i);
                    if (def == null) {
                        continue;
                    }
                    switch (def.getCombatActionType()) {
                        case MELEE:
                            def.setCombatAction(MeleeCombatAction.getAction());
                            break;
                        case RANGE:
                            def.setCombatAction(RangeCombatAction.getAction());
                            break;
                        case MAGE:
                            def.setCombatAction(MagicCombatAction.getAction());
                            break;
                        case CUSTOM:
                            File[] files = new File("build/classes/org/apollo/game/model/combat/action/npc").listFiles();
                            String packageName = "org.apollo.game.model.combat.action.npc.";
                            for (File f : files) {
				if (!f.getName().endsWith(".class")) {
                                    continue;
                                }
                                String fileName = packageName + f.getName().substring(0, f.getName().length() - 6);
                                String requiredName = packageName + Misc.formatName(NPCDefinition.forId(i).getName()).replace(" ", "");
                                System.out.println(fileName);
                                if (!fileName.equals(requiredName)) {
                                    continue;
                                }
                                Class<?> fileClass = Class.forName(fileName);
                                if (fileClass.getSuperclass() != AbstractCombatAction.class) {
                                    continue;
                                }
                                AbstractCombatAction combatAction = (AbstractCombatAction) fileClass.newInstance();
                                def.setCombatAction(combatAction);
                                System.out.println("Loading custom npc attacks: "+fileName+" with id: "+i);
                            }
                            break;
                    }
                }
                logger.info("Loaded " + definitions.size() + " combat NPC definitions.");
            } else {
                logger.info("NPC combat definitions not found!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//		Map<Integer, CombatNPCDefinition> defs = new HashMap<Integer, CombatNPCDefinition>(NPCDefinition.getDefinitions().length);
//		Map<Skill, Integer> skills = new HashMap<Skill, Integer>();
//		skills.put(Skill.ATTACK, 25);
//		skills.put(Skill.DEFENCE, 25);
//		skills.put(Skill.HITPOINTS, 53);
//		defs.put(19, new CombatNPCDefinition(36, 6, 6, skills, new int[] { -4, 27, 21, -64, -20, 81, 82, 70, -11, 71, 26, 6, 0 }, false, Animation.create(7042), Animation.create(7050), Animation.create(836), AttackType.SLASH, CombatStyle.AGGRESSIVE_1, Spell.WIND_STRIKE, -1, -1, new NPCDrop[] { new NPCDrop(1, new Item(526)) }, new NPCDrop[0], 50, CombatActionType.CUSTOM));
//		XMLController.writeXML(defs, new File("data/newcombat.xml"));
    }
    /**
     * The npc's id.
     */
    private int id;
    /**
     * The npc's max hit.
     */
    private int maxHit;
    /**
     * Is this npc aggressive.
     */
    private boolean aggressive = false;
    /**
     * Is this npc poisonous.
     */
    private boolean poisonous = false;
    /**
     * Is this npc immune to poison.
     */
    private boolean immuneToPoison = false;
    /**
     * The autocast spell id, if the attack style is magic.
     */
    private Spell spell;
    /**
     * The combat cooldown delay.
     */
    private int combatCooldownDelay;
    /**
     * The drawback graphic, for range attacks.
     */
    private Graphic drawbackGraphic;
    /**
     * The projectile id, for range attacks.
     */
    private int projectileId;
    /**
     * The amount of ticks before this npc respawns.
     */
    private int respawnTicks;
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
     * The attack type.
     */
    private AttackType attackType;
    /**
     * The combat style.
     */
    private CombatStyle combatStyle;
    /**
     * The combat action.
     */
    private CombatAction combatAction;
    /**
     * The combat action type.
     */
    private CombatActionType combatActionType;
    /**
     * The npc's skill levels.
     */
    private Map<Skill, Integer> skills;
    /**
     * The npc's bonuses.
     */
    private int[] bonuses = new int[13];
    /**
     * The list of items this npc drops.
     */
    private NPCDrop[] constantDrops;
    /**
     * The list of items this npc drops.
     */
    private NPCDrop[] randomDrops;
    /**
     * The godwars team this npc is on.
     */
    private GodWarsMinion godWarsTeam;

    /**
     * Creates the definition.
     * @param id The id.
     */
    private CombatNPCDefinition(int maxHit, int combatCooldownDelay, Map<Skill, Integer> skills, int[] bonuses, boolean aggressive, Animation attack, Animation defend, Animation death, AttackType attackType, CombatStyle combatStyle, Spell spell, NPCDrop[] constantDrops, NPCDrop[] randomDrops, int respawnTicks, CombatActionType combatActionType) {
        this.combatCooldownDelay = combatCooldownDelay;
        this.maxHit = maxHit;
        this.skills = skills;
        this.bonuses = bonuses;
        this.aggressive = aggressive;
        this.attack = attack;
        this.defend = defend;
        this.attackType = attackType;
        this.combatStyle = combatStyle;
        this.death = death;
        this.spell = spell;
        this.constantDrops = constantDrops;
        this.randomDrops = randomDrops;
        this.respawnTicks = respawnTicks;
        this.combatActionType = combatActionType;
    }

    /**
     * The combat action enum.
     * @author Michael
     */
    private enum CombatActionType {

        MELEE,
        RANGE,
        MAGE,
        CUSTOM
    }

    /**
     * The skill statistic enum.
     * @author Michael
     */
    public enum Skill {

        ATTACK(org.apollo.game.model.Skill.ATTACK),
        DEFENCE(org.apollo.game.model.Skill.DEFENCE),
        HITPOINTS(org.apollo.game.model.Skill.HITPOINTS),
        RANGE(org.apollo.game.model.Skill.RANGED),
        MAGIC(org.apollo.game.model.Skill.MAGIC);
        /**
         * The list of skills.
         */
        private static Map<Integer, Skill> skills = new HashMap<Integer, Skill>();

        public static Skill skillForId(int skill) {
            return skills.get(skill);
        }

        /**
         * Populates the skill list.
         */
        static {
            for (Skill skill : Skill.values()) {
                skills.put(skill.getId(), skill);
            }
        }
        /**
         * The id of the skill.
         */
        private int id;

        private Skill(int id) {
            this.id = id;
        }

        /**
         * @return the id
         */
        public int getId() {
            return id;
        }
    }

    /**
     * @return the maxHit
     */
    public int getMaxHit() {
        return maxHit;
    }

    /**
     * Gets the combat cooldown delay.
     * @return The combat cooldown delay.
     */
    public int getCombatCooldownDelay() {
        return combatCooldownDelay;
    }

    /**
     * @return the skills
     */
    public Map<Skill, Integer> getSkills() {
        return skills;
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
     * @return the combatActionType
     */
    public CombatActionType getCombatActionType() {
        return combatActionType;
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
     * @return the aggressive
     */
    public boolean isAggressive() {
        return aggressive;
    }

    /**
     * @return the spell
     */
    public Spell getSpell() {
        return spell;
    }

    /**
     * @return the constantDrops
     */
    public NPCDrop[] getConstantDrops() {
        return constantDrops;
    }

    /**
     * @return the randomDrops
     */
    public NPCDrop[] getRandomDrops() {
        return randomDrops;
    }

    /**
     * @return the respawnTicks
     */
    public int getRespawnTicks() {
        return respawnTicks;
    }

    /**
     * @param spell the spell to set
     */
    public void setSpell(Spell spell) {
        this.spell = spell;
    }

    public GodWarsMinion getGodWarsTeam() {
        return godWarsTeam;
    }

    /**
     * @return the drawbackGraphic
     */
    public Graphic getDrawbackGraphic() {
        return drawbackGraphic;
    }

    /**
     * @return the projectileId
     */
    public int getProjectileId() {
        return projectileId;
    }

    public void setAggressive(boolean aggressive) {
        this.aggressive = aggressive;
    }

    public void setCombatCooldownDelay(int combatCooldownDelay) {
        this.combatCooldownDelay = combatCooldownDelay;
    }

    public void setMaxHit(int maxHit) {
        this.maxHit = maxHit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isImmuneToPoison() {
        return immuneToPoison;
    }

    public void setImmuneToPoison(boolean immuneToPoison) {
        this.immuneToPoison = immuneToPoison;
    }

    public boolean isPoisonous() {
        return poisonous;
    }

    public void setPoisonous(boolean poisonous) {
        this.poisonous = poisonous;
    }

    public enum GodWarsMinion {

        SARADOMIN,
        ZAMORAK,
        BANDOS,
        ARMADYL
    }
}