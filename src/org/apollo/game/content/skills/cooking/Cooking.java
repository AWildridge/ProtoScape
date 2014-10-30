package org.apollo.game.content.skills.cooking;

import org.apollo.game.action.impl.CookingMeatAction;
import org.apollo.game.event.impl.CloseInterfaceEvent;
import org.apollo.game.event.impl.OpenChatInterfaceEvent;
import org.apollo.game.event.impl.SetInterfaceModelEvent;
import org.apollo.game.event.impl.SetInterfaceTextEvent;
import org.apollo.game.model.Item;
import org.apollo.game.model.Player;
import org.apollo.game.model.Skill;

/**
 * Cooking.java
 * 
 * @author The Wanderer
 */
public class Cooking {

    public static int cookAmount;
    static boolean flag;
    static MeatData meat;

    public static void startCookMeat(final Player player, int cookingAmount) {
        cookAmount = cookingAmount;
            if (player.getInventory().contains(meat.getRaw())) {
                player.send((new CloseInterfaceEvent()));
                if (player.hasAttributeTag("cooking")) {
                    return;
                }
                player.getAttributeTags().add("cooking");
                player.startAction(new CookingMeatAction(player, meat));
            }
    }

    public static void setupCookMeat(Player player, int itemId, int objectId) {
        for (MeatData m : MeatData.values()) {
            if (m.getRaw() == itemId) {
                meat = m;
                if (objectId == 114 || objectId == 2732 || objectId == 2728) {
                    if (player.getSkillSet().getSkill(Skill.COOKING).getCurrentLevel() < m.getLevelReq()) {
                        player.sendMessage("You need level " + m.getLevelReq() + " cooking to cook this item.");
                        return;
                    }
                    if (player.getInventory().contains(m.getRaw())) {
                        player.send(new OpenChatInterfaceEvent(1743));
                        player.send(new SetInterfaceModelEvent(13716, 200, m.getRaw()));
                        player.send(new SetInterfaceTextEvent(13717, new Item(m.getRaw()).getDefinition().getName()));
                        if (objectId == 114) {
                            m.burnLevel -= 1;
                        }
                        if (player.getEquipment().contains(775)) {
                            if (itemId == 359 || itemId == 377 || itemId == 371 || itemId == 7944 || itemId == 383 || itemId == 395 || itemId == 389) {
                                m.burnLevel -= 6;
                            }
                        }
                        if (objectId == 114) {
                            if (player.getEquipment().contains(775)) {
                                if (itemId == 359 || itemId == 377 || itemId == 371 || itemId == 7944 || itemId == 383 || itemId == 395 || itemId == 389) {
                                    m.burnLevel -= 6;
                                }
                            }
                        }
                        if (objectId == 2732) {
                            m.burnLevel += 1;
                        }
                        flag = isRange(player, objectId);
                    }
                }
            }
        }
    }

    public static boolean isRange(Player player, int objectId) {
        if (objectId == 2732) {
            return false;
        }
        if (objectId == 114 || objectId == 2728 || objectId == 2729 || objectId == 2730 || objectId == 2731) {
            return true;
        }
        return true;
    }
}
