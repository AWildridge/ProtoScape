/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apollo.game.model;

import org.apollo.game.event.impl.SetInterfaceModelEvent;

/**
 *
 * @author D
 */
public class WeaponInterfaces {

	public static void sendWeapon(Player player, int id, String name, String genericName) {
               
		if (name.equals("unarmed")) {
			player.sendSidebarInterface(0, 5855);
			player.getEventManager().sendString(5857, genericName);
		} else if (name.endsWith("whip")) {
                        player.sendSidebarInterface(0, 12290);
			player.send(new SetInterfaceModelEvent(12291, 200, id));
			player.getEventManager().sendString(12293, genericName);
		} else if (name.endsWith("Scythe")) {
			player.sendSidebarInterface(0, 776);
			player.send(new SetInterfaceModelEvent(777, 200, id));
			player.getEventManager().sendString(779, genericName);
		} else if (name.endsWith("bow") || name.startsWith("crystal bow") || name.startsWith("toktz-xil-ul")) {
			player.sendSidebarInterface(0, 1764);
			player.send(new SetInterfaceModelEvent(1765, 200, id));
			player.getEventManager().sendString(1767, genericName);
		} else if (name.startsWith("Staff") || name.endsWith("staff")) {
			player.sendSidebarInterface(0, 328);
			player.send(new SetInterfaceModelEvent(329, 200, id));
			player.getEventManager().sendString(331, genericName);
		} else if (name.startsWith("dart")) {
			player.sendSidebarInterface(0, 4446);
			player.send(new SetInterfaceModelEvent(4447, 200, id));
			player.getEventManager().sendString(4449, genericName);
		} else if (name.startsWith("dagger")) {
			player.sendSidebarInterface(0, 2276);
			player.send(new SetInterfaceModelEvent(2277, 200, id));
			player.getEventManager().sendString(2279, genericName);
		} else if (name.startsWith("pickaxe")) {
			player.sendSidebarInterface(0, 5570);
			player.send(new SetInterfaceModelEvent(5571, 200, id));
			player.getEventManager().sendString(5573, genericName);
		} else if (genericName.startsWith("axe") || genericName.startsWith("battleaxe")) {
			player.sendSidebarInterface(0, 1698);
			player.send(new SetInterfaceModelEvent(1699, 200, id));
			player.getEventManager().sendString(1701, genericName);
		} else if (genericName.startsWith("axe") || genericName.startsWith("battleaxe")) {
			player.sendSidebarInterface(0, 1698);
			player.send(new SetInterfaceModelEvent(1699, 200, id));
			player.getEventManager().sendString(1701, genericName);
		} else if (name.startsWith("halberd")) {
			player.sendSidebarInterface(0, 8460);
			player.send(new SetInterfaceModelEvent(8461, 200, id));
			player.getEventManager().sendString(8463, genericName);
		} else {
			player.sendSidebarInterface(0, 2423);
			player.send(new SetInterfaceModelEvent(2424, 200, id));
			player.getEventManager().sendString(2426, genericName);
		}
	}
        
        public static void UpdateWep(Player player) {
                Item item = player.getEquipment().get(EquipmentConstants.WEAPON);
                String itemName = null;
                String genericName = null;
                if(item != null) {
                    if(item.getDefinition().getName() != null) {
                    itemName = item.getDefinition().getName().toLowerCase();
                    genericName = item.getDefinition().getName();
                    } else {
                    itemName = "unarmed";
                    genericName = "Unarmed";
                }
                } else {
                    itemName = "unarmed";
                    genericName = "Unarmed";
                }
                
                if(item != null) {
                WeaponInterfaces.sendWeapon(player, item.getId(), itemName, genericName);
                } else {
                WeaponInterfaces.sendWeapon(player, -1, itemName, genericName);
                }
        }
        
}
