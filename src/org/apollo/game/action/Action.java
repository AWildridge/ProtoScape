package org.apollo.game.action;

import org.apollo.game.model.Character;
import org.apollo.game.scheduling.ScheduledTask;

/**
 * An action is a specialised {@link ScheduledTask} which is specific to a
 * character.
 * <p>
 * <strong>ALL</strong> actions <strong>MUST</strong> implement the
 * {@link #equals(Object)} method. This is to check if two actions are
 * identical: if they are, then the new action does not replace the old one (so
 * spam/accidental clicking won't cancel your action, and start another from
 * scratch).
 * @author Graham
 */
public abstract class Action<T extends Character> extends ScheduledTask {

	/**
	 * The character performing the action.
	 */
	private final T character;

	/**
	 * A flag indicating if this action is stopping.
	 */
	private boolean stopping = false;
        
        public enum QueuePolicy {
		
		/**
		 * This indicates actions will always be queued.
		 */
		ALWAYS,
		
		/**
		 * This indicates actions will never be queued.
		 */
		NEVER,
		
		/**
		 * This indicates actions will be cleared, then queued.
		 */
		FORCE
		
	}
	
	/**
	 * A queue policy determines whether the action can occur while walking.
	 * @author Graham Edgecombe
	 * @author Brett Russell
	 *
	 */
	public enum WalkablePolicy {
		
		/**
		 * This indicates actions may occur while walking.
		 */
		WALKABLE,
		
		/**
		 * This indicates actions cannot occur while walking.
		 */
		NON_WALKABLE,
		
		/**
		 * This indicates actions can continue while following.
		 */
		FOLLOW,
		
	}

	/**
	 * Creates a new action.
	 * @param delay The delay in pulses.
	 * @param immediate A flag indicating if the action should happen
	 * immediately.
	 * @param character The character performing the action.
	 */
	public Action(int delay, boolean immediate, T character) {
		super(delay, immediate);
		this.character = character;
	}
        
        /**
	 * Gets the queue policy of this action.
	 * @return The queue policy of this action.
	 */
	public abstract QueuePolicy getQueuePolicy();
	
	/**
	 * Gets the WalkablePolicy of this action.
	 * @return The walkable policy of this action.
	 */
	public abstract WalkablePolicy getWalkablePolicy();

	/**
	 * Gets the character which performed the action.
	 * @return The character.
	 */
	public T getCharacter() {
		return character;
	}

	@Override
	public void stop() {
		super.stop();
		if (!stopping) {
			stopping = true;
			character.stopAction();
		}
	}
        
        public void stopWithoutProcessingNext() {
		super.stop();
	}

}
