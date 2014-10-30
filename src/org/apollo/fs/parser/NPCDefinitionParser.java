package org.apollo.fs.parser;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.apollo.fs.IndexedFileSystem;
import org.apollo.fs.archive.Archive;
import org.apollo.game.model.def.NPCDefinition;
import org.apollo.util.ByteBufferUtil;

/**
 * Parses NPC definitions from the cache
 *
 * @author The Wanderer
 * @author Nikki (reference)
 */
public class NPCDefinitionParser {

    /**
     * The indexed file system.
     */
    private final IndexedFileSystem fs;

    /**
     * Creates the npc definition parser.
     * @param fs The indexed file system.
     */
    public NPCDefinitionParser(IndexedFileSystem fs) {
        this.fs = fs;
    }

    /**
     * Parses the object definitions in the cache.
     *
     * @throws IOException
     * if an I/O error occurs.
     */
    public NPCDefinition[] parse() throws IOException {
        Archive config = Archive.decode(fs.getFile(0, 2));
        ByteBuffer dat = config.getEntry("npc.dat").getBuffer();
        ByteBuffer idx = config.getEntry("npc.idx").getBuffer();

        int count = idx.getShort();
        int[] indices = new int[count];
        int index = 2;
        for (int i = 0; i < count; i++) {
            indices[i] = index;
            index += idx.getShort();
        }
        NPCDefinition[] defs = new NPCDefinition[count];
        for (int i = 0; i < count; i++) {
            dat.position(indices[i]);
            defs[i] = parseDefinition(i, dat);
        }

        return defs;
    }

    public NPCDefinition parseDefinition(int id, ByteBuffer data) {
        NPCDefinition def = new NPCDefinition(id);

        while (true) {
            int code = data.get() & 0xff;
            if (code == 0) {
                return def;
            }
            switch (code) {
                case 1:
                    int someCounter;
                    int modelArrayLength = data.get() & 0xff;
                    for (int i = 0; i < modelArrayLength; i++) {
                        int[] models = new int[modelArrayLength];
                        models[i] = data.getShort();
                        if(id == 95) {
                            System.out.println(models[i]);
                        }
                    }
                    break;
                case 2:
                    def.setName(ByteBufferUtil.readString(data));
                    break;
                case 3:
                    def.setDesc(ByteBufferUtil.readString(data));
                    break;

                case 12:
                    def.setSize(data.get());
                    break;
                case 13:
                    def.setStandAnim(data.getShort());
                    break;
                case 14:
                    def.setWalkAnim(data.getShort());
                    break;
                case 17:
                    // walk etc
                    data.getShort();
                    data.getShort();
                    data.getShort();
                    data.getShort();
                    break;
                case 30:
                case 31:
                case 32:
                case 33:
                case 34:
                case 35:
                case 36:
                case 37:
                case 38:
                case 39:
                    String actions[] = new String[10];
                    actions[code - 30] = ByteBufferUtil.readString(data); // actions
                    def.setActions(actions);
                    break;
                case 40:
                    someCounter = data.get() & 0xff; // model colours
                    for (int i = 0; i < someCounter; i++) {
                        data.getShort();
                        data.getShort();
                    }
                    break;
                case 60:
                    someCounter = data.get() & 0xff;
                    for (int i = 0; i < someCounter; i++) {
                        data.getShort();
                    }
                    break;
                case 90:
                case 91:
                case 92:
                    data.getShort();
                    break;
                case 95:
                    def.setCombatLevel(data.getShort());
                    break;
                case 97:
                    data.getShort();
                    break;
                case 98:
                    data.getShort();
                    break;
                case 100:
                case 101:
                    data.get();
                    break;
                case 102:
                case 103:
                    data.getShort();
                    break;
                case 106:
                    data.getShort();
                    data.getShort();
                    someCounter = data.get() & 0xff;
                    for (int i2 = 0; i2 <= someCounter; i2++) {
                        data.getShort();
                    }
                    break;
            }
        }
    }
}