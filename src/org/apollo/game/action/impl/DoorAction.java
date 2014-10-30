package org.apollo.game.action.impl;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apollo.game.action.Action;
import org.apollo.game.model.Character;
import org.apollo.game.model.Player;
import org.apollo.game.model.Position;
import org.apollo.game.model.World;
import org.apollo.game.model.def.StaticObjectDefinition;
import org.apollo.game.model.obj.StaticObject;
import org.apollo.util.XMLController;

/**
 * 
 */
public class DoorAction extends Action {

    /**
     * Logger instance.
     */
    private static final Logger logger = Logger.getLogger(DoorAction.class.getName());
    private static HashMap<Integer, Door> doorMap = new HashMap<Integer, Door>();
    private StaticObject object;
    private Door door;

    public DoorAction(StaticObject g, Character player) {
        super(0, true, player);
        object = g;
        door = doorMap.get(g.getDefinition().getId());
    }

    public static boolean isDoor(int id, int action) {
        StaticObjectDefinition obdef = StaticObjectDefinition.forId(id);
        return (doorMap.containsKey(id) && (((!doorMap.get(id).nonDefaultAction) && action == 0) || action == doorMap.get(id).action))
                || (obdef.getName().equals("Door")
                && obdef.getActionNames()[0] != null
                && (obdef.getActionNames()[0].equals("Close")
                || obdef.getActionNames()[0].equals("Open")));
    }

    @Override
    public QueuePolicy getQueuePolicy() {
        return QueuePolicy.FORCE;
    }

    @Override
    public WalkablePolicy getWalkablePolicy() {
        return WalkablePolicy.NON_WALKABLE;
    }

    @Override
    public void execute() {
        if (door != null && door.lockFunction != null && door.hasLockScript) {
            stop();
            return;
        }
        int rotation = object.getRotation();
        int id = object.getDefinition().getId();
        int type = object.getType();
        Position position = object.getPosition();
        World.getWorld().unregister(object);
        if (door == null && object.getDefinition().isDoorClosed()) {
            Point t = translateCalcOpen(rotation);
            position = position.transform(t.x, t.y, 0);
            rotation = openDirection(rotation);
            id++;
        } else if (door == null) {
            Point t = translateCalcClose(rotation);
            position = position.transform(t.x, t.y, 0);
            rotation = closeDirection(rotation);
            id--;
        } else {
            if (door.getRotation() != 5) {
                rotation = door.isAbsRotation() ? door.getRotation() : (rotation + door.getRotation());
            }
            if (door.translate()) {
                position = new Position(position.getX() + door.getTranslateX(), position.getY() + door.getTranslateY(), position.getHeight());
            }
            if (door.isGate()) {
                StaticObject gate = door.getGateObject(object);
                if (gate != null) {
                    handleGates(gate);
                } else {
                    Position objectPosition = new Position(position.getX() + door.gateX, position.getY() + door.gateY, getCharacter().getPosition().getHeight());
                    ((Player) getCharacter()).sendMessage("Gate fault:" + objectPosition.toString());
                }
            }
            if (door.isReplace()) {
                id = door.getReplaceId();
            }
        }
        object = new StaticObject(StaticObjectDefinition.forId(id), position, type, rotation);
        World.getWorld().register(object);
        stop();
    }

    private Point translateCalcOpen(int rot) {
        switch (rot) {
            case 0:
                return new Point(-1, 0);
            case 1:
                return new Point(0, 1);
            case 2:
                return new Point(1, 0);
            case 3:
                return new Point(0, -1);
        }
        return null;
    }

    private Point translateCalcClose(int rot) {
        switch (rot) {
            case 1:
                return new Point(1, 0);
            case 2:
                return new Point(0, -1);
            case 3:
                return new Point(-1, 0);//X
            case 0:
                return new Point(0, 1);
        }
        return null;
    }

    private int openDirection(int rot) {
        switch (rot) {
            case 0:
                return 1;
            case 1:
                return 2;
            case 2:
                return 3;
            case 3:
                return 0;
        }
        return rot;
    }

    private int closeDirection(int rot) {
        switch (rot) {
            case 1:
                return 0;
            case 2:
                return 1;
            case 3:
                return 2;
            case 0:
                return 3;
        }
        return rot;
    }

    private void handleGates(StaticObject object) {
        Door door = doorMap.get(object.getDefinition().getId());
        int rotation = object.getRotation();
        int id = object.getDefinition().getId();
        int type = object.getType();
        Position position = object.getPosition();
        World.getWorld().unregister(object);
        if (door.isReplace()) {
            id = door.getReplaceId();
        }
        if (door.getRotation() != 5) {
            rotation = door.isAbsRotation() ? door.getRotation() : (rotation + door.getRotation());
        }
        if (door.translate()) {
            position = new Position(position.getX() + door.getTranslateX(), position.getY() + door.getTranslateY(), position.getHeight());
        }
        object = new StaticObject(StaticObjectDefinition.forId(id), position, type, rotation);
        World.getWorld().register(object);
    }

    public static void init() throws IOException {
        logger.log(Level.INFO, "Loading door definitions");
        XMLController.alias("door", Door.class);
        Door[] doors = XMLController.readXML(new File("./data/doors.xml"));
        for (Door o : doors) {
            doorMap.put(o.getObjectId(), o);
        }
        logger.log(Level.INFO, "Loaded " + doorMap.size() + " door definitions");
    }

    public class Door {

        private boolean isGate;
        private int objectId;
        private int rotation;
        private int replaceId;
        private boolean replace;
        private boolean absRotation;
        private int translateX;
        private int translateY;
        private boolean translate;
        private int gateId;
        private int gateX;
        private int gateY;
        private boolean nonDefaultAction = false;
        private int action;
        private boolean hasLockScript = false;
        private String lockFunction = "";

        public Door(boolean gate, int objectId, int rotation, int replaceId, boolean replace, boolean absRotation, boolean translate, int translateX, int translateY, int gateX, int gateY, int gateId) {
            isGate = gate;
            this.objectId = objectId;
            this.rotation = rotation;
            this.replaceId = replaceId;
            this.replace = replace;
            this.absRotation = absRotation;
            this.translate = translate;
            this.translateX = translateX;
            this.translateY = translateY;
            this.gateX = gateX;
            this.gateY = gateY;
            this.gateId = gateId;
        }

        public boolean isGate() {
            return isGate;
        }

        public int getObjectId() {
            return objectId;
        }

        public int getRotation() {
            return rotation;
        }

        public int getReplaceId() {
            return replaceId;
        }

        public boolean isReplace() {
            return replace;
        }

        public boolean isAbsRotation() {
            return absRotation;
        }

        public int getTranslateX() {
            return translateX;
        }

        public int getTranslateY() {
            return translateY;
        }

        public boolean translate() {
            return translate;
        }

        public StaticObject getGateObject(StaticObject object) {
            Position oloc = object.getPosition();
            Position objectloc = new Position(oloc.getX() + gateX, oloc.getY() + gateY, oloc.getHeight());
            Collection<StaticObject> gs = World.getWorld().getRegionRepository().getRegionByPosition(objectloc).getStaticObjects();
            StaticObject g = null;
            for (StaticObject b : gs) {
                if (b.getPosition().equals(objectloc) && b.getDefinition().getId() == gateId) {
                    g = b;
                }
            }
            return g;
        }
    }
}
