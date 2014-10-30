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
public class AnswerTradeEvent extends Event {
    
    private int id;
    
    public AnswerTradeEvent(int id) {
        this.id = id;
    }
    
    public int getId() {
        return id;
    }

}
