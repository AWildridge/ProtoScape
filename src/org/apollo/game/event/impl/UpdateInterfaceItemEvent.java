/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apollo.game.event.impl;

import org.apollo.game.event.Event;
import org.apollo.game.model.Item;

/**
 *
 * @author D
 */
public class UpdateInterfaceItemEvent extends Event {
    
    private final int interfaceId;
    private final int childId;
    private final  int type;
    private final  Item[] items;
    
    public UpdateInterfaceItemEvent(int interfaceId, int childId, int type, Item[] items) {
        this.interfaceId = interfaceId;
        this.childId = childId;
        this.type = type;
        this.items = items;
    }
    
}
