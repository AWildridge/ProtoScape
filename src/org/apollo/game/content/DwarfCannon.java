package org.apollo.game.content;

import org.apollo.game.event.impl.DamageEvent;
import org.apollo.game.model.Animation;
import org.apollo.game.model.NPC;
import org.apollo.game.model.Player;
import org.apollo.game.model.Position;
import org.apollo.game.model.Skill;
import org.apollo.game.model.World;
import org.apollo.game.scheduling.ScheduledTask;
import org.apollo.game.model.Character;
import org.apollo.game.model.combat.CombatFormulae;
import org.apollo.util.Misc;

/**
 * A dwarf cannon..
 *
 * yeah i see what you mean now, AJ
 *
 *
 * @author Rodrigo Molina
 */
public class DwarfCannon {

    public void f(Player player) {
        //so you do it like this
        Cannon cannon = new Cannon(CannonFace.NORTH, player.getPosition(), 0);
        registerCannon(cannon, player);
    }

    /**
     * The world.
     */
    private static World world = World.getWorld();

    /**
     * Registers a cannon.
     * @param cannon The player's cannon.
     * @param player The player.
     */
    public static void registerCannon(Cannon cannon, final Player player) {
        if (player.getCannon() != null) {
            player.sendMessage("You already have a cannon out!");
            return;
        }
        player.sendMessage("You create a cannon..");
        player.setCannon(cannon);
        Position pos = new Position(player.getPosition().getX() - 2, player.getPosition().getY());
        player.getWalkingQueue().addStep(pos);
        player.turnTo(cannon.getPos());
    	    //now freeze the player or make it so they cannot move at all.
        //and delete the cannon base item
        world.schedule(new ScheduledTask(5, false) {

            @Override
            public void execute() {
                BuildingStates state = player.getBuild();
                if (state == null) {
                    state = BuildingStates.CREATE_BASE;
                }
                if (!buildCannon(player, state)) {
                    stop();
                }
            }

        });
    }

    /**
     * The building states of this cannon.
     *
     *
     * @author Rodrigo Molina
     */
    public enum BuildingStates {

        /**
         * Creates the base of the cannon.
         */
        CREATE_BASE,
        /**
         * They have just created a new cannon and have set the base and is
         * requesting to make a new cannon.
         */
        BASE_IS_GLOBAL,
        //..etc.
    }

    /**
     * Build's the cannon at a specified time frame of 2 seconds.
     *
     * @param cannon The cannon being build.
     * @param build The current state of the cannon.
     *
     * @return true if the cannon still requires another step, false if it was
     * the last step to be build for the cannon.
     */
    public static boolean buildCannon(Player player, BuildingStates build) {
        int object = 0;
        switch (build) {
            case CREATE_BASE:

                break;
            case BASE_IS_GLOBAL:
                object = 4;//blah
                break;
            //and the last one, null the building state and return false
        }
        player.playAnimation(Animation.create(827));
        player.sendNewGlobalObject(player.getCannon().getPos(), object, 1, 10);
        return true;
    }

    /**
     * Fires the cannon.
     *
     * @param cannon The cannon which to be fired.
     */
    public static void fire(final Cannon cannon, final Player player) {
        int balls = cannon.getCannonBalls();
        if (balls < 0 || balls > 30) {
            destroy(DestroyState.RUN_OUT_OF_BALLS, player);
        }
        world.schedule(new ScheduledTask(3, false) {

            @Override
            public void execute() {
                if (changeCannonFace(cannon)) {
                    updateCannon(player);
                    for (NPC npc : world.getNPCRepository()) {
                        if (npc == null) {
                            continue;
                        }
                        /*
                         * Now.. the hard part..
                         * 
                         * we have to check if the npc is north of cannon AND the cannon is facing north..
                         * 
                         * so we have to find a method which will check each direction..
                         */
                        CannonFace face = getCannonFace(npc.getPosition().getX(), npc.getPosition().getY(), cannon);
                        if (face != null) {
                            //we found an npc nearby!
                            if (face == cannon.getFace()) {
                                //shoot the bitch.
                                if (!shootCannon(cannon, npc, player)) {
                                    stop();
                                } else {
                                    cannon.decrement();
                                }
                            }
                        }
                    }
                }
            }

        });
    }

    /**
     * Get's the face that this npc is directly good or w.e so we can attack it.
     *
     * @param x The coord x of the npc.
     * @param y The coord y of the npc.
     * @param cannon The cannon
     * @return a face variable that this npc is in line with.
     */
    private static CannonFace getCannonFace(int otherX, int otherY, Cannon cannon) {
        int myX = cannon.getPos().getX();
        int myY = cannon.getPos().getY();
        CannonFace face = null;
        if (myY == otherY) {
            if (myX > otherX) {
                face = CannonFace.SOUTH;
            } else if (myX < otherX) {
                face = CannonFace.NORTH;
            }
        } else if (myX == otherX) {
            if (myY > otherY) {
                face = CannonFace.EAST;
            } else if (myY < otherY) {
                face = CannonFace.WEST;
            }
        }
        return face;
    }

    /**
     * Multiple states of which the cannon was destroyed.
     *
     *
     * @author Rodrigo Molina
     */
    public enum DestroyState {

        /**
         * The cannon has ran out of the max 30 balls, and cannot shoot anymore
         * cannonballs.
         */
        RUN_OUT_OF_BALLS,
        /**
         * The player has requested to pick up their cannon
         */
        PICKUP,
        /**
         * The player has logged out whilst having an cannon out in the open.
         */
        LOGOUT;
    }

    /**
     * Destroys the cannon because an problem has occurred and should be
     * tackled.
     *
     * @param cannon The cannon to be destroyed.
     * @param state The state of which the destroying issue is related to.
     */
    public static void destroy(DestroyState state, Player player) {
        switch (state) {
            case RUN_OUT_OF_BALLS:
                player.sendMessage("You cannon has ran out of ammo!");
                //here we just set the face back to north.
                player.getCannon().setFace(CannonFace.NORTH);
                updateCannon(player);
                player.getCannon().setCannonBalls(0);
                player = null;
                //now we don't want to set the cannon to null, but just update the cannon and make it stay.
                return;
            case PICKUP:
            case LOGOUT:
    		//TODO:
                //we have to give them the items of the cannon.
                break;
            default:
                player.sendMessage("Your cannon has been destroyed!");
        }
        //now null the player's cannon.
        player.setCannon(null);
        //then we can null the player for the JVM to collect the unused object.
        player = null;
    }

    /**
     * Shoots the cannon by sending the cannon ball projectile at the enemy.
     *
     * @param cannon The current cannon shooting the ammo.
     * @param enemy The enemy to shoot the ammo at.
     *
     * @return true if the shooting projectile was successful or not.
     */
    private static boolean shootCannon(Cannon cannon, Character enemy, Player player) {
    	    //first add some combat checks, if in non-multi or in multi, etc.

        //now shoot the projectile
        byte offX = (byte) (((cannon.getPos().getX() - enemy.getPosition().getX())) * -1);
        byte offY = (byte) ((cannon.getPos().getY() - enemy.getPosition().getY()) * -1);
        enemy.playProjectile(cannon.getPos(), offX, offY, 53, 30, 50, 60, 20, 20, -enemy.getIndex() + 1, 0, 0);
        int damage = Misc.random(CombatFormulae.calculateRangeMaxHit(enemy, false));
        //TODO: Prob lower the damage or add something to the forumlae since it should not be calculated based on weapon?
        enemy.inflictDamage(new DamageEvent(damage, enemy.getHP(), enemy.getSkillSet().getSkill(Skill.HITPOINTS).getMaximumLevel()), player);
        return true;
    }

    /**
     * Updates the cannon by sending the object animation of the cannon.
     *
     * @param cannon The cannon.
     */
    private static void updateCannon(Player player) {
        if (player.getCannon().getFace() != null) {
            player.sendObjectAnimation(player.getCannon().getPos(), player.getCannon().getFace().getAnim(), 10, -1);
        }
    }

    /**
     * Changes the cannon face variable. TODO: start this..
     *
     * @param cannon The cannon to change face.
     * @return true if the face was successful, else if it was null or
     * unsucessfull
     */
    private static boolean changeCannonFace(Cannon cannon) {
        if (cannon.getFace() != null) {
            switch (cannon.getFace()) {
                case NORTH:
                    //change to next one..
                    break;
                case NORTH_WEST:

                    break;
                case NORTH_EAST:

                    break;
                case WEST:

                    break;
                case EAST:

                    break;
                case SOUTH_WEST:

                    break;
                case SOUTH_EAST:

                    break;
                case SOUTH:

                    break;
            }
            return true;
        }
        return false;
    }

    /**
     * Each facing state of the cannon.
     *
     *
     * @author Rodrigo Molina
     */
    public enum CannonFace {

        WEST(Animation.create(514)),
        NORTH_WEST(Animation.create(515)),
        NORTH(Animation.create(516)),
        NORTH_EAST(Animation.create(517)),
        EAST(Animation.create(518)),
        SOUTH_EAST(Animation.create(519)),
        SOUTH(Animation.create(520)),
        SOUTH_WEST(Animation.create(521));

        private Animation anim;

        CannonFace(Animation anim) {
            this.anim = anim;
        }

        private Animation getAnim() {
            return anim;
        }
    }
}
