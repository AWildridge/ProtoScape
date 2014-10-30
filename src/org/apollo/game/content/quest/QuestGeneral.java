package org.apollo.game.content.quest;

import org.apollo.game.content.quest.impl.steps.Step;
import org.apollo.game.event.impl.OpenInterfaceEvent;
import org.apollo.game.event.impl.SetInterfaceTextEvent;
import org.apollo.game.model.Player;
import org.apollo.game.model.inter.quest.QuestConstants;

/**
 * @author Rodrigo Molina
 */
public abstract class QuestGeneral {

    protected Step[] steps;
    protected int currentStepSlot;

    /**
     * Gets the current step that this player is in.
     *
     * @return the step.
     */
    public Step getCurrentStep() {
        return steps[currentStepSlot];
    }

    public int getStepSlot() {
        return currentStepSlot;
    }

    public void displayInterface(Player player) {
        player.send(new SetInterfaceTextEvent(QuestConstants.QUEST_TEXT[0], ((Quest) (this)).getQuestName()));
        String[] localStep = player.getQuestHolder().hasStartedQuest((Quest) this) ? getCurrentStep().getText() : this.getFirstStep().getText();
        int length = 0;
        for (int a = 1; a < localStep.length; a++) {
            player.send(new SetInterfaceTextEvent(QuestConstants.QUEST_TEXT[a], localStep[a]));
            length++;
        }
        //now get rid of the rest.
        for (int a = ++length; a < QuestConstants.QUEST_TEXT.length; a++) {
            player.send(new SetInterfaceTextEvent(QuestConstants.QUEST_TEXT[a], ""));
        }
        player.send(new OpenInterfaceEvent(QuestConstants.QUEST_INTERFACE));
    }

    /**
     * Check's if the current step equals the index or the step specified.
     *
     * @param index The index of the step slot.
     * @return true if the current slot is equal to the index.
     */
    public boolean checkStep(int index) {
        return currentStepSlot == index;
    }

    public void increaseStep() {
        if (steps[currentStepSlot].moveOn()) {
            currentStepSlot++;
        }
    }

    public void executeStep() {
        if (steps[currentStepSlot] != null) {
            steps[currentStepSlot].executeStep();
        }
    }

    public void executeFirstStep() {
        steps[0].executeStep();
    }

    public void executeLastStep() {
        steps[steps.length - 1].executeStep();
    }

    /**
     * Gets the first step of this quest.
     *
     * @return {@link steps} index 0
     */
    public Step getFirstStep() {
        return steps[0];
    }

    /**
     * Gets the step by the index specified.
     *
     * @param step The step to grab.
     * @return the {@link steps} step.
     */
    public Step getStep(int step) {
        return steps[step];
    }
}