package org.apollo.game.content.skills.farming;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apollo.game.event.impl.SendConfigEvent;
import org.apollo.game.model.Player;
import org.apollo.game.model.World;
import org.apollo.game.scheduling.ScheduledTask;


/**
 * A class to handle the growing of herbs. We will use this to grow local herbs.
 * 
 * 
 * @author Rodrigo Molina
 */
public class HerbGrowing {

    private static HerbGrowing hg;

    public static HerbGrowing getHerbGrowing() {
	if (hg == null)
	    hg = new HerbGrowing();
	return hg;
    }

    private BlockingQueue<HerbGrow> growing = new LinkedBlockingQueue<HerbGrow>(
	    10000);
    private List<OfflineGrow> offline = new LinkedList<OfflineGrow>();

    public void add(HerbGrow hg) {
	synchronized (growing) {
	    growing.offer(hg);
	}
    }

    public OfflineGrow find(String name) {
	for (OfflineGrow og : offline) {
	    if (og.playerName.equals(name))
		return og;
	}
	return null;
    }

    public void listenForGrowth() {
	World.getWorld().schedule(new ScheduledTask(1, false) {
	    @Override
	    public void execute() {
		if (growing.size() > 0) {
		    for (HerbGrow hg : growing) {
			if (hg.isDone()) {
			    Player player = null;
			    for (Player p : World.getWorld().getPlayerRepository()) {
				if (p != null) {
				    if (p.getUndefinedName().equalsIgnoreCase(hg.getPlayerName())) {
					player = p;
					break;
				    }
				}
			    }
			    if (player == null) {
				System.out
					.println("hes offline, but his crop has grown one up.");
				OfflineGrow og = null;
				for (OfflineGrow of : offline) {
				    if (of.playerName.equals(hg.getPlayerName())) {
					og = of;
					break;
				    }
				}
				if (og != null) {
				    boolean found = false;
				    for (int a = 0; a < og.hgs.length; a++) {
					if (og.hgs[a] != null
						&& og.hgs[a].getPatch().getObjectId() == hg.getPatch().getObjectId()) {
					    og.amounts[a] += 1;
					    found = true;
					}
				    }
				    if (!found) {
					for (int a = 0; a < og.hgs.length; a++) {
					    if (og.hgs[a] == null) {
						og.hgs[a] = hg;
						og.amounts[a] = 1;
						break;
					    }
					}
				    }
				} else {
				    offline.add(new OfflineGrow(hg
					    .getPlayerName(), 1, hg));
				}
				hg.resetTime();
				growing.add(hg);
			    } else {
				player.getFarmingPatches().nextGrowingState(hg, null);
				growing.remove(hg);
			    }
			}
		    }
		}
		for (Player p : World.getWorld().getPlayerRepository()) {
		    if (p != null) {
			// the weeds crop should grow back after.. lets say 1
			// minute.
			Patch[] patches = p.getFarmingPatches()
				.patchNeedWeedGrow();
			if (patches != null) {
			    for (Patch pat : patches) {
				if (pat != null) {
				    if (pat.isDone()) {
					pat.reset();
					pat.addState(-1);
					p.send(new SendConfigEvent(pat.getConfigId(), pat.getState()));
				    }
				}
			    }
			}
		    }
		}
	    }
	});
    }

    /**
     * I don't know. I really like making new inner-classes. I guess it just
     * seems easier for me.
     * 
     * 
     * @author Rodrigo Molina
     */
    public static class OfflineGrow {
	private String playerName;
	private int amounts[];
	private HerbGrow hgs[];

	public OfflineGrow(String name, int first, HerbGrow firsth) {
	    this.playerName = name;
	    this.amounts = new int[50];
	    this.hgs = new HerbGrow[50];
	    amounts[0] = first;
	    hgs[0] = firsth;
	}

	public HerbGrow[] getHerbGrows() {
	    return hgs;
	}

	public int[] getAmounts() {
	    return amounts;
	}
    }

    /**
     * A class to represent each herb (of any sort) that is growing.
     * 
     * 
     * @author Rodrigo Molina
     */
    public static class HerbGrow {
	/**
	 * The player who is in charge of this herb.
	 */
	private String playerName;
	private int id;
	private Patch patch;
	private PatchInfo info;
	private int maxTime;// in seconds.
	private int time;

	/**
	 * Creates a new growing herb.
	 * 
	 * @param playerName
	 *            The name of the player who this herb belongs to.
	 * @param id
	 *            The object id.
	 * @param patch
	 *            The patch of this herb.
	 * @param info
	 *            The info of the patch.
	 * @param maxTime
	 *            The max time (in seconds) for each grow cycle.
	 */
	public HerbGrow(String playerName, int id, Patch patch, PatchInfo info,
		int maxTime) {
	    this.setPlayerName(playerName);
	    this.setId(id);
	    this.setPatch(patch);
	    this.setInfo(info);
	    this.maxTime = maxTime;
	    this.time = 0;
	}

	public boolean isDone() {
	    return time++ >= maxTime;
	}

	public String getPlayerName() {
	    return playerName;
	}

	public void setPlayerName(String playerName) {
	    this.playerName = playerName;
	}

	public int getId() {
	    return id;
	}

	public void setId(int id) {
	    this.id = id;
	}

	public Patch getPatch() {
	    return patch;
	}

	public void setPatch(Patch patch) {
	    this.patch = patch;
	}

	public PatchInfo getInfo() {
	    return info;
	}

	public void setInfo(PatchInfo info) {
	    this.info = info;
	}

	public void resetTime() {
	    time = 0;
	}
    }
}
