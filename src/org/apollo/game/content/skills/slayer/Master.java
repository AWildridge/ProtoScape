package org.apollo.game.content.skills.slayer;

/**
 * Encapsulates the data for each of the slayer masters.
 * @author The Wanderer
 */
public enum Master {
 
    TURAEL(TaskSet.TURAEL, 70, 1, 3),
    MAZCHNA(TaskSet.MAZCHNA, 1596, 1, 20),
    VANNAKAA(TaskSet.VANNAKAA, 1597, 1, 40),
    CHAELDAR(TaskSet.CHAELDAR, 1598, 1, 70),
    DURADEL(TaskSet.DURADEL, 1599, 50, 100);

    TaskSet task;
    int id;
    int slayerReq;
    int combatLevelReq;
    
    private Master(TaskSet task, int id, int slayerReq, int combatLevelReq) {
        this.task = task;
        this.id = id;
        this.slayerReq = slayerReq;
        this.combatLevelReq = combatLevelReq;
    }

    public int getCombatLevelReq() {
        return combatLevelReq;
    }

    public void setCombatLevelReq(int combatLevelReq) {
        this.combatLevelReq = combatLevelReq;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSlayerReq() {
        return slayerReq;
    }

    public void setSlayerReq(int slayerReq) {
        this.slayerReq = slayerReq;
    }

    public TaskSet getTask() {
        return task;
    }

    public void setTask(TaskSet task) {
        this.task = task;
    }
    
}
