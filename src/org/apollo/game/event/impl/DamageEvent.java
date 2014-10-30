package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 *
 * @author Arrowzftw
 */
public class DamageEvent extends Event {

    /**
     * Holds the different types of hit.
     * @author Graham Edgecombe
     *
     */
    public enum HitType {

        /**
         * The zero damage, blue hit.
         */
        ZERO_DAMAGE_HIT(0),
        /**
         * The normal, red hit.
         */
        NORMAL_HIT(1),
        /**
         * The poison, green hit.
         */
        POISON_HIT(2),
        /**
         * The disease, orange hit.
         */
        DISEASE_HIT(3);
        /**
         * The id of the hit.
         */
        private int id;

        /**
         * Creates the hit type.
         * @param id The id of the hit.
         */
        private HitType(int id) {
            this.id = id;
        }

        /**
         * Gets the id of the hit.
         * @return The id of the hit.
         */
        public int getId() {
            return id;
        }
    }

    /**
     * Holds the hit priority types.
     */
    public enum HitPriority {
        /**
         * Low priority means that when the next loop is called that checks the hit queue, if the hit
         * is not picked out, it is never displayed, used for hits such as Ring of Recoil.
         */
        LOW_PRIORITY,
        /**
         * High priority means that the hit will wait in the queue until it's displayed, used for
         * hits such as special attacks.
         */
        HIGH_PRIORITY;
    }
    
    private HitPriority hitPriority;
    private int damageDone;
    private int firstDamageDone;
    private int secondDamageDone;
    private int hitType;
    private int currentHp;
    private int maxHp;

    public DamageEvent(int damageDone, int currentHp, int maxHp) {
        this.damageDone = damageDone;
        this.hitType = damageDone == 0 ? 0 : 1;
        this.currentHp = currentHp;
        this.maxHp = maxHp;
    }
    
    public DamageEvent(int firstDamageDone, int secondDamageDone, int currentHp, int maxHp) {
        this.damageDone = firstDamageDone + secondDamageDone;
        this.currentHp = currentHp;
        this.maxHp = maxHp;
    }

    public DamageEvent(int damageDone, HitType hitType, int currentHp, int maxHp) {
        this.damageDone = damageDone;
        this.hitType = hitType.getId();
        this.currentHp = currentHp;
        this.maxHp = maxHp;
    }
    
    public DamageEvent(int damageDone, HitPriority hitPriority, int currentHp, int maxHp) {
        this.damageDone = damageDone;
        this.hitType = damageDone == 0 ? 0 : 1;
        this.hitPriority = hitPriority;
        this.currentHp = currentHp;
        this.maxHp = maxHp;
    }

    public void setCurrentHp(int currentHp) {
        this.currentHp = currentHp;
    }

    public void setDamageDone(int damageDone) {
        this.damageDone = damageDone;
    }

    public void setHitType(int hitType) {
        this.hitType = hitType;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public HitPriority getHitPriority() {
        return hitPriority;
    }

    public void setHitPriority(HitPriority hitPriority) {
        this.hitPriority = hitPriority;
    }

    public int getDamageDone() {
        return damageDone;
    }

    public int getHitType() {
        return hitType;
    }

    public int getHp() {
        return currentHp;
    }

    public int getFirstDamageDone() {
        return firstDamageDone;
    }

    public int getSecondDamageDone() {
        return secondDamageDone;
    }

    public int getMaxHp() {
        return maxHp;
    }
}