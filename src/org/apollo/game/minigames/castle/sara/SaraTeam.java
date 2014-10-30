package org.apollo.game.minigames.castle.sara;

import org.apollo.game.event.impl.WalkableInterfaceEvent;
import org.apollo.game.minigames.castle.CastleWars;
import org.apollo.game.minigames.castle.Team;
import org.apollo.game.model.Player;
import org.apollo.util.ArrayUtils;

/**
 * The sara team.
 *
 *
 * @author Rodrigo Molina
 */
public class SaraTeam implements Team {

    	/**
    	 * The players inside this team.
    	 */
    	private Player[] players = new Player[CastleWars.MAX_PLAYERS_ALLOWED];
    	
        @Override
        public boolean addPlayer(Player player) {
            int slot = ArrayUtils.getFreeSlot(players);
            if(slot != -1)
        	players[slot] = player;
            else return false;
            enterWaiting(player);
            return true;
        }

        @Override
        public Player[] getList() {
            return players;
        }

        @Override
        public int getSize() {
            return ArrayUtils.getSize(players);
        }

        @Override
        public void enterWaiting(Player player) {
            player.send(new WalkableInterfaceEvent(11351));
        }

        @Override
        public void leaveWaiting(Player player) {
            
        }

	@Override
	public boolean contains(Player player) {
	    return ArrayUtils.contains(players, player);
	}

	@Override
	public void leaveGame(Player player) {
	    // TODO Auto-generated method stub
	    
	}

	@Override
	public void endGame() {
	    // TODO Auto-generated method stub
	    
	}

	@Override
	public void startGame() {
	    // TODO Auto-generated method stub
	    
	}

	@Override
	public void killedPlayer(Player dead, Player killer) {
	    // TODO Auto-generated method stub
	    killer.sendMessage("OH YEah YOU KILLED YOUR ENEMY GOOD JOB");
	}

}
