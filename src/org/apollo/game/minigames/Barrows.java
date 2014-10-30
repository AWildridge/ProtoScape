package org.apollo.game.minigames;

import java.util.ArrayList;
import java.util.List;

import org.apollo.game.model.NPC;
import org.apollo.game.model.NPCManager;
import org.apollo.game.model.Player;
import org.apollo.util.Misc;

/**
 * A simple barrows system that I designed on the way.. 
 *
 *
 * @author Rodrigo Molina
 */
public class Barrows {

	/**
	 * The player..
	 */
	private Player player;

	/**
	 * Creates a new instance of this object.
	 * 
	 * @param pl
	 * 	The player..
	 */
	public Barrows(Player pl) {
		this.player = pl;
		brothersKilled = new ArrayList<Brother>(5);
		brothers = new Brother[5];
		brothers[0] = new Torag();
	}

	/**
	 * The random crypt which contains the tunnel.
	 */
	private int randomCrypt;

	/**
	 * The barrows count of the player.
	 */
	private int barrowsCount;

	/**
	 * An list of the brothers killed.
	 */
	private List<Brother> brothersKilled;

	/**
	 * A Local lists of the brothers.
	 */
	private Brother[] brothers;

	/**
	 * Handles the barrows chest, and spawns the brother if they have not killed that brother.
	 * 
	 * @param objectId
	 * 	The object id that they are clicking on.
	 */
	public void handleChest(Brother bro) {
		if (barrowsCount == 0 && randomCrypt == 0) {
    			randomCrypt = Misc.random(brothersKilled.size());
		}

		if (brothersKilled.get(randomCrypt) != null && bro != null){
    			if (brothersKilled.get(randomCrypt).equals(bro)) {
    			    enterTunnel();
    			} else {
    			    player.sendMessage("you have already killed this brother");
    			}
		}

		if (brothersKilled != null && bro != null) {
    			if (brothersKilled.contains(bro)) {
    			    	player.sendMessage("You have already killed this brother..");
    			} else {
    		    		bro.spawnBrother(player); 
    		    }
		}
	}

	/**
	 * I guess we can use this to represent each brother?
	 *
	 *
	 * @author Rodrigo Molina
	 */
	protected interface Brother{
		//add all the crap..
		public int getNpcId();
		public int getCryptId();
		public void spawnBrother(Player player);
	}

	//TODO: move this to an appropiate spot.
	public class Torag implements Brother {

	    @Override
	    public int getCryptId(){
			// TODO Auto-generated method stub
			return 0;
	    }

	    @Override
	    public void spawnBrother(Player player) {
		NPC npc = NPCManager.appendNpc(getNpcId(), null);
		npc.forceChat("You dare bother me in my rest!");
	    }

	    @Override
	    public int getNpcId() {
			// TODO Auto-generated method stub
			return 0;
	    }
		
	}

	private void enterTunnel() {
		player.sendMessage("You enter the tunnel..");
	}

	/**
	 * Increments the barrow count when the player has killed an brother.
	 */
	public void incrementCount(int npcID) {
		barrowsCount++;
		Brother bro = null;
		for (int j = 0; j < brothers.length; j++) {
        		if (brothers[j] != null) {
        		    if (brothers[j].getNpcId() == npcID) {
    					bro = brothers[j];
    					break;
        	    		}
        		}
		}
		if (bro != null) {
    			if (!brothersKilled.contains(bro)) {
    			    brothersKilled.add(bro);
    			}
		}
	}
}
