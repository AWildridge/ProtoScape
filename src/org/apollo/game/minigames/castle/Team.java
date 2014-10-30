package org.apollo.game.minigames.castle;

import org.apollo.game.model.Player;

/**
 * Represents one of the teams in {@link CastleWars}
 *
 *
 * @author Rodrigo Molina
 */
public interface Team {

    	/**
    	 * Adds the player to the local list.
    	 * 
    	 * @param player
    	 *	the player
    	 * @return true if there was a free slot, false if there wasn't.
    	 */
    	public abstract boolean addPlayer(Player player);
    
    	/**
    	 * Get's the list of player's inside this team, regardless of position or state.
    	 * 
    	 * @return a list of {@link Player} currently registered in this team.
    	 */
    	public abstract Player[] getList();
    	
    	/**
    	 * Get's the size of the player's array.
    	 * 
    	 * @return the player's in the array.
    	 */
    	public abstract int getSize();
    	
    	/**
    	 * Start's the local game, by refreshing skills for players and teleporting them
    	 * inside castle wars.
    	 */
    	public abstract void startGame();
    	
    	/**
    	 * Check's if the player specified is inside this team.
    	 * 
    	 * @param player
    	 * 	The player to check if they are in the team.
    	 * @return true if this {@link Player} is in this team.
    	 */
    	public abstract boolean contains(Player player);
    	
    	/**
    	 * Enter's the waiting room.
    	 * 
    	 * @param player
    	 * 	The player to enter.
    	 */
    	public abstract void enterWaiting(Player player);
    	
    	/**
    	 * Leave's the waiting upon request.
    	 * 
    	 * @param player
    	 * 	The player leaving.
    	 */
    	public abstract void leaveWaiting(Player player);
    	
    	/**
    	 * This player has killed this player..
    	 * 
    	 * @param dead
    	 * 	The player who died.
    	 * @param killer
    	 * 	The killer.
    	 */
    	public abstract void killedPlayer(Player dead, Player killer);
    	
    	/**
    	 * Leave's the game for this player, should support logout leaving and such
    	 * 
    	 * @param player
    	 * 	The player leaving this current game.
    	 */
    	public abstract void leaveGame(Player player);
    	
    	/**
    	 * End's the game by reseting everything and teleporting the players to the main arena.
    	 */
    	public abstract void endGame();
}
