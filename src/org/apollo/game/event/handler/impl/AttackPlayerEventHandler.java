package org.apollo.game.event.handler.impl;

import org.apollo.game.event.handler.EventHandler;
import org.apollo.game.event.handler.EventHandlerContext;
import org.apollo.game.event.impl.AttackPlayerEvent;
import org.apollo.game.model.Player;
import org.apollo.game.model.World;

/**
 * AttackPlayerEvent.java
 * @author The Wanderer
 */
public class AttackPlayerEventHandler extends EventHandler<AttackPlayerEvent> {
    
    @Override
    public void handle(EventHandlerContext ctx, Player player, AttackPlayerEvent event) {
        Player victim = (Player) World.getWorld().getPlayerRepository().get(event.getIndex());
        player.getCombatState().startAttacking(victim, false);
    }
    
}
