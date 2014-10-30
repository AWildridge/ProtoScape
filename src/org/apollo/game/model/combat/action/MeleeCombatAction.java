package org.apollo.game.model.combat.action;

import java.util.Random;

import org.apollo.game.GameConstants;
import org.apollo.game.event.impl.DamageEvent;
import org.apollo.game.model.Character;
import org.apollo.game.model.EquipmentConstants;
import org.apollo.game.model.Graphic;
import org.apollo.game.model.Item;
import org.apollo.game.model.Prayers;
import org.apollo.game.model.Skill;
import org.apollo.game.model.World;
import org.apollo.game.model.combat.CombatAction;
import org.apollo.game.model.combat.CombatFormulae;
import org.apollo.game.model.combat.CombatState.AttackType;
import org.apollo.game.model.def.PoisonType;
import org.apollo.game.scheduling.ScheduledTask;
import org.apollo.util.Misc;

/**
 * Normal melee combat action.
 * 
 * Credits to rs2-server team for formulas and a guideline
 * as how to structure it
 * @author The Wanderer
 *
 */
public class MeleeCombatAction extends AbstractCombatAction {

    
    /**
     * The singleton instance.
     */
    private static final MeleeCombatAction INSTANCE = new MeleeCombatAction();

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
    private MeleeCombatAction() {
    }

    @Override
    public boolean canHit(Character attacker, Character victim) {
        if (!super.canHit(attacker, victim)) {
            return false;
        }
        return true; // TODO implement!
    }

    @Override
    public void hit(final Character attacker, final Character victim) {
        super.hit(attacker, victim);

        boolean special = attacker.getCombatState().isSpecialOn() ? special(attacker, victim) : false;

        final int maxHit = CombatFormulae.calculateMeleeMaxHit(attacker, special);
        
        attacker.turnTo(victim.getPosition());
        
        final int damage = damage(maxHit, attacker, victim, attacker.getCombatState().getAttackType(), Skill.ATTACK, Prayers.PROTECT_FROM_MELEE);
        
        final int hit = Misc.random(5); // +1 as its exclusive

        if (!special) {
            int attackAnimationIndex = attacker.getCombatState().getCombatStyle().getId();
            if (attackAnimationIndex > 3) {
                attackAnimationIndex -= 4;
            }
            attacker.playAnimation(
                    (attacker.getEquipment().get(EquipmentConstants.WEAPON) != null && attacker.getEquipment().get(EquipmentConstants.WEAPON).getEquipmentDefinition() != null)
                    ? attacker.getEquipment().get(EquipmentConstants.WEAPON).getEquipmentDefinition().getAnimation().getAttack(attackAnimationIndex)
                    : attacker.getAttackAnimation());            
        }

        if (victim.getCombatState().getPoisonDamage() < 1 && random.nextInt(11) == 3 && victim.getCombatState().canBePoisoned()) {
            if (attacker.getEquipment().get(EquipmentConstants.WEAPON) != null && attacker.getEquipment().get(EquipmentConstants.WEAPON).getEquipmentDefinition() != null) {
                if (attacker.getEquipment().get(EquipmentConstants.WEAPON).getEquipmentDefinition().getPoisonType() != PoisonType.NONE) {
                    victim.getCombatState().setPoisonDamage(attacker.getEquipment().get(EquipmentConstants.WEAPON).getEquipmentDefinition().getPoisonType().getMeleeDamage(), attacker);
                    if (victim.getEventManager() != null) {
                        victim.sendMessage("You have been poisoned!");
                    }
                }
            }
        }

        if (CombatFormulae.fullGuthan(attacker)) {
            int guthan = random.nextInt(8);
            if (guthan == 3) {
                Skill skill = attacker.getSkillSet().getSkill(Skill.HITPOINTS);
                int current = skill.getCurrentLevel() + hit > skill.getMaximumLevel() ? skill.getMaximumLevel() : skill.getCurrentLevel() + hit;
                attacker.getSkillSet().setSkill(Skill.HITPOINTS, new Skill(skill.getExperience(), current, skill.getMaximumLevel()));
                victim.playGraphic(new Graphic(398, 0, 0));
            }
        }

        if (CombatFormulae.fullTorag(attacker)) {
            int torag = random.nextInt(8);
            if (torag == 3) {
                if (victim.getRunEnergy() <= 4) {
                    victim.setRunEnergy(0);
                } else {
                    victim.setRunEnergy(victim.getRunEnergy() - 4);
                }
                victim.playGraphic(new Graphic(399, 0, 0));
            }
        }

        World.getWorld().schedule(new ScheduledTask(1, true) {

            @Override
            public void execute() {
                victim.damageCharacter(new DamageEvent(damage, victim.getHP(), victim.getMaxHP()));
                victim.inflictDamage(new DamageEvent(damage, victim.getHP(), victim.getMaxHP()), attacker);
                smite(attacker, victim, hit);
                recoil(attacker, victim, hit);
                this.stop();
            }
        });
        
        addExperience(attacker, damage);

        victim.getActiveCombatAction().defend(attacker, victim);
        
        if (special) {
            final Item weapon = attacker.getEquipment().get(EquipmentConstants.WEAPON);
            if (weapon != null && weapon.getEquipmentDefinition() != null) {
                World.getWorld().schedule(new ScheduledTask(1, true) {

                    @Override
                    public void execute() {
                        for (int i = 1; i < weapon.getEquipmentDefinition().getSpecialHits(); i++) {
                            int dmg = damage(maxHit, attacker, victim, attacker.getCombatState().getAttackType(), Skill.ATTACK, Prayers.PROTECT_FROM_MELEE);
                            int specialHit = random.nextInt(dmg < 1 ? 1 : dmg + 1);
                            if (specialHit > victim.getSkillSet().getSkill(Skill.HITPOINTS).getCurrentLevel()) {
                                specialHit = victim.getSkillSet().getSkill(Skill.HITPOINTS).getCurrentLevel();
                            }
                            victim.inflictDamage(new DamageEvent(specialHit, victim.getHP(), victim.getMaxHP()), attacker);
                            victim.doubleDamageChracter(new DamageEvent(specialHit, victim.getHP(), victim.getMaxHP()));
                            smite(attacker, victim, specialHit);
                            addExperience(attacker, specialHit);
                            recoil(attacker, victim, specialHit);
                        }
                        this.stop();
                    }
                });
            }
        }
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
        return 1; //TODO: Halbers/spears = 2 squares
    }

    @Override
    public int damage(int maxHit, Character attacker, Character victim, AttackType attackType, int skill, int prayer) {
        return super.damage(maxHit, attacker, victim, attackType, skill, prayer);
    }

    @Override
    public void addExperience(Character attacker, int damage) {
        super.addExperience(attacker, damage);
        for (int i = 0; i < attacker.getCombatState().getCombatStyle().getSkills().length; i++) {
            attacker.getSkillSet().addExperience(attacker.getCombatState().getCombatStyle().getSkill(i),
                    (attacker.getCombatState().getCombatStyle().getExperience(i) * damage) * GameConstants.EXP_MODIFIER);
        }
    }

    @Override
    public void recoil(Character attacker, Character victim, int damage) {
        super.recoil(attacker, victim, damage);
    }

    @Override
    public void smite(Character attacker, Character victim, int damage) {
        super.smite(attacker, victim, damage);
    }
}
