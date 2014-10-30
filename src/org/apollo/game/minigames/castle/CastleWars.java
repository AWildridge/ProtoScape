package org.apollo.game.minigames.castle;

import org.apollo.game.event.impl.SendConfigEvent;
import org.apollo.game.event.impl.SetInterfaceTextEvent;
import org.apollo.game.minigames.castle.sara.SaraFlag;
import org.apollo.game.minigames.castle.sara.SaraTeam;
import org.apollo.game.minigames.castle.zammy.ZammyFlag;
import org.apollo.game.minigames.castle.zammy.ZammyTeam;
import org.apollo.game.model.Player;
import org.apollo.game.model.Position;
import org.apollo.game.model.World;
import org.apollo.game.scheduling.ScheduledTask;
import org.apollo.util.Misc;

/**
 * Castle wars lol
 *
 *
 * @author Rodrigo Molina
 */
public class CastleWars {

    	private static CastleWars castleWars;
    	
    	public static CastleWars getCastleWars() {
    	    return castleWars == null ? new CastleWars() : castleWars;
    	}
    	
    	private CastleObjectHandler objects;
    	
    	public CastleObjectHandler getObjectHandler() {
    	    return objects;
    	}
    	
    	/**
    	 * The score of the zammy team.
    	 */
    	private short zammyScore;
    	
    	/**
    	 * The score of the sara score.
    	 */
    	private short saraScore;
    	
    	/**
    	 * The sara flag.
    	 */
    	private SaraFlag saraFlag;
    	
    	/**
    	 * The zammy flag.
    	 */
    	private ZammyFlag zammyFlag;
    	
    	/**
    	 * The max players allowed in each team.
    	 */
    	public static final int MAX_PLAYERS_ALLOWED = 50;
    	
    	/**
    	 * The walkable interface of the game
    	 */
    	public static final int GAME_INTERFACE = 11146;
    	
    	/**
    	 * The walkable interface of the waiting room
    	 */
    	public static final int WAIT_INTERFACE = 11344;
    	/**
    	 * The sara team.
    	 */
    	private Team saraTeam;
    	
    	/**
    	 * The zammy team
    	 */
    	private Team zammyTeam;
    	
    	/**
    	 * The flag if the game is running
    	 */
    	private boolean gameRunning;
    	
    	/**
    	 * Creates a new castle wars object.
    	 */
    	public CastleWars() {
    	    saraTeam = new SaraTeam();
    	    zammyTeam = new ZammyTeam();
    	    objects = new CastleObjectHandler();
    	}
    	
    	/**
    	 * Join's the guthix team.
    	 * 
    	 * @param player
    	 * 	The player requesting to join
    	 * @return true of they joined
    	 */
    	public boolean joinGuthix(Player player) {
    	    if(!canEnter(player))
    		return false;
    	    if(Misc.random(10) > 5)
    		if(!saraTeam.addPlayer(player)) {
    		    player.sendMessage("There are too many players for you to join.");
    		    return false;
    		}
    	    else
    		if (!zammyTeam.addPlayer(player)) {
    		    player.sendMessage("There are too many players for you to join.");
    		    return false;
    		}
    	    return true;
    	}
    	
    	/**
    	 * Join's the sara team.
    	 * 
    	 * @param player
    	 * 	the player requesting to join
    	 * @return true if they joined
    	 */
    	public boolean joinSara(Player player) {
    	    if(!canEnter(player)) return false;
    	    if(!saraTeam.addPlayer(player)) {
    		player.sendMessage("There are too many players for you to join.");
    		return false;
    	    }
    	    return true;
    	}
    	
    	/**
    	 * joins the zammy team
    	 * 
    	 * @param player
    	 * 	the player joining
    	 * @return true if they joined
    	 */
    	public boolean joinZammy(Player player) {
    	    if(!canEnter(player)) return false;
    	    if(!zammyTeam.addPlayer(player)) {
    		player.sendMessage("There are too many players for you to join.");
    		return false;
    	    }
    	    return true;
    	}
    	
    	/**
    	 * Checks if the player can enter or not.
    	 * 
    	 * @param player
    	 * 	the player
    	 * @return true if they can; false if they can't
    	 */
    	public boolean canEnter(Player player) {
    	    if(player.getEquipment().get(5) != null || player.getEquipment().get(1) != null) {
    		player.sendMessage("You cannot wear capes or hats!");
    		return false;
    	    }
    	    if(saraTeam.contains(player) || zammyTeam.contains(player))
    		return false;
    	    //some other checks.
    	    return true;
    	}
    	
    	/**
    	 * Handle's the death for this player.
    	 * 
    	 * @param dead
    	 * 	The player who died.
    	 * @param killer
    	 * 	The player who got killed.
    	 */
    	public void handleDeath(Player dead, Player killer) {
    	    getTeam(killer).killedPlayer(dead, killer);
    	}
    	
    	/**
    	 * The minutes in the lobby till game starts
    	 */
    	private short lobbyMinutes = -1;
    	/**
    	 * The seconds used for the minutes since the task runs each second
    	 */
    	private short lobbySeconds;
    	/**
    	 * The amount of minutes to wait till game starts.
    	 */
    	private final short MINUTES = 20;
    	
    	/**
    	 * Starts the lobby timer
    	 */
    	public void startLobbyTimer() {
    	    World.getWorld().schedule(new ScheduledTask(1, false) {

		@Override
		public void execute() {
		    if(lobbyMinutes == -1) {
			lobbyMinutes = MINUTES;
		    }
		    lobbySeconds++;
		    if(lobbySeconds == 60) {
			lobbySeconds = 0;
			lobbyMinutes--;
		    }
		    for(Player p : World.getWorld().getPlayerRepository()) {
			if(inAnyTeam(p)) {
			    updateLobbyInterface(p);
			}
		    }
		    if(lobbyMinutes <= 0) {
			initGame();
			lobbyMinutes = -1;
			lobbySeconds = -1;
			this.stop();
		    }
		}
    		
    	    });
    	}
    	
    	/**
    	 * Updates the interface that is set in the lobby.
    	 * 
    	 * @param player
    	 *	the player to update for
    	 */
    	public void updateLobbyInterface(Player player) {
    	    
    	}
    	
    	/**
    	 * Check's if the victim is the player's enemy.
    	 * 
    	 * @param player
    	 * 	The player attacking
    	 * @param victim
    	 * 	The victim being attacked
    	 * @return true if they are on opposing sides
    	 */
    	public boolean isEnemy(Player player, Player victim) {
    	    if(zammyTeam == null || saraTeam == null)
    		return false;
    	    return getTeam(player) == zammyTeam ? getTeam(victim) == saraTeam : getTeam(victim) == zammyTeam;
    	}
    	
    	/**
    	 * Updates the game interface
    	 * 
    	 * @param player
    	 * 	the player
    	 */
    	public void updateGameInterface(Player player) {
    	    player.send(new SetInterfaceTextEvent(11353, gameMinutes+" Mins"));
    	    //for sara
    	    int saraConfig = 100;
    	    if(getTeam(player) == zammyTeam ? saraFlag.isDropped() : zammyFlag.isDropped()) {
    		saraConfig += 2097152 * 2;
    	    } else if (getTeam(player) == zammyTeam ? saraFlag.isTaken() : zammyFlag.isTaken()) {
    		saraConfig += 2097152 * 1;
    	    }
    	    saraConfig += 16777216 * (getTeam(player) == zammyTeam ? saraScore : zammyScore);
    	    player.send(new SendConfigEvent(getTeam(player) == zammyTeam ? 378 : 377, saraConfig));
    	    
    	    //for zammy
    	    int zammyConfig = 100;
    	    if(getTeam(player) == zammyTeam ? zammyFlag.isDropped() : saraFlag.isDropped()) {
    		zammyConfig += 2097152 * 2;
    	    } else if (getTeam(player) == zammyTeam ? zammyFlag.isTaken() : saraFlag.isTaken()) {
    		zammyConfig += 2097152 * 1;
    	    }
    	    zammyConfig += 16777216 * (getTeam(player) == zammyTeam ? zammyScore : saraScore);
    	    player.send(new SendConfigEvent(getTeam(player) == zammyTeam ? 377 : 378, zammyConfig));
    	}
    	
    	/**
    	 * Refreshes the objects locally.
    	 */
    	public void refreshObjects() {
    	    if(saraFlag == null && zammyFlag == null) {
    		//change these to the correct ones
    		saraFlag = new SaraFlag(new Position(5, 5));
    		zammyFlag = new ZammyFlag(new Position(5, 5));
    	    }
    	    objects = new CastleObjectHandler();
    	}
    	
    	/**
    	 * Starts the game..
    	 */
    	public void initGame() {
    	    setGameRunning(true);
    	    startGameTimer();
    	}
    	
    	/**
    	 * The minutes of the game
    	 */
    	private short gameMinutes = -1;
    	/**
    	 * The seconds of the game
    	 */
    	private short gameSeconds = -1;
    	/**
    	 * the max minutes till start of game
    	 */
    	private final short GAME_MINUTES = 25;
    	
    	/**
    	 * Starts the game timer.
    	 */
    	private void startGameTimer() {
    	    World.getWorld().schedule(new ScheduledTask(1, false) {

		@Override
		public void execute() {
		    if(gameMinutes == -1) {
			gameMinutes = GAME_MINUTES;
		    }
		    if(gameSeconds == -1) {
			gameSeconds = 60;
		    }
		    gameSeconds++;
		    if(gameSeconds >= 60) {
			gameSeconds = 0;
			gameMinutes--;
		    }
		    for(Player p : World.getWorld().getPlayerRepository()) {
			if(inAnyTeam(p)) {
			    updateGameInterface(p);
			}
		    }
		    if(gameMinutes >= 0) {
			saraTeam.endGame();
			zammyTeam.endGame();
			gameMinutes = -1;
			gameSeconds = -1;
			this.stop();
		    }
		}
    		
    	    });
    	}
    	
    	/**
    	 * Check's if the player is in any team.
    	 * 
    	 * @param player
    	 * 	the player
    	 * @return true if either lists contains this player
    	 */
    	public boolean inAnyTeam(Player player) {
    	    if(zammyTeam.contains(player))
    		return true;
    	    else if (saraTeam.contains(player))
    		return true;
    	    return false;
    	}
    	
    	/**
    	 * Get's the team that this player is in.
    	 * 
    	 * @param player
    	 * 	The player.
    	 * @return the team that this player is in.
    	 */
    	public Team getTeam(Player player) {
    	    if(zammyTeam.contains(player))
    		return zammyTeam;
    	    else if (saraTeam.contains(player))
    		return saraTeam;
    	    return saraTeam;
    	}
    	
    	public ZammyFlag getZammyFlag() {
    	    return zammyFlag;
    	}
    	
    	public ZammyTeam getZammyTeam() {
    	    return (ZammyTeam) zammyTeam;
    	}
    	
    	public SaraFlag getSaraFlag() {
    	    return saraFlag;
    	}
    	public SaraTeam getSaraTeam() {
    	    return (SaraTeam) saraTeam;
    	}

	public boolean isGameRunning() {
	    return gameRunning;
	}

	public void setGameRunning(boolean gameRunning) {
	    this.gameRunning = gameRunning;
	}
}
