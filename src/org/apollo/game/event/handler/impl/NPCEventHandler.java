package org.apollo.game.event.handler.impl;

import org.apollo.game.content.skills.fishing.Fishing;
import org.apollo.game.content.skills.thieving.Pickpocketing;
import org.apollo.game.content.skills.thieving.Thieving;
import org.apollo.game.event.handler.EventHandler;
import org.apollo.game.event.handler.EventHandlerContext;
import org.apollo.game.event.impl.NPCActionEvent;
import org.apollo.game.model.NPC;
import org.apollo.game.model.Player;
import org.apollo.game.model.Position;
import org.apollo.game.model.World;
import org.apollo.game.model.inter.bank.BankUtils;
import org.apollo.game.model.inter.dialog.DialogueSender;
import org.apollo.game.model.inter.dialog.DialogueSender.HeadAnimations;
import org.apollo.game.model.inter.dialog.impl.BankerListener;
import org.apollo.game.model.inter.dialog.impl.CookListener;
import org.apollo.game.model.inter.shop.ShopHandler;

/**
 * NPCEventHandler.java
 * 
 * @author The Wanderer
 */
public class NPCEventHandler extends EventHandler<NPCActionEvent> {

    @Override
    public void handle(EventHandlerContext ctx, Player player, NPCActionEvent event) {
        NPC npc = (NPC) World.getWorld().getNPCRepository().get(event.getIndex());
        int npcID = npc.getDefinition().getId();
        Position pos = new Position(npc.getPosition().getX(), npc.getPosition().getY(), npc.getPosition().getHeight());
        player.turnTo(pos);
        if (event.getOption() == 1) {
            switch (npcID) {
                case 316:
                case 317:
                case 312:
                case 334:
                    Fishing.handleFishing(npcID, player, 1, event.getIndex());
                    break;
                case 278:
                    if(player.getQuestHolder().hasStartedQuest(player.getQuestHolder().getCookAssistant())) {
                	DialogueSender.sendNPCChatOneLine(player, new CookListener(player), "What's wrong?", 278, 2, "Cook", HeadAnimations.SAD);
                    } else if (player.getQuestHolder().hasDoneQuest(player.getQuestHolder().getCookAssistant())) {
                	DialogueSender.sendNPCChatOneLine(player, new CookListener(player), "don't know for this.", 278, 2, "Cook", HeadAnimations.MORESAD);
                    } else {
                	DialogueSender.sendNPCChatOneLine(player, new CookListener(player), "What am I to do?", 278, 2, "Cook", HeadAnimations.MORESAD);
                    }
                    npc.turnTo(player.getPosition());
                    break;
                case 494:
                    DialogueSender.sendNPCChatOneLine(player, new BankerListener(player), "What would you like to do?", 494, 1, "Banker", HeadAnimations.CALM1);
                    break;
            }
        } else if (event.getOption() == 2) {
		//could just do..
    	    Pickpocketing pick = Pickpocketing.forId(npcID);
    	    if(pick != null) {
    		Thieving.handlePickpocketing(player, event.getIndex(), pos);
    		return;
    	    }
            switch (npcID) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 1714:
                case 1715:
                case 1710:
                case 1711:
                case 1712:
                case 15:
                case 18:
                case 187:
                case 2234:
                case 2235:
                case 3299:
                case 9:
                case 10:
                case 1305:
                case 1306:
                case 1307:
                case 1308:
                case 1309:
                case 1310:
                case 1311:
                case 1312:
                case 1313:
                case 1314:
                case 1883:
                case 1884:
                case 23:
                case 1880:
                case 1881:
                case 34:
                case 1904:
                case 1905:
                case 20:
                case 66:
                case 67:
                case 68:
                case 159:
                case 160:
                case 161:
                case 168:
                case 169:
                case 21:
                case 2363:
                case 2364:
                case 2365:
                case 2366:
                case 2367:
                    Thieving.handlePickpocketing(player, event.getIndex(), pos);
                    break;
                case 316:
                case 334:
                case 317:
                case 312:
                    Fishing.handleFishing(npcID, player, 2, event.getIndex());
                    break;

                case 494:
                    BankUtils.openBank(player);
                    break;
                case 519:
                    ShopHandler.open(5, player);
                    break;
                case 520:
                case 521:
                    ShopHandler.open(31, player);
                    break;


            }
        } else if (event.getOption() == 3) {
        } else if (event.getOption() == 4) {
            player.getCombatState().startAttacking(npc, false);
        }
    }
}
