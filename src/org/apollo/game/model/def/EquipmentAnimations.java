package org.apollo.game.model.def;

import org.apollo.game.model.Animation;

/**
 * The weapon animations class
 * @author Sir Sean
 *
 */
public class EquipmentAnimations {

    /**
     * The weapon stand animation
     */
    private Animation stand;
    /**
     * The weapon run animation
     */
    private Animation run;
    /**
     * The weapon walk animation
     */
    private Animation walk;
    /**
     * The weapon attack animations based on
     * attack style.
     */
    private Animation attack[];
    /**
     * The weapon defend animation
     */
    private Animation defend;
    /**
     * The weapon stand turn animation
     */
    private Animation standTurn;
    /**
     * The weapon turn 180D animation
     */
    private Animation turn180;
    /**
     * The weapon turn 90D clock wise
     */
    private Animation turn90ClockWise;
    /**
     * The weapon turn 90D counter clock wise
     */
    private Animation turn90CounterClockWise;

    /**
     * Sets the equipment animations per weapon in the constructor :)
     * @param stand The stand animation
     * @param run The run animation
     * @param walk The walk animation
     * @param attack The attack animation
     * @param standTurn The stand turn animation
     * @param turn180 The turn 180 turn animation
     * @param turn90ClockWise The turn 90 clock wise animation
     * @param turn90CounterClockWise The turn 90 counter clock wise animation
     */
    public EquipmentAnimations(Animation stand, Animation run, Animation walk, Animation attack[],
            Animation defend, Animation standTurn, Animation turn180,
            Animation turn90ClockWise, Animation turn90CounterClockWise) {
        this.stand = stand;
        this.run = run;
        this.walk = walk;
        this.attack = attack;
        this.defend = defend;
        this.standTurn = standTurn;
        this.turn180 = turn180;
        this.turn90ClockWise = turn90ClockWise;
        this.turn90CounterClockWise = turn90CounterClockWise;
    }

    /**
     * Gets the stand animation
     * @return the stand animation
     */
    public Animation getStand() {
        return stand;
    }

    /**
     * Gets the run animation
     * @return the run animation
     */
    public Animation getRun() {
        return run;
    }

    /**
     * Gets the walk animation
     * @return the walk animation
     */
    public Animation getWalk() {
        return walk;
    }

    /**
     * The attack animation
     * @param index The combat style index
     * @return the attack animation
     */
    public Animation[] getAttacks() {
        return attack;
    }

    /**
     * The attack animation
     * @param index The combat style index
     * @return the attack animation
     */
    public Animation getAttack(int index) {
        return attack[index];
    }

    /**
     * The defend animation
     * @return the defend animation
     */
    public Animation getDefend() {
        return defend;
    }

    /**
     * Gets the stand turn animation
     * @return the stand turn animation
     */
    public Animation getStandTurn() {
        return standTurn;
    }

    /**
     * Gets the 180 turn animation
     * @return the turn 180 animation
     */
    public Animation getTurn180() {
        return turn180;
    }

    /**
     * Gets the 90 clock wise turn animation
     * @return the turn 90 clock wise animation
     */
    public Animation getTurn90ClockWise() {
        return turn90ClockWise;
    }

    /**
     *Gets the 90 counter clockwise turn animation 
     * @return the turn 90 counter clock wise turn animation
     */
    public Animation getTurn90CounterClockWise() {
        return turn90CounterClockWise;
    }

    public void setAttack(int index, Animation attack) {
        this.attack[index] = attack;
    }

    public void setDefend(Animation defend) {
        this.defend = defend;
    }

    public void setRun(Animation run) {
        this.run = run;
    }

    public void setStand(Animation stand) {
        this.stand = stand;
    }

    public void setStandTurn(Animation standTurn) {
        this.standTurn = standTurn;
    }

    public void setTurn180(Animation turn180) {
        this.turn180 = turn180;
    }

    public void setTurn90ClockWise(Animation turn90ClockWise) {
        this.turn90ClockWise = turn90ClockWise;
    }

    public void setTurn90CounterClockWise(Animation turn90CounterClockWise) {
        this.turn90CounterClockWise = turn90CounterClockWise;
    }

    public void setWalk(Animation walk) {
        this.walk = walk;
    }
}
