package org.apollo.game.action;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import org.apollo.game.model.World;

/**
 * Stores a queue of pending actions.
 * @author blakeman8192
 * @author Graham Edgecombe
 *
 */
public class ActionQueue {
	
	/**
	 * The maximum number of actions allowed to be queued at once, deliberately
	 * set to the size of the player's inventory.
	 */
	public static final int MAXIMUM_SIZE = 28;
	
	/**
	 * A queue of <code>Action</code> objects.
	 */
	@SuppressWarnings("rawtypes")
	private final Queue<Action> queuedActions = new LinkedList<Action>();
	
	/**
	 * The current action.
	 */
	@SuppressWarnings("rawtypes")
	private Action currentAction = null;
	
	/**
	 * Cancels all queued action events.
	 */
	@SuppressWarnings("rawtypes")
	public void cancelQueuedActions() {
		for(Action actionTickable : queuedActions) {
			actionTickable.stop();
		}
		queuedActions.clear();
		if(currentAction != null)
			currentAction.stop();
		currentAction = null;
	}
	
	/**
	 * Adds an <code>Action</code> to the queue.
	 * @param action The action.
	 */
	@SuppressWarnings("rawtypes")
	public void addAction(Action action) {
		if(queuedActions.size() >= MAXIMUM_SIZE) {
			return;
		}
		int queueSize = queuedActions.size() + (currentAction == null ? 0 : 1);
		switch(action.getQueuePolicy()) {		
		case ALWAYS:
			break;
		case NEVER:
			if(queueSize > 0) {
				return;
			}
			break;
		case FORCE:
			cancelQueuedActions();
			break;
		}
		queuedActions.add(action);
		processNextAction();
	}
	
	/**
	 * Purges actions in the queue with a <code>WalkablePolicy</code> of <code>NON_WALKABLE</code>.
	 */
	@SuppressWarnings("rawtypes")
	public void clearNonWalkableActions() {
		if(currentAction != null) {
			switch(currentAction.getWalkablePolicy()) {
			case WALKABLE:
				break;
			case FOLLOW:
				break;
			case NON_WALKABLE:
				currentAction.stopWithoutProcessingNext();
				currentAction = null;
				break;
			}
		}
		if(queuedActions.size() > 0) {
			Iterator<Action> it = queuedActions.iterator();
			while(it.hasNext()) {
			    Action actionTickable = it.next();
				switch(actionTickable.getWalkablePolicy()) {
				case WALKABLE:
					break;
				case FOLLOW:
					break;
				case NON_WALKABLE:
					actionTickable.stopWithoutProcessingNext();
					it.remove();
					break;
				}
			}
		}
		processNextAction();
	}

	/**
	 * Processes this next action.
	 */
	public void processNextAction() {
		if(currentAction != null) {
			if(currentAction.isRunning()) {
				return;
			} else {
				currentAction = null;
			}
		}
		if(queuedActions.size() > 0) {
			currentAction = queuedActions.poll();
			World.getWorld().schedule(currentAction);
		}
	}
	
	/**
	 * Clears the action queue and current action.
	 */
	@SuppressWarnings("rawtypes")
	public void clearAllActions() {
		if(currentAction != null) {
			currentAction.stop();
			currentAction = null;
		}
		if(queuedActions.size() > 0) {
			Iterator<Action> it = queuedActions.iterator();
			while(it.hasNext()) {
			    Action actionTickable = it.next();
				actionTickable.stopWithoutProcessingNext();
				it.remove();
			}
		}		
	}

}
