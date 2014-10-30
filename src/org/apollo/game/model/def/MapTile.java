package org.apollo.game.model.def;

import org.apollo.game.model.Position;

public class MapTile {

    public static final int TILE_FLAGS_CLIPPING_BIT = 1;
    public static final int TILE_FLAGS_BRIDGE_BIT = 2;
    public static final int TILE_FLAGS_ROOF_REMOVAL_BIT = 4;
    public static final int TILE_FLAGS_BLACK_MINIMAP_BIT = 8;
    private int overlay = 0;
    private int underlay = 0;
    private int flags = 0;
    private int height = -1;
    private Position position = null;
    private int shape = 0;

    public int getOverlay() {
        return overlay;
    }

    public void setOverlay(int overlay) {
        this.overlay = overlay;
    }

    public int getUnderlay() {
        return underlay;
    }

    public void setUnderlay(int underlay) {
        this.underlay = underlay;
    }

    public int getFlags() {
        return flags;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public int getShape() {
        return shape;
    }

    public void setShape(int shape) {
        this.shape = shape;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
