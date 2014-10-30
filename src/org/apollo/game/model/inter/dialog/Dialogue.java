package org.apollo.game.model.inter.dialog;

/**
 * Represents one single dialogue.
 *
 *
 * @author Rodrigo Molina
 */
public class Dialogue {

    	/**
    	 * The interface id of this dialogue to match which type of dialogue it is.
    	 */
    	private int interfaceId;
    	
    	/**
    	 * The dialogue ID of the dialogue to be used for giving items or going to the next dialogue.
    	 */
    	private int dialogueID;
    	
    	/**
    	 * The current dialogue listener which is stalking on this dialogue!
    	 */
    	private DialogueListener listener;
    	
    	/**
    	 * Creates a new dialogue.
    	 * 
    	 * @param interfaceId
    	 * 	The interface id.
    	 * @param dialogueId
    	 * 	The dialogue id.
    	 */
    	public Dialogue(int interfaceId, int dialogueId, DialogueListener listener) {
    	    this.setDialogueID(dialogueId);
    	    this.setInterfaceId(interfaceId);
    	    this.setListener(listener);
    	}

    	/**
    	 * Get's the next dialgue.
    	 * 
    	 * @return the next dialogue id.
    	 */
    	public int getNextDialogue() {
    	    return ++dialogueID;
    	}
    	
	public int getInterfaceId() {
	    return interfaceId;
	}

	public void setInterfaceId(int interfaceId) {
	    this.interfaceId = interfaceId;
	}

	public int getDialogueID() {
	    return dialogueID;
	}

	public void setDialogueID(int dialogueID) {
	    this.dialogueID = dialogueID;
	}

	public DialogueListener getListener() {
	    return listener;
	}

	public void setListener(DialogueListener listener) {
	    this.listener = listener;
	}
	
	@Override
	public String toString() {
	    return "Interface: ["+interfaceId+"] Dialogue Id: ["+dialogueID+"] Listener: ["+listener+"]";
	}
}
