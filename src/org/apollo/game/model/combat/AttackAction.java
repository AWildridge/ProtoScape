package org.apollo.game.model.combat;

import org.apollo.game.action.DistancedAction;
import org.apollo.game.model.Character;
import org.apollo.game.model.Character.InteractionMode;
import org.apollo.game.model.Item;
import org.apollo.game.model.NPC;
import org.apollo.game.model.Player;
import org.apollo.game.model.Position;
import org.apollo.game.model.Skill;
import org.apollo.game.model.World;
import org.apollo.game.scheduling.impl.BarrowsDegradingTask;

/**
 * The periodic attack event.
 * 
 * Credits to rs2-server team for formulas and a guideline
 * as how to structure it
 * @author The Wanderer
 */
@SuppressWarnings("rawtypes")
public class AttackAction extends DistancedAction {

    /**
     * Creates the attack event.
     * 
     * @param Character
     *            The entity that is attacking.
     * @param retaliating
     *            The retaliation flag.
     */
    @SuppressWarnings("unchecked")
    public AttackAction(Character character, boolean retaliating) {
        super(character.getCombatCooldownDelay(), !retaliating, character, character.getInteractingCharacter().getPosition(), character.getActiveCombatAction().distance(character.getInteractingCharacter()));
        System.out.println(character.getActiveCombatAction().distance(character.getInteractingCharacter()));
        System.out.println(character.getCombatState().getCombatStyle().getId());
        System.out.println(character.getCombatState().getCombatStyle());
    }

    @Override
    public QueuePolicy getQueuePolicy() {
        return QueuePolicy.FORCE;
    }

    @Override
    public WalkablePolicy getWalkablePolicy() {
        return WalkablePolicy.FOLLOW;
    }

    @SuppressWarnings("unused")
    @Override
    public void executeAction() {
        final Character character = getCharacter();
        InteractionMode mode = character.getInteractionMode();
        Character target = character.getInteractingCharacter();
        if (character.isDestroyed()) {
            if (target != null && target.getEventManager() == null) {
                ((NPC) target).setWalkOverridden(false);
            }
            this.stop(); // they disconnected/got removed
            return;
        }

        if (character.getEventManager() != null) {
            if (character.getSkillSet().getSkill(Skill.HITPOINTS).getCurrentLevel() <= 0) {
                stop();
                return;
            }
        }

        if (target == null || target.isDestroyed() || target.getCombatState().isDead() || mode != InteractionMode.ATTACK) {
            character.resetInteractingCharacter();
            if (character instanceof NPC) {
                ((NPC) character).setWalkOverridden(false);
            }
            if (target != null && target instanceof NPC) {
                ((NPC) target).setWalkOverridden(false);
            }
            target = null;
            this.stop(); // the target disconnected / got removed
            return;
        }

        final CombatAction action = character.getActiveCombatAction();

        if (!action.canHit(character, target)) {
            character.getCombatState().setQueuedSpell(null);
            character.resetInteractingCharacter();
            if (character instanceof NPC) {
                ((NPC) character).setWalkOverridden(false);
            }
            if (target instanceof NPC) {
                ((NPC) target).setWalkOverridden(false);
            }
            this.stop();
            return;
        }
        character.getWalkingQueue().clear();

        int extraDistance = 0;

        if ((character.getFirstDirection().toInteger() != -1 || character.getSecondDirection().toInteger() != -1)
                && (target.getFirstDirection().toInteger() != -1 || target.getSecondDirection().toInteger() != -1)) {
            extraDistance = 3;
        }
        int steps = Math.abs(character.getPosition().getY() - target.getPosition().getY());
        Position[] positions = new Position[steps + 1];
        positions[0] = target.getPosition();
        for(int i = 0; i < steps; i++) {
            positions[i + 1] = new Position(target.getPosition().getX(), target.getPosition().getY() + (steps + (i - steps)) + 1);
        }
        

        if(target.getEventManager() == null && target.isAutoRetaliating()) {
            for(Position pos : positions) {
                target.getWalkingQueue().addStep(pos);
            }
        }
        /*if (!character.getPosition().lineOfSight(target) || !character.getPosition().isWithinDistance(target.getPosition(), character.getActiveCombatAction().distance(character) + extraDistance)) {
        if (!character.getCombatState().isDead() && character.getCombatState().canMove()) {
        PathFinder pf = new RS2PathFinder();
        Path p = pf.findPath(character.getPosition(), character.getPosition().closestTileOfCharacter(target));
        
        
        if (p == null)
        return;
        
        if (p.getPoints().size() < 1)
        return;
        
        for (Point p2 : p.getPoints()) {
        character.getWalkingQueue().addStep(new Position(p2.getX(), p2.getY()));
        }
        character.getWalkingQueue().pulse();
        }
        this.setDelay(1);
        return;
        }*/

        this.setDelay(character.getCombatCooldownDelay());
        if (target != null && mode == InteractionMode.ATTACK) {
            action.hit(character, target);
            character.setInCombat(true);
            target.setInCombat(true);
            if (character.getEventManager() != null) {
                for (int i = 0; i < character.getEquipment().getItems().length; i++) {
                    for (int j = 0; j < BarrowsDegradingTask.barrowsNames.length; j++) {
                        if (character.getEquipment().get(i) != null &&
                                character.getEquipment().get(i).getDefinition().getName().contains(BarrowsDegradingTask.barrowsNames[j])) {
                            character.sendMessage("Setting barrows task in motion...");
                            Item item = character.getEquipment().get(i);
                            System.out.println(item.getTickInCombat());
                            World.getWorld().schedule(new BarrowsDegradingTask((Player) character, item));
                        }
                    }
                }
            }
            character.getCombatState().setAttackDelay(character.getCombatCooldownDelay());
        } else {
            this.stop(); // will be restarted later if another attack starts
        }
    }
}
