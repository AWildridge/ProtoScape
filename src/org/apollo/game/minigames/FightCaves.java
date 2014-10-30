package org.apollo.game.minigames;

import org.apollo.game.model.NPCManager;
import org.apollo.game.model.Player;
import org.apollo.game.model.Position;
import org.apollo.game.model.World;
import org.apollo.game.model.inter.dialog.DialogueSender;
import org.apollo.game.model.inter.dialog.DialogueSender.HeadAnimations;
import org.apollo.game.model.inter.dialog.impl.DefaultListener;
import org.apollo.game.scheduling.ScheduledTask;
import org.apollo.util.Misc;

/**
 * An basic fight caves minigame system.
 *
 * @author Rodrigo Molina
 */
public class FightCaves {

    	/**
    	 * The player who is attempting to enter the fight caves, or inside the current minigame.
    	 */
    	private Player player;
    	
    	/**
    	 * Creates a new instance of this object and assigns the player object.
    	 * 
    	 * @param pl
    	 * 	The player whom is inside this minigame.
    	 */
    	public FightCaves(Player pl) {
    	    this.player = pl;
    	}
    	
    	/**
    	 * The x coordinate of the starting point when starting the minigame.
    	 */
    	private final int STARTING_POINT_X = 2413;
    	
    	/**
    	 * The y coordinate of the starting point when starting the minigame.
    	 */
    	private final int STARTING_POINT_Y = 5117;
    	
    	/**
    	 * The wave id of the player.
    	 */
    	private int waveId;
    	
    	/**
    	 * The amount of nps inside the current wave.
    	 */
    	private int npcsKilled;
    	
    	/**
    	 * The amount of npcs in this current wave.
    	 */
    	private int npcsAmountInWave;
    	
    	/**
    	 * The level 22 whom drains prayer.
    	 */
    	private final int TZ_KIH = 2627;
    	/**
    	 * The level 45 whom spawns once and drops 2 of tz-kek-2
    	 */
    	private final int TZ_KEK_ = 2629;
    	/**
    	 * The level 22 that spawn twice after killing tox-xil.
    	 */
    	private final int TZ_KEK_22 = 2628;
    	
    	/**
    	 * The level 90 whom ranges, and shoots out spikes.
    	 */
    	private final int TOK_XIL = 2632;
    	
    	/**
    	 * The 180 who walks slow as freak, but melees a lot of damage.
    	 */
    	private final int YT_MEJKOT = 2741;
    	/**
    	 * The level 360 whom spawns before jad.
    	 */
    	private final int KET_ZEK = 2743;
    	/**
    	 * JAD!  The final master.
    	 */
    	private final int TOK_XIL_JAD = 2745;
    	/**
    	 * The healers whom spawn when jad gets to half hp.
    	 */
    	private final int JADS_HEALERS = 2746;
    	
    	/**
    	 * The random coords of where the npcs will be randomly spawned.
    	 */
    	private int[][] coords = {
    		{ 2398, 5086 },{ 2387, 5095 },{ 2407, 5098 },{ 2417, 5082 },{ 2390, 5076 },{ 2410, 5090 }
    	};
    	
    	private int[][] waves = {
    		{},//ignore the first index, waves start at 1.
    		{ TZ_KIH }, { TZ_KIH, TZ_KIH }, { TZ_KEK_ }, { TZ_KEK_, TZ_KIH }, { TZ_KEK_, TZ_KIH, TZ_KIH }, { TZ_KEK_, TZ_KEK_ }, { TOK_XIL }, { TOK_XIL, TZ_KIH }, { TOK_XIL, TZ_KIH, TZ_KIH }, { TOK_XIL, TZ_KEK_ }, { TOK_XIL, TZ_KEK_, TZ_KIH }, { TOK_XIL, TZ_KEK_, TZ_KIH, TZ_KIH }, { TOK_XIL, TZ_KEK_, TZ_KEK_ }, { TOK_XIL, TOK_XIL }, { YT_MEJKOT }, { YT_MEJKOT, TZ_KIH }, { YT_MEJKOT, TZ_KIH, TZ_KIH }, { YT_MEJKOT, TZ_KEK_ }, { YT_MEJKOT, TZ_KEK_, TZ_KIH  }, { YT_MEJKOT, TZ_KEK_, TZ_KIH, TZ_KIH }, { YT_MEJKOT, TZ_KEK_, TZ_KEK_ }, { YT_MEJKOT, TOK_XIL }, { YT_MEJKOT, TOK_XIL, TZ_KIH }, { YT_MEJKOT, TOK_XIL, TZ_KIH, TZ_KIH }, { YT_MEJKOT, TOK_XIL, TZ_KEK_ }, { YT_MEJKOT, TOK_XIL, TZ_KEK_, TZ_KIH }, { YT_MEJKOT, TOK_XIL, TZ_KEK_, TZ_KIH, TZ_KIH }, { YT_MEJKOT, TOK_XIL, TZ_KEK_, TZ_KEK_ }, { YT_MEJKOT, TOK_XIL, TOK_XIL }, { YT_MEJKOT, YT_MEJKOT }, { KET_ZEK }, { KET_ZEK, TZ_KIH }, { KET_ZEK, TZ_KIH, TZ_KIH }, { KET_ZEK, TZ_KEK_ }, { KET_ZEK, TZ_KEK_, TZ_KIH }, { KET_ZEK, TZ_KEK_, TZ_KIH, TZ_KIH }, { KET_ZEK, TZ_KEK_, TZ_KEK_ },{ KET_ZEK, TOK_XIL }, { KET_ZEK, TOK_XIL, TZ_KIH }, { KET_ZEK, TOK_XIL, TZ_KIH, TZ_KIH }, { KET_ZEK, TOK_XIL, TZ_KEK_ }, { KET_ZEK, TOK_XIL, TZ_KEK_, TZ_KIH }, { KET_ZEK, TOK_XIL, TZ_KEK_, TZ_KIH, TZ_KIH }, { KET_ZEK, TOK_XIL, TZ_KEK_, TZ_KEK_ }, { KET_ZEK, TOK_XIL, TOK_XIL }, { KET_ZEK, YT_MEJKOT }, { KET_ZEK, YT_MEJKOT, TZ_KIH }, { KET_ZEK, YT_MEJKOT, TZ_KIH, TZ_KIH }, { KET_ZEK, YT_MEJKOT, TZ_KEK_ }, { KET_ZEK, YT_MEJKOT, TZ_KEK_, TZ_KIH }, { KET_ZEK, YT_MEJKOT, TZ_KEK_, TZ_KIH, TZ_KIH }, { KET_ZEK, YT_MEJKOT, TZ_KEK_, TZ_KEK_ }, { KET_ZEK, YT_MEJKOT, TOK_XIL }, { KET_ZEK, YT_MEJKOT, TOK_XIL, TZ_KIH }, { KET_ZEK, YT_MEJKOT, TOK_XIL, TZ_KIH, TZ_KIH }, { KET_ZEK, YT_MEJKOT, TOK_XIL, TZ_KEK_ }, { KET_ZEK, YT_MEJKOT, TOK_XIL, TZ_KEK_, TZ_KIH }, { KET_ZEK, YT_MEJKOT, TOK_XIL, TZ_KEK_, TZ_KIH, TZ_KIH }, { KET_ZEK, YT_MEJKOT, TOK_XIL, TZ_KEK_, TZ_KEK_ }, { KET_ZEK, YT_MEJKOT, TOK_XIL, TOK_XIL }, { KET_ZEK, YT_MEJKOT, YT_MEJKOT }, { KET_ZEK, KET_ZEK },
    		
    		/*
    		 * FINAL WAVE!!!!!!
    		 */
    		{ TOK_XIL_JAD }
    	};
    	
    	/**
    	 * Attempts to enter a player into the cave, and activate the timed minigame.
    	 */
    	public void enterCaves() {
    	    //Add some other checks if necessary..
    	    
    	    player.getSkillSet().refreshSkills();
    	    player.teleport(new Position(STARTING_POINT_X, STARTING_POINT_Y, player.getIndex() * 4));
    	    player.sendMessage("You enter the caves...");
    	    player.sendMessage("The minigame will start in 10 seconds..");
    	    
    	    player.getFightCaves().setWaveId(1);
    	    player.getFightCaves().startMinigame();
    	}
    	
    	/**
    	 * Starts the minigame.
    	 */
    	public void startMinigame() {
    	    if(player.getFightCaves().getWaveId() > 0) {
    		//prob do some more checks..?
    		World.getWorld().schedule(new ScheduledTask(10, false) {

		    @Override
		    public void execute() {
			try {
			    spawn();
			} catch (ArrayIndexOutOfBoundsException e) {
			    e.printStackTrace();
			    System.out.println("Error occured when spawning npcs for player: "+player.getUndefinedName());
			    System.out.println("Player is on wave: "+player.getFightCaves().getWaveId());
			    player.sendMessage("An error has occured whilst trying to spawn npcs for you, please tell an admin immediately.");
			}
			this.stop();
		    }
    		    
    		});
    	    } else {
    		throw new IllegalStateException("The player is trying to start a fight caves game with wave id under or equal to 0.");
    	    }
    	}
    	
    	/**
    	 * An player has killed an npc, and needs to receive the next set of npcs.
    	 */
    	public void npcKilled() {
    	    if(waveId <= 0) return;
    	    if(waveId == 63) {
		//JAD!
		//Now do what is needed.
		//send the dialogue and wait a bit till jad spawns.
		assignReward(true);
		return;
	    }
    	    player.getFightCaves().incrementNpcsKilled();
    	    if(player.getFightCaves().getNpcsKilled() >= player.getFightCaves().getNpcsAmountInWave()) {
    		player.getFightCaves().incrementWave();
        	if(waveId == 62) {
        	    DialogueSender.sendNPCChatTwoLines(player, new DefaultListener(player), "This is the last wave!", "Get prepared to fight Jad!!", 2743, 0, "Tok-", HeadAnimations.HAPPY); 
        	    World.getWorld().schedule(new ScheduledTask(15, false) {

			@Override
			public void execute() {
		    	    player.getFightCaves().setNpcsAmountInWave(1);
		    	    player.getFightCaves().setNpcsKilled(0);
	        	    Position pos = new Position(1, 1, player.getIndex() * 4);
	        	    NPCManager.appendNpc(2745, pos);
	        	    stop();
			}
        		
        	    });
        	    return;
        	}
    		World.getWorld().schedule(new ScheduledTask(10, false) {

		    @Override
		    public void execute() {
			try {
			    spawn();
			} catch (ArrayIndexOutOfBoundsException e) {
			    e.printStackTrace();
			    System.out.println("Error occured when spawning npcs for player: "+player.getUndefinedName());
			    System.out.println("Player is on wave: "+player.getFightCaves().getWaveId());
			    player.sendMessage("An error has occured whilst trying to spawn npcs for you, please tell an admin immediately.");
			}
			player.sendMessage("You are on wave "+player.getFightCaves().getWaveId()+", with "+player.getFightCaves().getNpcsAmountInWave()+" npcs inside here.");
			this.stop();
		    }
    		    
    		});
    	    }
    	}
    	
    	/**
    	 * Spawns the npcs for the certain wave.
    	 * 
    	 * @throws ArrayIndexOutOfBoundsException
    	 * 	Throws array index out of bounds, just in case we try to reach an index
    	 * 	that is not reachable, and we can handle it properly by letting the {@link Player} know
    	 * 	that we have ran into an problem, and he should report to an Administrator as soon as
    	 * 	possible.
    	 */
    	private void spawn() throws ArrayIndexOutOfBoundsException {
    	    int[] monsters = waves[waveId];
    	    int amount = monsters.length;
    	    player.getFightCaves().setNpcsAmountInWave(amount);
    	    player.getFightCaves().setNpcsKilled(0);
    	    for(int idx = 0; idx < amount; idx++) {
    		if(monsters[idx] <= 0) continue;
    		int npc = monsters[idx];
    		final int random = Misc.random(coords.length - 1);
    		int x = coords[random][0];
    		int y = coords[random][1];
    		Position randomPos = new Position(x, y, player.getIndex() * 4);
    		//now spawn the npcs at the coords.
    		NPCManager.appendNpc(npc, randomPos);
    		System.out.println("Spawned npc: "+npc+", at "+randomPos);
    	    }
    	}
    	
    	/**
    	 * Assigns an reward to the player (Fire cape, and the tok money).
    	 * 
    	 * @param flag
    	 * 	true if the wave they last left off was 63 or not.
    	 */
    	public void assignReward(boolean flag) {
    	    int tokkulAmount = getTokkul();
    	    if(flag) {
    		//HE KILLED JAD!
    		DialogueSender.sendNPCChatTwoLines(player, new DefaultListener(player), "You have killed jad! As a token of", "appreciation, you have been rewarded a fire cape!", 2743, 0, "Tok-", HeadAnimations.HAPPY);
    		player.getInventory().add(6570);
    		player.getFightCaves().setWaveId(0);
    		player.getInventory().add(629, tokkulAmount);
    		return;
    	    }
    	    player.getInventory().add(6529, tokkulAmount);
    	    player.getFightCaves().setWaveId(0);
    	    player.sendMessage("The minigame has ended, and you recieve tokkul for your courageous acts!");
    	    player.getSkillSet().refreshSkills();
    	    player.teleport(new Position(2438, 5168, 0));
    	}
    	
    	/**
    	 * Get's the amount of tokkul given after completing the minigame.
    	 * 
    	 * @return the amount of tokkul.
    	 */
    	private int getTokkul() {
    	    if(player.getFightCaves().getWaveId() == 63)
    		return 16064;
    	    if(waveId == 0) return 254;
    	    double tok = player.getFightCaves().getWaveId() * 254.98;
    	    return (int) Math.round(tok);
    	}
    	
	public int getWaveId() {
	    return waveId;
	}

	public void setWaveId(int waveId) {
	    this.waveId = waveId;
	}
	
	/**
	 * Increments the wave.
	 */
	public void incrementWave() {
	    waveId++;
	}

	/**
	 * Increments the npcs killed in this current wave.
	 */
	public void incrementNpcsKilled() {
	    npcsKilled++;
	}
	
	public int getNpcsKilled() {
	    return npcsKilled;
	}

	public void setNpcsKilled(int npcsKilled) {
	    this.npcsKilled = npcsKilled;
	}

	public int getNpcsAmountInWave() {
	    return npcsAmountInWave;
	}

	public void setNpcsAmountInWave(int npcsAmountInWave) {
	    this.npcsAmountInWave = npcsAmountInWave;
	}
}