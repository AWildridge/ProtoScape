package org.apollo.game.content.cluescroll;

import org.apollo.game.event.impl.OpenInterfaceEvent;
import org.apollo.game.event.impl.SetInterfaceTextEvent;
import org.apollo.game.model.Player;
import org.apollo.game.model.Position;
import org.apollo.util.Misc;

/**
 * @author Rodrigo Molina
 */
public class LevelOneHandler {

	private Player player;
	
	private LevelOneText[] stageDone;
	private int stage;
	private LevelOneText text;
	
	protected LevelOneHandler(Player player) {
		this.player = player;
	}
	
	public void showInterface(int itemId) {
		player.send(new OpenInterfaceEvent(ClueScrolls.CLUE_TEXT_INTERFACE));
		if(stageDone == null && text == null && stage == 0) {
			text = LevelOneText.values()[Misc.random(LevelOneText.values().length - 1)];
			stage = 1;
			stageDone = new LevelOneText[10];
		}
		int start = 6968;
		int end = 6975;
		for(int i = 0; i < text.getText().length; i++) {
			player.send(new SetInterfaceTextEvent(start, text.getText()[i]));
			start++;
			if(start == end) break;
		}
	}
	
	private enum LevelOneText {
		//max is 8
		COOK_LUMBRIDGE(new String[] {"", "", "", "Talk to the cook in the lumbridge castle.", "", "", "", ""})
		
		;
		
		
		private String[] text;
		private int npcTalkId;
		private Position bury;
		
		LevelOneText(String[] text) {
			this.setText(text);
		}
		public String[] getText() {
			return text;
		}
		public void setText(String[] text) {
			this.text = text;
		}
	}
}
