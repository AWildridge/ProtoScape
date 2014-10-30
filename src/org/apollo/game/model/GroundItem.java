package org.apollo.game.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apollo.game.event.impl.PositionEvent;
import org.apollo.game.event.impl.RemoveGroundItemEvent;
import org.apollo.game.event.impl.SendGroundItemEvent;
import org.apollo.game.scheduling.impl.ProcessGroundItemsTask;

public class GroundItem {

	private static final GroundItem instance = new GroundItem();
	/**
	 * The amount of pulses before a ground item turns global.
	 */
	public final int GLOBAL_PULSES = 250;

	/**
	 * The task that processes all registered ground items.
	 */
	private static ProcessGroundItemsTask processTask;

	/**
	 * Create a ground item.
	 * 
	 * @param p
	 * @param itemId
	 * @param itemAmount
	 * @param pos
	 */
	public void create(final Character character, int itemId, int itemAmount, Position pos) {
		Item item = new Item(itemId, itemAmount);
		boolean stackable = item.getDefinition().isStackable();
		if (stackable) {
			GroundItem groundItem = new GroundItem(((Player) character).getUndefinedName(), item, pos);
			character.send(new PositionEvent(character.getPosition(), pos));
			character.send(new SendGroundItemEvent(new Item(itemId, itemAmount), pos));
			updateMap(groundItem, item);
		} else
			for (int i = 0; i < itemAmount; i++) {
				GroundItem groundItem = new GroundItem(((Player) character).getUndefinedName(), new Item(itemId, 1), pos);
				character.send(new PositionEvent(character.getPosition(), pos));
				character.send(new SendGroundItemEvent(new Item(itemId, 1), pos));
				updateMap(groundItem, new Item(itemId, 1));
			}
	}

	/**
	 * Deletes and unregisters a ground item.
	 * 
	 * @param groundItem
	 *            The ground item to unregister.
	 */
	public void delete(GroundItem groundItem) {
		getItems().get(groundItem.getPosition()).remove(groundItem);

		if (getItems().get(groundItem.getPosition()).isEmpty())
			groundItems.remove(groundItem.getPosition());

		for (Player player : World.getWorld().getPlayerRepository()) {
			player.send(new PositionEvent(player.getPosition(), groundItem.getPosition()));
			player.send(new RemoveGroundItemEvent(groundItem.getItem().getId(), groundItem.getItem().getAmount()));
		}
	}

	/**
	 * Send player items.
	 * @param player
	 */
	public void login(Player player) {
		Collection<List<GroundItem>> collection = getItems().values();

		Iterator<List<GroundItem>> iterator = collection.iterator();

		while (iterator.hasNext()) {
			List<GroundItem> list = iterator.next();
			if (list == null)
				return;
			for (int i = 0; i < list.size(); i++) {
				GroundItem g = (GroundItem) list.get(i);
				if (g == null)
					continue;
				if (g.controllerName.equals(player.getUndefinedName())) {
					player.send(new PositionEvent(player.getPosition(), g.getPosition()));
					player.send(new SendGroundItemEvent(new Item(g.getItem().getId(), g.getItem().getAmount()), g.getPosition()));
				}
			}
		}
	}

	/**
	 * Pick up a item.
	 * @param p
	 * @param position
	 * @param itemId
	 */
	public void pickup(final Player p, Position position, int itemId) {
		if (p.getInventory().freeSlots() <= 0) {
			p.sendMessage("Not enough inventory slots.");
		} else {
			GroundItem item = get(position);
			if (item != null) {
				if (item.item.getId() == itemId) {
					if (equals(p.getPosition(), item)) {
						delete(item);
						p.getInventory().add(item.item);
					}
				}
			}
		}
	}
	
	/**
	 * Check's if the item is inside the ground item list.
	 * 
	 * @param item
	 * 	The ground item.
	 * @return {@link Boolean}
	 */
	public boolean contains(GroundItem item) {
	    if(groundItems.containsKey(item.getPosition())) {
		return true;
	    }
	    return false;
	}
	
	/**
	 * Are we at the item
	 * @param p
	 * @param item
	 * @return {@link Boolean}
	 */
	public boolean equals(Position p, GroundItem item) {
		if (p.getLocalX() == item.getPosition().getLocalX())
			if (p.getLocalY() == item.getPosition().getLocalY())
				return true;
		return false;
	}
	
	/**
	 * Get the item at the specified position.
	 * @param p
	 * @return {@link GroundItem}
	 */
	public GroundItem get(Position p) {
		int findx = p.getX() - 8 * p.getTopLeftRegionX();
		int findy = p.getY() - 8 * p.getTopLeftRegionY();
		
		Collection<List<GroundItem>> collection = getItems().values();
		Iterator<List<GroundItem>> iterator = collection.iterator();

		while (iterator.hasNext()) {
			List<GroundItem> list = iterator.next();
			if (list == null)
				return null;
			for (int i = 0; i < list.size(); i++) {
				GroundItem g = list.get(i);
				int lookx = g.getPosition().getX() - 8 * g.getPosition().getTopLeftRegionX();
				int looky = g.getPosition().getY() - 8 * g.getPosition().getTopLeftRegionY();
				if (findx == lookx && findy == looky) {
					return g;
				}
			}
		}
		return null;
	}

	/**
	 * Check if the process is running.
	 */
	public void processTaskCheck() {
		if (processTask == null) {
			processTask = new ProcessGroundItemsTask();
			World.getWorld().schedule(processTask);
		}
	}

	/**
	 * Update the map items.
	 * 
	 * @param g
	 * @param item
	 */
	public void updateMap(GroundItem g, Item item) {
		if (groundItems.get(g.getPosition()) == null) {
			groundItems.put(g.getPosition(), new ArrayList<GroundItem>());
		}
		groundItems.get(g.getPosition()).add(g);
		processTaskCheck();
	}

	/**
	 * The name of the player who controls this item.
	 */
	private String controllerName;

	/**
	 * The item that is on the floor.
	 */
	private Item item;

	/**
	 * The position of the item.
	 */
	private Position position;

	/**
	 * The amount of remaining pulses until this item disappears.
	 */
	private int pulses;

	/**
	 * The map of the list of ground items on a position.
	 */
	private final Map<Position, List<GroundItem>> groundItems = new HashMap<Position, List<GroundItem>>();

	/**
	 * Creates a private ground item.
	 * 
	 * @param controllerName
	 *            The controller of this item.
	 * @param item
	 *            The item.
	 * @param position
	 *            The position.
	 */
	public GroundItem(String controllerName, Item item, Position position) {
		pulses = 350;
		this.controllerName = controllerName;
		this.item = item;
		this.position = position;
	}
	
	/**
	 * Prevent instantation.
	 */
	private GroundItem() { }

	/**
	 * Decreases the pulses.
	 * 
	 * @param pulses
	 *            The pulses to decrease by.
	 */
	public void decreasePulses(int pulses) {
		this.pulses -= pulses;
	}

	/**
	 * Gets the controller's name.
	 * 
	 * @return The controller's name.
	 */
	public String getControllerName() {
		return controllerName;
	}
	
	/**
	 * Get the ground items.
	 * @return {@link GroundItem}'s
	 */
	public Map<Position, List<GroundItem>> getItems() {
		return groundItems;
	}

	/**
	 * Gets the item.
	 * 
	 * @return The item.
	 */
	public Item getItem() {
		return item;
	}

	/**
	 * Gets the position.
	 * 
	 * @return The position.
	 */
	public Position getPosition() {
		return position;
	}

	/**
	 * Gets the pulses.
	 * 
	 * @return The pulses.
	 */
	public int getPulses() {
		return pulses;
	}
	
	/**
	 * Get the instance.
	 * @return {@link GroundItem}'s
	 */
	public static GroundItem getInstance() {
		return instance;
	}

}