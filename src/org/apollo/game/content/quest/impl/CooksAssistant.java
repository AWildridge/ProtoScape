package org.apollo.game.content.quest.impl;

import org.apollo.game.content.quest.Quest;
import org.apollo.game.content.quest.QuestDifficulty;
import org.apollo.game.content.quest.QuestGeneral;
import org.apollo.game.content.quest.QuestLength;
import org.apollo.game.content.quest.impl.cooks.FirstStep;
import org.apollo.game.content.quest.impl.cooks.SecondStep;
import org.apollo.game.content.quest.impl.steps.Step;
import org.apollo.game.model.Player;
import org.apollo.game.model.Skill;

/**
 * The cooks assistant quest.  minor quest as the start.
 *
 *
 * @author Rodrigo Molina
 */
public class CooksAssistant extends QuestGeneral implements Quest {
    	
    	/**
    	 * The points given.
    	 */
    	public final int POINTS_GIVEN = 1;
    	
    	/**
    	 * The player.
    	 */
    	private Player player;
    	
    	/**
    	 * The max steps of this quest.
    	 */
    	private final int MAX_STEPS = 2;
    	
    	
    	/**
    	 * Creates a new cooks assistant.
    	 * 
    	 * @param player
    	 * 	The player.
    	 */
	public CooksAssistant(Player player) {
		super();
		this.player = player;
    	}
    	
	/**
	 * Fill's up the steps array.
	 */
	@Override
    	public CooksAssistant fillSteps() {
    	    steps = new Step[MAX_STEPS];
    	    steps[0] = new FirstStep(this, player);
    	    steps[1] = new SecondStep(this, player);
    	    //add more steps here.
	    return this;
    	}

	@Override
	public void giveReward() {
	    steps = null;
	    player.getQuestHolder().finishedQuest(this);
	    //now display the reward interface and give them their items.
	    player.getSkillSet().addExperience(Skill.COOKING, 300);
	    player.getInventory().add(500, 995);
	    player.getInventory().add(325, 20);
	}

	@Override
	public int pointsGiven() {
	    return this.POINTS_GIVEN;
	}

	@Override
	public QuestDifficulty getDifficulty() {
	    return QuestDifficulty.VERY_EASY;
	}

	@Override
	public QuestLength getLength() {
	    return QuestLength.VERY_SHORT;
	}

	@Override
	public String getQuestName() {
		return "Cook's Assistant";
	}
}