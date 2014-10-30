package org.apollo.game.model.inter.dialog.impl;

import org.apollo.game.model.Player;
import org.apollo.game.model.inter.dialog.DialogueListener;
import org.apollo.game.model.inter.dialog.DialogueSender;
import org.apollo.game.model.inter.dialog.DialogueSender.HeadAnimations;

/**
 * a listener for the npc cook dialogue.
 *
 *
 * @author Rodrigo Molina
 */
public class CookListener implements DialogueListener {

    	/**
    	 * The player.
    	 */
    	private Player player;
    	
    	/**
    	 * Creates a new cook listener.
    	 * 
    	 * @param player
    	 * 	the player
    	 */
    	public CookListener(Player player) {
    	    this.player = player;
    	}
    	
        @Override
        public void interfaceClosed() {
    		player.setDialog(null);
    		if(player.getQuestHolder().getCookAssistant().getFirstStep() != null && player.getQuestHolder().getCookAssistant().getFirstStep().getStep() >= 3)
    		    player.getQuestHolder().getCookAssistant().getFirstStep().resetStep();
        }
    
        @Override
        public boolean buttonClicked(int button) {
            System.out.println("buton clicked in dialogues: "+button);
            switch(button) {
            	case 2482://first option
            	case 2461:
            	    if(player.getDialog() != null) {
            		switch(player.getDialog().getDialogueID()) {
            		case 3:
            		    if(!player.getQuestHolder().hasStartedQuest(player.getQuestHolder().getCookAssistant()))
            			DialogueSender.sendPlayerChatOneLine(player, this, "What's wrong?", 10, HeadAnimations.HAPPY);
            		    else
            			DialogueSender.sendPlayerChatOneLine(player, this, "I forgot what to do.", 11, HeadAnimations.HAPPY);
            		    break;
            		case 7:
            		    DialogueSender.sendPlayerChatOneLine(player, new DefaultListener(player), "I'm always happy to help a cook in distress.", 7, HeadAnimations.HAPPY);
            		    player.getQuestHolder().startQuest(player.getQuestHolder().getCookAssistant());
            		    break;
            		}
            	    }
            	    break;
            	case 2462:
            	case 2483://second option
            	    if(player.getDialog() != null) {
            		switch(player.getDialog().getDialogueID()) {
            		case 3:
            		    DialogueSender.sendPlayerChatOneLine(player, this, "Can you make me a cake?", 15, HeadAnimations.DEFAULT);
            		    break;
            		case 7:
            		    DialogueSender.sendPlayerChatOneLine(player, new DefaultListener(player), "I can't right now.  Maybe later.", 10, HeadAnimations.SAD);
            		    player.getQuestHolder().getCookAssistant().getCurrentStep().resetStep();
            		    break;
            		}
            	    }
            	    break;
            	case 2484://third option
            	    if(player.getDialog() != null) {
            		switch(player.getDialog().getDialogueID()) {
            		case 3:
            		    DialogueSender.sendPlayerChatOneLine(player, this, "You don't look very happy!", player.getDialog().getNextDialogue(), HeadAnimations.DEFAULT);
            		    break;
            		}
            	    }
            	    break;
            	case 2485://fourth option
            	    if(player.getDialog() != null) {
            		switch(player.getDialog().getDialogueID()) {
            		case 3:
            		    DialogueSender.sendPlayerChatOneLine(player, this, "Nice hat!", player.getDialog().getNextDialogue(), HeadAnimations.DEFAULT);
            		    break;
            		}
            	    }
            	    break;
            }
    	    	return false;
        }
    
        @Override
        public void continued() {
            if(player.getDialog() != null) {
        	switch(player.getDialog().getDialogueID()) {
        	case 5:
        	case 6:
        	    player.getQuestHolder().getCookAssistant().executeStep();
        	    break;
        	case 2:
        	    if(!player.getQuestHolder().hasStartedQuest(player.getQuestHolder().getCookAssistant()))
        		DialogueSender.sendOptionFourLines(player, this, "What's wrong?", "Can you make me a cake?", "You don't look very happy!", "Nice hat!", player.getDialog().getNextDialogue());
        	    else if (!player.getQuestHolder().hasDoneQuest(player.getQuestHolder().getCookAssistant()))
        		DialogueSender.sendOptionFourLines(player, this, "What am I supposed to be doing again?", "Can you make me a cake?", "You don't look very happy!", "Nice hat!", player.getDialog().getNextDialogue());
        	    //else
        	    //they finished the quest.
        	    break;
        	case 7:
        	    if(!player.getQuestHolder().hasDoneQuest(player.getQuestHolder().getCookAssistant()) && player.getQuestHolder().hasStartedQuest(player.getQuestHolder().getCookAssistant())) {
         		player.getQuestHolder().startQuest(player.getQuestHolder().getCookAssistant());
         	    }
        	    DialogueSender.sendNPCChatTwoLines(player, new DefaultListener(player), "Thank you so much for accepting to help.", "I hope to see you soon.", 278, 1, "Cook", HeadAnimations.HAPPY);
        	    break;
        	case 10:
        	    player.getQuestHolder().getCookAssistant().executeFirstStep();
        	    break;
        	case 11:
        	    DialogueSender.sendNPCChatTwoLines(player, new DefaultListener(player), "You need to get the ingredients", " for me to bake a cake.", 278, 12, "Cook", HeadAnimations.DEFAULT);
        	    break;
        	case 4:
        	    if(!player.getQuestHolder().hasStartedQuest(player.getQuestHolder().getCookAssistant()))
        		DialogueSender.sendOptionFourLines(player, this, "What's wrong?", "Can you make me a cake?", "You don't look very happy!", "Nice hat!", 3);
        	    else if (!player.getQuestHolder().hasDoneQuest(player.getQuestHolder().getCookAssistant()))
        		DialogueSender.sendOptionFourLines(player, this, "What am I supposed to be doing again?", "Can you make me a cake?", "You don't look very happy!", "Nice hat!", 3);
        	    break;
        	case 15:
        	    DialogueSender.sendNPCChatOneLine(player, new DefaultListener(player), "No, I cannot.  I do not have the ingredients.", 278, 12, "Cook", HeadAnimations.DEFAULT);
        	    break;
        	}
            }
        }

}
