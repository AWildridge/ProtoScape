package org.apollo.game.model.inter.dialog;

import org.apollo.game.model.inter.InterfaceListener;

/**
 * An {@link InterfaceListener} which also listens for dialogue-specific events (i.e. clicking buttons).
 * @author Chris Fletcher
 */
public interface DialogueListener extends InterfaceListener {

	/**
	 * Called when the player has clicked the specified button.
	 * <p>
	 * Note that this method is invoked when any button is clicked whilst the dialogue is opened. In case the
	 * button is not being handled by this listener, simply return {@code false} to allow further processing
	 * of the event.
	 * </p>
	 * @param button The button interface id.
	 * @return {@code true} if the event handler chain should be broken.
	 */
	public boolean buttonClicked(int button);
        
        /**
	 * Called when the player has clicked the "Click here to continue" button on a chatting dialogue.
	 */
	public void continued();

}