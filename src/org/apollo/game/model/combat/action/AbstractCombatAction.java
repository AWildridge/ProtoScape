package org.apollo.game.model.combat.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apollo.game.GameConstants;
import org.apollo.game.event.impl.DamageEvent;
import org.apollo.game.event.impl.DamageEvent.HitPriority;
import org.apollo.game.model.Animation;
import org.apollo.game.model.BoundaryManager;
import org.apollo.game.model.Character;
import org.apollo.game.model.EquipmentConstants;
import org.apollo.game.model.Graphic;
import org.apollo.game.model.Item;
import org.apollo.game.model.Player;
import org.apollo.game.model.Prayers;
import org.apollo.game.model.Skill;
import org.apollo.game.model.World;
import org.apollo.game.model.combat.CombatAction;
import org.apollo.game.model.combat.CombatFormulae;
import org.apollo.game.model.combat.CombatState.AttackType;
import org.apollo.game.model.combat.CombatState.CombatStyle;
import org.apollo.game.scheduling.ScheduledTask;

/**
 * Provides a skeletal implementation for <code>CombatAction</code>s on which
 * other code should base their code off.
 * <p>
 * This implementation contains code common to ALL implementations, e.g. the
 * 'canDamage' method checks wilderness levels of the players.
 * </p>
 * Credits to rs2-server team for formulas and a guideline
 * as how to structure it
 * @author The Wanderer
 *
 */
public abstract class AbstractCombatAction implements CombatAction {

    /**
     * The random number generator.
     */
    private final Random random = new Random();

    @Override
    public boolean canHit(Character attacker, Character victim) {
	if(!BoundaryManager.isWithinBoundary(attacker.getPosition(), "Wilderness") && BoundaryManager.isWithinBoundary(victim.getPosition(), "Wilderness")) {
	    attacker.sendMessage("You must be in the wilderness to do this!");
	    return false;
	}
	if(BoundaryManager.isWithinBoundary(attacker.getPosition(), "Wilderness") && !BoundaryManager.isWithinBoundary(victim.getPosition(), "Wilderness")) {
	    attacker.sendMessage("The other player is not in the wilderness.");
	    return false;
	}       
	if(attacker instanceof Player && victim instanceof Player) {
            Player attack = (Player) attacker;
            Player vic = (Player) victim;
            int combatDif = (attack.getSkillSet().getCombatLevel() - vic.getSkillSet().getCombatLevel()) < 0 ? (vic.getSkillSet().getCombatLevel() - attack.getSkillSet().getCombatLevel()) : (attack.getSkillSet().getCombatLevel() - vic.getSkillSet().getCombatLevel()) ;
            if(combatDif > attack.getWildLevel() || combatDif > vic.getWildLevel()) {
        	attack.sendMessage("You must go deeper in the wilderness to attack this player.");
        	return false;
            }
        }
        if (attacker.getCombatState().isDead() || victim.getCombatState().isDead()) {

            return false;
        }
        if (!attacker.canDamage(victim)) {
            return false;
        }

        if (!victim.canDamage(attacker)) {
            return false;
        }

        if (!BoundaryManager.isWithinBoundary(attacker.getPosition(), "MultiCombat")
                || !BoundaryManager.isWithinBoundary(victim.getPosition(), "MultiCombat")) {
            if (attacker.getCombatState().getLastDamageEventTimer() > (System.currentTimeMillis() + 4000)) { //10 cycles for tagging timer
                if (attacker.getCombatState().getLastDamageEventBy() != null && victim != attacker.getCombatState().getLastDamageEventBy()) {
                    if (attacker.getEventManager() != null) {
                        attacker.sendMessage("I'm already under attack.");
                    }
                    return false;
                }
            }

            if (victim.getCombatState().getLastDamageEventTimer() > (System.currentTimeMillis() + 4000)) { //10 cycles for tagging timer
                if (victim.getCombatState().getLastDamageEventBy() != null && attacker != victim.getCombatState().getLastDamageEventBy()) {
                    if (attacker.getEventManager() != null) {
                        attacker.sendMessage("Someone else is already fighting your opponent.");
                    }
                    return false;
                }
            }
        }

        return true; // TODO implement!
    }

    @Override
    public void hit(final Character attacker, Character victim) {
        /**
         * This is to prevent immediate teaming, EG: someone walking into Edgeville(1v1) and 2 people casting spells at the same time, usually it wouldn't set the timer till the damage had been inflicted
         */
        victim.getCombatState().setLastDamageEventTimer(10000);
        victim.getCombatState().setLastDamageEventBy(attacker);
        /**
         * Stops other emotes from overlapping important ones.
         */
        attacker.getCombatState().setWeaponSwitchTimer(1000);
        attacker.getCombatState().setCanAnimate(false);
        World.getWorld().schedule(new ScheduledTask(1, false) {

            public void execute() {
                attacker.getCombatState().setCanAnimate(true);
                this.stop();
            }
        });
    }

    @Override
    public void defend(Character attacker, Character victim) {

        if (victim.isAutoRetaliating()) {
            if (victim.getCombatState().getAttackEvent() == null || victim.getInteractingCharacter() != attacker) {
                victim.getCombatState().increaseAttackDelay(3);
                if(canHit(victim, attacker)) {
                    victim.getCombatState().startAttacking(attacker, attacker.isAutoRetaliating());
                }
            }
        }
        if (victim.getCombatState().canAnimate()) {
            Animation defend = Animation.create(404);
            Item shield = victim.getEquipment().get(EquipmentConstants.SHIELD);
            Item weapon = victim.getEquipment().get(EquipmentConstants.WEAPON);
            String shieldName = shield != null ? shield.getDefinition().getName() : "";
            if (shieldName.contains("shield") || shieldName.contains("ket-xil")) {
                defend = shield.getEquipmentDefinition().getAnimation().getDefend();
            } else if (weapon != null) {
                defend = weapon.getEquipmentDefinition().getAnimation().getDefend();
            } else if (shield != null) {
                defend = shield.getEquipmentDefinition().getAnimation().getDefend();
            } else {
                defend = victim.getDefendAnimation();
            }
            victim.playAnimation(defend);
        }
    }

    /**
     * This method is required when a special isn't done during a hit cycle, e.g., Dragon Battleaxe
     * @param player The player.
     * @param id The weapon id.
     * @param skills The skills to be affected.
     * @param percentageModifier The percentage the skills are modified. Note: If you want subtracted, make it negative, else, positive.
     */
    public static void doSpecial(Player player, int id, int[] skills, double percentageModifier) {
        Item weapon = new Item(id);
        Skill skill;
        if (player.getCombatState().getSpecialEnergy() >= weapon.getEquipmentDefinition().getSpecialConsumption()) {
            if (skills.length >= 1) {
                for (int i = 0; i < skills.length; i++) {
                    skill = player.getSkillSet().getSkill(skills[i]);
                    player.getSkillSet().setSkill(skills[i], new Skill(skill.getExperience(), skill.getMaximumLevel() + (int) (skill.getMaximumLevel() * percentageModifier), skill.getMaximumLevel()));
                }
            }
            switch (id) {
                case 1377:
                    player.playAnimation(new Animation(1056));
                    player.playGraphic(new Graphic(246));
                    player.forceChat("Raarrrrrgggggghhhhhhh!");
                    skill = player.getSkillSet().getSkill(Skill.STRENGTH);
                    player.getSkillSet().setSkill(Skill.STRENGTH, new Skill(skill.getExperience(), skill.getMaximumLevel() + (int) (skill.getMaximumLevel() * .2), skill.getMaximumLevel()));
                    break;

                case 35:
                    player.playAnimation(new Animation(1057));
                    player.playGraphic(new Graphic(247));
                    player.forceChat("For Camelot!");
                    skill = player.getSkillSet().getSkill(Skill.DEFENCE);
                    player.getSkillSet().setSkill(Skill.DEFENCE, new Skill(skill.getExperience(), skill.getMaximumLevel() + 8, skill.getMaximumLevel()));
                    break;

            }
            player.getCombatState().decreaseSpecial(weapon.getEquipmentDefinition().getSpecialConsumption());
        } else {
            player.sendMessage("You do not have enough special energy left.");
            return;
        }

    }

    @Override
    public boolean special(final Character attacker, final Character victim) {
        Item weapon = attacker.getEquipment().get(EquipmentConstants.WEAPON);
        if (weapon != null && weapon.getEquipmentDefinition() != null) {
            attacker.getCombatState().inverseSpecial();
            if (attacker.getCombatState().getSpecialEnergy() >= weapon.getEquipmentDefinition().getSpecialConsumption()) {
                switch (weapon.getId()) {
                    case 4151:
                        attacker.playAnimation(weapon.getEquipmentDefinition().getAnimation().getAttack(0));
                        victim.playGraphic(new Graphic(341, 0, 100));
                        break;
                    case 1305:
                        attacker.playAnimation(new Animation(1058));
                        attacker.playGraphic(new Graphic(248));
                        break;

                    case 3101:
                        attacker.playAnimation(new Animation(274));
                        break;

                    case 4587:
                        attacker.playAnimation(new Animation(1872));
                        attacker.playGraphic(new Graphic(347, 0, 100));
                        if (victim.getEventManager() != null) {
                            Player v = (Player) victim;
                            Prayers.deActivatePrayer(v, Prayers.PROTECT_FROM_MELEE);
                            Prayers.deActivatePrayer(v, Prayers.PROTECT_FROM_MAGIC);
                            Prayers.deActivatePrayer(v, Prayers.PROTECT_FROM_MISSILES);
                            v.sendMessage("You are damaged.");
                        }
                        break;

                    case 3204:
                        attacker.playAnimation(new Animation(1203));
                        attacker.playGraphic(new Graphic(282, 0, 100));
                        break;


                    case 6739:
                        attacker.playAnimation(new Animation(2876));
                        attacker.playGraphic(new Graphic(479, 0, 100));
                        break;

                    case 7158:
                        attacker.playAnimation(new Animation(3157));
                        attacker.playGraphic(new Graphic(479, 0, 100));
                        if (BoundaryManager.isWithinBoundary(attacker.getPosition(), "MultiCombat")) {

                            for (Character c : attacker.getLocalNPCList()) { //NPCS only
                                if (c.getPosition().isWithinDistance(attacker.getPosition(), 1)) {
                                    int hit = CombatFormulae.calculateMeleeMaxHit(attacker, true);
                                    DamageEvent damageEvent = new DamageEvent(
                                            damage(hit, attacker, c, AttackType.CRUSH, Skill.ATTACK, Prayers.PROTECT_FROM_MELEE) + 1,
                                            c.getHP(), c.getMaxHP());
                                    c.inflictDamage(damageEvent, attacker);
                                    c.damageCharacter(damageEvent);
                                }
                            }
                        }

                        break;



                    case 1434:
                        attacker.playAnimation(new Animation(1060));
                        attacker.playGraphic(new Graphic(251, 0, 100));
                        break;
                    case 5698:
                        attacker.playAnimation(Animation.create(1062));
                        attacker.playGraphic(new Graphic(252, 0, 100));
                        break;
                    case 4153:
                        int hit = CombatFormulae.calculateMeleeMaxHit(attacker, false);
                        DamageEvent damageEvent = new DamageEvent(
                                damage(hit, attacker, victim, AttackType.CRUSH, Skill.ATTACK, Prayers.PROTECT_FROM_MELEE) + 1,
                                victim.getHP(), victim.getMaxHP());
                        victim.inflictDamage(damageEvent, attacker);
                        victim.damageCharacter(damageEvent);
                        attacker.playAnimation(Animation.create(1667));
                        attacker.playGraphic(new Graphic(340, 0, 100));
                        break;
                    case 861:
                        if (attacker.getEquipment().get(EquipmentConstants.ARROWS).getAmount() < 2) {
                            attacker.sendMessage("You need atleast 2 arrows to perform this special.");
                            return false;
                        }
                        attacker.playAnimation(Animation.create(1074));
                        attacker.playGraphic(new Graphic(256, 0, 100));
                        World.getWorld().schedule(new ScheduledTask(1, false) {

                            public void execute() {
                                attacker.playGraphic(new Graphic(256, 0, 100));
                                this.stop();
                            }
                        });
                        int offsetX = (attacker.getPosition().getX() - victim.getCentrePosition().getX()) * -1;
                        int offsetY = (attacker.getPosition().getY() - victim.getCentrePosition().getY()) * -1;
                        attacker.playProjectile(attacker.getPosition(), (byte) offsetX, (byte) offsetY, 249, 30, 50, 35, 43, 35, victim.getProjectileLockonIndex(), 10, 36);
                        attacker.playProjectile(attacker.getPosition(), (byte) offsetX, (byte) offsetY, 249, 60, 50, 65, 43, 35, victim.getProjectileLockonIndex(), 10, 36);
                        break;
                    case 805:
                        int specRemaining = attacker.getCombatState().getSpecialEnergy() - 10;
                        if (specRemaining > 30) {
                            specRemaining = 30;
                        }
                        specRemaining = (int) Math.floor(specRemaining / 10);
                        List<Character> closeCharacters = new ArrayList<Character>();
                        if (BoundaryManager.isWithinBoundary(attacker.getPosition(), "MultiCombat")) {
                            for (Character character : World.getWorld().getRegionRepository().getLocalCharacters(victim)) {
                                if (closeCharacters.size() < specRemaining) {
                                    if (character != attacker && character != victim
                                            && character.getPosition().isWithinDistance(victim.getPosition(), 5)
                                            && attacker.getActiveCombatAction().canHit(attacker, character)
                                            && BoundaryManager.isWithinBoundary(character.getPosition(), "MultiCombat")) {
                                        closeCharacters.add(character);
                                        character.getCombatState().setLastDamageEventTimer(10000);
                                        character.getCombatState().setLastDamageEventBy(attacker);
                                    }
                                }
                            }
                        }

                        int count = 3;
                        final int maxDamageEvent = CombatFormulae.calculateRangeMaxHit(attacker, true);
                        final int damage = damage(maxDamageEvent, attacker, victim, attacker.getCombatState().getAttackType(), Skill.RANGED, Prayers.PROTECT_FROM_MISSILES);

                        if (BoundaryManager.isWithinBoundary(attacker.getPosition(), "MultiCombat")) {
                            Character lastCharacter = victim;
                            int lastDelay = 60;
                            int lastSpeed = 90;
                            for (final Character character : closeCharacters) {
                                int newOffsetX = (lastCharacter.getPosition().getX() - character.getCentrePosition().getX()) * -1;
                                int newOffsetY = (lastCharacter.getPosition().getY() - character.getCentrePosition().getY()) * -1;
                                lastCharacter.playProjectile(lastCharacter.getPosition(), (byte) newOffsetX, (byte) newOffsetY, 258, lastDelay, 50, lastSpeed, 43, 35, character.getProjectileLockonIndex(), 13, 48);
                                World.getWorld().schedule(new ScheduledTask(count, false) {

                                    @Override
                                    public void execute() {
                                        character.inflictDamage(new DamageEvent(damage, character.getHP(), character.getMaxHP()), attacker);
                                        character.damageCharacter(new DamageEvent(damage, character.getHP(), character.getMaxHP()));
                                        smite(attacker, character, damage);
                                        recoil(attacker, character, damage);
                                        victim.getActiveCombatAction().defend(attacker, character);
                                        this.stop();
                                    }
                                });
                                lastDelay += 40;
                                lastSpeed += 35;
                                lastCharacter = character;
                                count++;
                            }
                        }

                        attacker.getCombatState().decreaseSpecial(specRemaining * 10);
                        attacker.getCombatState().setSpecial(false);
                        attacker.playAnimation(Animation.create(1068));
                        attacker.playGraphic(new Graphic(257, 0, 100));
                        int newOffsetX = (attacker.getPosition().getX() - victim.getCentrePosition().getX()) * -1;
                        int newOffsetY = (attacker.getPosition().getY() - victim.getCentrePosition().getY()) * -1;
                        attacker.playProjectile(attacker.getPosition(), (byte) newOffsetX, (byte) newOffsetY, 258, 45, 50, 55, 43, 35, victim.getProjectileLockonIndex(), 13, 48);
                        break;
                }
                attacker.getCombatState().decreaseSpecial(weapon.getEquipmentDefinition().getSpecialConsumption());
                return true;
            } else {
                attacker.sendMessage("You do not have enough special energy left.");
                return false;
            }
        }
        return false;
    }

    @Override
    public int damage(int maxDamageEvent, Character attacker, Character victim, AttackType attackType, int skill, int prayer) {
        if (attackType == AttackType.NONE) {
            System.out.println("Attack type = none");
            return -1;
        }
        if (attackType == null) {
            attackType = AttackType.STAB;
        }
        boolean veracEffect = false;
        if (skill == Skill.ATTACK) {
            if (CombatFormulae.fullVerac(attacker)) {
                if (random.nextInt(8) == 3) {
                    veracEffect = true;
                }
            }
        }

        double attackCalc = (attacker.getBonus(attackType.getId()) == 0 ? 1 : attacker.getBonus(attackType.getId())) * attacker.getSkillSet().getSkill(skill).getCurrentLevel(); // +1 as its exclusive

        /**
         * Prayer calculations.
         */
        if (skill == Skill.ATTACK) {
            //melee attack prayer modifiers
            if (attacker.getCombatState().getPrayer(Prayers.CLARITY_OF_THOUGHT)) {
                attackCalc *= 1.05;
            } else if (attacker.getCombatState().getPrayer(Prayers.IMPROVED_REFLEXES)) {
                attackCalc *= 1.10;
            } else if (attacker.getCombatState().getPrayer(Prayers.INCREDIBLE_REFLEXES)) {
                attackCalc *= 1.15;
            }
        }

        /**
         * As with the melee/range max hit calcs, combat style bonuses are added AFTER the modifiers have taken place.
         */
        if (attacker.getCombatState().getCombatStyle() == CombatStyle.ACCURATE) {
            attackCalc += 3;
        } else if (attacker.getCombatState().getCombatStyle() == CombatStyle.CONTROLLED_1
                || attacker.getCombatState().getCombatStyle() == CombatStyle.CONTROLLED_2
                || attacker.getCombatState().getCombatStyle() == CombatStyle.CONTROLLED_3) {
            attackCalc += 1;
        }


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

        if (veracEffect) {
            defenceCalc = 0;
        }

        /**
         * The chance to succeed out of 1.0.
         */
        //double hitSucceed = 0.515 * (attackCalc / defenceCalc);
        /**
         * Protection prayers.
         * Note: If an NPC is hitting on a protection prayer, it is 100% blocked, where as if a player is hitting on a protection prayer, their damage is simply reduced by 40%.
         * Also, if the attacker has the Verac effect active, it will ignore the opponent's protection prayers.
         */
        int hit = (int) (maxDamageEvent * ((victim.getCombatState().getPrayer(prayer) && !veracEffect) ? attacker.getProtectionPrayerModifier() : 1)); // +1 as its exclusive
        if (hit > victim.getSkillSet().getSkill(Skill.HITPOINTS).getCurrentLevel()) {
            hit = victim.getSkillSet().getSkill(Skill.HITPOINTS).getCurrentLevel();
        }
        if (hit <= -1) {
            hit = 0;
        }
        return random.nextInt(hit + 1) + 1; //+1 cuz random generates exclusively
    }

    @Override
    public void addExperience(Character attacker, int damage) {
        attacker.getSkillSet().addExperience(Skill.HITPOINTS, (damage * 1.33) * GameConstants.EXP_MODIFIER);
    }

    @Override
    public void recoil(Character attacker, Character victim, int damage) {
        if (victim.getEquipment().get(EquipmentConstants.RING) != null && victim.getEquipment().get(EquipmentConstants.RING).getId() == 2550) {
            if (damage > 0) {
                int recoil = (int) Math.floor(damage / 10);
                if (recoil > victim.getCombatState().getRingOfRecoil()) {
                    recoil = victim.getCombatState().getRingOfRecoil();
                }
                if (recoil > attacker.getSkillSet().getSkill(Skill.HITPOINTS).getCurrentLevel()) {
                    recoil = attacker.getSkillSet().getSkill(Skill.HITPOINTS).getCurrentLevel();
                }
                if (recoil < 1) {
                    return;
                }
                victim.getCombatState().setRingOfRecoil(victim.getCombatState().getRingOfRecoil() - recoil);
                attacker.inflictDamage(new DamageEvent(recoil, HitPriority.LOW_PRIORITY, attacker.getHP(), attacker.getMaxHP()), victim);
                if (victim.getCombatState().getRingOfRecoil() < 1) {
                    victim.getEquipment().remove(new Item(2550));
                    victim.getCombatState().setRingOfRecoil(40);
                    victim.sendMessage("Your Ring of Recoil has shattered.");
                }
            }
        }
    }

    @Override
    public void smite(Character attacker, Character victim, int damage) {
        if (attacker.getCombatState().getPrayer(Prayers.SMITE)) {
            int prayerDrain = (int) (damage * 0.25);
            victim.getSkillSet().decreaseLevel(Skill.PRAYER, prayerDrain);
        }
    }
}