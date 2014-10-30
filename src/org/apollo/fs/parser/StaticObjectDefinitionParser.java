package org.apollo.fs.parser;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.apollo.fs.IndexedFileSystem;
import org.apollo.fs.archive.Archive;
import org.apollo.game.model.Animation;
import org.apollo.game.model.def.StaticObjectDefinition;
import org.apollo.util.ByteBufferUtil;

/**
 * A class which parses object definitions.
 * @author Jimmy Frix
 */
public final class StaticObjectDefinitionParser {

    /**
     * The indexed file system.
     */
    private IndexedFileSystem fs;

    /**
     * Creates the parser.
     * @param fs The indexed file system.
     */
    public StaticObjectDefinitionParser(IndexedFileSystem fs) {
        this.fs = fs;
    }

    /**
     * Parses the object definitions.
     * @return An array of definitions.
     * @throws IOException 
     */
    public StaticObjectDefinition[] parse() throws IOException {
        Archive config = Archive.decode(fs.getFile(0, 2));
        ByteBuffer dat = config.getEntry("loc.dat").getBuffer();
        ByteBuffer idx = config.getEntry("loc.idx").getBuffer();

        int count = idx.getShort();
        int[] indices = new int[count];
        int index = 2;
        for (int i = 0; i < count; i++) {
            indices[i] = index;
            index += idx.getShort();
        }

        StaticObjectDefinition[] defs = new StaticObjectDefinition[count];
        for (int i = 0; i < count; i++) {
            dat.position(indices[i]);
            defs[i] = parseDefinition(i, dat);
        }

        return defs;
    }

    /**
     * Parses a single definition.
     * @param id The item's id.
     * @param buffer The buffer.
     * @return The definition.
     */
    public StaticObjectDefinition parseDefinition(int id, ByteBuffer data) {
        StaticObjectDefinition def = new StaticObjectDefinition(id);

        while (true) {
            int configId = data.get() & 0xFF;

            if (configId == 0) {
                return def;
            } else if (configId == 1) {
                int amount = data.get() & 0xFF;
                for (int i = 0; i < amount; i++) {
                    data.getShort();
                    data.get();
                }
            } else if (configId == 2) {
                def.setName(ByteBufferUtil.readString(data));
            } else if (configId == 3) {
                def.setDescription(ByteBufferUtil.readString(data));
            } else if (configId == 5) {
                int amount = data.get() & 0xFF;
                for (int i = 0; i < amount; i++) {
                    data.getShort();
                }
            } else if (configId == 14) {
                def.setYSize(data.get() & 0xFF);
            } else if (configId == 15) {
                def.setXSize(data.get() & 0xFF);
            } else if (configId == 17) {
                def.setWalkable(true);
            } else if (configId == 18) {
                def.setSolid(false);
            } else if (configId == 19) {
                def.setActions((data.get() & 0xFF) == 1);
            } else if (configId == 21) {
                @SuppressWarnings("unused")
                boolean flatTerrain = true;
            } else if (configId == 22) {
                @SuppressWarnings("unused")
                boolean flatShading = true;
            } else if (configId == 23) {
            } else if (configId == 24) {
                int animationId = data.getShort();
                if(animationId == 65535) {
                    animationId = -1;
                }
                def.setAnimation(new Animation(animationId));
            } else if (configId == 28) {
                data.get();
            } else if (configId == 29) {
                data.get();
            } else if (configId >= 30 && configId < 39) {
                String[] actions = def.getActionNames();
                if (actions == null) {
                    actions = new String[10];
                    def.setActionNames(actions);
                }
                String action = ByteBufferUtil.readString(data);
                actions[configId - 30] = action;
                def.setActionNames(actions);
                if (def.getActionNames()[0] != null) {
                    def.setDoorClosed(def.getActionNames()[0].equals("Open"));
                }
            } else if (configId == 39) {
                data.get();
            } else if (configId == 40) {
                int amount = data.get() & 0xFF;
                for (int i = 0; i < amount; i++) {
                    data.getShort();
                    data.getShort();
                }
            } else if (configId == 60) {
                int mapFunctionId = data.getShort();
            } else if (configId == 65) {
                int scaleX = data.getShort();
            } else if (configId == 66) {
                int scaleY = data.getShort();
            } else if (configId == 67) {
                int scaleZ = data.getShort();
            } else if (configId == 68) {
                int mapSceneId = data.getShort();
            } else if (configId == 69) {
                data.get();
            } else if (configId == 70) {
                int offsetX = data.getShort();
            } else if (configId == 71) {
                int offsetY = data.getShort();
            } else if (configId == 72) {
                int offsetZ = data.getShort();
            } else if (configId == 75) {
                data.get();
            } else {
                continue;
            }
        }
    }
}
