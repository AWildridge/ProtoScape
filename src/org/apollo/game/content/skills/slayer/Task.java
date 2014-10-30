package org.apollo.game.content.skills.slayer;

import org.apollo.game.model.Player;

/**
 * A task a player owns.
 * @author The Wanderer
 */
public class Task {
    
    Player player;
    Monster monster;
    int amountAssigned;
    
    public Task(Player player, Monster monster, int amountAssigned) {
        this.player = player;
        this.monster = monster;
        this.amountAssigned = amountAssigned;
    }

    public int getAmountAssigned() {
        return amountAssigned;
    }

    public void setAmountAssigned(int amountAssigned) {
        this.amountAssigned = amountAssigned;
    }

    public Monster getMonster() {
        return monster;
    }

    public void setMonster(Monster monster) {
        this.monster = monster;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
    
    
}
