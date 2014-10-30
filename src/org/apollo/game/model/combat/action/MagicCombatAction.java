package org.apollo.game.model.combat.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apollo.game.GameConstants;
import org.apollo.game.event.impl.DamageEvent;
import org.apollo.game.event.impl.SendConfigEvent;
import org.apollo.game.model.BoundaryManager;
import org.apollo.game.model.Character;
import org.apollo.game.model.Graphic;
import org.apollo.game.model.Item;
import org.apollo.game.model.Position;
import org.apollo.game.model.Prayers;
import org.apollo.game.model.Skill;
import org.apollo.game.model.World;
import org.apollo.game.model.combat.CombatAction;
import org.apollo.game.model.combat.CombatFormulae;
import org.apollo.game.model.combat.CombatState.AttackType;
import org.apollo.game.model.combat.CombatState.CombatStyle;
import org.apollo.game.model.def.ItemDefinition;
import org.apollo.game.model.def.PoisonType;
import org.apollo.game.scheduling.ScheduledTask;
import org.apollo.game.scheduling.impl.SkillNormalizationTask;
import org.apollo.util.NameUtil;

/**
 * Normal magic combat action.
 * 
 * Credits to rs2-server team for formulas and a guideline
 * as how to structure it
 * @author The Wanderer
 */
public class MagicCombatAction extends AbstractCombatAction {

    /**
     * The singleton instance.
     */
    private static final MagicCombatAction INSTANCE = new MagicCombatAction();

    /**
     * Gets the singleton instance.
     * @return The singleton instance.
     */
    public static CombatAction getAction() {
        return INSTANCE;
    }
    /**
     * The random number generator.
     */
    private final Random random = new Random();

    /**
     * Default private constructor.
     */
    private MagicCombatAction() {
    }

    @Override
    public boolean canHit(Character attacker, Character victim) {
        if (!super.canHit(attacker, victim)) {
            return false;
        }
        if (attacker.getAutocastSpell() != null) {
            attacker.getCombatState().setQueuedSpell(attacker.getAutocastSpell());
        }
        return canCastSpell(attacker, victim, attacker.getCombatState().getQueuedSpell());
    }

    @Override
    public void hit(final Character attacker, final Character victim) {
        super.hit(attacker, victim);

        attacker.getCombatState().setCurrentSpell(attacker.getCombatState().getQueuedSpell());
        attacker.getCombatState().setQueuedSpell(null);

        Spell spell = attacker.getCombatState().getCurrentSpell();
        int clientSpeed;
        int gfxDelay;
        if (attacker.getPosition().isWithinDistance(victim.getPosition(), 1)) {
            clientSpeed = 80;
            gfxDelay = 80;
        } else if (attacker.getPosition().isWithinDistance(victim.getPosition(), 5)) {
            clientSpeed = 95;
            gfxDelay = 80;
        } else if (attacker.getPosition().isWithinDistance(victim.getPosition(), 8)) {
            clientSpeed = 105;
            gfxDelay = 120;
        } else {
            clientSpeed = 125;
            gfxDelay = 120;
        }
        int delay = (gfxDelay / 20) - 1;
        
        executeSpell(spell, attacker, attacker, victim, spell, attacker.getAutocastSpell() != null, clientSpeed, INSTANCE, gfxDelay, delay);
    }

    @Override
    public void defend(Character attacker, Character victim) {
        super.defend(attacker, victim);
    }

    @Override
    public boolean special(Character attacker, Character victim) {
        return super.special(attacker, victim);
    }

    @Override
    public int distance(Character attacker) {
        return 10;
    }

    @Override
    public int damage(int maxHit, Character attacker, Character victim, AttackType attackType, int skill, int prayer) {
        double attackCalc = (attacker.getBonus(attackType.getId()) == 0 ? 1 : attacker.getBonus(attackType.getId())) * attacker.getSkillSet().getSkill(skill).getCurrentLevel(); // +1 as its exclusive

        double defenceCalc = (victim.getBonus(attackType.getId() + 5) == 0 ? 1 : victim.getBonus(attackType.getId() + 5)) * victim.getSkillSet().getSkill(Skill.DEFENCE).getCurrentLevel(); // +1 as its exclusive

        /**
         * Prayer calculations.
         */
        if (victim.getCombatState().getPrayer(Prayers.THICK_SKIN)) {
            defenceCalc *= 1.05;
        } else if (victim.getCombatState().getPrayer(Prayers.ROCK_SKIN)) {
            defenceCalc *= 1.10;
        } else if (victim.getCombatState().getPrayer(Prayers.STEEL_SKIN)) {
            defenceCalc *= 1.15;
        }

        /**
         * As with the melee/range max hit calcs, combat style bonuses are added AFTER the modifiers have taken place.
         */
        if (victim.getCombatState().getCombatStyle() == CombatStyle.DEFENSIVE) {
            defenceCalc += 3;
        } else if (victim.getCombatState().getCombatStyle() == CombatStyle.CONTROLLED_1
                || victim.getCombatState().getCombatStyle() == CombatStyle.CONTROLLED_2
                || victim.getCombatState().getCombatStyle() == CombatStyle.CONTROLLED_3) {
            defenceCalc += 1;
        }

        /**
         * The chance to succeed out of 1.0.
         */
        double hitSucceed = 0.515 * (attackCalc / defenceCalc);
        if (hitSucceed > 1.0) {
            hitSucceed = 1;
        }
        if (hitSucceed < random.nextDouble()) {
            return -1; //NOTE: For magic, we return -1. This is because if a spell has a max hit of 0, its damage will always return 0, saying that it has failed, when it hasn't!
        } else {
            /**
             * Protection prayers.
             * Note: If an NPC is hitting on a protection prayer, it is 100% blocked, where as if a player is hitting on  a protection prayer, their damage is simply reduced by 40%.
             */
            int hit = (int) (maxHit * (victim.getCombatState().getPrayer(prayer) ? attacker.getProtectionPrayerModifier() : 1)); // +1 as its exclusive
            if (hit > victim.getSkillSet().getSkill(Skill.HITPOINTS).getCurrentLevel()) {
                hit = victim.getSkillSet().getSkill(Skill.HITPOINTS).getCurrentLevel();
            }
            return hit;
        }
    }

    @Override
    public void addExperience(Character attacker, int damage) {
        super.addExperience(attacker, damage);
        attacker.getSkillSet().addExperience(Skill.MAGIC, (2 * damage) * GameConstants.EXP_MODIFIER);
    }

    @Override
    public void recoil(Character attacker, Character victim, int damage) {
        super.recoil(attacker, victim, damage);
    }

    @Override
    public void smite(Character attacker, Character victim, int damage) {
        super.smite(attacker, victim, damage);
    }

    public static void executeSpell(Spell spell, Character character, Object... args) {
        if (spell.getSpellName().contains("Teleport")) {
            if (!character.canTeleport()) {
                return;
            }
        }
        if (!checkRunes(character, spell)) {
            return;
        }
        /*if (ScriptManager.getScriptManager().invokeWithFailTest(spell.getSpellName(), args)) {
            for (int i = 0; i < spell.getRunes().length; i++) {
                if (character.getInventory() != null && spell.getRune(i) != null) {
                    if (deleteRune(character, spell.getRune(i))) {
                        if (character.getInventory() != null) {
                            character.getInventory().remove(spell.getRune(i));
                        }
                    }
                }
            }
        } else if (character.getEventManager() != null) {
            character.sendMessage("Spell script missing: " + spell.getSpellName() + ".");
        }*/
    }

    public boolean canCastSpell(Character attacker, Character victim, Spell spell) {
        if (attacker.getSkillSet().getSkill(Skill.MAGIC).getCurrentLevel() < spell.getLevelRequired()) {
            attacker.sendMessage("Your Magic level is not high enough for this spell.");
            return false;
        }
        if (!attacker.spellbookIgnore()) {
            switch (spell.getSpellBook()) {
                case MODERN_MAGICS:
                    if (attacker.getCombatState().getSpellBook() == SpellBook.ANCIENT_MAGICKS.getSpellBookId()) {
                        attacker.sendMessage("You can only cast modern spells whilst on the modern spellbook.");
                        return false;
                    }
                    break;
                case ANCIENT_MAGICKS:
                    if (attacker.getCombatState().getSpellBook() == SpellBook.MODERN_MAGICS.getSpellBookId()) {
                        attacker.sendMessage("You can only cast ancient spells whilst on the ancient spellbook.");
                        return false;
                    }
                    break;
            }
        }
        if (!checkRunes(attacker, spell)) {
            return false;
        }
        /**
         * Extra items required for casting.
         */
        if (spell.getRequiredItem() != null) {
            if (attacker.getEquipment() != null && attacker.getEquipment().getCount(spell.getRequiredItem().getId()) < spell.getRequiredItem().getAmount()) {
                attacker.sendMessage("You must be wielding a " + ItemDefinition.forId(spell.getRequiredItem().getId()).getName() + " to cast this spell.");
                return false;
            }
        }
        switch (spell) {
            case CONFUSE:
            case STUN:
                if (victim.isAttackDrained()) {
                    attacker.sendMessage("Your foe's attack has already been weakened.");
                    return false;
                }
                break;
            case WEAKEN:
            case ENFEEBLE:
                if (victim.isStrengthDrained()) {
                    attacker.sendMessage("Your foe's strength has already been weakened.");
                    return false;
                }
                break;
            case CURSE:
            case VULNERABILITY:
                if (victim.isDefenceDrained()) {
                    attacker.sendMessage("Your foe's defence has already been weakened.");
                    return false;
                }
                break;
            case CRUMBLE_UNDEAD:
                if (!victim.getDefinedName().toLowerCase().contains("skeleton") && !victim.getDefinedName().toLowerCase().contains("zombie") && !victim.getDefinedName().toLowerCase().contains("shade") && !victim.getDefinedName().toLowerCase().contains("ghost")) {
                    if (attacker.getEventManager() != null) {
                        attacker.sendMessage("This spell only affects skeletons, zombies, ghosts and shades.");
                    }
                    return false;
                }
                break;
            case TELE_BLOCK:
                if (!victim.canBeTeleblocked()) {
                    if (attacker.getEventManager() != null) {
                        attacker.sendMessage("Nothing interesting happens.");
                    }
                    return false;
                } else if (victim.getCombatState().isTeleblocked()) {
                    if (attacker.getEventManager() != null) {
                        attacker.sendMessage("That player is already teleblocked.");
                    }
                    return false;
                }
                break;
        }
        return true;
    }

    public void hitEnemy(final Character attacker, Character victim, final Spell spell, final Graphic graphic, final PoisonType poisonType, boolean multi, int maxDamage, int delay, int freezeTimer) {
        if (!BoundaryManager.isWithinBoundary(attacker.getPosition(), "MultiCombat")) {
            multi = false;
        }
        final ArrayList<Character> enemies = new ArrayList<Character>();
        ArrayList<Position> locationsUsed = new ArrayList<Position>();
        enemies.add(victim);
        locationsUsed.add(victim.getPosition());
        if (multi) {
            for (Character character : World.getWorld().getRegionRepository().getLocalCharacters(victim)) {
                if (character != attacker && character != victim && character.getPosition().isWithinDistance(victim.getPosition(), 1)
                        && !locationsUsed.contains(character.getPosition()) && super.canHit(attacker, character)
                        && !BoundaryManager.isWithinBoundary(character.getPosition(), "MultiCombat")) {
                    enemies.add(character);
                    locationsUsed.add(character.getPosition());
                }
            }
        }
        final ArrayList<Integer> hits = new ArrayList<Integer>();
        for (int i = 0; i < enemies.size(); i++) {
            final Character enemy = enemies.get(i);
            int hit = damage(maxDamage, attacker, enemy, AttackType.MAGIC, Skill.MAGIC, Prayers.PROTECT_FROM_MAGIC); // +1 as its exclusive
            if (graphic != null) {
                enemy.playGraphic(hit < 0 ? Graphic.create(85, graphic.getDelay(), 100) : graphic);
            }
            if (hit > -1) {
                hit = (hit > 0 ? random.nextInt(hit) : 0) + (maxDamage > 0 ? 1 : 0); //1 because we started at -1
                if (hit > 0) {

                    if (hit > enemy.getSkillSet().getSkill(Skill.HITPOINTS).getCurrentLevel()) {
                        hit = enemy.getSkillSet().getSkill(Skill.HITPOINTS).getCurrentLevel();
                    }

                    if (CombatFormulae.fullAhrim(attacker)) {
                        int ahrim = random.nextInt(8);
                        if (ahrim == 3) {
                            Skill skill = victim.getSkillSet().getSkill(Skill.STRENGTH);
                            int current = skill.getCurrentLevel() - 5 >= 0 ? skill.getCurrentLevel() - 5 : 0;
                            victim.getSkillSet().setSkill(Skill.STRENGTH, new Skill(skill.getExperience(), current, skill.getMaximumLevel()));
                            victim.playGraphic(Graphic.create(400, 0, 100));
                        }
                    }

                    //freezing happens immediately
                    if (freezeTimer > 0) {
                        if (enemy.getCombatState().canMove() && enemy.getCombatState().canBeFrozen()) {
                            enemy.getCombatState().setCanMove(false);
                            enemy.getCombatState().setCanBeFrozen(false);
                            enemy.getWalkingQueue().clear();
                            if (enemy.getEventManager() != null) {
                                enemy.sendMessage("You have been frozen!");
                            }
                            World.getWorld().schedule(new ScheduledTask(freezeTimer + delay, false) {

                                @Override
                                public void execute() {
                                    enemy.getCombatState().setCanMove(true);
                                    this.stop();
                                }
                            });
                            World.getWorld().schedule(new ScheduledTask(freezeTimer + delay + 5, false) {

                                @Override
                                public void execute() {
                                    enemy.getCombatState().setCanBeFrozen(true);
                                    this.stop();
                                }
                            });
                        }
                    }
                }
            }
            addExperience(attacker, hit < 0 ? 0 : hit);
            hits.add(i, hit);
        }
        World.getWorld().schedule(new ScheduledTask(delay, false) {

            public void execute() {
                for (int i = 0; i < enemies.size(); i++) {
                    final Character enemy = enemies.get(i);
                    int hit = hits.get(i);

                    if (hit > enemy.getSkillSet().getSkill(Skill.HITPOINTS).getCurrentLevel()) {//check again for hp, as it could of changed between the time of delay
                        hit = enemy.getSkillSet().getSkill(Skill.HITPOINTS).getCurrentLevel();
                    }

                    if (hit > 0) {
                        //only inflict damage if the hit is > 0
                        enemy.inflictDamage(new DamageEvent(hit, enemy.getHP(), enemy.getMaxHP()), attacker);
                        smite(attacker, enemy, hit);
                        recoil(attacker, enemy, hit);
                    }

                    enemy.getActiveCombatAction().defend(attacker, enemy);
                    if (poisonType != PoisonType.NONE) {
                        if (enemy.getCombatState().getPoisonDamage() < 1 && enemy.getCombatState().canBePoisoned() && random.nextInt(11) == 3) {
                            enemy.getCombatState().setPoisonDamage(poisonType.getRangeDamage(), attacker);
                            if (enemy.getEventManager() != null) {
                                enemy.sendMessage("You have been poisoned!");
                            }
                        }
                    }
                    int drainedLevel = -1;
                    double drainModifier = 0;
                    switch (spell) {
                        case SHADOW_RUSH:
                        case SHADOW_BURST:
                            Skill skill = enemy.getSkillSet().getSkill(Skill.ATTACK);
                            int current = skill.getMaximumLevel() - (int) (enemy.getSkillSet().getSkill(Skill.ATTACK).getMaximumLevel() * 0.10);
                            enemy.getSkillSet().setSkill(Skill.ATTACK, new Skill(skill.getExperience(), current, skill.getMaximumLevel()));
                            break;
                        case SHADOW_BLITZ:
                        case SHADOW_BARRAGE:
                            Skill skills = enemy.getSkillSet().getSkill(Skill.ATTACK);
                            int currents = skills.getMaximumLevel() - (int) (enemy.getSkillSet().getSkill(Skill.ATTACK).getMaximumLevel() * 0.15);
                            enemy.getSkillSet().setSkill(Skill.ATTACK, new Skill(skills.getExperience(), currents, skills.getMaximumLevel()));
                            break;
                        case BLOOD_RUSH:
                        case BLOOD_BURST:
                        case BLOOD_BLITZ:
                        case BLOOD_BARRAGE:
                            int heal = (int) (hit * 0.25);
                            Skill newSkill = attacker.getSkillSet().getSkill(Skill.HITPOINTS);
                            int newCurrent = newSkill.getCurrentLevel() + heal < newSkill.getMaximumLevel() ? newSkill.getCurrentLevel() + heal : newSkill.getMaximumLevel();
                            attacker.getSkillSet().setSkill(Skill.HITPOINTS, new Skill(newSkill.getExperience(), newCurrent, newSkill.getMaximumLevel()));
                            if (attacker.getEventManager() != null) {
                                attacker.sendMessage("You drain some of your opponent's health.");
                            }
                            break;
                        case TELE_BLOCK:
                            enemy.getCombatState().setTeleblocked(true);
                            World.getWorld().schedule(new ScheduledTask(500, false) {

                                @Override
                                public void execute() {
                                    enemy.getCombatState().setTeleblocked(false);
                                    this.stop();
                                }
                            });
                            break;
                        case SARADOMIN_STRIKE:
                            enemy.getSkillSet().decreaseLevel(Skill.PRAYER, 1);
                            break;
                        case CLAWS_OF_GUTHIX:
                            Skill newSkills = enemy.getSkillSet().getSkill(Skill.DEFENCE);
                            int newCurrents = newSkills.getMaximumLevel() - (int) (enemy.getSkillSet().getSkill(Skill.DEFENCE).getMaximumLevel() * 0.5);
                            enemy.getSkillSet().setSkill(Skill.DEFENCE, new Skill(newSkills.getExperience(), newCurrents, newSkills.getMaximumLevel()));
                            break;
                        case FLAMES_OF_ZAMORAK:
                            Skill newerSkill = enemy.getSkillSet().getSkill(Skill.MAGIC);
                            int newerCurrent = newerSkill.getMaximumLevel() - 5;
                            enemy.getSkillSet().setSkill(Skill.MAGIC, new Skill(newerSkill.getExperience(), newerCurrent, newerSkill.getMaximumLevel()));
                            break;
                        case CONFUSE:
                            drainedLevel = Skill.ATTACK;
                            drainModifier = 0.05;
                            enemy.setAttackDrained(true);
                            break;
                        case WEAKEN:
                            drainedLevel = Skill.STRENGTH;
                            drainModifier = 0.05;
                            enemy.setStrengthDrained(true);
                            break;
                        case CURSE:
                            drainedLevel = Skill.DEFENCE;
                            drainModifier = 0.05;
                            enemy.setDefenceDrained(true);
                            break;
                        case STUN:
                            drainedLevel = Skill.ATTACK;
                            drainModifier = 0.10;
                            enemy.setAttackDrained(true);
                            break;
                        case ENFEEBLE:
                            drainedLevel = Skill.STRENGTH;
                            drainModifier = 0.10;
                            enemy.setStrengthDrained(true);
                            break;
                        case VULNERABILITY:
                            drainedLevel = Skill.DEFENCE;
                            drainModifier = 0.10;
                            enemy.setDefenceDrained(true);
                            break;
                    }
                    if (drainedLevel != -1 && drainModifier != 0) {
                        int levelBefore = enemy.getSkillSet().getSkill(drainedLevel).getCurrentLevel();
                        Skill skill = enemy.getSkillSet().getSkill(Skill.MAGIC);
                        int current = skill.getMaximumLevel() - (int) (drainModifier * enemy.getSkillSet().getSkill(drainedLevel).getMaximumLevel());
                        enemy.getSkillSet().setSkill(Skill.MAGIC, new Skill(skill.getExperience(), current, skill.getMaximumLevel()));
                        int levelDifference = levelBefore - enemy.getSkillSet().getSkill(drainedLevel).getCurrentLevel();
                        if (levelDifference < 1) {
                            levelDifference = 1;
                        }
                        World.getWorld().schedule(new ScheduledTask(levelDifference * SkillNormalizationTask.CYCLE_AMOUNT, false) {

                            @Override
                            public void execute() {
                                switch (spell) {
                                    case CONFUSE:
                                    case STUN:
                                        enemy.setAttackDrained(false);
                                        break;
                                    case WEAKEN:
                                    case ENFEEBLE:
                                        enemy.setStrengthDrained(false);
                                        break;
                                    case CURSE:
                                    case VULNERABILITY:
                                        enemy.setDefenceDrained(false);
                                        break;
                                }
                            }
                        });
                    }
                }
                attacker.getCombatState().setCurrentSpell(null);
                this.stop();
            }
        });
    }

    private static boolean checkRunes(Character character, Spell spell) {
        for (int i = 0; i < spell.getRunes().length; i++) {
            if (spell.getRune(i) != null) {
                if (deleteRune(character, spell.getRune(i))) {
                    if (character.getInventory() != null && character.getInventory().getCount(spell.getRune(i).getId()) < spell.getRune(i).getAmount()) {
                        String runeName = NameUtil.formatName(ItemDefinition.forId(spell.getRune(i).getId()).getName().toLowerCase());
                        if (character.getEventManager() != null) {
                            character.sendMessage("You do not have enough " + runeName + "s to cast this spell.");
                        }
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static void setAutocast(Character character, Spell spell, String spellName) {
        if (spell == null && character.getAutocastSpell() != null) {//we don't want to set our atk style back to accurate if we aren't already autocasting
            if (character.getEventManager() != null) {
                character.send(new SendConfigEvent(108, 0));
                character.getCombatState().setAttackType(AttackType.CRUSH);
                character.getCombatState().setCombatStyle(CombatStyle.ACCURATE);
                character.send(new SendConfigEvent(43, 0));
            }
        } else if (spell != null) {
            character.getCombatState().setCombatStyle(CombatStyle.AUTOCAST);
            if (character.getEventManager() != null) {
                character.send(new SendConfigEvent(108, 3));
                character.send(new SendConfigEvent(43, 3));
            }
        }
        character.setAutocastSpell(spell);
        character.getEventManager().sendString(352, spellName.length() > 0 ? spellName : "(none set)");
    }
    public static final int FIRE_RUNE = 554,
            WATER_RUNE = 555, AIR_RUNE = 556,
            EARTH_RUNE = 557, MIND_RUNE = 558,
            BODY_RUNE = 559, DEATH_RUNE = 560,
            NATURE_RUNE = 561, CHAOS_RUNE = 562,
            LAW_RUNE = 563, COSMIC_RUNE = 564,
            BLOOD_RUNE = 565, SOUL_RUNE = 566;

    /**
     * Represents spells.
     * @author Michael Bull
     *
     */
    public static enum Spell {

        /**
         * Wind Strike.
         */
        WIND_STRIKE(1152, 1, SpellBook.MODERN_MAGICS, new Item[]{new Item(AIR_RUNE, 1), new Item(MIND_RUNE, 1)}, "windStrike", null),
        /**
         * Water Strike.
         */
        WATER_STRIKE(1154, 5, SpellBook.MODERN_MAGICS, new Item[]{new Item(WATER_RUNE, 1), new Item(AIR_RUNE, 1), new Item(MIND_RUNE, 1)}, "waterStrike", null),
        /**
         * Earth Strike.
         */
        EARTH_STRIKE(1156, 9, SpellBook.MODERN_MAGICS, new Item[]{new Item(EARTH_RUNE, 2), new Item(AIR_RUNE, 1), new Item(MIND_RUNE, 1)}, "earthStrike", null),
        /**
         * Fire Strike.
         */
        FIRE_STRIKE(1158, 13, SpellBook.MODERN_MAGICS, new Item[]{new Item(FIRE_RUNE, 3), new Item(AIR_RUNE, 2), new Item(MIND_RUNE, 1)}, "fireStrike", null),
        /**
         * Wind Bolt.
         */
        WIND_BOLT(1160, 17, SpellBook.MODERN_MAGICS, new Item[]{new Item(AIR_RUNE, 2), new Item(CHAOS_RUNE, 1)}, "windBolt", null),
        /**
         * Water Bolt.
         */
        WATER_BOLT(1163, 23, SpellBook.MODERN_MAGICS, new Item[]{new Item(AIR_RUNE, 2), new Item(WATER_RUNE, 2), new Item(CHAOS_RUNE, 1)}, "waterBolt", null),
        /**
         * Earth Bolt.
         */
        EARTH_BOLT(1166, 29, SpellBook.MODERN_MAGICS, new Item[]{new Item(AIR_RUNE, 2), new Item(EARTH_RUNE, 3), new Item(CHAOS_RUNE, 1)}, "earthBolt", null),
        /**
         * Fire Bolt.
         */
        FIRE_BOLT(1169, 35, SpellBook.MODERN_MAGICS, new Item[]{new Item(AIR_RUNE, 3), new Item(WATER_RUNE, 5), new Item(CHAOS_RUNE, 1)}, "fireBolt", null),
        /**
         * Wind Blast.
         */
        WIND_BLAST(1172, 41, SpellBook.MODERN_MAGICS, new Item[]{new Item(AIR_RUNE, 3), new Item(DEATH_RUNE, 1)}, "windBlast", null),
        /**
         * Water Blast.
         */
        WATER_BLAST(1175, 47, SpellBook.MODERN_MAGICS, new Item[]{new Item(AIR_RUNE, 3), new Item(WATER_RUNE, 3), new Item(DEATH_RUNE, 1)}, "waterBlast", null),
        /**
         * Earth Blast.
         */
        EARTH_BLAST(1177, 53, SpellBook.MODERN_MAGICS, new Item[]{new Item(AIR_RUNE, 3), new Item(EARTH_RUNE, 4), new Item(DEATH_RUNE, 1)}, "earthBlast", null),
        /**
         * Fire Blast.
         */
        FIRE_BLAST(1181, 59, SpellBook.MODERN_MAGICS, new Item[]{new Item(AIR_RUNE, 4), new Item(FIRE_RUNE, 5), new Item(DEATH_RUNE, 1)}, "fireBlast", null),
        /**
         * Wind Wave.
         */
        WIND_WAVE(1183, 62, SpellBook.MODERN_MAGICS, new Item[]{new Item(AIR_RUNE, 5), new Item(BLOOD_RUNE, 1)}, "windWave", null),
        /**
         * Water Wave.
         */
        WATER_WAVE(1185, 65, SpellBook.MODERN_MAGICS, new Item[]{new Item(AIR_RUNE, 5), new Item(WATER_RUNE, 7), new Item(BLOOD_RUNE, 1)}, "waterWave", null),
        /**
         * Earth Wave.
         */
        EARTH_WAVE(1188, 70, SpellBook.MODERN_MAGICS, new Item[]{new Item(AIR_RUNE, 5), new Item(EARTH_RUNE, 7), new Item(BLOOD_RUNE, 1)}, "earthWave", null),
        /**
         * Fire Wave.
         */
        FIRE_WAVE(1189, 75, SpellBook.MODERN_MAGICS, new Item[]{new Item(AIR_RUNE, 5), new Item(FIRE_RUNE, 7), new Item(BLOOD_RUNE, 1)}, "fireWave", null),
        /**
         * Saradomin Strike.
         */
        SARADOMIN_STRIKE(1190, 60, SpellBook.MODERN_MAGICS, new Item[]{new Item(FIRE_RUNE, 2), new Item(BLOOD_RUNE, 2), new Item(AIR_RUNE, 4)}, "saradominStrike", null),
        /**
         * Claws of Guthix.
         */
        CLAWS_OF_GUTHIX(1191, 60, SpellBook.MODERN_MAGICS, new Item[]{new Item(FIRE_RUNE, 1), new Item(BLOOD_RUNE, 2), new Item(AIR_RUNE, 4)}, "clawsOfGuthix", null),
        /**
         * Flames of Zamorak.
         */
        FLAMES_OF_ZAMORAK(1192, 60, SpellBook.MODERN_MAGICS, new Item[]{new Item(FIRE_RUNE, 4), new Item(BLOOD_RUNE, 2), new Item(AIR_RUNE, 1)}, "flamesOfZamorak", null),
        /**
         * Iban Blast.
         */
        IBAN_BLAST(1539, 50, SpellBook.MODERN_MAGICS, new Item[]{new Item(FIRE_RUNE, 5), new Item(DEATH_RUNE, 1)}, "ibanBlast", null),
        /**
         * Magic Dart.
         */
        MAGIC_DART(12037, 50, SpellBook.MODERN_MAGICS, new Item[]{new Item(MIND_RUNE, 4), new Item(DEATH_RUNE, 1)}, "magicDart", null),
        /**
         * Crumble Undead.
         */
        CRUMBLE_UNDEAD(1171, 39, SpellBook.MODERN_MAGICS, new Item[]{new Item(EARTH_RUNE, 2), new Item(AIR_RUNE, 2), new Item(CHAOS_RUNE, 1)}, "crumbleUndead", null),
        /**
         * Smoke Rush.
         */
        SMOKE_RUSH(12939, 50, SpellBook.ANCIENT_MAGICKS, new Item[]{new Item(DEATH_RUNE, 2), new Item(CHAOS_RUNE, 2), new Item(FIRE_RUNE, 1)}, "smokeRush", null),
        /**
         * Shadow Rush.
         */
        SHADOW_RUSH(12987, 52, SpellBook.ANCIENT_MAGICKS, new Item[]{new Item(DEATH_RUNE, 2), new Item(CHAOS_RUNE, 2), new Item(SOUL_RUNE, 1)}, "shadowRush", null),
        /**
         * Blood Rush.
         */
        BLOOD_RUSH(12901, 56, SpellBook.ANCIENT_MAGICKS, new Item[]{new Item(DEATH_RUNE, 2), new Item(CHAOS_RUNE, 2), new Item(BLOOD_RUNE, 1)}, "bloodRush", null),
        /**
         * Ice Rush.
         */
        ICE_RUSH(12861, 58, SpellBook.ANCIENT_MAGICKS, new Item[]{new Item(DEATH_RUNE, 2), new Item(CHAOS_RUNE, 2), new Item(WATER_RUNE, 2)}, "iceRush", null),
        /**
         * Smoke Burst.
         */
        SMOKE_BURST(12963, 62, SpellBook.ANCIENT_MAGICKS, new Item[]{new Item(DEATH_RUNE, 2), new Item(CHAOS_RUNE, 4), new Item(AIR_RUNE, 2)}, "smokeBurst", null),
        /**
         * Shadow Burst.
         */
        SHADOW_BURST(13011, 64, SpellBook.ANCIENT_MAGICKS, new Item[]{new Item(DEATH_RUNE, 2), new Item(CHAOS_RUNE, 4), new Item(AIR_RUNE, 2), new Item(AIR_RUNE, 2)}, "shadowBurst", null),
        /**
         * Blood Burst.
         */
        BLOOD_BURST(12919, 68, SpellBook.ANCIENT_MAGICKS, new Item[]{new Item(DEATH_RUNE, 2), new Item(CHAOS_RUNE, 4), new Item(BLOOD_RUNE, 2)}, "bloodBurst", null),
        /**
         * Ice Burst.
         */
        ICE_BURST(12881, 70, SpellBook.ANCIENT_MAGICKS, new Item[]{new Item(DEATH_RUNE, 2), new Item(CHAOS_RUNE, 4), new Item(WATER_RUNE, 2)}, "iceBurst", null),
        /**
         * Smoke Blitz.
         */
        SMOKE_BLITZ(12951, 74, SpellBook.ANCIENT_MAGICKS, new Item[]{new Item(DEATH_RUNE, 2), new Item(FIRE_RUNE, 2), new Item(BLOOD_RUNE, 2), new Item(AIR_RUNE, 2)}, "smokeBlitz", null),
        /**
         * Shadow Blitz.
         */
        SHADOW_BLITZ(12999, 76, SpellBook.ANCIENT_MAGICKS, new Item[]{new Item(DEATH_RUNE, 2), new Item(BLOOD_RUNE, 2), new Item(AIR_RUNE, 2), new Item(SOUL_RUNE, 2)}, "shadowBlitz", null),
        /**
         * Blood Blitz.
         */
        BLOOD_BLITZ(12911, 80, SpellBook.ANCIENT_MAGICKS, new Item[]{new Item(DEATH_RUNE, 2), new Item(BLOOD_RUNE, 4)}, "bloodBlitz", null),
        /**
         * Ice Blitz.
         */
        ICE_BLITZ(12871, 82, SpellBook.ANCIENT_MAGICKS, new Item[]{new Item(DEATH_RUNE, 2), new Item(BLOOD_RUNE, 2), new Item(WATER_RUNE, 3)}, "iceBlitz", null),
        /**
         * Smoke Barrage.
         */
        SMOKE_BARRAGE(12975, 86, SpellBook.ANCIENT_MAGICKS, new Item[]{new Item(DEATH_RUNE, 4), new Item(BLOOD_RUNE, 2), new Item(AIR_RUNE, 4), new Item(FIRE_RUNE, 4)}, "smokeBarrage", null),
        /**
         * Shadow Barrage.
         */
        SHADOW_BARRAGE(13023, 88, SpellBook.ANCIENT_MAGICKS, new Item[]{new Item(DEATH_RUNE, 4), new Item(BLOOD_RUNE, 2), new Item(AIR_RUNE, 4), new Item(SOUL_RUNE, 3)}, "shadowBarrage", null),
        /**
         * Blood Barrage.
         */
        BLOOD_BARRAGE(12929, 92, SpellBook.ANCIENT_MAGICKS, new Item[]{new Item(DEATH_RUNE, 4), new Item(BLOOD_RUNE, 4), new Item(SOUL_RUNE, 1)}, "bloodBarrage", null),
        /**
         * Ice Barrage.
         */
        ICE_BARRAGE(12891, 94, SpellBook.ANCIENT_MAGICKS, new Item[]{new Item(DEATH_RUNE, 4), new Item(BLOOD_RUNE, 2), new Item(WATER_RUNE, 6)}, "iceBarrage", null),
        /**
         * Tele-block.
         */
        TELE_BLOCK(12445, 85, SpellBook.MODERN_MAGICS, new Item[]{new Item(LAW_RUNE, 1), new Item(CHAOS_RUNE, 1), new Item(DEATH_RUNE, 1)}, "teleBlock", null),
        /**
         * Bind.
         */
        BIND(1572, 20, SpellBook.MODERN_MAGICS, new Item[]{new Item(EARTH_RUNE, 3), new Item(WATER_RUNE, 3), new Item(NATURE_RUNE, 2)}, "bind", null),
        /**
         * Snare.
         */
        SNARE(1582, 50, SpellBook.MODERN_MAGICS, new Item[]{new Item(EARTH_RUNE, 4), new Item(WATER_RUNE, 4), new Item(NATURE_RUNE, 3)}, "snare", null),
        /**
         * Entangle.
         */
        ENTANGLE(1592, 79, SpellBook.MODERN_MAGICS, new Item[]{new Item(EARTH_RUNE, 5), new Item(WATER_RUNE, 5), new Item(NATURE_RUNE, 4)}, "entangle", null),
        /**
         * Confuse.
         */
        CONFUSE(1153, 3, SpellBook.MODERN_MAGICS, new Item[]{new Item(WATER_RUNE, 3), new Item(EARTH_RUNE, 2), new Item(BODY_RUNE, 1)}, "confuse", null),
        /**
         * Weaken.
         */
        WEAKEN(1157, 11, SpellBook.MODERN_MAGICS, new Item[]{new Item(WATER_RUNE, 3), new Item(EARTH_RUNE, 2), new Item(BODY_RUNE, 1)}, "weaken", null),
        /**
         * Curse.
         */
        CURSE(1161, 19, SpellBook.MODERN_MAGICS, new Item[]{new Item(WATER_RUNE, 2), new Item(EARTH_RUNE, 3), new Item(BODY_RUNE, 1)}, "curse", null),
        /**
         * Vulnerability.
         */
        VULNERABILITY(1542, 76, SpellBook.MODERN_MAGICS, new Item[]{new Item(WATER_RUNE, 6), new Item(EARTH_RUNE, 6), new Item(SOUL_RUNE, 1)}, "vulnerability", null),
        /**
         * Enfeeble.
         */
        ENFEEBLE(1543, 73, SpellBook.MODERN_MAGICS, new Item[]{new Item(WATER_RUNE, 8), new Item(EARTH_RUNE, 8), new Item(SOUL_RUNE, 1)}, "enfeeble", null),
        /**
         * Stun.
         */
        STUN(1562, 80, SpellBook.MODERN_MAGICS, new Item[]{new Item(WATER_RUNE, 12), new Item(EARTH_RUNE, 12), new Item(SOUL_RUNE, 1)}, "stun", null),
        /**
         * Varrock teleport.
         */
        VARROCK_TELEPORT(1164, 25, SpellBook.MODERN_MAGICS, new Item[]{new Item(LAW_RUNE, 1), new Item(FIRE_RUNE, 1), new Item(AIR_RUNE, 3)}, "varrockTeleport", null),
        /**
         * Lumbridge teleport.
         */
        LUMBRIDGE_TELEPORT(1167, 31, SpellBook.MODERN_MAGICS, new Item[]{new Item(LAW_RUNE, 1), new Item(EARTH_RUNE, 1), new Item(AIR_RUNE, 3)}, "lumbridgeTeleport", null),
        /**
         * Falador teleport.
         */
        FALADOR_TELEPORT(1170, 37, SpellBook.MODERN_MAGICS, new Item[]{new Item(LAW_RUNE, 1), new Item(WATER_RUNE, 1), new Item(AIR_RUNE, 3)}, "faladorTeleport", null),
        /**
         * Camelot teleport.
         */
        CAMELOT_TELEPORT(1174, 45, SpellBook.MODERN_MAGICS, new Item[]{new Item(LAW_RUNE, 1), new Item(AIR_RUNE, 5)}, "camelotTeleport", null),
        /**
         * Ardougne teleport.
         */
        ARDOUGNE_TELEPORT(1540, 51, SpellBook.MODERN_MAGICS, new Item[]{new Item(LAW_RUNE, 2), new Item(WATER_RUNE, 2)}, "ardougneTeleport", null),
        /**
         * Watchtower teleport.
         */
        WATCHTOWER_TELEPORT(1541, 58, SpellBook.MODERN_MAGICS, new Item[]{new Item(LAW_RUNE, 2), new Item(EARTH_RUNE, 2)}, "watchtowerTeleport", null),
        /**
         * Trollheim teleport.
         */
        TROLLHEIM_TELEPORT(7455, 61, SpellBook.MODERN_MAGICS, new Item[]{new Item(LAW_RUNE, 2), new Item(FIRE_RUNE, 2)}, "trollheimTeleport", null),
        /**
         * Paddewwa teleport.
         */
        PADDEWWA_TELEPORT(13035, 54, SpellBook.ANCIENT_MAGICKS, new Item[]{new Item(LAW_RUNE, 2), new Item(FIRE_RUNE, 2)}, "paddewwaTeleport", null),
        /**
         * Senntisten teleport.
         */
        SENNTISTEN_TELEPORT(13045, 60, SpellBook.ANCIENT_MAGICKS, new Item[]{new Item(LAW_RUNE, 2), new Item(SOUL_RUNE, 1)}, "senntistenTeleport", null),
        /**
         * Kharyrll teleport.
         */
        KHARYRLL_TELEPORT(13053, 66, SpellBook.ANCIENT_MAGICKS, new Item[]{new Item(LAW_RUNE, 2), new Item(BLOOD_RUNE, 1)}, "kharyrllTeleport", null),
        /**
         * Lassar teleport.
         */
        LASSAR_TELEPORT(13061, 72, SpellBook.ANCIENT_MAGICKS, new Item[]{new Item(LAW_RUNE, 2), new Item(WATER_RUNE, 4)}, "lassarTeleport", null),
        /**
         * Dareeyak teleport.
         */
        DAREEYAK_TELEPORT(13069, 78, SpellBook.ANCIENT_MAGICKS, new Item[]{new Item(LAW_RUNE, 2), new Item(FIRE_RUNE, 3), new Item(AIR_RUNE, 2)}, "dareeyakTeleport", null),
        /**
         * Carrallangar teleport.
         */
        CARRALLANGAR_TELEPORT(13079, 84, SpellBook.ANCIENT_MAGICKS, new Item[]{new Item(LAW_RUNE, 2), new Item(SOUL_RUNE, 2)}, "carrallangarTeleport", null),
        /**
         * Annakarl teleport.
         */
        ANNAKARL_TELEPORT(13087, 90, SpellBook.ANCIENT_MAGICKS, new Item[]{new Item(LAW_RUNE, 2), new Item(BLOOD_RUNE, 2)}, "annakarlTeleport", null),
        /**
         * Ghorrock teleport.
         */
        GHORROCK_TELEPORT(13095, 96, SpellBook.ANCIENT_MAGICKS, new Item[]{new Item(LAW_RUNE, 2), new Item(WATER_RUNE, 8)}, "ghorrockTeleport", null);
        /**
         * A map of spell IDs.
         */
        private static Map<Integer, Spell> spells = new HashMap<Integer, Spell>();

        /**
         * Gets a spell by its ID.
         * @param spell The Spell id.
         * @return The spell, or <code>null</code> if the id is not a spell.
         */
        public static Spell forId(int spell) {
            return spells.get(spell);
        }

        /**
         * Populates the prayer map.
         */
        static {
            for (Spell spell : Spell.values()) {
                spells.put(spell.id, spell);
            }
        }
        /**
         * The id of this spell.
         */
        private int id;
        /**
         * The level required to use this spell.
         */
        private int levelRequired;
        /**
         * The spellbook this spell is on.
         */
        private SpellBook spellBook;
        /**
         * The runes required for this spell.
         */
        private Item[] runes;
        /**
         * The spell's name for script parsing..
         */
        private String spellName;
        /**
         * The item required to cast this spell.
         */
        private Item requiredItem;

        /**
         * Creates the spell.
         * @param id The spell id.
         * @return 
         */
        private Spell(int id, int levelRequired, SpellBook spellBook, Item[] runes, String spellName, Item requiredItem) {
            this.id = id;
            this.levelRequired = levelRequired;
            this.spellBook = spellBook;
            this.runes = runes;
            this.spellName = spellName;
        }

        /**
         * Gets the spell id.
         * @return The spell id.
         */
        public int getSpellId() {
            return id;
        }

        /**
         * Gets the level required to use this spell.
         * @return The level required to use this spell.
         */
        public int getLevelRequired() {
            return levelRequired;
        }

        /**
         * Gets the spell book this spell is on.
         * @return The spell book this spell is on.
         */
        public SpellBook getSpellBook() {
            return spellBook;
        }

        /**
         * Gets the runes required for this spell.
         * @return The runes required for this spell.
         */
        public Item[] getRunes() {
            return runes;
        }

        /**
         * Gets the rune required for this spell by its index.
         * @return The rune required for this spell by its index.
         */
        public Item getRune(int index) {
            return runes[index];
        }

        /**
         * Gets the spell's name for script parsing.
         * @return the spellName
         */
        public String getSpellName() {
            return spellName;
        }

        /**
         * Gets the required item to cast this spell.
         * @return The required item to cast this spell.
         */
        public Item getRequiredItem() {
            return requiredItem;
        }
    }

    /**
     * Represents spell books.
     * @author Michael Bull
     *
     */
    public static enum SpellBook {

        MODERN_MAGICS(0),
        ANCIENT_MAGICKS(1);
        /**
         * A map of spell book IDs.
         */
        private static Map<Integer, SpellBook> spellBooks = new HashMap<Integer, SpellBook>();

        /**
         * Gets a spell book by its ID.
         * @param spellBook The Spell book id.
         * @return The spell book, or <code>null</code> if the id is not a spell book.
         */
        public static SpellBook forId(int spellBook) {
            return spellBooks.get(spellBook);
        }

        /**
         * Populates the spell book map.
         */
        static {
            for (SpellBook spellBook : SpellBook.values()) {
                spellBooks.put(spellBook.id, spellBook);
            }
        }
        /**
         * The id of this spell book.
         */
        private int id;

        /**
         * Creates the spell book.
         * @param id The spellBook id.
         * @return 
         */
        private SpellBook(int id) {
            this.id = id;
        }

        /**
         * Gets the spellBook id.
         * @return The spellBook id.
         */
        public int getSpellBookId() {
            return id;
        }
    }

    public static boolean deleteRune(Character character, Item rune) {
        int id = rune.getId();
        if (!(id == 556 && character.getEquipment().get(3) != null && character.getEquipment().get(3).getId() == 1381)
                && !(id == 555 && character.getEquipment().get(3) != null && character.getEquipment().get(3).getId() == 1383)
                && !(id == 557 && character.getEquipment().get(3) != null && character.getEquipment().get(3).getId() == 1385)
                && !(id == 554 && character.getEquipment().get(3) != null && (character.getEquipment().get(3).getId() == 1387 || character.getEquipment().get(3).getId() == 3053))
                && !(id == 555 && character.getEquipment().get(3) != null && character.getEquipment().get(3).getId() == 6563)
                && !(id == 557 && character.getEquipment().get(3) != null && (character.getEquipment().get(3).getId() == 6563 || character.getEquipment().get(3).getId() == 3053))) {
            return true;
        }
        return false;
    }
}
