package org.apollo.game.event.handler.impl;

import org.apollo.game.content.skills.cooking.Cooking;
import org.apollo.game.content.skills.crafting.Crafting;
import org.apollo.game.content.skills.fletching.Fletching;
import org.apollo.game.content.skills.smithing.Smithing;
import org.apollo.game.event.handler.EventHandler;
import org.apollo.game.event.handler.EventHandlerContext;
import org.apollo.game.event.impl.ButtonEvent;
import org.apollo.game.model.EquipmentConstants;
import org.apollo.game.model.Item;
import org.apollo.game.model.Player;
import org.apollo.game.model.Prayers;
import org.apollo.game.model.Skill;
import org.apollo.game.model.Trade;
import org.apollo.game.model.combat.CombatState.CombatStyle;
import org.apollo.game.model.combat.action.AbstractCombatAction;

/**
 * ButtonEventHandler.java
 * @author The Wanderer
 */
public class ButtonEventHandler extends EventHandler<ButtonEvent> {

    @Override
    public void handle(EventHandlerContext ctx, final Player player, ButtonEvent event) {
	if(event.getInterfaceId() >= 14873 && event.getInterfaceId() <= 14882) {
	    if(player.getBankPin() != null)
		player.getBankPin().clickButton(event.getInterfaceId());
	}
        switch (event.getInterfaceId()) {
        //dialogue shit
            case 2485:
            case 2484:
            case 2483:
            case 2482:
            case 2461:
            case 2462:
            case 2472:
            case 2471:
            case 2473:
        	player.getInterfaceSet().buttonClicked(event.getInterfaceId());
            	break;
            case 7333:
        	player.getQuestHolder().getCookAssistant().displayInterface(player);
        	break;
            case 7332:
        	player.getQuestHolder().getBlackKnightFortress().displayInterface(player);
        	break;
            case 14922:
        	if(player.getBankPin() != null) {
        	    player.getBankPin().exit();
        	}
        	break;
            case 14921:
        	if(player.getBankPin() != null) {
        	    player.getBankPin().dontKnowIt();
        	}
        	break;
            //Prayers
            case 5609:
            case 5610:
            case 5611:
            case 5612:
            case 5613:
            case 5614:
            case 5615:
            case 5616:
            case 5617:
            case 5618:
            case 5619:
            case 5620:
            case 5621:
            case 5622:
            case 5623:
                Prayers.activatePrayer(player, event.getInterfaceId() - 5609);
                break;
            case 683:
            case 684:
            case 685:
                Prayers.activatePrayer(player, event.getInterfaceId() - 683 + 15);
                break;

            //Cooking
            case 13720:
                Cooking.startCookMeat(player, 1);
                break;

            case 12296:
                player.getCombatState().setCombatStyle(CombatStyle.DEFENSIVE);
                break;

            case 12297:
                player.getCombatState().setCombatStyle(CombatStyle.CONTROLLED_1);
                break;

            case 5862:
        	player.getCombatState().setCombatStyle(CombatStyle.AGGRESSIVE_1);
        	break;
        	
            case 12298:
                player.getCombatState().setCombatStyle(CombatStyle.ACCURATE);
                break;

            case 150:
                player.setAutoRetaliate(true);
                break;

            case 151:
                player.setAutoRetaliate(false);
                break;

            case 7622: //Special attack bar - Spears
            case 7537: //Special attack bar - Bows
            case 7587: //Special attack bar - Swords
            case 7487: //Special attack bar - Axes
            case 7612: //Special attack bar - Maces
            case 7788: //Special attack bar - Claws
            case 7562: //Special attack bar - Daggers
            case 12311: //Special attack bar - Abyssal Whip
            case 8481: //Special attack bar - Halberds
            case 7687: //Special attack bar - 2H's
            case 7662: //Special attack bar - Spears
            case 7637: //Special attack bar - Thrownaxes
                int weaponId = player.getEquipment().get(EquipmentConstants.WEAPON).getId();
                switch(weaponId) {
                    case 1377:
                        int[] skills = {Skill.ATTACK, Skill.DEFENCE, Skill.RANGED, Skill.MAGIC};
                        AbstractCombatAction.doSpecial(player, weaponId, skills, -.1);
                        break;
                        
                    case 35:
                        AbstractCombatAction.doSpecial(player, weaponId, new int[] {}, 0);
                        break;
                }
                player.getCombatState().inverseSpecial();
                break;
            case 7462: //Special attack bar - Granite maul
                final Item weapon = player.getEquipment().get(EquipmentConstants.WEAPON);
                if (player.getCombatState().getAttackEvent() != null && player.getActiveCombatAction().canHit(player, player.getInteractingCharacter())) {
                    if (weapon != null && weapon.getId() == 4153) {
                        if (player.getCombatState().getSpecialEnergy() >= weapon.getEquipmentDefinition().getSpecialConsumption()) {
                            player.getActiveCombatAction().special(player, player.getInteractingCharacter());
                        } else {
                            player.sendMessage("You do not have enough special energy left.");
                        }
                    }
                } else {
                    player.getCombatState().inverseSpecial();
                    if (player.getCombatState().isSpecialOn()) {
                        player.sendMessage("Warning: Since the maul's special is an instant attack, it will be wasted when used");
                        player.sendMessage("first strike.");
                    }
                }
                break;

            case 13719:
                Cooking.startCookMeat(player, 5);
                break;

            //Crafting D'hide Bodies or fletching.
            case 8889:
        	    if(player.isFletching())
        		    Fletching.fletchArrowShafts(player, 1);
        	    else if (player.isCrafting())
        		    Crafting.craftLeather(player, 1, 0);
                break;

            case 8888:
        	    if(player.isFletching())
        		    Fletching.fletchArrowShafts(player, 5);
        	    else if (player.isCrafting())
        		    Crafting.craftLeather(player, 5, 0);
                break;

            case 8887:
        	    if(player.isFletching())
        		    Fletching.fletchArrowShafts(player, 10);
        	    else if (player.isCrafting())
        		    Crafting.craftLeather(player, 10, 0);
                break;
            //peee
            case 8893:
        	    if(player.isFletching())
        		    Fletching.fletchBows(player, 1, true);
        	    else if (player.isCrafting())
        		    Crafting.craftLeather(player, 1, 0);
                break;

            case 8892:
        	    if(player.isFletching())
        		    Fletching.fletchBows(player, 5, true);
        	    else if (player.isCrafting())
        		    Crafting.craftLeather(player, 5, 0);
                break;

            case 8891:
        	    if(player.isFletching())
        		    Fletching.fletchBows(player, 10, true);
        	    else if (player.isCrafting())
        		    Crafting.craftLeather(player, 10, 0);
                break;
                //uhh
            case 8897:
        	    if(player.isFletching())
        		    Fletching.fletchArrowShafts(player, 1);
        	    else if (player.isCrafting())
        		    Crafting.craftLeather(player, 1, 0);
                break;

            case 8896:
        	    if(player.isFletching())
        		    Fletching.fletchBows(player, 5, false);
        	    else if (player.isCrafting())
        		    Crafting.craftLeather(player, 5, 0);
                break;

            case 8895:
        	    if(player.isFletching())
        		    Fletching.fletchBows(player, 10, false);
        	    else if (player.isCrafting())
        		    Crafting.craftLeather(player, 10, 0);
                break;
                
            case 3420:
                if (player.getTradeStage() == 1 && player.getOfferOpponent()[0].getTradeStage() == 1) {
                    player.getEventManager().sendString("Waiting on other player.", 3431);
                    player.getOfferOpponent()[0].getEventManager().sendString("Other player has accepted.", 3431);
                    player.setTradeStage(2);
                    ctx.breakHandlerChain();
                    return;
                } else if (player.getTradeStage() == 1 && player.getOfferOpponent()[0].getTradeStage() == 2) {
                    player.setTradeStage(2);
                    Trade.changeToConfirmScreen(player, player.getOfferOpponent()[0]);
                    ctx.breakHandlerChain();
                    return;
                }
                break;

            case 3546:
            case 13218:
            case 13902:
                if (player.getTradeStage() == 2 && player.getOfferOpponent()[0].getTradeStage() == 2) {
                    player.getEventManager().sendString("Waiting on other player.", 3535);
                    player.getOfferOpponent()[0].getEventManager().sendString("Other player has accepted.", 3535);
                    player.setTradeStage(3);
                    ctx.breakHandlerChain();
                    return;
                } else if (player.getTradeStage() == 2 && player.getOfferOpponent()[0].getTradeStage() == 3 && Trade.hasRoom(player)) {
                    player.setTradeStage(3);
                    // player.getInventory().transfer(player.getOfferOpponent()[0].getOffer(), player.getInventory());
                    // player.getInventory().transfer(player.getOffer(), player.getOfferOpponent()[0].getInventory());
                    player.getInterfaceSet().close();
                    player.getOfferOpponent()[0].getInterfaceSet().close();
                    Trade.declineTrade(new Player[]{player, player.getOfferOpponent()[0]});
                }

                break;

            case 2798:
                Crafting.craftLeather(player, 5, 0);
                break;

            case 1747:
                //Crafting.craftLeather(player, player.getInventory().getCount(1743), 0);
                break;

            //Crafting Leather
            case 8635:
                Crafting.craftLeather(player, 1, 0);
                break;

            case 8634:
                Crafting.craftLeather(player, 5, 0);
                break;

            case 8633:
                Crafting.craftLeather(player, 10, 0);
                break;

            case 8638:
                Crafting.craftLeather(player, 1, 5);
                break;

            case 8637:
                Crafting.craftLeather(player, 5, 5);
                break;

            case 8636:
                Crafting.craftLeather(player, 10, 5);
                break;

            case 8641:
                Crafting.craftLeather(player, 1, 4);
                break;

            case 8640:
                Crafting.craftLeather(player, 5, 4);
                break;

            case 8639:
                Crafting.craftLeather(player, 10, 4);
                break;

            case 8644:
                Crafting.craftLeather(player, 1, 1);
                break;

            case 8643:
                Crafting.craftLeather(player, 5, 1);
                break;

            case 8642:
                Crafting.craftLeather(player, 10, 1);
                break;

            case 8647:
                Crafting.craftLeather(player, 1, 2);
                break;

            case 8646:
                Crafting.craftLeather(player, 5, 2);
                break;

            case 8645:
                Crafting.craftLeather(player, 10, 2);
                break;

            case 8650:
                Crafting.craftLeather(player, 1, 6);
                break;

            case 8649:
                Crafting.craftLeather(player, 5, 6);
                break;

            case 8648:
                Crafting.craftLeather(player, 10, 6);
                break;

            case 8653:
                Crafting.craftLeather(player, 1, 3);
                break;

            case 8652:
                Crafting.craftLeather(player, 5, 3);
                break;

            case 8651:
                Crafting.craftLeather(player, 10, 3);
                break;

            case 8965:
                Crafting.craftLeather(player, 1, 4);
                break;

            case 8964:
                Crafting.craftLeather(player, 5, 4);
                break;

            case 8963:
                Crafting.craftLeather(player, 10, 4);
                break;

            case 8961:
                Crafting.craftLeather(player, 1, 3);
                break;

            case 8960:
                Crafting.craftLeather(player, 5, 3);
                break;

            case 8959:
                Crafting.craftLeather(player, 10, 3);
                break;

            case 8957:
                Crafting.craftLeather(player, 1, 2);
                break;

            case 8956:
                Crafting.craftLeather(player, 5, 2);
                break;

            case 8955:
                Crafting.craftLeather(player, 10, 2);
                break;

            case 8953:
                Crafting.craftLeather(player, 1, 1);
                break;

            case 8952:
                Crafting.craftLeather(player, 5, 1);
                break;

            case 8951:
                Crafting.craftLeather(player, 10, 1);
                break;

            case 8949:
                Crafting.craftLeather(player, 1, 0);
                break;

            case 8948:
                Crafting.craftLeather(player, 5, 0);
                break;

            case 8947:
                Crafting.craftLeather(player, 10, 0);
                break;

            //Start of Smelting
            case 3987:
                Smithing.handleSmelting(player, 0, 1);
                break;

            case 3986:
                Smithing.handleSmelting(player, 0, 5);
                break;

            case 2807:
                Smithing.handleSmelting(player, 0, 10);
                break;

            case 3991:
                Smithing.handleSmelting(player, 1, 1);
                break;

            case 3990:
                Smithing.handleSmelting(player, 1, 5);
                break;

            case 3989:
                Smithing.handleSmelting(player, 1, 10);
                break;

            case 3995:
                Smithing.handleSmelting(player, 2, 1);
                break;

            case 3994:
                Smithing.handleSmelting(player, 2, 5);
                break;

            case 3993:
                Smithing.handleSmelting(player, 2, 10);
                break;

            case 3999:
                Smithing.handleSmelting(player, 3, 1);
                break;

            case 3998:
                Smithing.handleSmelting(player, 3, 5);
                break;

            case 3997:
                Smithing.handleSmelting(player, 3, 10);
                break;

            case 4003:
                Smithing.handleSmelting(player, 4, 1);
                break;

            case 4002:
                Smithing.handleSmelting(player, 4, 5);
                break;

            case 4001:
                Smithing.handleSmelting(player, 4, 10);
                break;

            case 7441:
                Smithing.handleSmelting(player, 5, 1);
                break;

            case 7440:
                Smithing.handleSmelting(player, 5, 5);
                break;

            case 6397:
                Smithing.handleSmelting(player, 5, 10);
                break;

            case 7446:
                Smithing.handleSmelting(player, 6, 1);
                break;

            case 7444:
                Smithing.handleSmelting(player, 6, 5);
                break;

            case 7443:
                Smithing.handleSmelting(player, 6, 10);
                break;

            case 7450:
                Smithing.handleSmelting(player, 7, 1);
                break;

            case 7449:
                Smithing.handleSmelting(player, 7, 5);
                break;

            case 7448:
                Smithing.handleSmelting(player, 7, 10);
                break;
            case 153:
                player.getWalkingQueue().setRunningQueue(true);
                break;
            case 152:
                player.getWalkingQueue().setRunningQueue(false);
                break;
        }
    }
}
