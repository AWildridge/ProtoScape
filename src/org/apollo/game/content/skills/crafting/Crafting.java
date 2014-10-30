package org.apollo.game.content.skills.crafting;

import org.apollo.game.action.impl.LeatherCraftingAction;
import org.apollo.game.event.impl.CloseInterfaceEvent;
import org.apollo.game.event.impl.OpenChatInterfaceEvent;
import org.apollo.game.event.impl.OpenInterfaceEvent;
import org.apollo.game.event.impl.SetInterfaceModelEvent;
import org.apollo.game.event.impl.SetInterfaceTextEvent;
import org.apollo.game.model.Animation;
import org.apollo.game.model.Item;
import org.apollo.game.model.Player;
import org.apollo.game.model.Skill;

/**
 * Version 1.0.0
 * 
 * Crafting.java
 * 
 * @author The Wanderer
 * @author Ares_
 */
public class Crafting {

    static int type;
    public static int amt;
    private static int STAFF = 1391;
    private static int CHISEL = 1755;
    private static int WOOL = 1759;

    public static void setupInterface(Player player, int hide) {
        player.send(new CloseInterfaceEvent());
        type = hide;
        LeatherData hides = LeatherData.forId(hide);
        if (hide == 1743) {
            player.send(new CloseInterfaceEvent());
            player.send(new OpenChatInterfaceEvent(4429));
            player.send(new SetInterfaceTextEvent(2799, "Hardleather body"));
            player.send(new SetInterfaceModelEvent(1746, 150, 1131));
        } else if (hide == 1741) {
            player.send(new CloseInterfaceEvent());
            player.send(new OpenInterfaceEvent(2311));
        } else if (hide == 6289) {
            player.send(new CloseInterfaceEvent());
            player.send(new OpenChatInterfaceEvent(8938));
            player.send(new SetInterfaceTextEvent(8949, "Body"));
            player.send(new SetInterfaceTextEvent(8953, "Chaps"));
            player.send(new SetInterfaceTextEvent(8957, "Vambraces"));
            player.send(new SetInterfaceTextEvent(8961, "Bandana"));
            player.send(new SetInterfaceTextEvent(8965, "Boots"));
            player.send(new SetInterfaceModelEvent(8941, 150, 6322));
            player.send(new SetInterfaceModelEvent(8942, 150, 6324));
            player.send(new SetInterfaceModelEvent(8943, 150, 6330));
            player.send(new SetInterfaceModelEvent(8944, 150, 6326));
            player.send(new SetInterfaceModelEvent(8945, 150, 6328));
        } else {
            player.send(new CloseInterfaceEvent());
            player.send(new OpenChatInterfaceEvent(8880));
            Item firstItem = new Item(hides.getProductId(0));
            Item secondItem = new Item(hides.getProductId(1));
            Item thirdItem = new Item(hides.getProductId(2));
            player.send(new SetInterfaceTextEvent(8889, firstItem.getDefinition().getName()));
            player.send(new SetInterfaceTextEvent(8893, secondItem.getDefinition().getName()));
            player.send(new SetInterfaceTextEvent(8897, thirdItem.getDefinition().getName()));
            player.send(new SetInterfaceModelEvent(8883, 150, hides.getProductId(0)));
            player.send(new SetInterfaceModelEvent(8884, 150, hides.getProductId(1)));
            player.send(new SetInterfaceModelEvent(8885, 150, hides.getProductId(2)));
        }
    }

    public static void craftLeather(final Player player, int amount, final int index) {
        amt = amount;
        final LeatherData hides = LeatherData.forId(type);
        if(hides == null) {
        	System.out.println("Type invalid: "+type);
        	return;
        }
        player.send(new CloseInterfaceEvent());
        if (player.getSkillSet().getSkill(Skill.CRAFTING).getCurrentLevel() < hides.getLevelReq(index)) {
            player.sendMessage("You need level " + hides.getLevelReq(index) + " Crafting to craft this.");
            return;
        }
        if (!player.getInventory().contains(1734)) {
            player.sendMessage("You need thread to craft armor.");
            return;
        }
        if (player.getInventory().getCount(hides.getHide()) < hides.getHideAmount(index)) {
            player.sendMessage("You don't have the required amount of " + new Item(hides.getHide()).getDefinition().getName().toLowerCase() + " to make this item.");
            return;
        }
        player.playAnimation(new Animation(1249));

        player.startAction(new LeatherCraftingAction(player, hides, index));
    }
    private static int[] hideModels = {1739, 1739, 7801, 6287, 1753, 1751, 1749, 1747};
    private static String[] hideNames = {"Soft leather", "Hard leather", "Snake skin", "Snake skin", "Green D'hide", "Blue D'hide", "Red D'hide", "Black D'hide"};
    private static String[] hideCosts = {"1", "3", "20", "15", "20", "20", "20,", "20"};

    public static void setupTanningInterface(Player player) {
        player.send(new CloseInterfaceEvent());
        player.send(new OpenInterfaceEvent(14670));
        for (int i = 0; i < hideModels.length; i++) {
            player.send(new SetInterfaceModelEvent(14769 + i, 200, hideModels[i]));
            player.send(new SetInterfaceTextEvent(14777 + i, hideNames[i]));
            player.send(new SetInterfaceTextEvent(14785 + i, hideCosts[i] + " coins"));
        }
    }

    public static void executeCut(Player player, int itemUsed, int usedWith) {
        int itemId = itemUsed != CHISEL ? itemUsed : usedWith;
        final Item item = new Item(itemId, 1);
        if (GemData.forId(itemId) == null) {
            return;
        }
        if (player.getSkillSet().getSkill(Skill.CRAFTING).getCurrentLevel() < GemData.forId(itemId).getLevelRequired()) {
            player.sendMessage("You need a Crafting level of " + GemData.forId(itemId).getLevelRequired() + " to cut this gem.");
            return;
        }
        if (itemUsed == CHISEL && usedWith == GemData.forId(itemId).getUnCut() || usedWith == CHISEL && itemUsed == GemData.forId(itemId).getUnCut()) {
            player.playAnimation(new Animation(GemData.forId(itemId).getAnimation()));
            player.getInventory().remove(new Item(GemData.forId(itemId).getUnCut(), 1));
            player.getInventory().add(new Item(GemData.forId(itemId).getReward(), 1));
            player.getSkillSet().addExperience(12, GemData.forId(itemId).experience());
            player.sendMessage("You cut the " + item.getDefinition().getName().toLowerCase() + ".");
        }
    }

    public static void executeAmulet(Player player, int itemUsed, int usedWith) {
        int itemId = itemUsed != WOOL ? itemUsed : usedWith;
        if (AmuletData.forId(itemId) == null) {
            return;
        }
        if (player.getSkillSet().getSkill(Skill.CRAFTING).getCurrentLevel() < AmuletData.forId(itemId).getLevelRequired()) {
            player.sendMessage("You need a Crafting level of " + AmuletData.forId(itemId).getLevelRequired() + " to string this amulet.");
            return;
        }
        if (itemUsed == WOOL && usedWith == AmuletData.forId(itemId).getUnStrung() || usedWith == WOOL && itemUsed == AmuletData.forId(itemId).getUnStrung()) {
            player.getInventory().remove(new Item(WOOL, 1));
            player.getInventory().remove(new Item(AmuletData.forId(itemId).getUnStrung(), 1));
            player.getInventory().add(new Item(AmuletData.forId(itemId).getReward(), 1));
            player.getSkillSet().addExperience(12, AmuletData.forId(itemId).getExperience());
            player.sendMessage("You string the amulet.");
        }
    }

    public static void executeStaff(Player player, int itemUsed, int usedWith) {
        int itemId = itemUsed != STAFF ? itemUsed : usedWith;
        if (StaffData.forId(itemId) == null) {
            return;
        }
        if (player.getSkillSet().getSkill(Skill.CRAFTING).getCurrentLevel() < StaffData.forId(itemId).getLevelRequired()) {
            player.sendMessage("You need a Crafting level of " + StaffData.forId(itemId).getLevelRequired() + " to cut this gem.");
            return;
        }
        if (itemUsed == STAFF && usedWith == StaffData.forId(itemId).getOrb() || usedWith == STAFF && itemUsed == StaffData.forId(itemId).getOrb()) {
            player.getInventory().remove(new Item(STAFF, 1));
            player.getInventory().remove(new Item(StaffData.forId(itemId).getOrb(), 1));
            player.getInventory().add(new Item(StaffData.forId(itemId).getReward(), 1));
            player.getSkillSet().addExperience(12, StaffData.forId(itemId).getExperience());
            player.sendMessage("You put the battlestaff togther.");
        }
    }
}
