package org.apollo.game.content;

import org.apollo.game.content.DwarfCannon.CannonFace;
import org.apollo.game.model.Position;

/**
 * Represents a single cannon.
 *
 *
 * @author Rodrigo Molina
 */
public class Cannon {

    /**
     * The face of the cannon.
     */
    private CannonFace face;

    /**
     * The position of the cannon.
     */
    private Position pos;

    /**
     * The cannon balls amount inside the cannon.
     */
    private int cannonBalls;

    /**
     * Creates a new cannon.
     *
     * @param player The player.
     * @param face The current face.
     * @param pos The position of the cannon.
     * @param cannonBalls The amount of balls.
     */
    public Cannon(CannonFace face, Position pos, int cannonBalls) {
        this.pos = pos;
        this.face = face;
        this.cannonBalls = cannonBalls;
    }

    /**
     * Decrements the cannon balls value.
     */
    public void decrement() {
        cannonBalls--;
    }

    public CannonFace getFace() {
        return face;
    }

    public void setFace(CannonFace face) {
        this.face = face;
    }

    public int getCannonBalls() {
        return cannonBalls;
    }

    public void setCannonBalls(int cannonBalls) {
        this.cannonBalls = cannonBalls;
    }

    public Position getPos() {
        return pos;
    }

    public void setPos(Position pos) {
        this.pos = pos;
    }
}
