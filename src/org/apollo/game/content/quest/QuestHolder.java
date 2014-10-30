package org.apollo.game.content.quest;

import org.apollo.game.content.quest.impl.BlackKnightsFortress;
import org.apollo.game.content.quest.impl.CooksAssistant;
import org.apollo.game.model.Player;
import org.apollo.util.ArrayUtils;

/**
 * An class to hold the quest data for each player.
 *
 *
 * @author Rodrigo Molina
 */
public class QuestHolder {

    /**
     * The list of quests that this player has started.
     */
    private Quest questsStarted[] = new Quest[255];
    /**
     * The quests finished.
     */
    private Quest questsFinished[] = new Quest[255];
    //now load this..
    /**
     * The total quests points this player has.
     */
    private int questPoints;
    /**
     * The player whom we are holding the quest for.
     */
    private Player player;

    /**
     * Creates a new quest holder.
     * 
     * @param player
     * 	The player.
     */
    public QuestHolder(Player player) {
        this.player = player;
        cooks = new CooksAssistant(player).fillSteps();
        blackKnights = new BlackKnightsFortress(player).fillSteps();
    }
    /**
     * The cooks assistant quest.
     */
    private Quest cooks;
    /**
     * The black knights quest.
     */
    private Quest blackKnights;

    /**
     * Starts the first step.
     * 
     * @param quest
     * 	The quest.
     */
    public void startFirstStep(Quest quest) {
        ((QuestGeneral) quest).executeFirstStep();
    }

    /**
     * Starts the quest.
     * 
     * @param quest
     * 	The quest started.
     */
    public void startQuest(Quest quest) {
        questsStarted[ArrayUtils.getFreeSlot(questsStarted)] = quest;
        ((QuestGeneral) quest).increaseStep();
        player.updateQuestTab();
    }

    /**
     * Checks if the player has done this quest or not or even started it.
     * 
     * @param quest
     * 	the quest
     * @return true if the quests finished contains this quest.
     */
    public boolean hasDoneQuest(Quest quest) {
        for (int a = 0; a < questsFinished.length; a++) {
            if (questsFinished[a] != null) {
                if (questsFinished[a] == quest) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if the player has started this quest or not.
     * 
     * @param quest
     * 	The quest.
     * @return true if the quests started contains this quest.
     */
    public boolean hasStartedQuest(Quest quest) {
        for (int a = 0; a < questsStarted.length; a++) {
            if (questsStarted[a] != null) {
                if (questsStarted[a] == quest) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Gets the cooks assistant.
     *TODO: Should possibly find another way to incorporate this or store it somewhere else.
     * @return the cooks.
     */
    public CooksAssistant getCookAssistant() {
        return (CooksAssistant) cooks;
    }

    /**
     * Gets the black knights fortress quest.
     * 
     * @return the black knights fortress.
     */
    public BlackKnightsFortress getBlackKnightFortress() {
        return (BlackKnightsFortress) blackKnights;
    }

    /**
     * Get's the arrays in this class.
     * 
     * @return the values
     */
    public Object[] toArray() {
        return new Object[]{questPoints, questsStarted, questsFinished};
    }
//voids used for saving/loading.

    /**
     * Sets the attributes of this class.
     * 
     * @param questPoints
     * 	The total quests points.
     * @param quests
     * 	The quests.
     * @param questsFinished
     * 	The quests finished.
     */
    public void setAttributes(int questPoints, Quest[] quests, Quest[] questsFinished) {
        this.questPoints = questPoints;
        this.questsStarted = quests;
        this.questsFinished = questsFinished;
    }

    /**
     * Finishes the quest by adding it to the quests finished array.
     * 
     * @param quest
     * 	The quest to be added which is finished.
     */
    public void finishedQuest(Quest quest) {
        questPoints += quest.pointsGiven();
        for (int a = 0; a < this.questsStarted.length; a++) {
            if (questsStarted[a] != null) {
                if (questsStarted[a] == quest) {
                    questsStarted[a] = null;
                    break;
                }
            }
        }
        int slot = ArrayUtils.getFreeSlot(QuestHolder.this.questsFinished);
        if (slot != -1) {
            questsFinished[slot] = quest;
        }
    }
    
    public Quest[] getQuestsStarted() {
	    return questsStarted;
    }
    public Quest[] getQuestsFinished() {
	    return questsFinished;
    }
}
