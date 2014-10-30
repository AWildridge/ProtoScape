package org.apollo.game.scheduling.impl;

import java.util.Iterator;
import java.util.List;

import org.apollo.game.event.impl.PositionEvent;
import org.apollo.game.event.impl.SendGroundItemEvent;
import org.apollo.game.model.GroundItem;
import org.apollo.game.model.Item;
import org.apollo.game.model.Player;
import org.apollo.game.model.Position;
import org.apollo.game.model.World;
import org.apollo.game.scheduling.ScheduledTask;

/**
 * A {@link ScheduledTask} which processes all ground items currently in game,
 * decreasing their pulses, making them global or removing them if needed.
 * 
 * @author Michael Bull (Scu11)
 */
public final class ProcessGroundItemsTask extends ScheduledTask {

	/**
	 * Creates the ground item process task.
	 */
	public ProcessGroundItemsTask() {
		super(0, false);
	}

	@Override
	public void execute() {
		for (Position position : GroundItem.getInstance().getItems().keySet()) {
			List<GroundItem> groundItems = GroundItem.getInstance().getItems().get(position);
			if (groundItems != null) {
                            
				for(Iterator<GroundItem> it$ = groundItems.iterator(); it$.hasNext();) {
					GroundItem groundItem = it$.next();				
					groundItem.decreasePulses(1);
					//if (!groundItem.getItem().getDefinition().isUntradeable()) {
						if (groundItem.getPulses() == GroundItem.getInstance().GLOBAL_PULSES) { // tradeables appear for a player for 60 seconds, then are removed 150 seconds after that
							for (Player player : World.getWorld().getPlayerRepository()) {
								if (!player.getUndefinedName().equals(groundItem.getControllerName()) && player.getPosition().isWithinDistance(position, 60)) {
                                                                        player.send(new PositionEvent(player.getPosition(), groundItem.getPosition()));
									player.send(new SendGroundItemEvent(new Item(groundItem.getItem().getId(), groundItem.getItem().getAmount()), groundItem.getPosition()));
								}
							}
						}
					//}

					if (groundItem.getPulses() == 0) {
						it$.remove();
						GroundItem.getInstance().delete(groundItem);
						if (GroundItem.getInstance().getItems().get(position) == null) {
							break;
						}
					}
				}
			}
		}
	}

}