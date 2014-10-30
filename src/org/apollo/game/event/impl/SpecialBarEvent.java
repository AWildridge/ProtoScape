/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 *
 * @author D
 */
public class SpecialBarEvent extends Event {
    
    private int id;
    private boolean value;
    private byte state;
    
    public SpecialBarEvent(int id, boolean value) {
        this.id = id;
        this.value = value;
        this.state = (byte) (value ? 1 : 0);
    }
    
    public int getId() {
        return id;
    }
    
    public boolean getValue() {
        return value;
    }
    
    public byte getState() {
        return state;
    }
    
}
