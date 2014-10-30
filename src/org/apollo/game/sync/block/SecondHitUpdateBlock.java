package org.apollo.game.sync.block;

import org.apollo.game.event.impl.DamageEvent;

/**
 * DoubleHitUpdateBlock.java
 * @author The Wanderer
 */
public class SecondHitUpdateBlock extends SynchronizationBlock {
    
    private final DamageEvent damage;
    
    SecondHitUpdateBlock(DamageEvent damage) {
        this.damage = damage;
    }
    
    public DamageEvent getDamage() {
        return damage;
    }
}
