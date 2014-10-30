package org.apollo.game.content.skills.fishing;

import org.apollo.game.model.Position;

/**
 * Spots.java
 * @author The Wanderer
 */
public enum Spots {

    CATHERBY(new int[]{}, new Position[]{});
    int[] id;
    Position[] pos;

    private Spots(int[] id, Position[] pos) {
        this.id = id;
        this.pos = pos;
    }
}
