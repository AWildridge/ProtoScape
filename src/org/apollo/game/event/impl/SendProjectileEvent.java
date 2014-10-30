package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * SendProjectileEvent.java
 * @author The Wanderer
 * @author Jake_G
 */
public class SendProjectileEvent extends Event {

    private int offsetY, offsetX, startHeight, endHeight;
    private int lockon, id, speed, angle, delay, slope, radius;

    public SendProjectileEvent(int offsetX, int offsetY, int id, int delay, int angle, int speed, int startHeight, int endHeight, int lockon, int slope, int radius) {
        this.offsetY = offsetY;
        this.offsetX = offsetX;
        this.id = id;
        this.delay = delay;
        this.angle = angle;
        this.speed = speed;
        this.startHeight = startHeight;
        this.endHeight = endHeight;
        this.lockon = lockon;
        this.slope = slope;
        this.radius = radius;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getStartHeight() {
        return startHeight;
    }

    public int getEndHeight() {
        return endHeight;
    }

    public int getAngle() {
        return angle;
    }

    public int getLockon() {
        return lockon;
    }

    public int getDelay() {
        return delay;
    }

    public int getRadius() {
        return radius;
    }

    public int getSlope() {
        return slope;
    }

    public int getId() {
        return id;
    }

    public int getSpeed() {
        return speed;
    }
}
