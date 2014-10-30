package org.apollo.game.event.handler.impl;

import org.apollo.game.content.skills.smithing.Smithing;
import org.apollo.game.event.handler.EventHandler;
import org.apollo.game.event.handler.EventHandlerContext;
import org.apollo.game.event.impl.ItemActionEvent;
import org.apollo.game.model.Player;
/**
 * SmithingEventHandler.java
 * @author The Wanderer
 */
public class SmithingEventHandler extends EventHandler<ItemActionEvent> {
    
    /**
	 * Converts an option to an amount.
	 * @param option The option.
	 * @return The amount.
	 * @throws IllegalArgumentException if the option is not legal.
	 */
	private static final int optionToAmount(int option) {
		switch (option) {
		case 1:
			return 1;
		case 2:
			return 5;
		case 3:
			return 10;
                case 4:
                        return 0;
                case 5:
                        return 0;
		}
		throw new IllegalArgumentException();
	}

	@Override
	public void handle(EventHandlerContext ctx, Player player, ItemActionEvent event) {
                int amount = optionToAmount(event.getOption());
		
                switch(event.getInterfaceId()) {
                    case 1119:
                    case 1120:
                    case 1121:
                    case 1122:
                    case 1123:
                        Smithing.handleSmithing(player, event.getId(), amount);
                        break;
                }
		
	}
    
}
