package org.apollo.game.content.skills.firemaking;

import java.util.LinkedList;
import java.util.List;

import org.apollo.game.content.randoms.RandomHandler;
import org.apollo.game.model.Animation;
import org.apollo.game.model.GroundItem;
import org.apollo.game.model.Item;
import org.apollo.game.model.Player;
import org.apollo.game.model.Position;
import org.apollo.game.model.Skill;
import org.apollo.game.model.World;
import org.apollo.game.scheduling.ScheduledTask;
import org.apollo.util.Misc;

/**
 * An basic firemaking system.
 *
 *
 * @author Rodrigo Molina
 */
public class Firemaking {

    
    public static enum FireData {
	
	REGULAR(1511, 2732, 1, 20, 592);
	
	private int itemId;
	private int fireId;
	private int levelReq;
	private int expGiven;
	private int ashesId;
	
	FireData(int itemId, int fireId, int levelReq, int expGiven, int ashesId) {
	    this.itemId = itemId;
	    this.fireId = fireId;
	    this.levelReq = levelReq;
	    this.expGiven = expGiven;
	    this.ashesId = ashesId;
	}
	
	public static FireData forId(int id) {
	    FireData data = null;
	    for(FireData fire : FireData.values()) {
		if(fire.getItemID() == id)
		    data = fire;
	    }
	    return data;
	}
	
	public int getItemID() {
	    return itemId;
	}
	public int getFireId() {
	    return fireId;
	}
	public int getLevelReq() {
	    return levelReq;
	}
	public int getExpGiven() {
	    return expGiven;
	}
	public int getAshesId() {
	    return ashesId;
	}
    }

    private final static int ASHES = 0;
    
    public static boolean check(Player player, FireData fire) {
	if(player.hasAttributeTag("FIRE_MAKING"))
	    return false;
	if(player.getSkillSet().getSkill(Skill.FIREMAKING).getCurrentLevel() < fire.getLevelReq()) {
	    player.sendMessage("You need a higher level to light this fire.");
	    return false;
	}
	/*
	 * TODO: More checks here!  Like fire in a bank or any other places
	 */
	
	return true;
    }
    
    private static List<Fire> fires = new LinkedList<Fire>();
    
    private static Fire getLast(Fire after) {
	Fire last = null;
	int index = fires.indexOf(after);
	try {
	    	if(fires.get(index) != null) {
	    	    int beforeIndex = --index;
	    	    if(fires.get(beforeIndex) != null & fires.get(beforeIndex).getTime() >= after.getTime())
	    		//prob re-do this depending on the nano time.
	    		last = fires.get(beforeIndex);
		}
	} catch (IndexOutOfBoundsException e) {
	    return null;
	}
	return last;
    }
    
    public static void lightFire(final Player player, final FireData data, final int itemToDelete) {
	if(!check(player, data))
	    return;
	
	final Fire current = new Fire(data, System.nanoTime(), new Position(player.getPosition().getX() - 1, player.getPosition().getY()));
	
	for(Fire f : fires) {
	    if(f == null) continue;
	    if(f.getFirePos().equals(current.getFirePos())) {
		player.sendMessage("You cannot light a fire over another!");
		return;
	    }
	}
	
	fires.add(current);
	
	int time = 0;
	Fire lastFire = getLast(current);
	//TODO: Should it really be like this?  Or should we add some more formulas to determine
	//the time it takes to light the fire, based on the level and the log?
	if(lastFire != null) {
	    time = 2;//these aren't really accurate lol.
	    //means they have recently created a fire..
	} else {
	    //means this is their first fire.
	    time = Misc.random(4) + 2;
	}
	
	player.stopAction();
	player.playAnimation(Animation.FIRE_MAKING);
	player.getInventory().remove(itemToDelete);
	player.sendMessage("You attempt to light the fire..");
	player.getAttributeTags().add("FIRE_MAKING");
	GroundItem.getInstance().create(player, data.getItemID(), 1, player.getPosition());
	World.getWorld().schedule(new ScheduledTask(time, false) {

	    @Override
	    public void execute() {
		final GroundItem fire = new GroundItem(player.getUndefinedName(), new Item(data.getItemID(), 1), player.getPosition());
		GroundItem.getInstance().delete(fire);
		player.sendNewGlobalObject(fire.getPosition(), data.getFireId(), 1, 10);
		final Position pos = new Position(player.getPosition().getX() - 1, player.getPosition().getY());
		//add some object checks... at the moment nothing lol.
		player.getWalkingQueue().addStep(pos);
		player.getAttributeTags().remove("FIRE_MAKING");
		player.sendMessage("You sucessfully light the fire. (dont know if this is right message?)");
		player.turnTo(new Position(player.getPosition().getX() + 1, player.getPosition().getY()));
		//now let's start the next task to replace the fire with ashes.
		RandomHandler.doAction(player, "firemaking");;
		World.getWorld().schedule(new ScheduledTask(60, false) {

		    @Override
		    public void execute() {
			player.sendRemoveObject(fire.getPosition(), 1, 10);
			GroundItem.getInstance().create(player, ASHES, 0, fire.getPosition());
			GroundItem.getInstance().create(player, data.getAshesId(), 1, fire.getPosition());
			fires.remove(current);
			stop();
		    }
		});
		stop();
	    }
	    
	});
    }
    
    
    /**
     * Represents a single fire.  We will store each fire
     * in a list, so that we cannot light over an fire.
     *
     * @author Rodrigo Molina
     */
    protected static class Fire {
	
	private FireData fire;
	private long time;
	private Position firePos;
	
	public Fire(FireData fire, long time, Position pos) {
	    this.setFire(fire);
	    this.setTime(time);
	    this.setFirePos(pos);
	}

	public FireData getFire() {
	    return fire;
	}

	public void setFire(FireData fire) {
	    this.fire = fire;
	}

	public long getTime() {
	    return time;
	}

	public void setTime(long time) {
	    this.time = time;
	}

	public Position getFirePos() {
	    return firePos;
	}

	public void setFirePos(Position firePos) {
	    this.firePos = firePos;
	}
    }
}
