package org.apollo.game.model.combat;

import java.util.HashMap;
import java.util.Map;

import org.apollo.game.event.impl.SendConfigEvent;
import org.apollo.game.model.Character;
import org.apollo.game.model.Character.InteractionMode;
import org.apollo.game.model.NPC;
import org.apollo.game.model.Player;
import org.apollo.game.model.Prayers.Prayer;
import org.apollo.game.model.Skill;
import org.apollo.game.model.World;
import org.apollo.game.model.combat.action.MagicCombatAction.Spell;
import org.apollo.game.scheduling.impl.PoisonDrainTask;
import org.apollo.game.scheduling.impl.SpecialEnergyRestoreTask;
import org.apollo.game.sync.block.SynchronizationBlock;

/**
 * Holds details related to a specific character's state in combat.
 * 
 * Credits to rs2-server team for formulas and a guideline
 * as how to structure it
 * @author The Wanderer
 */
public final class CombatState {

    /**
     * The character whose combat state this is.
     */
    private Character character;
    /**
     * The damage map of this entity.
     */
    private DamageMap damageMap = new DamageMap();
    /**
     * The current attack event.
     */
    private AttackAction attackEvent;

    /**
     * Gets the current attack event.
     * @return The current attack event.
     */
    public AttackAction getAttackEvent() {
        return attackEvent;
    }
    /**
     * The attack delay. (Each value of 1 counts for 600ms, e.g. 3 = 1800ms).
     */
    private int attackDelay;

    /**
     * Gets the current attack delay.
     * @return The current attack delay.
     */
    public int getAttackDelay() {
        return attackDelay;
    }

    /**
     * Sets the current attack delay.
     * @param attackDelay The attack delay to set.
     */
    public void setAttackDelay(int attackDelay) {
        this.attackDelay = attackDelay;
    }

    /**
     * Increases the current attack delay.
     * @param amount The amount to increase by.
     */
    public void increaseAttackDelay(int amount) {
        this.attackDelay += amount;
    }

    /**
     * Decreases the current attack delay.
     * @param amount The amount to decrease by.
     */
    public void decreaseAttackDelay(int amount) {
        this.attackDelay -= amount;
    }
    /**
     * The spell delay. (Each value of 1 counts for 600ms, e.g. 3 = 1800ms).
     */
    private int spellDelay;

    /**
     * @return the spellDelay
     */
    public int getSpellDelay() {
        return spellDelay;
    }

    /**
     * @param spellDelay the spellDelay to set
     */
    public void setSpellDelay(int spellDelay) {
        this.spellDelay = spellDelay;
    }

    /**
     * @param spellDelay the spellDelay to set
     */
    public void increaseSpellDelay(int amount) {
        this.spellDelay += amount;
    }

    /**
     * @param spellDelay the spellDelay to set
     */
    public void decreaseSpellDelay(int amount) {
        this.spellDelay -= amount;
    }

    /**
     * Creates the combat state class for the specified character.
     * @param character The character.
     */
    public CombatState(Character character) {
        this.character = character;
    }

    /**
     * Gets the damage map of this entity.
     * @return The damage map.
     */
    public DamageMap getDamageMap() {
        return damageMap;
    }

    /**
     * Begins an attack on the specified victim.
     * @param victim The victim.
     * @param retaliating A boolean flag indicating if the attack is a
     * retaliation.
     */
    public void startAttacking(Character victim, boolean retaliating) {
        character.setInteractingCharacter(InteractionMode.ATTACK, victim);
        //prevents running to the character if you are already in distance
       /* if (character.getPosition().isWithinDistance(victim.getPosition(), character.getActiveCombatAction().distance(character))) {
            character.getWalkingQueue().clear();
            character.sendMessage("This is not yet supported, please get closer to the npc.");
            //TODO: Support npc attacking diagonally or something else.. its wierd.
             //well first figure out what this is for..
            return;
        }*/
        if (attackEvent == null || !attackEvent.isRunning()) {
            setLastDamageEventTimer(10000);
            attackEvent = new AttackAction(character, retaliating);
            //character.stopAction();
            character.startAction(attackEvent);
            if (character instanceof NPC) {
                ((NPC) character).setWalkOverridden(true);
            }
        } // else the attack event is reused to preserve cooldown period
    }

    public static enum CombatStyle {

        ACCURATE(0, new int[]{Skill.ATTACK}, new double[]{4}),
        AGGRESSIVE_1(1, new int[]{Skill.STRENGTH}, new double[]{4}),
        AGGRESSIVE_2(2, new int[]{Skill.STRENGTH}, new double[]{4}),
        DEFENSIVE(3, new int[]{Skill.DEFENCE}, new double[]{4, 1.33}),
        CONTROLLED_1(4, new int[]{Skill.ATTACK, Skill.STRENGTH, Skill.DEFENCE}, new double[]{1.33, 1.33, 1.33}),
        CONTROLLED_2(5, new int[]{Skill.ATTACK, Skill.STRENGTH, Skill.DEFENCE}, new double[]{1.33, 1.33, 1.33}),
        CONTROLLED_3(6, new int[]{Skill.ATTACK, Skill.STRENGTH, Skill.DEFENCE}, new double[]{1.33, 1.33, 1.33}),
        AUTOCAST(7, new int[]{Skill.MAGIC}, new double[]{2});
        /**
         * A map of combat styles.
         */
        private static Map<Integer, CombatStyle> combatStyles = new HashMap<Integer, CombatStyle>();

        /**
         * Gets a combat style by its ID.
         * @param combatStyle The combat style id.
         * @return The combat style, or <code>null</code> if the id is not a combat style.
         */
        public static CombatStyle forId(int combatStyle) {
            return combatStyles.get(combatStyle);
        }

        /**
         * Populates the combat style map.
         */
        static {
            for (CombatStyle combatStyle : CombatStyle.values()) {
                combatStyles.put(combatStyle.id, combatStyle);
            }
        }
        /**
         * The combat style's id.
         */
        private int id;
        /**
         * The skills this combat style adds experience to.
         */
        private int[] skills;
        /**
         * The amounts of experience this combat style adds.
         */
        private double[] experiences;

        private CombatStyle(int id, int[] skills, double[] experiences) {
            this.id = id;
            this.skills = skills;
            this.experiences = experiences;
        }

        /**
         * Gets the combat styles id.
         * @return The combat styles id.
         */
        public int getId() {
            return id;
        }

        /**
         * Gets the skills this attack type adds experience to.
         * @return The skills this attack type adds experience to.
         */
        public int[] getSkills() {
            return skills;
        }

        /**
         * Gets a skill this attack type adds experience to by its index.
         * @param index The skill index.
         * @return The skill this attack type adds experience to by its index.
         */
        public int getSkill(int index) {
            return skills[index];
        }

        /**
         * Gets the experience amounts this attack type adds.
         * @return The experience amounts this attack type adds.
         */
        public double[] getExperiences() {
            return experiences;
        }

        /**
         * Gets an amount of experience this attack type adds by its index.
         * @param index The experience index.
         * @return The amount of experience this attack type adds by its index.
         */
        public double getExperience(int index) {
            return experiences[index];
        }
    }

    /**
     * Used for defence calculation, EG: White mace vs Low crush defence.
     * @author Michael Bull
     *
     */
    public static enum AttackType {

        STAB(0),
        SLASH(1),
        CRUSH(2),
        MAGIC(3),
        RANGE(4),
        NONE(5);
        /**
         * A map of attack types.
         */
        private static Map<Integer, AttackType> attackTypes = new HashMap<Integer, AttackType>();

        /**
         * Gets a attack type by its ID.
         * @param attackType The attack type id.
         * @return The attack type, or <code>null</code> if the id is not a attack type.
         */
        public static AttackType forId(int attackType) {
            return attackTypes.get(attackType);
        }

        /**
         * Populates the attack type map.
         */
        static {
            for (AttackType attackType : AttackType.values()) {
                attackTypes.put(attackType.id, attackType);
            }
        }
        /**
         * The attack type's id.
         */
        private int id;

        private AttackType(int id) {
            this.id = id;
        }

        /**
         * Gets the attack types id.
         * @return The attack types id.
         */
        public int getId() {
            return id;
        }
    }

    /*
     * Combat attributes.
     */
    /**
     * The character's combat style.
     */
    private CombatStyle combatStyle = CombatStyle.ACCURATE;
    /**
     * The character's attack type.
     */
    private AttackType attackType;
    /**
     * The character's state of life.
     */
    private boolean isDead;
    /**
     * The character's spell book.
     */
    private int spellBook = 0;
    /**
     * The current spell this character is casting.
     */
    private Spell currentSpell;
    /**
     * The spell to be performed once our combat cooldown is over.
     */
    private Spell queuedSpell;
    /**
     * The character's poison damage.
     */
    private int poisonDamage = 0;
    /**
     * The character's last hit timer.
     */
    private long lastDamageEventTimer;
    /**
     * The character who last hit this character.
     */
    private Character lastDamageEventBy;
    /**
     * The delay before you can equip another weapon, used to stop emotes overlapping (EG: whip using dds anim).
     */
    private long weaponSwitchTimer;
    /**
     * The character's poisonable flag.
     */
    private boolean canBePoisoned = true;
    /**
     * Ring of Recoil use amount.
     */
    private int ringOfRecoil = 40;
    /**
     * The movement flag.
     */
    private boolean canMove = true;
    /**
     * The frozen flag.
     */
    private boolean canBeFrozen = true;
    /**
     * The teleblock flag.
     */
    private boolean teleblocked;
    /**
     * The charged flag.
     */
    private boolean charged;
    /**
     * The eating flag.
     */
    private boolean canEat = true;
    /**
     * The drinking flag.
     */
    private boolean canDrink = true;
    /**
     * The animation flag.
     * This flag stops important emotes overlapping each other, EG: block emote overlapping attack emote.
     */
    private boolean canAnimate = true;
    /**
     * The teleport flag.
     */
    private boolean canTeleport = true;
    /**
     * Special attack flag.
     */
    private boolean special = false;
    /**
     * Special energy amount.
     */
    private int specialEnergy = 100;
    /**
     * The active prayers.
     */
    private boolean[] prayers = new boolean[18];
    /**
     * The players prayer head icon.
     */
    private int prayerHeadIcon = -1;

    /**
     * Sets the character's combat style.
     * @param combatStyle The combat style to set.
     */
    public void setCombatStyle(CombatStyle combatStyle) {
        this.combatStyle = combatStyle;
    }

    /**
     * Gets the combat style.
     * @return The combat style.
     */
    public CombatStyle getCombatStyle() {
        return combatStyle; // TODO
    }

    /**
     * Sets the character's attack type.
     * @param attackType The attack type to set.
     */
    public void setAttackType(AttackType attackType) {
        this.attackType = attackType;
    }

    /**
     * Gets the attack type.
     * @return The attack type.
     */
    public AttackType getAttackType() {
        return attackType; // TODO
    }

    /**
     * Gets the character's state of life.
     * @return The character's state of life.
     */
    public boolean isDead() {
        return isDead;
    }

    /**
     * Sets the character's state of life.
     * @param isDead The state of life to set.
     */
    public void setDead(boolean isDead) {
        this.isDead = isDead;
    }

    /**
     * @return the spellBook
     */
    public int getSpellBook() {
        return spellBook;
    }

    /**
     * @param spellBook the spellBook to set
     */
    public void setSpellBook(int spellBook) {
        this.spellBook = spellBook;
    }

    /**
     * @return the currentSpell
     */
    public Spell getCurrentSpell() {
        return currentSpell;
    }

    /**
     * @param currentSpell the currentSpell to set
     */
    public void setCurrentSpell(Spell currentSpell) {
        this.currentSpell = currentSpell;
    }

    /**
     * @return the queuedSpell
     */
    public Spell getQueuedSpell() {
        return queuedSpell;
    }

    /**
     * @param queuedSpell the queuedSpell to set
     */
    public void setQueuedSpell(Spell queuedSpell) {
        this.queuedSpell = queuedSpell;
    }

    /**
     * @return the poisonDamage
     */
    public int getPoisonDamage() {
        return poisonDamage;
    }

    /**
     * @param poisonDamage the poisonDamage to set
     */
    public void setPoisonDamage(int poisonDamage, Character attacker) {
        this.poisonDamage = poisonDamage;
        if (character.getPoisonDrainTask() == null && poisonDamage > 0) {
            character.setPoisonDrainTask(new PoisonDrainTask(character, attacker));
            World.getWorld().schedule(character.getPoisonDrainTask());
        } else if (character.getPoisonDrainTask() != null && poisonDamage < 1) {
            character.getPoisonDrainTask().stop();
            character.setPoisonDrainTask(null);
        }
    }

    /**
     * @param poisonDamage the poisonDamage to set
     */
    public void decreasePoisonDamage(int poisonDamage) {
        this.poisonDamage -= poisonDamage;
        if (character.getPoisonDrainTask() != null && this.poisonDamage < 1) {
            character.getPoisonDrainTask().stop();
            character.setPoisonDrainTask(null);
        }
    }

    /**
     * @return the canBePoisoned
     */
    public boolean canBePoisoned() {
        return canBePoisoned;
    }

    /**
     * @param canBePoisoned the canBePoisoned to set
     */
    public void setCanBePoisoned(boolean canBePoisoned) {
        this.canBePoisoned = canBePoisoned;
    }

    /**
     * @return the ringOfRecoil
     */
    public int getRingOfRecoil() {
        return ringOfRecoil;
    }

    /**
     * @param ringOfRecoil the ringOfRecoil to set
     */
    public void setRingOfRecoil(int ringOfRecoil) {
        this.ringOfRecoil = ringOfRecoil;
    }

    /**
     * @return the lastDamageEventTimer
     */
    public long getLastDamageEventTimer() {
        return lastDamageEventTimer;
    }

    /**
     * @param lastDamageEventTimer the lastDamageEventTimer to set
     */
    public void setLastDamageEventTimer(long lastDamageEventTimer) {
        this.lastDamageEventTimer = lastDamageEventTimer + System.currentTimeMillis();
    }

    /**
     * @return the lastDamageEventBy
     */
    public Character getLastDamageEventBy() {
        return lastDamageEventBy;
    }

    /**
     * @param lastDamageEventBy the lastDamageEventBy to set
     */
    public void setLastDamageEventBy(Character lastDamageEventBy) {
        this.lastDamageEventBy = lastDamageEventBy;
    }

    /**
     * @return the weaponSwitchTimer
     */
    public long getWeaponSwitchTimer() {
        return weaponSwitchTimer;
    }

    /**
     * @param weaponSwitchTimer the weaponSwitchTimer to set
     */
    public void setWeaponSwitchTimer(long weaponSwitchTimer) {
        this.weaponSwitchTimer = weaponSwitchTimer + System.currentTimeMillis();
    }

    /**
     * Sets the players prayer head icon.
     * @param prayerHeadIcon The prayer head icon to set.
     */
    public void setPrayerHeadIcon(int prayerHeadIcon) {
        this.prayerHeadIcon = prayerHeadIcon;
        if(character.getEventManager() != null) {
            ((Player) character).getAppearance().setHeadIcon((byte)this.prayerHeadIcon);
            character.getBlockSet().add(SynchronizationBlock.createAppearanceBlock((Player) character));
        }
        
    }

    /**
     * Gets the players prayer head icon.
     * @return The players prayer head icon.
     */
    public int getPrayerHeadIcon() {
        return prayerHeadIcon;
    }

    /**
     * Resets all the players prayers.
     */
    public void resetPrayers() {
        for (int i = 0; i < prayers.length; i++) {
            prayers[i] = false;
            if (character instanceof Player) {
                character.send(new SendConfigEvent(Prayer.forId(i).getClientConfiguration(), 0));
            }
        }
        setPrayerHeadIcon(-1);
        if (character.getPrayerUpdateTask() != null) {
            character.getPrayerUpdateTask().stop();
            character.setPrayerUpdateTask(null);
        }
    }

    /**
     * @return the canMove
     */
    public boolean canMove() {
        return canMove;
    }

    /**
     * @param canMove the canMove to set
     */
    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }

    /**
     * @return the canBeFrozen
     */
    public boolean canBeFrozen() {
        return canBeFrozen;
    }

    /**
     * @param canBeFrozen the canBeFrozen to set
     */
    public void setCanBeFrozen(boolean canBeFrozen) {
        this.canBeFrozen = canBeFrozen;
    }

    /**
     * @return the isTeleblocked
     */
    public boolean isTeleblocked() {
        return teleblocked;
    }

    /**
     * @param teleblocked the teleblocked to set
     */
    public void setTeleblocked(boolean teleblocked) {
        this.teleblocked = teleblocked;
    }

    /**
     * @return the charged
     */
    public boolean isCharged() {
        return charged;
    }

    /**
     * @param charged the charged to set
     */
    public void setCharged(boolean charged) {
        this.charged = charged;
    }

    /**
     * Sets the character's eating flag.
     * @param canEat The eating flag to set.
     */
    public void setCanEat(boolean canEat) {
        this.canEat = canEat;
    }

    /**
     * Gets the character's eating flag.
     * @return The character's eating flag.
     */
    public boolean canEat() {
        return canEat;
    }

    /**
     * Sets the character's drinking flag.
     * @param canEat The drinking flag to set.
     */
    public void setCanDrink(boolean canDrink) {
        this.canDrink = canDrink;
    }

    /**
     * Gets the character's drinking flag.
     * @return The character's drinking flag.
     */
    public boolean canDrink() {
        return canDrink;
    }

    /**
     * Sets the character's animation flag.
     * @param canAnimate The animation flag to set.
     */
    public void setCanAnimate(boolean canAnimate) {
        this.canAnimate = canAnimate;
    }

    /**
     * Gets the character's animation flag.
     * @return The character's animation flag.
     */
    public boolean canAnimate() {
        return canAnimate;
    }

    /**
     * @return the canTeleport
     */
    public boolean canTeleport() {
        return canTeleport;
    }

    /**
     * @param canTeleport the canTeleport to set
     */
    public void setCanTeleport(boolean canTeleport) {
        this.canTeleport = canTeleport;
    }

    /**
     * Gets the special attack flag.
     * @return The special attack flag.
     */
    public boolean isSpecialOn() {
        return special;
    }

    /**
     * Inverses the special attack flag.
     */
    public void inverseSpecial() {
        this.special = !this.special;
        if (character instanceof Player) {
            character.send(new SendConfigEvent(301, isSpecialOn() ? 1 : 0));
        }
    }

    /**
     * Sets the special attack flag.
     * @param special The special attack flag to set.
     */
    public void setSpecial(boolean special) {
        this.special = special;
        if (character instanceof Player) {
            character.send(new SendConfigEvent(301, isSpecialOn() ? 1 : 0));
        }
    }

    /**
     * Increases the special energy amount.
     * @param amount The amount to increase by.
     */
    public void increaseSpecial(int amount) {
        if (amount > (100 - this.specialEnergy)) {
            amount = 100 - this.specialEnergy;
        }
        this.specialEnergy += amount;
        if (character instanceof Player) {
            character.send(new SendConfigEvent(300, getSpecialEnergy() * 10));
        }
    }

    /**
     * Decreases the special energy amount.
     * @param amount The amount to decrease by.
     */
    public void decreaseSpecial(int amount) {
        if (amount > specialEnergy) {
            amount = specialEnergy;
        }
        this.specialEnergy -= amount;
        if (this.specialEnergy < 100 && character.getSpecialUpdateTask() == null) {
            character.setSpecialUpdateTask(new SpecialEnergyRestoreTask(character));
            World.getWorld().schedule(character.getSpecialUpdateTask());
        }
        if (character instanceof Player) {
            character.send(new SendConfigEvent(301, 0));
            character.send(new SendConfigEvent(300, getSpecialEnergy() * 10));
        }
       
    }

    /**
     * Gets the special energy amount.
     * @return The special energy amount.
     */
    public int getSpecialEnergy() {
        return specialEnergy;
    }

    /**
     * Sets the special energy amount.
     * @param specialEnergy The special energy amount to set.
     */
    public void setSpecialEnergy(int specialEnergy) {
        /**
         * Indicate the special energy has decreased and needs refilling.
         */
        if (specialEnergy < this.specialEnergy && specialEnergy < 100) {
            if (character.getSpecialUpdateTask() == null) {
                character.setSpecialUpdateTask(new SpecialEnergyRestoreTask(character));
                World.getWorld().schedule(character.getSpecialUpdateTask());
            }
        } else if (specialEnergy > 99) {
            if (character.getSpecialUpdateTask() != null) {
                character.getSpecialUpdateTask().stop();
                character.setSpecialUpdateTask(null);
            }
        }
        this.specialEnergy = specialEnergy;
    }

    /**
     * @return the prayers
     */
    public boolean[] getPrayers() {
        return prayers;
    }

    /**
     * @param index
     * @return the prayers
     */
    public boolean getPrayer(int index) {
        return prayers[index];
    }

    /**
     * @param prayers the prayers to set
     */
    public void setPrayers(boolean[] prayers) {
        this.prayers = prayers;
    }

    /**
     * Sets a prayer by its index.
     * @param index The index.
     * @param prayer The flag.
     */
    public void setPrayer(int index, boolean prayer) {
        this.prayers[index] = prayer;
    }
}
