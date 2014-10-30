/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apollo.game.event.handler.impl;
import org.apollo.game.event.handler.EventHandler;
import org.apollo.game.event.handler.EventHandlerContext;
import org.apollo.game.event.impl.PickupItemEvent;
import org.apollo.game.model.GroundItem;
import org.apollo.game.model.Player;
import org.apollo.game.model.Position;

/**
 *
 * @author Arrowzftw
 */

public final class PickupItemHandler extends EventHandler<PickupItemEvent> {

	@Override
	public void handle(EventHandlerContext ctx, Player player, PickupItemEvent event) {
		GroundItem.getInstance().pickup(player, new Position(event.getX(), event.getY(), player.getPosition().getHeight()), event.getItemId());
        }
}
