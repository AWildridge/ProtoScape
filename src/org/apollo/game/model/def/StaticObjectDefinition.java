package org.apollo.game.model.def;

import org.apollo.game.model.Animation;
import org.apollo.game.model.obj.StaticObject;

/**
 * Represents a type of {@link StaticObject}.
 * @author Jimmy Frix
 * @author The Wanderer
 */
public final class StaticObjectDefinition {

    private String[] actionNames;
    private String description;
    private boolean hasActions;
    private int id;
    private Animation animation;
    private String name;
    private boolean solid;
    private boolean walkable;
    private int xSize;
    private int ySize;
    private boolean doorClosed;
    
    public StaticObjectDefinition(int id) {
        this.id = id;
    }

    public String[] getActionNames() {
        return actionNames;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getXSize() {
        return xSize;
    }

    public int getYSize() {
        return ySize;
    }

    public Animation getAnimation() {
        return animation;
    }

    public void setAnimation(Animation animation) {
        this.animation = animation;
    }

    public boolean hasActions() {
        return hasActions;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setActionNames(String[] actionNames) {
        this.actionNames = actionNames;
    }

    public void setActions(boolean hasActions) {
        this.hasActions = hasActions;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSolid(boolean solid) {
        this.solid = solid;
    }

    public void setWalkable(boolean walkable) {
        this.walkable = walkable;
    }

    public void setDoorClosed(boolean doorClosed) {
        this.doorClosed = doorClosed;
    }
    
    

    public void setXSize(int xSize) {
        this.xSize = xSize;
    }

    public void setYSize(int ySize) {
        this.ySize = ySize;
    }

    public boolean solid() {
        return solid;
    }

    public boolean isDoorClosed() {
        return doorClosed;
    }
    
    public boolean walkable() {
        return walkable;
    }
    private static StaticObjectDefinition[] definitions;

    public static StaticObjectDefinition forId(int id) {
        return definitions[id];
    }

    public static void init(StaticObjectDefinition[] definitions) {
        StaticObjectDefinition.definitions = definitions;
        for (int id = 0; id < definitions.length; id++) {
            StaticObjectDefinition def = definitions[id];
            if (def.getId() != id) {
                throw new RuntimeException("Object definition id mismatch!");
            }
        }
    }
}