package org.apollo.game.sync.block;

import org.apollo.game.event.impl.DamageEvent;

/**
 *
 * @author ArrowzFtw
 */
public class HitUpdateBlock extends SynchronizationBlock {
    
    private final DamageEvent damage;
    
    HitUpdateBlock(DamageEvent damage) {
        this.damage = damage;
    }
    
    public DamageEvent getDamage() {
        return damage;
    }
    
}