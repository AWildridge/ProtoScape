package org.apollo.game.content.quest.impl.cooks;

import org.apollo.game.content.quest.Quest;
import org.apollo.game.content.quest.impl.steps.Step;
import org.apollo.game.model.Player;
import org.apollo.game.model.inter.dialog.DialogueSender;
import org.apollo.game.model.inter.dialog.DialogueSender.HeadAnimations;
import org.apollo.game.model.inter.dialog.impl.CookListener;
import org.apollo.game.model.inter.dialog.impl.DefaultListener;

/**
 * The first step of cooks assistant.
 * 
 *
 * @author Rodrigo Molina
 */
public class FirstStep extends Step {

	/**
	 * The current player.
	 */
	private Player player;

	/**
	 * Creates the first step.
	 * 
	 * @param quest
	 * 	The quest.
	 * @param player
	 * 	The player.
	 */
	public FirstStep(Quest quest, Player player) {
		super(quest);
		this.player = player;
	}

	private String[] firstDialogue = {
		"Oh dear, oh dear. I'm in a terrible terrible",
		"mess!  It's the Dukes' birthday today, and I should be",
		"making him a lovely big birthday cake.",
	};

	private String[] secondDialogue = {
		"I've forgotten to buy the ingredients. I'll never get",
		"them in time now. He'll sack me!  What will I do? I have",
		"four children and a goat to look after. Would you help",
		"me?  Please?"
	};

	private String[] thirdOptions = {
		"I'm always happy to help a cook in distress.",
		"I can't right now, Maybe later."
	};

	@Override
	public void executeStep() {
	    if (player.getQuestHolder().hasStartedQuest(getQuest())) {
		    DialogueSender.sendNPCChatTwoLines(player, new DefaultListener(player), "Please return the ingredients", "for the cake in time.", 278, 1, "Cook", HeadAnimations.DEFAULT);
		    return;
	    }
	    switch (getStep()) {
			case -1:
				this.increaseStep();
			case 0:
				DialogueSender.sendNPCChatThreeLines(player, new CookListener(player), firstDialogue[0], firstDialogue[1], firstDialogue[2], 278, 5, "Cook", HeadAnimations.NEARLYCRYING);
				this.increaseStep();
				break;
			case 1:
				DialogueSender.sendNPCChatFourLines(player, new CookListener(player), secondDialogue[0], secondDialogue[1], secondDialogue[2], secondDialogue[3], 278, 6, "Cook", HeadAnimations.MORESAD);
				this.increaseStep();
				break;
			case 2:
				DialogueSender.sendOptionTwoLines(player, new CookListener(player), thirdOptions[0], thirdOptions[1], 7);
				this.increaseStep();
				break;
			case 3:
				player.getQuestHolder().startQuest(getQuest());
				player.getInterfaceSet().close();
				player.setDialog(null);
				this.resetStep();
				break;
	    }
	}

	@Override
	public boolean moveOn() {
	    return getStep() == -1;
	}

	@Override
	public String[] getText() {
	    return interfaceText;
	}

	private String[] interfaceText = {
		"",
		 "Talk to the Cook inside the Lumbridge Castle."
		
	};
}
