package org.apollo.game.model;

import java.util.ArrayList;
import java.util.List;

import org.apollo.game.model.def.StaticObjectDefinition;
import org.apollo.game.model.obj.StaticObject;
import org.apollo.game.pf.LineOfSightPathFinder;
import org.apollo.game.pf.Path;
import org.apollo.game.pf.PathFinder;

/**
 * Represents a position in the world.
 * @author Graham
 */
public final class Position {

    /**
     * The number of height levels.
     */
    public static final int HEIGHT_LEVELS = Integer.MAX_VALUE;
    /**
     * The maximum distance players/NPCs can 'see'.
     */
    public static final int MAX_DISTANCE = 15;
    /**
     * The x coordinate.
     */
    private final int x;
    /**
     * The y coordinate.
     */
    private final int y;
    /**
     * The height level.
     */
    private final int height;

    /**
     * Creates a position at the default height.
     * @param x The x coordinate.
     * @param y The y coordinate.
     */
    public Position(int x, int y) {
        this(x, y, 0);
    }

    /**
     * Creates a position with the specified height.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param height The height.
     */
    public Position(int x, int y, int height) {
        if (height < 0 || height >= HEIGHT_LEVELS) {
            throw new IllegalArgumentException("Height out of bounds");
        }
        this.x = x;
        this.y = y;
        this.height = height;
    }

    /**
     * Gets the x coordinate.
     * @return The x coordinate.
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the y coordinate.
     * @return The y coordinate.
     */
    public int getY() {
        return y;
    }

    /**
     * Gets the height level.
     * @return The height level.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Gets the x coordinate of the region.
     * @return The region x coordinate.
     */
    public int getTopLeftRegionX() {
        return (x / 8) - 6;
    }

    /**
     * Gets the y coordinate of the region.
     * @return The region y coordinate.
     */
    public int getTopLeftRegionY() {
        return (y / 8) - 6;
    }

    /**
     * Gets the x coordinate of the central region.
     * @return The x coordinate of the central region.
     */
    public int getCentralRegionX() {
        return x / 8;
    }

    /**
     * Gets the y coordinate of the central region.
     * @return The y coordinate of the central region.
     */
    public int getCentralRegionY() {
        return y / 8;
    }

    /**
     * Gets the x coordinate inside the region of this position.
     * @return The local x coordinate.
     */
    public int getLocalX() {
        return getLocalX(this);
    }

    /**
     * Gets the y coordinate inside the region of this position.
     * @return The local y coordinate.
     */
    public int getLocalY() {
        return getLocalY(this);
    }

    /**
     * Gets the local x coordinate inside the region of the {@code base}
     * position.
     * @param base The base position.
     * @return The local x coordinate.
     */
    public int getLocalX(Position base) {
        return x - (base.getTopLeftRegionX() * 8);
    }

    /**
     * Gets the local y coordinate inside the region of the {@code base}
     * position.
     * @param base The base position.
     * @return The local y coordinate.
     */
    public int getLocalY(Position base) {
        return y - (base.getTopLeftRegionY() * 8);
    }

    @Override
    public int hashCode() {
        return ((height << 30) & 0xC0000000) | ((y << 15) & 0x3FFF8000) | (x & 0x7FFF);
    }

    /**
     * Gets the distance between this position and another position. Only X and
     * Y are considered (i.e. 2 dimensions).
     * @param other The other position.
     * @return The distance.
     */
    public double getDistance(Position other) {
        int deltaX = x - other.x;
        int deltaY = y - other.y;
        return (Math.sqrt(deltaX * deltaX + deltaY * deltaY));
    }

    /**
     * Gets the longest horizontal or vertical delta between the two positions.
     * @param other The other position.
     * @return The longest horizontal or vertical delta.
     */
    public int getLongestDelta(Position other) {
        int deltaX = x - other.x;
        int deltaY = y - other.y;
        return Math.max(deltaX, deltaY);
    }

    /**
     * Checks if the position is within distance of another.
     * @param other The other position.
     * @param distance The distance.
     * @return {@code true} if so, {@code false} if not.
     */
    public boolean isWithinDistance(Position other, int distance) {
        int deltaX = Math.abs(x - other.x);
        int deltaY = Math.abs(y - other.y);
        return deltaX <= distance && deltaY <= distance;
    }

    /**
     * Checks if this location is within range of another.
     * @param other The other location.
     * @return <code>true</code> if the location is in range,
     * <code>false</code> if not.
     */
    public boolean isWithinDistance(Position other) {
        if (height != other.height) {
            return false;
        }
        int deltaX = other.x - x, deltaY = other.y - y;
        return deltaX <= 14 && deltaX >= -15 && deltaY <= 14 && deltaY >= -15;
    }

    /**
     * Checks if this location is next to another.
     * @param other The other location.
     * @return <code>true</code> if so, <code>false</code> if not.
     */
    public boolean isNextTo(Position other) {
        if (height != other.height) {
            return false;
        }
        /*int deltaX = Math.abs(other.x - x), deltaY = Math.abs(other.y - y);
        return deltaX <= 1 && deltaY <= 1;*/
        return (getX() == other.getX() && getY() != other.getY()
                || getX() != other.getX() && getY() == other.getY()
                || getX() == other.getX() && getY() == other.getY());
    }

    /**
     * Gets the closest tile of an character to this location.
     */
    public Position closestTileOfCharacter(Character character) {
        if (character.getWidth() < 2 && character.getHeight() < 2) {
            Position location = null;
            List<Position> characterPositions = new ArrayList<Position>();
            characterPositions.add(new Position(character.getPosition().getX(), character.getPosition().getY() + 1, character.getPosition().getHeight()));
            characterPositions.add(new Position(character.getPosition().getX() + 1, character.getPosition().getY(), character.getPosition().getHeight()));
            characterPositions.add(new Position(character.getPosition().getX(), character.getPosition().getY() - 1, character.getPosition().getHeight()));
            characterPositions.add(new Position(character.getPosition().getX() - 1, character.getPosition().getY(), character.getPosition().getHeight()));
            for (Position loc : characterPositions) {
                if (location == null || distanceToPoint(loc) < distanceToPoint(location)) {
                    location = loc;
                }
            }
            return location;
        }
        Position location = null;
        for (int x = 0; x < character.getWidth(); x++) {
            for (int y = 0; y < character.getPosition().getHeight(); y++) {
                Position loc = new Position(character.getPosition().getX() + x, character.getPosition().getY() + y, character.getPosition().getHeight());
                if (loc.isNextTo(this) && (location == null || !location.isNextTo(this))) {
                    location = loc;
                } else if (location == null || loc.distanceToPoint(this) < location.distanceToPoint(this)) {
                    location = loc;
                }
            }
        }
        return closestTileToCharacter(character);
    }

    /**
     * Gets the closest tile of an character to this location.
     */
    public Position closestTileToCharacter(Character character) {
        if (character.getWidth() < 2 && character.getHeight() < 2) {
            return character.getPosition();
        }
        Position location = null;
        for (int x = 0; x < character.getWidth(); x++) {
            for (int y = 0; y < character.getHeight(); y++) {
                Position loc = new Position(character.getPosition().getX() + x, character.getPosition().getY() + y, character.getPosition().getHeight());
                if (loc.isNextTo(this) && (location == null || !location.isNextTo(this))) {
                    location = loc;
                } else if (location == null || loc.distanceToPoint(this) < location.distanceToPoint(this)) {
                    location = loc;
                }
            }
        }
        return location;
    }

    public boolean lineOfSight(Character character) {
        PathFinder pf = new LineOfSightPathFinder();
        Position tile = closestTileToCharacter(character);
        Path p = pf.findPath(this, tile);

        if (p == null) {
            return true;
        }

        if (p.getPoints().size() > 0 && (p.getPoints().getLast().getX() != tile.getX() || p.getPoints().getLast().getY() != tile.getY())) {
            return false;
        }

        return true;
    }

    /**
     * Gets the distance to a location.
     * @param other The location.
     * @return The distance from the other location.
     */
    public int distanceToPoint(Position other) {
        int absX = x;
        int absY = y;
        int pointX = other.getX();
        int pointY = other.getY();
        return (int) Math.sqrt(Math.pow(absX - pointX, 2) + Math.pow(absY - pointY, 2));
    }

    /**
     * Checks if this location is within interaction range of another.
     * @param other The other location.
     * @return <code>true</code> if the location is in range,
     * <code>false</code> if not.
     */
    public boolean isWithinInteractionDistance(Position other) {
        if (height != other.height) {
            return false;
        }
        int deltaX = other.x - x, deltaY = other.y - y;
        return deltaX <= 2 && deltaX >= -3 && deltaY <= 2 && deltaY >= -3;
    }
    
    /**
     * Checks if this location is within interaction range of the object.
     * @param object The object
     * @return <code>true</code> if the location is in range,
     * <code>false</code> if not.
     */
    public boolean isWithinInteractionDistance(StaticObject object) {
        Position other = object.getPosition();
        StaticObjectDefinition definition = object.getDefinition();
        System.out.println(definition.getXSize() + "," + definition.getYSize());
        if (height != other.height) {
            return false;
        }
        int deltaX = other.x - x, deltaY = other.y - y;
        return deltaX <= 2 && deltaX >= -3 && deltaY <= 2 && deltaY >= -3;
    }

    /**
     * Creates a new location based on this location.
     * @param diffX X difference.
     * @param diffY Y difference.
     * @param diffZ Z difference.
     * @return The new location.
     */
    public Position transform(int diffX, int diffY, int diffZ) {
        return new Position(x + diffX, y + diffY, height + diffZ);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Position other = (Position) obj;
        if (height != other.height) {
            return false;
        }
        if (x != other.x) {
            return false;
        }
        if (y != other.y) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return Position.class.getName() + " [x=" + x + ", y=" + y + ", height=" + height + "]";
    }
}
