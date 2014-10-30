package org.apollo.game.scheduling.impl;

import java.util.Random;

import org.apollo.game.event.impl.ResetAnimationEvent;
import org.apollo.game.minigames.castle.CastleWars;
import org.apollo.game.model.Animation;
import org.apollo.game.model.Character;
import org.apollo.game.model.NPC;
import org.apollo.game.model.Player;
import org.apollo.game.model.Position;
import org.apollo.game.model.RequestManager.RequestState;
import org.apollo.game.model.RequestManager.RequestType;
import org.apollo.game.model.World;
import org.apollo.game.scheduling.ScheduledTask;

/**
 * The death tickable handles player and npc deaths. Drops loot, does animation,
 * teleportation, etc.
 * 
 * @author Graham
 * @author Scu11
 */
public class DeathTask extends ScheduledTask {

    /**
     * The character who has just died.
     */
    private Character character;
    /**
     * The random number generator.
     */
    private final Random random = new Random();

    /**
     * Creates the death event for the specified entity.
     * 
     * @param entity
     *            The character whose death has just happened.
     */
    public DeathTask(Character character, int ticks) {
        super(ticks, false);
        this.character = character;
    }

    @Override
    public void execute() {
        if (character.getCombatState().isDead()) {

            Position teleportTo = Character.DEFAULT_LOCATION;
            if (character.getCombatState().getLastDamageEventBy() != null
                    && character.getCombatState().getLastDamageEventBy().getCombatState().getLastDamageEventTimer() > (System.currentTimeMillis() + 4000)
                    && character.getCombatState().getLastDamageEventBy().getCombatState().getLastDamageEventBy() == character) {
                try {
                    character.getCombatState().getLastDamageEventBy().getCombatState().setLastDamageEventBy(null);
                    character.getCombatState().getLastDamageEventBy().getCombatState().setLastDamageEventTimer(0);
                } catch (Exception e) {
                }
            }
            if (character.getCombatState().getLastDamageEventBy() != null
                    && character.getCombatState().getLastDamageEventBy() != null
                    && character.getEventManager() != null) {
                if (character.getEventManager() != null
                        && ((Player) character) != null
                        && ((Player) character).getRequestManager().getRequestType() == RequestType.DUEL
                        && ((Player) character).getRequestManager().getState() == RequestState.ACTIVE) {// validates
                    // they
                    // are
                    // both
                    // players
                    character.getCombatState().getLastDamageEventBy().sendMessage(
                            "Well done! You have defeated "
                            + character.getUndefinedName() + "!");
                } else {
                    switch (random.nextInt(9)) {
                        case 0:
                            character.getCombatState().getLastDamageEventBy().sendMessage(
                                    "You have defeated "
                                    + character.getUndefinedName() + ".");
                            break;
                        case 1:
                            character.getCombatState().getLastDamageEventBy().sendMessage(
                                    "Can anyone defeat you? Certainly not "
                                    + character.getUndefinedName() + ".");
                            break;
                        case 2:
                            character.getCombatState().getLastDamageEventBy().sendMessage(
                                    character.getUndefinedName()
                                    + " falls before your might.");
                            break;
                        case 3:
                            character.getCombatState().getLastDamageEventBy().sendMessage(
                                    "A humiliating defeat for "
                                    + character.getUndefinedName() + ".");
                            break;
                        case 4:
                            character.getCombatState().getLastDamageEventBy().sendMessage(
                                    "You were clearly a better fighter than "
                                    + character.getUndefinedName() + ".");
                            break;
                        case 5:
                            character.getCombatState().getLastDamageEventBy().sendMessage(
                                    character.getUndefinedName()
                                    + " has won a free ticket to Lumbridge.");
                            break;
                        case 6:
                            character.getCombatState().getLastDamageEventBy().sendMessage(
                                    "It's all over for "
                                    + character.getUndefinedName() + ".");
                            break;
                        case 7:
                            character.getCombatState().getLastDamageEventBy().sendMessage(
                                    "With a crushing blow you finish "
                                    + character.getUndefinedName() + ".");
                            break;
                        case 8:
                            character.getCombatState().getLastDamageEventBy().sendMessage(
                                    character.getUndefinedName()
                                    + " regrets the day they met you in combat.");
                            break;
                        case 9:
                            character.getCombatState().getLastDamageEventBy().sendMessage(
                                    character.getUndefinedName()
                                    + " didn't stand a chance against you.");
                            break;
                    }
                }
            }
            character.setInteractingCharacter(null, null);
            character.getCombatState().setLastDamageEventBy(null);
            character.getCombatState().setLastDamageEventTimer(0);
            character.getCombatState().setDead(false);
            character.setRunEnergy(100);
            character.getCombatState().setSpecialEnergy(100);
            if (character.getEventManager() != null) {
                character.sendMessage("Oh dear, you are dead!");
                character.getBonuses().refreshBonuses();
            }
            character.getCombatState().setPoisonDamage(0, null);
            final Character killer = (character.getCombatState().getDamageMap().highestDamage() != null && !character.isDestroyed()) ? character.getCombatState().getDamageMap().highestDamage() : character;
            /*if (character instanceof Player && World.getWorld().getCastleWars().getIngamePlayers().contains((Player)character)){
            World.getWorld().getCastleWars().playerDeath((Player)character,(Player)killer);
            this.stop();
            return;
            }
            if (!(character.getEventManager() != null
            && ((Player) character) != null
            && ((Player) character).getRequestManager().getRequestType() == RequestType.DUEL && ((Player) character)
            .getRequestManager().getState() == RequestState.ACTIVE)) {
            character.dropLoot(killer instanceof Player ? killer : character);
            character.setDefaultAnimations();
            } else {
            teleportTo = new Position(3366, 3269, 0);
            Player player = (Player) character;
            Player partner = player.getRequestManager().getAcquaintance();
            Duel.winDuel(partner, player);
            }*/
            character.getSkillSet().refreshSkills();
            character.getCombatState().setCurrentSpell(null);
            character.getCombatState().setQueuedSpell(null);
            character.setAutocastSpell(null);
            if(character instanceof Player && killer instanceof Player && CastleWars.getCastleWars().inAnyTeam((Player) character) && CastleWars.getCastleWars().inAnyTeam((Player) killer)) {
        	CastleWars.getCastleWars().handleDeath((Player) killer, (Player) character);
        	this.stop();
        	return;
            }
            if (character instanceof Player) {
                character.teleport(teleportTo);
            }
            character.getCombatState().resetPrayers();
            character.getActionQueue().cancelQueuedActions();
            character.getActionQueue().clearNonWalkableActions();
            World.getWorld().schedule(new ScheduledTask(1, false) {

                @Override
                public void execute() {
                    for (Player p : World.getWorld().getPlayerRepository()) {
                        if (p.getPosition().isWithinDistance(character.getPosition())) {
                            p.send(new ResetAnimationEvent());
                            p.playAnimation(new Animation(-1));
                        }
                    }
                    if (character instanceof NPC) {
                        NPC n = (NPC) character;
                        n.dropLoot((Player) character.getCombatState().getDamageMap().highestDamage());
                        World.getWorld().unregister((NPC) character);
                        if(((Player) character.getCombatState().getDamageMap().highestDamage()).getFightCaves().getWaveId() > 0) {
                            ((Player) character.getCombatState().getDamageMap().highestDamage()).getFightCaves().npcKilled();
            		}
                    } else {
                        character.getCombatState().setCanMove(true);
                    }
                    character.getCombatState().getDamageMap().reset();
                    character.getCombatState().setLastDamageEventBy(null);
                    character.setInCombat(false);
                    killer.getCombatState().setLastDamageEventBy(null);
                    killer.setInCombat(false);
                    killer.setInteractingCharacter(null, null);
                    this.stop();
                }
            });
        }
        this.stop();
    }
}