package org.apollo.game.content.skills.slayer;

import java.util.Random;
import org.apollo.game.model.NPC;
import org.apollo.game.model.Player;
import org.apollo.game.model.def.NPCDefinition;

/**
 * Slayer.java
 * @author The Wanderer
 */
public class Slayer {
    
    public Task generateTask(Player player, NPC npc) {
        for(Master master : Master.values()) {
            if(master.getId() == npc.getDefinition().getId()) {
                TaskSet taskSet = master.getTask();
                Random random = new Random();
                int monsterIndex = random.nextInt(taskSet.getMonsters().length);
                //TODO: Logic for quest-only monsters
                /*if(task.getMonsters(monsterIndex) == Monster)*/
                int amountAssigned = taskSet.getTaskAmountLowHigh(monsterIndex, 0) + 
                            random.nextInt(taskSet.getTaskAmountLowHigh(monsterIndex, 1) - taskSet.getTaskAmountLowHigh(monsterIndex, 0));
                Task playerTask = new Task(player, taskSet.getMonsters(monsterIndex), amountAssigned);
                return playerTask;
            }
        }
        return null;
        
    }
    
}
