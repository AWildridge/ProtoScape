package org.apollo.game.content.quest;

/**
 * Represents a basic quest.  This type of quest represents a vey easy quest.
 *
 *
 * @author Rodrigo Molina
 */
public interface Quest {

        /**
         * Give's the player their reward that they have earned for finishing this quest.
         */
        public void giveReward();
        
        /**
         * Get's the name of the quest that show sup on the interface.
         * @return
         */
        public String getQuestName();
        
        /**
         * Fill's up the steps array that this quest has.
         * 
         * @return The quest.
         */
        public Quest fillSteps();
        
        /**
         * The points given when the player finishes this quest.
         * 
         * @return the points rewarded.
         */
        public int pointsGiven();
        
        /**
         * The difficulty of this quest.
         * 
         * @return the {@link QuestDifficulty} enum type.
         */
        public QuestDifficulty getDifficulty();
        
        /**
         * The length of this quest.
         * 
         * @return the {@link QuestLength} enum type.
         */
        public QuestLength getLength();
}
