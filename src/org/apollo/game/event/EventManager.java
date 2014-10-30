/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apollo.game.event;

import org.apollo.game.event.impl.SendConfigEvent;
import org.apollo.game.event.impl.SetInterfaceTextEvent;
import org.apollo.game.event.impl.SpecialBarEvent;
import org.apollo.game.event.impl.UpdateItemsEvent;
import org.apollo.game.model.EquipmentConstants;
import org.apollo.game.model.Item;
import org.apollo.game.model.Player;
import org.apollo.game.model.def.WeaponStyle;

/**
 * @author The Wanderer - renamed to EventManager, as that is what this is.
 *     Also repackaged.
 * @author D
 */
public class EventManager {
    
    private Player player;
    
    public EventManager(Player player) {
        this.player = player;
    }
    
    public void sendUpdateItems(int frame, Item[] items) {
        player.send(new UpdateItemsEvent(frame, items));
    }
    
    public void sendString(int id, String text) {
        player.send(new SetInterfaceTextEvent(id, text));
    }
    public void sendString(String text, int id) {
        player.send(new SetInterfaceTextEvent(id, text));
    }
    
    public void sendSetInterfaceHidden(int id, boolean state) {
        player.send(new SpecialBarEvent(id, state));
    }
    
    public void sendConfig(int id, int state) {
        player.send(new SendConfigEvent(id, state));
    }
    
    /**
     * Special bar frame ID's.
     */
    public static final int[] SPECIAL_BAR_fRAMES = new int[] { 7611, 7561,
                    7586, 7686, 7611, 7511, 7636, 7812, 8505, 7486, 7561, 12335, 7711 };

    public void sendSpecialBar() {
            Item weapon = player.getEquipment().get(EquipmentConstants.WEAPON);

            boolean weaponStyleSpecial = false;
            if (weapon != null && weapon.getEquipmentDefinition() != null) {
                    for (int i = 0; i < weapon.getEquipmentDefinition()
                                    .getWeaponStyles().length; i++) {
                            if (weapon.getEquipmentDefinition().getWeaponStyle(i) == WeaponStyle.SPECIAL) {
                                    weaponStyleSpecial = true;
                                    break;
                            }
                    }
            }
            
            if (weapon == null || weapon.getEquipmentDefinition() == null || !weaponStyleSpecial) {
                    player.getEventManager().sendSetInterfaceHidden(7599, true);
                    player.getEventManager().sendSetInterfaceHidden(7649, true);
                    player.getEventManager().sendSetInterfaceHidden(7549, true);
                    player.getEventManager().sendSetInterfaceHidden(7574, true);
                    player.getEventManager().sendSetInterfaceHidden(7674, true);
                    player.getEventManager().sendSetInterfaceHidden(7599, true);
                    player.getEventManager().sendSetInterfaceHidden(7499, true);
                    player.getEventManager().sendSetInterfaceHidden(7624, true);
                    player.getEventManager().sendSetInterfaceHidden(7800, true);
                    player.getEventManager().sendSetInterfaceHidden(8493, true);
                    player.getEventManager().sendSetInterfaceHidden(7474, true);
                    player.getEventManager().sendSetInterfaceHidden(7549, true);
                    player.getEventManager().sendSetInterfaceHidden(7699, true);
                    player.getEventManager().sendSetInterfaceHidden(12323, true);
                    return;
            }
                    sendSetInterfaceHidden(7599, false);
                    sendSetInterfaceHidden(7649, false);
                    sendSetInterfaceHidden(7549, false);
                    sendSetInterfaceHidden(7574, false);
                    sendSetInterfaceHidden(7674, false);
                    sendSetInterfaceHidden(7599, false);
                    sendSetInterfaceHidden(7499, false);
                    sendSetInterfaceHidden(7624, false);
                    sendSetInterfaceHidden(7800, false);
                    sendSetInterfaceHidden(8493, false);
                    sendSetInterfaceHidden(7474, false);
                    sendSetInterfaceHidden(7549, false);
                    sendSetInterfaceHidden(7699, false);
                    sendSetInterfaceHidden(12323, false);
                    sendConfig(300, player.getCombatState().getSpecialEnergy() * 10);
                    sendConfig(301, player.getCombatState().isSpecialOn() ? 1 : 0);
    }
    
}
