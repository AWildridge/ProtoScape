package org.apollo.game.model.inter.dialog.impl;

import org.apollo.game.model.Player;
import org.apollo.game.model.inter.bank.BankPinEnterAmountListener;
import org.apollo.game.model.inter.bank.BankUtils;
import org.apollo.game.model.inter.dialog.DialogueListener;
import org.apollo.game.model.inter.dialog.DialogueSender;

/**
 * Listens to the bank npc dialogues.
 *
 *
 * @author Rodrigo Molina
 */
public class BankerListener implements DialogueListener {
    
    	/**
    	 * The player..
    	 */
    	private Player player;
    	
    	/**
    	 * Creates a new banker listener.
    	 * 
    	 * @param player
    	 * 	the player.
    	 */
    	public BankerListener(Player player) {
    	    this.player = player;
    	}

	@Override
	public void interfaceClosed() {
	    player.setDialog(null);
	}

	@Override
	public boolean buttonClicked(int button) {
	    switch(button) {
	    case 2471:
		if(player.getDialog() != null) {
		    switch(player.getDialog().getDialogueID()) {
		    case 2:
			BankUtils.openBank(player);
			break;
		    }
		}
		break;
	    case 2472:
		if(player.getDialog() != null) {
		    switch(player.getDialog().getDialogueID()) {
		    case 2:
			if(player.isPinSet()) {
			    //should we set this as to remove the bank pin?
			    player.getInterfaceSet().close();
			} else {
			    player.getInterfaceSet().close();
			    player.getInterfaceSet().openEnterAmountDialog(new BankPinEnterAmountListener(player));
			}
			break;
		    }
		}
		break;
	    case 2473:
		player.getInterfaceSet().close();
		break;
	    }
	    return false;
	}

	@Override
	public void continued() {
	    if(player.getDialog() != null) {
		switch(player.getDialog().getDialogueID()) {
		case 1://send options
		    DialogueSender.sendOptionThreeLines(player, this, "Access my bank.", (player.isPinSet() ? "Nothing for now either." : "Set a Bank pin."), "Never mind.", player.getDialog().getNextDialogue());
		    break;
		}
	    }
	}
}
