package org.apollo.game.model.inter.dialog;

import org.apollo.game.event.impl.ChatHeadAnimationEvent;
import org.apollo.game.event.impl.SendNPCHeadEvent;
import org.apollo.game.event.impl.SendPlayerHeadEvent;
import org.apollo.game.event.impl.SetInterfaceTextEvent;
import org.apollo.game.model.Animation;
import org.apollo.game.model.Player;
import org.apollo.util.TextUtil;

/**
 * An class to send different dialogue types.
 *
 *
 *
 * @author Rodrigo Molina
 */
public class DialogueSender {
    	
	/**
	 * Sends a player chat meaning that the player head shows and he's talking back.
	 * 
	 * @param player
	 * 	The player.
	 * @param listener
	 * 	The stalker.
	 * @param firstText
	 * 	The first text to display.
	 * @param secondText
	 * 	The second text to display.
	 * @param thirdText
	 * 	The third text to display.
	 * @param fourthText
	 * 	The fourth text to display.
	 * @param dialogueId
	 * 	The dialogue id.
	 * @param anim
	 * 	The animation.
	 */
	public static void sendPlayerChatFourLines(Player player, DialogueListener listener, String firstText, String secondText, String thirdText, String fourthText, int dialogueId, HeadAnimations anim) {
	    player.send(new ChatHeadAnimationEvent(987, Animation.create(anim.getAnim())));
	    player.send(new SendPlayerHeadEvent(987));
	    player.send(new SetInterfaceTextEvent(989, firstText));
	    player.send(new SetInterfaceTextEvent(990, secondText));
	    player.send(new SetInterfaceTextEvent(991, thirdText));
	    player.send(new SetInterfaceTextEvent(992, fourthText));
	    player.send(new SetInterfaceTextEvent(988, TextUtil.capitalize(player.getUndefinedName())));
	    player.getInterfaceSet().openDialogue(listener, 986);
	    player.setDialog(new Dialogue(986, dialogueId, listener));
	}
	
	/**
	 * Sends a player chat meaning that the player head shows and he's talking back.
	 * 
	 * @param player
	 * 	The player.
	 * @param listener
	 * 	The stalker.
	 * @param firstText
	 * 	The first text to display.
	 * @param secondText
	 * 	The second text to display.
	 * @param thirdText
	 * 	The third text to display.
	 * @param dialogueId
	 * 	The dialogue id.
	 * @param anim
	 * 	The animation.
	 */
	public static void sendPlayerChatThreeLines(Player player, DialogueListener listener, String firstText, String secondText, String thirdText, int dialogueId, HeadAnimations anim) {
	    player.send(new ChatHeadAnimationEvent(980, Animation.create(anim.getAnim())));
	    player.send(new SendPlayerHeadEvent(980));
	    player.send(new SetInterfaceTextEvent(982, firstText));
	    player.send(new SetInterfaceTextEvent(983, secondText));
	    player.send(new SetInterfaceTextEvent(984, thirdText));
	    player.send(new SetInterfaceTextEvent(981, TextUtil.capitalize(player.getUndefinedName())));
	    player.getInterfaceSet().openDialogue(listener, 979);
	    player.setDialog(new Dialogue(979, dialogueId, listener));
	}
	
    	/**
    	 * Sends a player chat meaning that the player head shows and he's talking back.
    	 * 
    	 * @param player
    	 * 	The player.
    	 * @param listener
    	 * 	The stalker.
    	 * @param firstText
    	 * 	The first text to display.
    	 * @param secondText
    	 * 	The second text to display.
    	 * @param dialogueId
    	 * 	The dialogue id.
    	 * @param anim
    	 * 	The animation.
    	 */
    	public static void sendPlayerChatTwoLines(Player player, DialogueListener listener, String firstText, String secondText, int dialogueId, HeadAnimations anim) {
    	    player.send(new ChatHeadAnimationEvent(974, Animation.create(anim.getAnim())));
    	    player.send(new SendPlayerHeadEvent(974));
    	    player.send(new SetInterfaceTextEvent(976, firstText));
    	    player.send(new SetInterfaceTextEvent(977, secondText));
    	    player.send(new SetInterfaceTextEvent(975, TextUtil.capitalize(player.getUndefinedName())));
    	    player.getInterfaceSet().openDialogue(listener, 973);
    	    player.setDialog(new Dialogue(973, dialogueId, listener));
    	}
    	
   	/**
    	 * Sends a player chat meaning that the player head shows and he's talking back.
    	 * 
    	 * @param player
    	 * 	The player.
    	 * @param listener
    	 * 	The stalker.
    	 * @param firstText
    	 * 	The first text to display.
    	 * @param dialogueId
    	 * 	The dialogue id.
    	 * @param anim
    	 * 	The animation.
    	 */
    	public static void sendPlayerChatOneLine(Player player, DialogueListener listener, String firstText, int dialogueId, HeadAnimations anim) {
    	    player.send(new ChatHeadAnimationEvent(969, Animation.create(anim.getAnim())));
    	    player.send(new SendPlayerHeadEvent(969));
    	    player.send(new SetInterfaceTextEvent(971, firstText));
    	    player.send(new SetInterfaceTextEvent(970, TextUtil.capitalize(player.getUndefinedName())));
    	    player.getInterfaceSet().openDialogue(listener, 968);
    	    player.setDialog(new Dialogue(968, dialogueId, listener));
    	}
    	
    	/**
    	 * Sends an npc chat dialogue with only one line.
    	 * 
    	 * @param player
    	 * 	the player
    	 * @param listener
    	 * 	the listener
    	 * @param firstLine
    	 * 	the first line
    	 * @param npcID
    	 * 	the npc id
    	 * @param dialogueId
    	 * 	the dialogue id
    	 * @param npcName
    	 * 	the npc name
    	 */
    	public static void sendNPCChatOneLine(Player player, DialogueListener listener, String firstLine, int npcID, int dialogueId, String npcName, HeadAnimations anim) {
    	    player.send(new SendNPCHeadEvent(4883, npcID));
    	    player.send(new ChatHeadAnimationEvent(4883, Animation.create(anim.getAnim())));
    	    player.send(new SetInterfaceTextEvent(4885, firstLine));
    	    player.send(new SetInterfaceTextEvent(4884, npcName));
    	    player.getInterfaceSet().openDialogue(listener, 4882);
    	    player.setDialog(new Dialogue(4882, dialogueId, listener));
    	}
    	
    	/**
    	 * Sends an npc chat dialogue with only two lines to display.
    	 * 
    	 * @param player
    	 * 	The player.
    	 * @param listener
    	 * 	The listener of the dialogue.
    	 * @param firstText
    	 * 	The first text to display.
    	 * @param secondText
    	 * 	The second text to display.
    	 * @param npcID
    	 * 	The npc id of the head to display (the npc chatting).
    	 * @param dialogueId
    	 * 	The dialogue id.
    	 * @param npcName
    	 * 	The npc name.
    	 * @param anim
    	 * 	The type of head animation to display.
    	 */
    	public static void sendNPCChatTwoLines(Player player, DialogueListener listener, String firstText, String secondText, int npcID, int dialogueId, String npcName, HeadAnimations anim) {
    	    player.send(new SendNPCHeadEvent(4888, npcID));
    	    player.send(new ChatHeadAnimationEvent(4888, Animation.create(anim.getAnim())));
    	    player.send(new SetInterfaceTextEvent(4890, firstText));
    	    player.send(new SetInterfaceTextEvent(4891, secondText));
    	    player.send(new SetInterfaceTextEvent(4889, npcName));
    	    player.getInterfaceSet().openDialogue(listener, 4887);
    	    player.setDialog(new Dialogue(4887, dialogueId, listener));
    	}
    	
    	/**
    	 * Sends an npc chat dialogue with only three lines to display.
    	 * 
    	 * @param player
    	 * 	The player.
    	 * @param listener
    	 * 	The listener.
    	 * @param firstText
    	 * 	The first text to display.
    	 * @param secondText
    	 * 	The second text to display.
    	 * @param thirdText;
    	 * 	The third text to display.
    	 * @param npcID
    	 * 	The npc id of the head to display (the npc chatting).
    	 * @param dialogueId
    	 * 	The dialogue id.
    	 * @param npcName
    	 * 	The npc name.
    	 * @param anim
    	 * 	The type of head animation to display.
    	 */
    	public static void sendNPCChatThreeLines(Player player, DialogueListener listener, String firstText, String secondText, String thirdText, int npcID, int dialogueId, String npcName, HeadAnimations anim) {
    	    player.send(new SendNPCHeadEvent(4895, npcID));
    	    player.send(new ChatHeadAnimationEvent(4895, Animation.create(anim.getAnim())));
    	    player.send(new SetInterfaceTextEvent(4896, firstText));
    	    player.send(new SetInterfaceTextEvent(4897, secondText));
    	    player.send(new SetInterfaceTextEvent(4898, thirdText));
    	    player.send(new SetInterfaceTextEvent(4895, npcName));
    	    player.getInterfaceSet().openDialogue(listener, 4893);
    	    player.setDialog(new Dialogue(4893, dialogueId, listener));
    	}
    	
    	/**
    	 * Sends the npc chat dialogue with four lines to display.
    	 * 
    	 * @param player
    	 * 	The player.
    	 * @param listener
    	 * 	The listener to stalk on this dialogue chat.
    	 * @param firstText
    	 * 	The first text to display.
    	 * @param secondText
    	 * 	The second text to display.
    	 * @param thirdText
    	 * 	The third text to display.
    	 * @param fourthText
    	 * 	The fourth text to display.
    	 * @param npcID
    	 * 	The npc id of the head npc to display on dialogue chat.
    	 * @param dialogueId
    	 * 	The dialogue if of this chat.
    	 * @param npcName
    	 * 	The npc name to display.
    	 * @param anim
    	 * 	The type of head animation to display.
    	 */
    	public static void sendNPCChatFourLines(Player player, DialogueListener listener, String firstText, String secondText, String thirdText, String fourthText, int npcID, int dialogueId, String npcName, HeadAnimations anim) {
   	    player.send(new SendNPCHeadEvent(4901, npcID));//TODO: this isnt showing
    	    player.send(new ChatHeadAnimationEvent(4901, Animation.create(anim.getAnim())));
   	    player.send(new SetInterfaceTextEvent(4902, npcName));
    	    player.send(new SetInterfaceTextEvent(4903, firstText));
    	    player.send(new SetInterfaceTextEvent(4904, secondText));
    	    player.send(new SetInterfaceTextEvent(4905, thirdText));
    	    player.send(new SetInterfaceTextEvent(4906, fourthText));
    	    player.getInterfaceSet().openDialogue(listener, 4900);
    	    player.setDialog(new Dialogue(4900, dialogueId, listener));
    	}
    	
    	/**
    	 * Sends a single statement.
    	 * 
    	 * @param player
    	 * 	the player.
    	 * @param text
    	 * 	The text to display.
    	 * @param listener
    	 * 	The listener.
    	 * @param dialogueId
    	 * 	the dialogue id.
    	 */
    	public static void sendSingleStatement(Player player, String text, DialogueListener listener, int dialogueId) {
    	    player.send(new SetInterfaceTextEvent(357, text));
    	    player.send(new SetInterfaceTextEvent(358, "Click here to continue"));
    	    player.getInterfaceSet().openDialogue(listener, 356);
    	    player.setDialog(new Dialogue(356, dialogueId, listener));
    	}
    	
    	/**
    	 * Sends an option of two lines for the player to choose from.
    	 * 
    	 * @param player
    	 * 	The player.
    	 * @param listener
    	 * 	The stalker stalking the dialogue.
    	 * @param firstText
    	 * 	The first text.
    	 * @param secondText
    	 * 	The second text.
    	 * @param dialogueId
    	 * 	The dialogue id of this particular dialogue.
    	 */
    	public static void sendOptionTwoLines(Player player, DialogueListener listener, String firstText, String secondText, int dialogueId) {
    	    player.send(new SetInterfaceTextEvent(2460, "Select an option"));
    	    player.send(new SetInterfaceTextEvent(2461, firstText));
    	    player.send(new SetInterfaceTextEvent(2462, secondText));
    	    player.getInterfaceSet().openDialogue(listener, 2459);
    	    player.setDialog(new Dialogue(2459, dialogueId, listener));
    	    
    	}

   	/**
    	 * Sends an option of two lines for the player to choose from.
    	 * 
    	 * @param player
    	 * 	The player.
    	 * @param listener
    	 * 	The stalker stalking the dialogue.
    	 * @param firstText
    	 * 	The first text.
    	 * @param secondText
    	 * 	The second text.
    	 * @param thirdText
    	 * 	The third text.
    	 * @param dialogueId
    	 * 	The dialogue id of this particular dialogue.
    	 */
    	public static void sendOptionThreeLines(Player player, DialogueListener listener, String firstText, String secondText, String thirdText, int dialogueId) {
    	    player.send(new SetInterfaceTextEvent(2470, "Select an option"));
    	    player.send(new SetInterfaceTextEvent(2471, firstText));
    	    player.send(new SetInterfaceTextEvent(2472, secondText));
    	    player.send(new SetInterfaceTextEvent(2473, thirdText));
    	    player.getInterfaceSet().openDialogue(listener, 2469);
    	    player.setDialog(new Dialogue(2469, dialogueId, listener));
    	}
    	
    	/**
    	 * Sends an option of four lines.
    	 * 
    	 * @param player
    	 * 	the player.
    	 * @param listener
    	 * 	the listener.
    	 * @param firstText
    	 * 	the first text
    	 * @param secondText
    	 * 	the second text
    	 * @param thirdText
    	 * 	the third text
    	 * @param fourthText
    	 * 	the fourth text
    	 */
    	public static void sendOptionFourLines(Player player, DialogueListener listener, String firstText, String secondText, String thirdText, String fourthText, int dialogueId) {
    	    player.send(new SetInterfaceTextEvent(2481, "Select an option"));
    	    player.send(new SetInterfaceTextEvent(2482, firstText));
    	    player.send(new SetInterfaceTextEvent(2483, secondText));
    	    player.send(new SetInterfaceTextEvent(2484, thirdText));
    	    player.send(new SetInterfaceTextEvent(2485, fourthText));
    	    player.getInterfaceSet().openDialogue(listener, 2480);
    	    player.setDialog(new Dialogue(2480, dialogueId, listener));
    	}
    	
    	/**
    	 * The npc/player head animations for the chat display.
    	 *
    	 *
    	 *
    	 * @author Rodrigo Molina
    	 */
    	public enum HeadAnimations {
		HAPPY(588), // - Joyful/happy
		CALM1(589), // - Speakingly calmly
		CALM2(590), // - Calm talk
		DEFAULT(591), // - Default speech
		EVIL(592), //  - Evil
		EVILCONTINUED(593), //  - Evil continued
		DELIGHTEDEVIL(594), //  - Delighted evil
		ANNOYED(595), //  - Annoyed
		DISTRESSED(596), //  - Distressed
		DISTRESSEDCONTINUED(597), //  - Distressed continued
		ALMOSTCRYING(598), //  - Almost crying
		BOWSHEADWHILESAD(599), //  - Bows head while sad
		DRUNKTOLEFT(600), //  - Talks and looks sleepy/drunk to left
		DRUNKTORIGHT(601), //  - Talks and looks sleepy/drunk to right
		DISINTERESTED(602), //  - Sleepy or disinterested
		SPEELY(603), //  - Tipping head as if sleepy.
		PLAINEVIL(604), //  - Plain evil (Grits teeth and moves eyebrows)
		LAUGH1(605), //  - Laughing or yawning
		LAUGH2(606), //  - Laughing or yawning for longer
		LAUGH3(607), //  - Laughing or yawning for longer
		LAUGH4(608), //  - Laughing or yawning
		EVILLAUGH(609), //  - Evil laugh then plain evil
		SAD(610), //  - Slightly sad
		MORESAD(611), //  - Quite sad
		ONONEHAND(612), //  - On one hand...
		NEARLYCRYING(613), //  - Close to crying
		ANGER1(614), //  - Angry
		ANGER2(615), //  - Angry
		ANGER3(616), //  - Angry
		ANGER4(617); //  - Angry
    	    
    	    private int anim;
    	    
    	    HeadAnimations(int anim) {
    		this.anim = anim;
    	    }
    	    
    	    public int getAnim() {
    		return anim;
    	    }
    	}
    	
}
