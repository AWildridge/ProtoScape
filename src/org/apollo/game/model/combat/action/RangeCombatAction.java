package org.apollo.game.model.combat.action;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apollo.game.GameConstants;
import org.apollo.game.event.impl.DamageEvent;
import org.apollo.game.model.Character;
import org.apollo.game.model.EquipmentConstants;
import org.apollo.game.model.Graphic;
import org.apollo.game.model.GroundItem;
import org.apollo.game.model.Item;
import org.apollo.game.model.NPC;
import org.apollo.game.model.Player;
import org.apollo.game.model.Position;
import org.apollo.game.model.Prayers;
import org.apollo.game.model.Skill;
import org.apollo.game.model.World;
import org.apollo.game.model.combat.CombatAction;
import org.apollo.game.model.combat.CombatFormulae;
import org.apollo.game.model.combat.CombatState.AttackType;
import org.apollo.game.model.combat.CombatState.CombatStyle;
import org.apollo.game.model.def.PoisonType;
import org.apollo.game.scheduling.ScheduledTask;

/**
 * Normal range combat action.
 * 
 * Credits to rs2-server team for formulas and a guideline
 * as how to structure it
 * @author The Wanderer
 *
 */
public class RangeCombatAction extends AbstractCombatAction {

    /**
     * The singleton instance.
     */
    private static final RangeCombatAction INSTANCE = new RangeCombatAction();

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
    private RangeCombatAction() {
    }

    public void getWeapon() {
        
    }
    @Override
    public boolean canHit(Character attacker, Character victim) {
        
        if (!super.canHit(attacker, victim)) {
            return false;
        }
        if (attacker.getEquipment().get(EquipmentConstants.WEAPON) == null && !attacker.rangeWeaponIgnore()) {
            return false;
        }
        Item weapon = attacker.getEquipment().get(EquipmentConstants.WEAPON);
        BowType bow = BowType.forId(weapon != null ? weapon.getId() : -1);
        if (bow != null && bow != BowType.CRYSTAL_BOW) {
          if(attacker instanceof Player) {  
              
            if (attacker.getEquipment().get(EquipmentConstants.ARROWS) == null || ArrowType.forId(attacker.getArrows()) == null) {
                attacker.sendMessage("There are no " + (bow == BowType.KARILS_XBOW ? "bolts" : "arrows") + " left in your quiver.");
                return false;
            }
          }
            
            boolean rangeArrows = false;
            for (int i = 0; i < BowType.forId(attacker.getEquipment().get(EquipmentConstants.WEAPON).getId()).getValidArrows().length; i++) {
                if (attacker.getEquipment().get(EquipmentConstants.ARROWS).getId() == BowType.forId(attacker.getEquipment().get(EquipmentConstants.WEAPON).getId()).getValidArrow(i)) {
                    rangeArrows = true;
                    break;
                }
            }
            if (!rangeArrows) {
                attacker.sendMessage("You can't use " + attacker.getEquipment().get(EquipmentConstants.ARROWS).getDefinition().getName()
                        + "s with a " + attacker.getEquipment().get(EquipmentConstants.WEAPON).getDefinition().getName() + ".");
                return false;
            }
        }
        return true; // TODO implement!
    }

    @Override
    public void hit(final Character attacker, final Character victim) {
        super.hit(attacker, victim);

        boolean special = attacker.getCombatState().isSpecialOn() ? special(attacker, victim) : false;

        final int maxHit = CombatFormulae.calculateRangeMaxHit(attacker, special);
        int damage = damage(maxHit, attacker, victim, attacker.getCombatState().getAttackType(), Skill.RANGED, Prayers.PROTECT_FROM_MISSILES);
        final int hit = random.nextInt(damage < 1 ? 1 : damage + 1); // +1 as its exclusive


        final BowType bow = BowType.forId(attacker.getWeapon());
        ArrowType arrows = ArrowType.forId(attacker.getArrows());
        KnifeType knife = KnifeType.forId(attacker.getWeapon());
        DartType dart = DartType.forId(attacker.getWeapon());
        ThrownaxeType thrownaxe = ThrownaxeType.forId(attacker.getWeapon());
        JavelinType javelin = JavelinType.forId(attacker.getWeapon());

        /**
         * Variables set by the ammunition type.
         */
        Graphic pullback;
        int projectile;
        final Item ammunition;
        final double dropRate;
        int hitDelay;

        if (knife != null || dart != null || thrownaxe != null || javelin != null || attacker.getWeapon() == 6522) {
            /*if (attacker.getEquipment() != null) {
                attacker.getBonuses().setBonus(BonusConstants.RANGE_ATTACK_BONUS, attacker.getEquipment().get(EquipmentConstants.WEAPON).getEquipmentDefinition().getBonus(12));//we are using a range weapon, so we must set to use the weapon's ranged strength
            }*/
            if (knife != null) {
                pullback = knife.getPullbackGraphic();
                projectile = knife.getProjectileId();
                dropRate = knife.getDropRate();
            } else if (dart != null) {
                pullback = dart.getPullbackGraphic();
                projectile = dart.getProjectileId();
                dropRate = dart.getDropRate();
            } else if (thrownaxe != null) {
                pullback = thrownaxe.getPullbackGraphic();
                projectile = thrownaxe.getProjectileId();
                dropRate = thrownaxe.getDropRate();
            } else if (javelin != null) {
                pullback = javelin.getPullbackGraphic();
                projectile = javelin.getProjectileId();
                dropRate = javelin.getDropRate();
            } else {
                pullback = null;
                projectile = 442;
                dropRate = 0.45;
            }
            ammunition = attacker.getEquipment().get(EquipmentConstants.WEAPON);
            hitDelay = 2;
        } else {
            /*if (attacker.getEquipment() != null) {
                attacker.getBonuses().setBonus(BonusConstants.RANGE_ATTACK_BONUS, attacker.getEquipment().get(EquipmentConstants.ARROWS).getEquipmentDefinition().getBonus(BonusConstants.RANGE_ATTACK_BONUS));//we are using arrows, so we must set to use the arrows' ranged strength
            }*/
            pullback = bow == BowType.CRYSTAL_BOW ? Graphic.create(250, 0, 100) : arrows.getPullbackGraphic();
            projectile = bow == BowType.CRYSTAL_BOW ? 249 : arrows.getProjectileId();
            dropRate = bow == BowType.CRYSTAL_BOW ? 1.1 : arrows.getDropRate();
            ammunition = attacker.getEquipment().get(EquipmentConstants.ARROWS);
            hitDelay = 2;
        }
        if (attacker.getEventManager() != null) {
            attacker.getBonuses().refreshBonuses();
        }


        if (!special) {
            if (pullback != null) {
                attacker.playGraphic(pullback);
            }

            int clientSpeed;
            int showDelay;
            int slope;
            if (knife != null || dart != null || thrownaxe != null || javelin != null || attacker.getWeapon() == 6522) {
                clientSpeed = 65;
                showDelay = 50;
                slope = 5;
            } else {
                if (bow == BowType.KARILS_XBOW) {
                    clientSpeed = 55;
                    showDelay = 45;
                    slope = 5;
                } else {
                    if (attacker.getPosition().isWithinDistance(victim.getPosition(), 1)) {
                        clientSpeed = 55;
                    } else if (attacker.getPosition().isWithinDistance(victim.getPosition(), 3)) {
                        clientSpeed = 55;
                    } else if (attacker.getPosition().isWithinDistance(victim.getPosition(), 8)) {
                        clientSpeed = 65;
                        hitDelay += 1;
                    } else {
                        clientSpeed = 75;
                    }
                    showDelay = 45;
                    slope = 10;
                }
            }
            int offsetX = (attacker.getPosition().getX() - victim.getCentrePosition().getX());
            int offsetY = (attacker.getPosition().getY() - victim.getCentrePosition().getY());
            //Position position = new Position(victim.getPosition().getX(), victim.getPosition().getY());
            if(attacker instanceof Player) {
                
        	attacker.playProjectile(victim.getCentrePosition(), (byte) offsetX, (byte) offsetY, projectile, showDelay, 50, clientSpeed, 43, 36, victim.getProjectileLockonIndex(), slope, 36);
            } else {
        	NPC npc = (NPC) attacker;
            	attacker.playProjectile(victim.getCentrePosition(), (byte) offsetX, (byte) offsetY, npc.getCombatDef().getProjectileId(), showDelay, 50, clientSpeed, 43, 36, victim.getProjectileLockonIndex(), slope, 36); 
            }
            
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
            if (arrows != null && attacker.getEquipment().get(EquipmentConstants.ARROWS).getEquipmentDefinition() != null) {
                if (attacker.getEquipment().get(EquipmentConstants.ARROWS).getEquipmentDefinition().getPoisonType() != PoisonType.NONE) {
                    victim.getCombatState().setPoisonDamage(attacker.getEquipment().get(EquipmentConstants.ARROWS).getEquipmentDefinition().getPoisonType().getRangeDamage(), attacker);
                    if (victim.getEventManager() != null) {
                        victim.sendMessage("You have been poisoned!");
                    }
                }
            } else if ((knife != null || dart != null || thrownaxe != null || javelin != null) && attacker.getEquipment().get(EquipmentConstants.WEAPON).getEquipmentDefinition() != null) {
                if (attacker.getEquipment().get(EquipmentConstants.WEAPON).getEquipmentDefinition().getPoisonType() != PoisonType.NONE) {
                    victim.getCombatState().setPoisonDamage(attacker.getEquipment().get(EquipmentConstants.WEAPON).getEquipmentDefinition().getPoisonType().getRangeDamage(), attacker);
                    if (victim.getEventManager() != null) {
                        victim.sendMessage("You have been poisoned!");
                    }
                }
            }
        }

        if ((bow == null || bow != BowType.CRYSTAL_BOW) && ammunition != null) {
            attacker.getEquipment().remove(new Item(ammunition.getId(), 1));
        }

        if (CombatFormulae.fullKaril(attacker)) {
            int karil = random.nextInt(8);
            if (karil == 3) {
                Skill skill = victim.getSkillSet().getSkill(Skill.AGILITY);
                victim.getSkillSet().setSkill(Skill.AGILITY, new Skill(skill.getExperience(), skill.getMaximumLevel() / 4, skill.getMaximumLevel()));
                victim.playGraphic(Graphic.create(401, 0, 100));
            }
        }

        World.getWorld().schedule(new ScheduledTask(hitDelay, false) {

            @Override
            public void execute() {
                victim.inflictDamage(new DamageEvent(hit, victim.getHP(), victim.getMaxHP()), attacker);
                victim.damageCharacter(new DamageEvent(hit, victim.getHP(), victim.getMaxHP()));
                smite(attacker, victim, hit);
                recoil(attacker, victim, hit);
                double r = random.nextDouble();
                if (r >= dropRate && ammunition != null) {
                    GroundItem.getInstance().create(attacker instanceof Player ? ((Player) attacker) : null, ammunition.getId(), 1, victim.getPosition());
                }
                victim.getActiveCombatAction().defend(attacker, victim);
                this.stop();
            }
        });
        addExperience(attacker, hit);

        if (special) {
            final Item weapon = attacker.getEquipment().get(EquipmentConstants.WEAPON);
            if (weapon != null && weapon.getEquipmentDefinition() != null) {
                World.getWorld().schedule(new ScheduledTask(2, false) {

                    @Override
                    public void execute() {
                        for (int i = 1; i < weapon.getEquipmentDefinition().getSpecialHits(); i++) {
                            double r = random.nextDouble();
                            if (r >= dropRate && ammunition != null) {
                                GroundItem.getInstance().create((Player) attacker, ammunition.getId(), 1, victim.getPosition());
                            }
                            if ((bow == null || bow != BowType.CRYSTAL_BOW) && ammunition != null) {
                                attacker.getEquipment().remove(new Item(ammunition.getId(), 1));
                            }
                            int dmg = damage(maxHit, attacker, victim, attacker.getCombatState().getAttackType(), Skill.RANGED, Prayers.PROTECT_FROM_MISSILES);
                            int specialHit = random.nextInt(dmg < 1 ? 1 : dmg + 1);
                            if (specialHit > victim.getSkillSet().getSkill(Skill.HITPOINTS).getCurrentLevel()) {
                                specialHit = victim.getSkillSet().getSkill(Skill.HITPOINTS).getCurrentLevel();
                            }
                            victim.inflictDamage(new DamageEvent(specialHit, victim.getHP(), victim.getMaxHP()), attacker);
                            victim.damageCharacter(new DamageEvent(specialHit, victim.getHP(), victim.getMaxHP()));
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
        int dist = 1;
        BowType bow = BowType.forId(attacker.getWeapon());
        KnifeType knife = KnifeType.forId(attacker.getWeapon());
        DartType dart = DartType.forId(attacker.getWeapon());
        ThrownaxeType thrownaxe = ThrownaxeType.forId(attacker.getWeapon());
        JavelinType javelin = JavelinType.forId(attacker.getWeapon());
        if (bow != null) {
            dist = bow.getDistance(attacker.getCombatState().getCombatStyle().getId() - (attacker.getCombatState().getCombatStyle() == CombatStyle.DEFENSIVE ? 1 : 0));
        } else if (knife != null) {
            dist = knife.getDistance(attacker.getCombatState().getCombatStyle().getId() - (attacker.getCombatState().getCombatStyle() == CombatStyle.DEFENSIVE ? 1 : 0));
        } else if (dart != null) {
            dist = dart.getDistance(attacker.getCombatState().getCombatStyle().getId() - (attacker.getCombatState().getCombatStyle() == CombatStyle.DEFENSIVE ? 1 : 0));
        } else if (thrownaxe != null) {
            dist = thrownaxe.getDistance(attacker.getCombatState().getCombatStyle().getId() - (attacker.getCombatState().getCombatStyle() == CombatStyle.DEFENSIVE ? 1 : 0));
        } else if (javelin != null) {
            dist = javelin.getDistance(attacker.getCombatState().getCombatStyle().getId() - (attacker.getCombatState().getCombatStyle() == CombatStyle.DEFENSIVE ? 1 : 0));
        } else if (attacker.getWeapon() == 6522) {
            dist = attacker.getCombatState().getCombatStyle() == CombatStyle.DEFENSIVE ? 9 : 7;
        }
        //return dist;
        return dist = attacker.getCombatState().getCombatStyle() == CombatStyle.DEFENSIVE ? 9 : 7;
    }

    @Override
    public int damage(int maxHit, Character attacker, Character victim, AttackType attackType, int skill, int prayer) {
        return super.damage(maxHit, attacker, victim, attackType, skill, prayer);
    }

    @Override
    public void addExperience(Character attacker, int damage) {
        super.addExperience(attacker, damage);
        if (attacker.getCombatState().getCombatStyle() == CombatStyle.DEFENSIVE) {
            //Longrange
            attacker.getSkillSet().addExperience(Skill.RANGED, (2 * damage) * GameConstants.EXP_MODIFIER);
            attacker.getSkillSet().addExperience(Skill.DEFENCE, (2 * damage) * GameConstants.EXP_MODIFIER);
        } else {
            attacker.getSkillSet().addExperience(Skill.RANGED, (4 * damage) * GameConstants.EXP_MODIFIER);
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

    public static enum BowType {

        LONGBOW(new int[]{839},
        new int[]{882, 883, 884, 885, 942,
    2523, 2533, 5616, 5617, 5622, 5623},
        new int[]{10, 10, 10}),
        SHORTBOW(new int[]{841},
        new int[]{882, 883, 884, 885, 942,
    2523, 2533, 5616, 5617, 5622, 5623},
        new int[]{7, 7, 9}),
        OAK_SHORTBOW(new int[]{843},
        new int[]{882, 883, 884, 885, 886,
    887, 942, 2523, 2533, 2534, 2535,
    5616, 5617, 5618, 5622, 5623, 5624},
        new int[]{7, 7, 9}),
        OAK_LONGBOW(new int[]{845},
        new int[]{882, 883, 884, 885, 886,
    887, 942, 2523, 2533, 2534, 2535,
    5616, 5617, 5618, 5622, 5623, 5624},
        new int[]{10, 10, 10}),
        WILLOW_LONGBOW(new int[]{847},
        new int[]{882, 883, 884, 885, 886,
    887, 888, 889, 942, 2523, 2533, 2534,
    2535, 2536, 2537, 5616, 5617, 5618,
    5619, 5622, 5623, 5624, 5625},
        new int[]{10, 10, 10}),
        WILLOW_SHORTBOW(new int[]{849},
        new int[]{882, 883, 884, 885, 886,
    887, 888, 889, 942, 2523, 2533, 2534,
    2535, 2536, 2537, 5616, 5617, 5618,
    5619, 5622, 5623, 5624, 5625},
        new int[]{7, 7, 9}),
        MAPLE_LONGBOW(new int[]{851},
        new int[]{882, 883, 884, 885, 886,
    887, 888, 889, 890, 891, 942, 2523,
    2533, 2534, 2535, 2536, 2537, 2538,
    2539, 5616, 5617, 5618, 5619, 5620,
    5622, 5623, 5624, 5625, 5626},
        new int[]{10, 10, 10}),
        MAPLE_SHORTBOW(new int[]{853},
        new int[]{882, 883, 884, 885, 886,
    887, 888, 889, 890, 891, 942, 2523,
    2533, 2534, 2535, 2536, 2537, 2538,
    2539, 5616, 5617, 5618, 5619, 5620,
    5622, 5623, 5624, 5625, 5626},
        new int[]{7, 7, 9}),
        YEW_LONGBOW(new int[]{855},
        new int[]{882, 883, 884, 885, 886,
    887, 888, 889, 890, 891, 892, 893,
    942, 2523, 2533, 2534, 2535, 2536,
    2537, 2538, 2539, 2540, 2541, 5616,
    5617, 5618, 5619, 5620, 5621, 5622,
    5623, 5624, 5625, 5626, 5627},
        new int[]{10, 10, 10}),
        YEW_SHORTBOW(new int[]{857},
        new int[]{882, 883, 884, 885, 886,
    887, 888, 889, 890, 891, 892, 893,
    942, 2523, 2533, 2534, 2535, 2536,
    2537, 2538, 2539, 2540, 2541, 5616,
    5617, 5618, 5619, 5620, 5621, 5622,
    5623, 5624, 5625, 5626, 5627},
        new int[]{7, 7, 9}),
        MAGIC_LONGBOW(new int[]{859},
        new int[]{882, 883, 884, 885, 886,
    887, 888, 889, 890, 891, 892, 893,
    942, 2523, 2533, 2534, 2535, 2536,
    2537, 2538, 2539, 2540, 2541, 5616,
    5617, 5618, 5619, 5620, 5621, 5622,
    5623, 5624, 5625, 5626, 5627},
        new int[]{10, 10, 10}),
        MAGIC_SHORTBOW(new int[]{861},
        new int[]{882, 883, 884, 885, 886,
    887, 888, 889, 890, 891, 892, 893,
    942, 2523, 2533, 2534, 2535, 2536,
    2537, 2538, 2539, 2540, 2541, 5616,
    5617, 5618, 5619, 5620, 5621, 5622,
    5623, 5624, 5625, 5626, 5627},
        new int[]{7, 7, 9}),
        CRYSTAL_BOW(new int[]{4212, 4214, 4215, 4216, 4217, 4218, 4219, 4220, 4221, 4222, 4223},
        new int[]{},
        new int[]{10, 10, 10}),
        KARILS_XBOW(new int[]{4734},
        new int[]{4740},
        new int[]{7, 7, 9});
        /**
         * A map of bow types.
         */
        private static Map<Integer, BowType> bowTypes = new HashMap<Integer, BowType>();

        /**
         * Gets an bow type by its item ID.
         * @param bowType The bow item id.
         * @return The bow type, or <code>null</code> if the id is not an bow type.
         */
        public static BowType forId(int bowType) {
            return bowTypes.get(bowType);
        }

        /**
         * Populates the bow type map.
         */
        static {
            for (BowType bowType : BowType.values()) {
                for (int i = 0; i < bowType.ids.length; i++) {
                    bowTypes.put(bowType.ids[i], bowType);
                }
            }
        }
        /**
         * The bows item id.
         */
        private int[] ids;
        /**
         * The arrows this bow can use.
         */
        private int[] validArrows;
        /**
         * The distances required to be near the victim
         * based on the mob's combat style.
         */
        private int[] distances;

        private BowType(int[] ids, int[] validArrows, int[] distances) {
            this.ids = ids;
            this.validArrows = validArrows;
            this.distances = distances;
        }

        /**
         * Gets the valid arrows this bow can use.
         * @return The valid arrows this bow can use.
         */
        public int[] getValidArrows() {
            return validArrows;
        }

        /**
         * Gets a valid arrow this bow can use by its index.
         * @param index The arrow index.
         * @return The valid arrow this bow can use by its index.
         */
        public int getValidArrow(int index) {
            return validArrows[index];
        }

        /**
         * Gets a distance required to be near the victim.
         * @param index The combat style index.
         * @return The distance required to be near the victim
         */
        public int getDistance(int index) {
            return distances[index];
        }
    }

    /**
     * An enum for all arrow types, this includes the drop rate percentage of this arrow 
     * (the higher quality the less likely it is to disappear), the ranged strength
     * bonus that Jagex now take into consideration and is used for the ranged max hit calculation.
     * @author Michael Bull
     * @author Sir Sean
     */
    public static enum ArrowType {

        BRONZE_ARROW(new int[]{882, 883, 5616, 5622}, 0.75, Graphic.create(19, 0, 100), 10),
        IRON_ARROW(new int[]{884, 885, 5617, 5623}, 0.7, Graphic.create(18, 0, 100), 9),
        STEEL_ARROW(new int[]{886, 887, 5618, 5624}, 0.65, Graphic.create(20, 0, 100), 11),
        MITHRIL_ARROW(new int[]{888, 889, 5619, 5625}, 0.6, Graphic.create(21, 0, 100), 12),
        ADAMANT_ARROW(new int[]{890, 891, 5620, 5626}, 0.5, Graphic.create(22, 0, 100), 13),
        RUNE_ARROW(new int[]{892, 893, 5621, 5627}, 0.4, Graphic.create(24, 0, 100), 15),
        BOLT_RACK(new int[]{4740}, 1.1, null, 27);
        /**
         * A map of arrow types.
         */
        private static Map<Integer, ArrowType> arrowTypes = new HashMap<Integer, ArrowType>();

        /**
         * Gets an arrow type by its item ID.
         * @param arrowType The arrow item id.
         * @return The arrow type, or <code>null</code> if the id is not an arrow type.
         */
        public static ArrowType forId(int arrowType) {
            return arrowTypes.get(arrowType);
        }

        /**
         * Populates the arrow type map.
         */
        static {
            for (ArrowType arrowType : ArrowType.values()) {
                for (int i = 0; i < arrowType.getIds().length; i++) {
                    arrowTypes.put(arrowType.ids[i], arrowType);
                }
            }
        }
        /**
         * The arrows item id.
         */
        private int ids[];
        /**
         * The percentage chance for the arrow to disappear once fired.
         */
        private double dropRate;
        /**
         * The pullback graphic.
         */
        private Graphic pullback;
        /**
         * The projectile id.
         */
        private int projectile;

        private ArrowType(int[] ids, double dropRate, Graphic pullback, int projectile) {
            this.ids = ids;
            this.dropRate = dropRate;
            this.pullback = pullback;
            this.projectile = projectile;
        }

        /**
         * @return the id
         */
        public int[] getIds() {
            return ids;
        }

        /**
         * @return the id
         */
        public int getId(int index) {
            return ids[index];
        }

        /**
         * Gets the arrow's percentage chance to disappear once fired
         * @return The arrow's percentage chance to disappear once fired.
         */
        public double getDropRate() {
            return dropRate;
        }

        /**
         * Gets the arrow's pullback graphic.
         * @return The arrow's pullback graphic.
         */
        public Graphic getPullbackGraphic() {
            return pullback;
        }

        /**
         * Gets the arrow's projectile id.
         * @return The arrow's projectile id.
         */
        public int getProjectileId() {
            return projectile;
        }
    }

    public static enum KnifeType {

        BRONZE_KNIFE(new int[]{864, 870, 5654, 5661}, 0.75, Graphic.create(219, 0, 100), 212, new int[]{4, 4, 6}),
        IRON_KNIFE(new int[]{863, 871, 5655, 5662}, 0.7, Graphic.create(220, 0, 100), 213, new int[]{4, 4, 6}),
        STEEL_KNIFE(new int[]{865, 872, 5656, 5663}, 0.65, Graphic.create(221, 0, 100), 214, new int[]{4, 4, 6}),
        MITHRIL_KNIFE(new int[]{866, 873, 5657, 5664}, 0.6, Graphic.create(223, 0, 100), 216, new int[]{4, 4, 6}),
        ADAMANT_KNIFE(new int[]{867, 875, 5659, 5666}, 0.5, Graphic.create(224, 0, 100), 217, new int[]{4, 4, 6}),
        RUNE_KNIFE(new int[]{868, 876, 5660, 5667}, 0.4, Graphic.create(225, 0, 100), 218, new int[]{4, 4, 6}),
        BLACK_KNIFE(new int[]{869, 874, 5658, 5665}, 0.6, Graphic.create(222, 0, 100), 215, new int[]{4, 4, 6});
        /**
         * A map of knife types.
         */
        private static Map<Integer, KnifeType> knifeTypes = new HashMap<Integer, KnifeType>();

        /**
         * Gets an knife type by its item ID.
         * @param knifeType The knife item id.
         * @return The knife type, or <code>null</code> if the id is not an knife type.
         */
        public static KnifeType forId(int knifeType) {
            return knifeTypes.get(knifeType);
        }

        /**
         * Populates the knife type map.
         */
        static {
            for (KnifeType knifeType : KnifeType.values()) {
                for (int i = 0; i < knifeType.ids.length; i++) {
                    knifeTypes.put(knifeType.ids[i], knifeType);
                }
            }
        }
        /**
         * The knifes item id's.
         */
        private int[] ids;
        /**
         * The percentage chance for the knife to disappear once fired.
         */
        private double dropRate;
        /**
         * The pullback graphic.
         */
        private Graphic pullback;
        /**
         * The projectile id.
         */
        private int projectile;
        /**
         * The distances required to be near the victim
         * based on the mob's combat style.
         */
        private int[] distances;

        private KnifeType(int ids[], double dropRate, Graphic pullback, int projectile, int[] distances) {
            this.ids = ids;
            this.dropRate = dropRate;
            this.distances = distances;
            this.pullback = pullback;
            this.projectile = projectile;
        }

        /**
         * @return the id
         */
        public int[] getIds() {
            return ids;
        }

        /**
         * @return the id
         */
        public int getId(int index) {
            return ids[index];
        }

        /**
         * Gets the knife's percentage chance to disappear once fired
         * @return The knife's percentage chance to disappear once fired.
         */
        public double getDropRate() {
            return dropRate;
        }

        /**
         * Gets a distance required to be near the victim.
         * @param index The combat style index.
         * @return The distance required to be near the victim
         */
        public int getDistance(int index) {
            return distances[index];
        }

        /**
         * Gets the knife's pullback graphic.
         * @return The knife's pullback graphic.
         */
        public Graphic getPullbackGraphic() {
            return pullback;
        }

        /**
         * Gets the knife's projectile id.
         * @return The knife's projectile id.
         */
        public int getProjectileId() {
            return projectile;
        }
    }

    public static enum DartType {

        BRONZE_DART(new int[]{806, 812, 5628, 5635}, 0.75, Graphic.create(232, 0, 100), 226, new int[]{3, 3, 5}),
        IRON_DART(new int[]{807, 813, 5629, 5636}, 0.7, Graphic.create(233, 0, 100), 227, new int[]{3, 3, 5}),
        STEEL_DART(new int[]{808, 814, 5630, 5637}, 0.65, Graphic.create(234, 0, 100), 228, new int[]{3, 3, 5}),
        MITHRIL_DART(new int[]{809, 815, 5632, 5639}, 0.6, Graphic.create(235, 0, 100), 229, new int[]{3, 3, 5}),
        ADAMANT_DART(new int[]{810, 816, 3633, 5640}, 0.5, Graphic.create(236, 0, 100), 230, new int[]{3, 3, 5}),
        RUNE_DART(new int[]{811, 817, 5636, 5641}, 0.4, Graphic.create(237, 0, 100), 231, new int[]{3, 3, 5}),
        BLACK_DART(new int[]{3093, 3094, 5631, 5638}, 0.6, Graphic.create(237, 0, 100), 231, new int[]{3, 3, 5});
        /**
         * A map of knife types.
         */
        private static Map<Integer, DartType> dartTypes = new HashMap<Integer, DartType>();

        /**
         * Gets an dart type by its item ID.
         * @param dartType The dart item id.
         * @return The dart type, or <code>null</code> if the id is not an dart type.
         */
        public static DartType forId(int dartType) {
            return dartTypes.get(dartType);
        }

        /**
         * Populates the dart type map.
         */
        static {
            for (DartType dartType : DartType.values()) {
                for (int i = 0; i < dartType.ids.length; i++) {
                    dartTypes.put(dartType.ids[i], dartType);
                }
            }
        }
        /**
         * The dart's item id's.
         */
        private int[] ids;
        /**
         * The percentage chance for the knife to disappear once fired.
         */
        private double dropRate;
        /**
         * The pullback graphic.
         */
        private Graphic pullback;
        /**
         * The projectile id.
         */
        private int projectile;
        /**
         * The distances required to be near the victim
         * based on the mob's combat style.
         */
        private int[] distances;

        private DartType(int ids[], double dropRate, Graphic pullback, int projectile, int[] distances) {
            this.ids = ids;
            this.dropRate = dropRate;
            this.distances = distances;
            this.pullback = pullback;
            this.projectile = projectile;
        }

        /**
         * @return the id
         */
        public int[] getIds() {
            return ids;
        }

        /**
         * @return the id
         */
        public int getId(int index) {
            return ids[index];
        }

        /**
         * Gets the dart's percentage chance to disappear once fired
         * @return The dart's percentage chance to disappear once fired.
         */
        public double getDropRate() {
            return dropRate;
        }

        /**
         * Gets a distance required to be near the victim.
         * @param index The combat style index.
         * @return The distance required to be near the victim
         */
        public int getDistance(int index) {
            return distances[index];
        }

        /**
         * Gets the dart's pullback graphic.
         * @return The dart's pullback graphic.
         */
        public Graphic getPullbackGraphic() {
            return pullback;
        }

        /**
         * Gets the dart's projectile id.
         * @return The dart's projectile id.
         */
        public int getProjectileId() {
            return projectile;
        }
    }

    public static enum ThrownaxeType {

        BRONZE_THROWNAXE(new int[]{800}, 0.75, Graphic.create(42, 0, 100), 36, new int[]{4, 4, 6}),
        IRON_THROWNAXE(new int[]{801}, 0.7, Graphic.create(43, 0, 100), 35, new int[]{4, 4, 6}),
        STEEL_THROWNAXE(new int[]{802}, 0.65, Graphic.create(44, 0, 100), 37, new int[]{4, 4, 6}),
        MITHRIL_THROWNAXE(new int[]{803}, 0.6, Graphic.create(45, 0, 100), 38, new int[]{4, 4, 6}),
        ADAMANT_THROWNAXE(new int[]{804}, 0.5, Graphic.create(46, 0, 100), 39, new int[]{4, 4, 6}),
        RUNE_THROWNAXE(new int[]{805}, 0.4, Graphic.create(48, 0, 100), 41, new int[]{4, 4, 6});
        /**
         * A map of knife types.
         */
        private static Map<Integer, ThrownaxeType> thrownaxeTypes = new HashMap<Integer, ThrownaxeType>();

        /**
         * Gets an thrownaxe type by its item ID.
         * @param thrownaxeType The thrownaxe item id.
         * @return The thrownaxe type, or <code>null</code> if the id is not an thrownaxe type.
         */
        public static ThrownaxeType forId(int thrownaxeType) {
            return thrownaxeTypes.get(thrownaxeType);
        }

        /**
         * Populates the thrownaxe type map.
         */
        static {
            for (ThrownaxeType thrownaxeType : ThrownaxeType.values()) {
                for (int i = 0; i < thrownaxeType.ids.length; i++) {
                    thrownaxeTypes.put(thrownaxeType.ids[i], thrownaxeType);
                }
            }
        }
        /**
         * The thrownaxe's item id's.
         */
        private int[] ids;
        /**
         * The percentage chance for the knife to disappear once fired.
         */
        private double dropRate;
        /**
         * The pullback graphic.
         */
        private Graphic pullback;
        /**
         * The projectile id.
         */
        private int projectile;
        /**
         * The distances required to be near the victim
         * based on the mob's combat style.
         */
        private int[] distances;

        private ThrownaxeType(int ids[], double dropRate, Graphic pullback, int projectile, int[] distances) {
            this.ids = ids;
            this.dropRate = dropRate;
            this.distances = distances;
            this.pullback = pullback;
            this.projectile = projectile;
        }

        /**
         * @return the id
         */
        public int[] getIds() {
            return ids;
        }

        /**
         * @return the id
         */
        public int getId(int index) {
            return ids[index];
        }

        /**
         * Gets the thrownaxe's percentage chance to disappear once fired
         * @return The thrownaxe's percentage chance to disappear once fired.
         */
        public double getDropRate() {
            return dropRate;
        }

        /**
         * Gets a distance required to be near the victim.
         * @param index The combat style index.
         * @return The distance required to be near the victim
         */
        public int getDistance(int index) {
            return distances[index];
        }

        /**
         * Gets the thrownaxe's pullback graphic.
         * @return The thrownaxe's pullback graphic.
         */
        public Graphic getPullbackGraphic() {
            return pullback;
        }

        /**
         * Gets the thrownaxe's projectile id.
         * @return The thrownaxe's projectile id.
         */
        public int getProjectileId() {
            return projectile;
        }
    }

    public static enum JavelinType {

        BRONZE_JAVELIN(new int[]{825, 831, 5642, 5648}, 0.75, Graphic.create(206, 0, 100), 20, new int[]{4, 4, 6}),
        IRON_JAVELIN(new int[]{826, 832, 5643, 5649}, 0.7, Graphic.create(207, 0, 100), 201, new int[]{4, 4, 6}),
        STEEL_JAVELIN(new int[]{827, 833, 5644, 5650}, 0.65, Graphic.create(208, 0, 100), 202, new int[]{4, 4, 6}),
        MITHRIL_JAVELIN(new int[]{828, 834, 5645, 5651}, 0.6, Graphic.create(209, 0, 100), 203, new int[]{4, 4, 6}),
        ADAMANT_JAVELIN(new int[]{829, 835, 5646, 5652}, 0.5, Graphic.create(210, 0, 100), 204, new int[]{4, 4, 6}),
        RUNE_JAVELIN(new int[]{830, 836, 5647, 5653}, 0.4, Graphic.create(211, 0, 100), 205, new int[]{4, 4, 6});
        /**
         * A map of javelin types.
         */
        private static Map<Integer, JavelinType> javelinTypes = new HashMap<Integer, JavelinType>();

        /**
         * Gets an javelin type by its item ID.
         * @param javelinType The javelin item id.
         * @return The javelin type, or <code>null</code> if the id is not an javelin type.
         */
        public static JavelinType forId(int javelinType) {
            return javelinTypes.get(javelinType);
        }

        /**
         * Populates the javelin type map.
         */
        static {
            for (JavelinType javelinType : JavelinType.values()) {
                for (int i = 0; i < javelinType.ids.length; i++) {
                    javelinTypes.put(javelinType.ids[i], javelinType);
                }
            }
        }
        /**
         * The javelin item id's.
         */
        private int[] ids;
        /**
         * The percentage chance for the knife to disappear once fired.
         */
        private double dropRate;
        /**
         * The pullback graphic.
         */
        private Graphic pullback;
        /**
         * The projectile id.
         */
        private int projectile;
        /**
         * The distances required to be near the victim
         * based on the mob's combat style.
         */
        private int[] distances;

        private JavelinType(int ids[], double dropRate, Graphic pullback, int projectile, int[] distances) {
            this.ids = ids;
            this.dropRate = dropRate;
            this.distances = distances;
            this.pullback = pullback;
            this.projectile = projectile;
        }

        /**
         * @return the id
         */
        public int[] getIds() {
            return ids;
        }

        /**
         * @return the id
         */
        public int getId(int index) {
            return ids[index];
        }

        /**
         * Gets the javelin's percentage chance to disappear once fired
         * @return The javelin's percentage chance to disappear once fired.
         */
        public double getDropRate() {
            return dropRate;
        }

        /**
         * Gets a distance required to be near the victim.
         * @param index The combat style index.
         * @return The distance required to be near the victim
         */
        public int getDistance(int index) {
            return distances[index];
        }

        /**
         * Gets the javelin's pullback graphic.
         * @return The javelin's pullback graphic.
         */
        public Graphic getPullbackGraphic() {
            return pullback;
        }

        /**
         * Gets the javelin's projectile id.
         * @return The javelin's projectile id.
         */
        public int getProjectileId() {
            return projectile;
        }
    }
}