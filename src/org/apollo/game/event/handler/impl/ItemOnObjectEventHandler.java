package org.apollo.game.event.handler.impl;

import org.apollo.game.content.skills.cooking.Cooking;
import org.apollo.game.content.skills.cooking.MeatData;
import org.apollo.game.content.skills.runecrafting.RuneCrafting;
import org.apollo.game.content.skills.smithing.Smithing;
import org.apollo.game.event.handler.EventHandler;
import org.apollo.game.event.handler.EventHandlerContext;
import org.apollo.game.event.impl.ItemOnObjectActionEvent;
import org.apollo.game.model.Animation;
import org.apollo.game.model.Item;
import org.apollo.game.model.Player;
import org.apollo.game.model.Position;

/**
 * ItemOnObjectEventHandler.java
 * @author The Wanderer
 */
public class ItemOnObjectEventHandler extends EventHandler<ItemOnObjectActionEvent> {
    
    @Override
    public void handle(EventHandlerContext ctx, Player player, ItemOnObjectActionEvent event) {
        Position pos = new Position(event.getObjectX(), event.getObjectY(), player.getPosition().getHeight());
        if(player.getPosition().isWithinInteractionDistance(pos)) {
            player.turnTo(pos);
            MeatData meat = MeatData.forId(event.getId());
            if(meat != null && meat.getRaw() == event.getId()) {
                Cooking.setupCookMeat(player, event.getId(), event.getObjectId());
                return;
            }
			
            switch(event.getId()) {
            	case 2714:
            	    if(event.getId() == 1947) {
            		if(player.getQuestHolder().hasStartedQuest(player.getQuestHolder().getCookAssistant())) {
            		    player.getQuestHolder().getCookAssistant().executeStep();
            		}
            	    }
            	    break;
                case 1925:
                    if(player.getInventory().contains(1925) && event.getObjectId() == 8689) {
                        player.playAnimation(new Animation(2292));
                        player.getInventory().remove(new Item(1925, 1));
                        player.getInventory().add(new Item(1927 ,1));	
                     } else {
                        player.sendMessage("You need a bucket to milk a cow!");			
                    }
                    break;
                case 5341:
                    player.getFarming().cleanPatch(event.getId(), event.getObjectId());
                    break;
                    
                    
                case 952:
                    player.getFarming().clearPatch(event.getId(), event.getObjectId());
                    break;
                    
                case 6032:
                case 6034:
                    player.getFarming().addCompost(event.getId(), event.getObjectId());
                    break;
                    
                case 6036:
                    player.getFarming().diseaseCuring(event.getId(), event.getObjectId());
                    break;
                    
                case 1438:
                case 1440:
                case 1442:
                case 1444:
                case 1446:
                case 1448:
                case 1452:
                case 1454:
                case 1456:
                case 1458:
                case 1462:
                    RuneCrafting.handleTalismanTeleporting(player, event.getId(), event.getObjectId());
                    RuneCrafting.handleComboRunes(player, event.getId(), event.getObjectId());
                    break;
                    
                case 5291:
                case 5292:
                case 5293:
                case 5294:
                case 5295:
                case 5296:
                case 5297:
                case 5298:
                case 5299:
                case 5300:
                case 5301:
                case 5302:
                case 5303:
                case 5304:
                    player.getFarming().handleHerbFarming(event.getId(), event.getObjectId());
                    break;
                    
                case 2349:
                case 2351:
                case 2353:
                case 2359:
                case 2361:
                case 2363:
                    Smithing.setupSmithingInterface(player, event.getId(), event.getObjectId(), event.getObjectX(), event.getObjectY());
                    break;
                    
                    
            }
        }
    }
}
