package org.apollo.game.content.skills.fletching;

import org.apollo.game.action.impl.FletchArrowShaftActon;
import org.apollo.game.action.impl.FletchBowAction;
import org.apollo.game.event.impl.CloseInterfaceEvent;
import org.apollo.game.event.impl.OpenChatInterfaceEvent;
import org.apollo.game.event.impl.SetInterfaceModelEvent;
import org.apollo.game.event.impl.SetInterfaceTextEvent;
import org.apollo.game.model.Animation;
import org.apollo.game.model.Item;
import org.apollo.game.model.Player;
import org.apollo.game.model.Skill;

/**
 * Handles all the fletching interfaces etc
 * 
 * @author Ares_
 * @author The Wanderer
 * @author Rodrigo Molina
 */
public class Fletching {

    /**
     * The headless arrow id
     */
    private static final int HEADLESS_ARROW = 53;
    /**
     * The shaft id.
     */
    public static final int SHAFT = 52;
    /**
     * Feather id
     */
    private static final int FEATHER = 314;
    /**
     * Bow string id
     */
    private static final int STRING = 1777;
    /**
     * Knife id
     */
    @SuppressWarnings("unused")
    private static final int KNIFE = 946;

    /**
     * Executes the arrow making action
     */
    public static void executeArrowTipping(Player player, int itemUsed, int usedWith) {
        int itemId = itemUsed != HEADLESS_ARROW ? itemUsed : usedWith;
        if (ArrowData.forId(itemId) == null) {
            return;
        }
        if (player.getSkillSet().getSkill(Skill.FLETCHING).getCurrentLevel() < ArrowData.forId(itemId).getLevelRequired()) {
            player.sendMessage("You need a Fletching level of " + ArrowData.forId(itemId).getLevelRequired() + " to make these arrows.");
            return;
        }
        if (itemUsed == HEADLESS_ARROW && usedWith == ArrowData.forId(itemId).getTipId() || usedWith == HEADLESS_ARROW && itemUsed == ArrowData.forId(itemId).getTipId()) {
            int amountMade = player.getInventory().getCount(HEADLESS_ARROW) < player.getInventory().getCount(ArrowData.forId(itemId).getTipId())
                    ? (player.getInventory().getCount(HEADLESS_ARROW) > 15 ? 15 : player.getInventory().getCount(HEADLESS_ARROW))
                    : (player.getInventory().getCount(ArrowData.forId(itemId).getTipId()) > 15 ? 15 : player.getInventory().getCount(ArrowData.forId(itemId).getTipId()));
            player.getInventory().remove(new Item(HEADLESS_ARROW, amountMade));
            player.getInventory().remove(new Item(ArrowData.forId(itemId).getTipId(), amountMade));
            player.getInventory().add(new Item(ArrowData.forId(itemId).getReward(), amountMade));
            player.getSkillSet().addExperience(Skill.FLETCHING, ArrowData.forId(itemId).getExperience() * amountMade);
            player.sendMessage("You attach the arrow tips to the headless arrows.");
        }
    }

    /**
     * Executes adding feathers to some shafts.
     * @param player The player.
     * @param itemUsed The item being used.
     * @param usedWith The item being used with.
     */
    public static void executeArrowFeathering(Player player, int itemUsed, int usedWith) {
        if (itemUsed == SHAFT && usedWith == FEATHER || usedWith == SHAFT && itemUsed == FEATHER) {
            int amountMade = player.getInventory().getCount(SHAFT) < player.getInventory().getCount(FEATHER)
                    ? (player.getInventory().getCount(SHAFT) > 15 ? 15 : player.getInventory().getCount(SHAFT))
                    : (player.getInventory().getCount(FEATHER) > 15 ? 15 : player.getInventory().getCount(FEATHER));
            player.getInventory().remove(new Item(SHAFT, amountMade));
            player.getInventory().remove(new Item(FEATHER, amountMade));
            player.getInventory().add(new Item(HEADLESS_ARROW, amountMade));
            player.getSkillSet().addExperience(Skill.FLETCHING, 1 * amountMade);
            player.sendMessage("You attach the feathers to the shafts.");
        }
    }

    /**
     * Executes the dart making stuff
     */
    public static void executeDart(Player player, int itemUsed, int usedWith) {
        int itemId = itemUsed != FEATHER ? itemUsed : usedWith;
        if (DartData.forId(itemId) == null) {
            return;
        }
        if (player.getSkillSet().getSkill(Skill.FLETCHING).getCurrentLevel() < DartData.forId(itemId).getLevelRequired()) {
            player.sendMessage("You need a Fletching level of " + DartData.forId(itemId).getLevelRequired() + " to make these darts.");
            return;
        }
        if (itemUsed == FEATHER && usedWith == DartData.forId(itemId).getDartTip() || usedWith == FEATHER && itemUsed == DartData.forId(itemId).getDartTip()) {
            int amountMade = player.getInventory().getCount(FEATHER) < player.getInventory().getCount(DartData.forId(itemId).getDartTip())
                    ? (player.getInventory().getCount(FEATHER) > 10 ? 10 : player.getInventory().getCount(FEATHER))
                    : (player.getInventory().getCount(DartData.forId(itemId).getDartTip()) > 10 ? 10 : player.getInventory().getCount(DartData.forId(itemId).getDartTip()));
            player.getInventory().remove(new Item(FEATHER, amountMade));
            player.getInventory().remove(new Item(DartData.forId(itemId).getDartTip(), amountMade));
            player.getInventory().add(new Item(DartData.forId(itemId).getReward(), amountMade));
            player.getSkillSet().addExperience(Skill.FLETCHING, DartData.forId(itemId).getExperience());
            player.sendMessage("You attach the dart tips to the dart body.");
        }
    }
    
    public static void fletchArrowShafts(final Player player, final int amount) {
	    player.send(new CloseInterfaceEvent());
	    player.playAnimation(Animation.FLETCHING);
	    player.startAction(new FletchArrowShaftActon(player, amount));
    }
    
    public static void fletchBows(final Player player, final int amount, boolean isShort) {
	    player.send(new CloseInterfaceEvent());
	    if(bow != null) {
		    int level = bow.getLevelReqs(isShort ? 0 : 1);
		    if(player.getSkillSet().getSkill(Skill.FLETCHING).getCurrentLevel() < level) {
			    player.sendMessage("You do not have the required level to fletch that bow.");
			    return;
		    }
		    player.playAnimation(Animation.FLETCHING);
		    player.startAction(new FletchBowAction(player, amount, bow, isShort));
	    }
    }

    static BowData bow;
    public static void setupFletching(Player player, int itemUsed, int itemUsedWith) {
	int log = 0;
	if(itemUsed == 946) {
		log = itemUsedWith;
	} else if (itemUsedWith == 946) {
		log = itemUsed;
	}
        BowData bows = BowData.forId(log);
        if(bows == null) return;
        player.send(new CloseInterfaceEvent());
        bow = bows;
        player.setFletching(true);
        player.setCrafting(false);
        if (log == 1511) {
            player.send(new OpenChatInterfaceEvent(8880));
            player.send(new SetInterfaceModelEvent(8883, 200, 52));
            player.send(new SetInterfaceModelEvent(8884, 200, 50));
            player.send(new SetInterfaceModelEvent(8885, 200, 48));
            player.send(new SetInterfaceTextEvent(8889, "Arrow Shafts"));
            player.send(new SetInterfaceTextEvent(8893, "Shortbow(u)"));
            player.send(new SetInterfaceTextEvent(8897, "Longbow(u)"));
        } else {
            player.send(new OpenChatInterfaceEvent(8866));
            player.send(new SetInterfaceModelEvent(8869, 200, bows.getUnstrungIds(0)));
            player.send(new SetInterfaceModelEvent(8870, 200, bows.getUnstrungIds(1)));
            player.send(new SetInterfaceTextEvent(8874, new Item(bows.getUnstrungIds(0)).getDefinition().getName()));
            player.send(new SetInterfaceTextEvent(8878, new Item(bows.getUnstrungIds(1)).getDefinition().getName()));
        }
    }

    /**
     * Executes bow stringing
     */
    public static void executeBowString(Player player, int itemUsed, int usedWith) {
        int index = 0;
        BowData bowData = null;
        int itemId = itemUsed != STRING ? itemUsed : usedWith;
        for (BowData bow : BowData.values()) {
            for (int i = 0; i < bow.getUnstrungIds().length; i++) {
                if (bow.unstrungIds[i] == itemId) {
                    bowData = bow;
                    index = i;
                }
            }
        }
        if (bowData == null) {
            return;
        }
        if (player.getSkillSet().getSkill(Skill.FLETCHING).getCurrentLevel() < bowData.getLevelReqs()[index]) {
            player.sendMessage("You need a Fletching level of " + bowData.getLevelReqs()[index] + " to string this bow.");
            return;
        }
        if (itemUsed == STRING && usedWith == bowData.getUnstrungIds()[index] || usedWith == STRING && itemUsed == bowData.getUnstrungIds()[index]) {
            player.getInventory().remove(new Item(STRING, 1));
            player.getInventory().remove(new Item(bowData.getUnstrungIds()[index], 1));
            player.getInventory().add(new Item(bowData.getRewards()[index], 1));
            player.getSkillSet().addExperience(Skill.FLETCHING, bowData.getExps()[index] / 2);
            player.sendMessage("You attach the string to the bow.");
        }
    }
}
