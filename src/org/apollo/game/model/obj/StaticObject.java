package org.apollo.game.model.obj;

import org.apollo.game.model.Position;
import org.apollo.game.model.World;
import org.apollo.game.model.def.StaticObjectDefinition;
import org.apollo.game.model.region.Region;

/**
 * Represents a static object in the game world.
 * @author Graham
 */
public final class StaticObject {

    /**
     * The object definition.
     */
    private StaticObjectDefinition definition;
    /**
     * The type.
     */
    private int type;
    /**
     * The rotation.
     */
    private int rotation;
    /**
     * If this object is visible to all players
     */
    private boolean global = true;
    /**
     * The name of the player that owns this object.
     */
    String controllerName;
    /**
     * The position.
     */
    private Position position;
    /**
     * Whether or not the object is in the cache.
     */
    private boolean clientside;

    public StaticObject(int id, Position position, int type, int rotation) {
        this.definition = StaticObjectDefinition.forId(id);
        this.position = position;
        this.type = type;
        this.rotation = rotation;
    }

    /**
     * Creates the game object.
     * @param def The object's definition.
     */
    public StaticObject(StaticObjectDefinition definition, Position position, int type, int rotation) {
        this.definition = definition;
        this.type = type;
        this.position = position;
        this.rotation = rotation;
    }

    /**
     * Creates the game object.
     * @param def The object's definition.
     */
    public StaticObject(StaticObjectDefinition definition, Position position, int type, int rotation, boolean clientside) {
        this.definition = definition;
        this.type = type;
        this.position = position;
        this.rotation = rotation;
        this.clientside = clientside;
    }

    /**
     * Creates the game object.
     * @param definition The definition.
     * @param position The location.
     * @param type The type.
     * @param rotation The rotation.
     * @param controllerName The player this object is visible to
     */
    public StaticObject(StaticObjectDefinition definition, Position position, int type, int rotation, String controllerName) {
        this.setPosition(position);
        this.definition = definition;
        this.type = type;
        this.rotation = rotation;
        this.controllerName = controllerName;
        this.global = false;
    }

    public StaticObjectDefinition getDefinition() {
        return definition;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public int getRotation() {
        return rotation;
    }

    public boolean isGlobal() {
        return global;
    }

    public int getType() {
        return type;
    }

    public boolean isClientside() {
        return clientside;
    }

    public boolean isOwnedBy(String name) {
        return isGlobal() || controllerName.equals(name);
    }
    
    private static StaticObject[] objects;
    
    public static void init(StaticObject[] objects) {
        for(int i = 0; i < objects.length; i++) {
            StaticObject object = objects[i];
            Region r = World.getWorld().getRegionRepository().getRegionByPosition(object.getPosition());
            r.addObject(object);
        }
        objects = null;
    }
}
