package org.apollo.game.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.logging.Logger;

import org.apollo.game.model.def.NPCDefinition;
import org.apollo.game.model.region.Region;

/**
 * NPCManager.java
 * @author The Wanderer
 */
public class NPCManager {

    /**
     * The logger instance - used to report error and confirmation messages
     */
    private static final Logger logger = Logger.getLogger(NPCManager.class.getName());

    /**
     * The class constructor
     */
    public NPCManager() {
    }

    public static void loadHP() {
        try {
            BufferedReader in = new BufferedReader(new FileReader("data/npc/npcHp.cfg"));
            String input;
            String[] npc;
            while ((input = in.readLine()) != null) {
                npc = null;
                if (!input.startsWith("/") && input.contains("\t")) {
                    npc = input.split("\t");
                    int npcId = Integer.parseInt(npc[0]);
                    int hp = Integer.parseInt(npc[1]) / 10;
                    NPCDefinition.forId(npcId).setHp(hp);
                    NPCDefinition.forId(npcId).setMaxHp(hp);
                }
            }
            in.close();
        } catch (Exception e) {
            System.out.println("Error setting HP for NPCs: " + e);
        }
    }

    /**
     * Loads and populates the NPC list
     */
    @SuppressWarnings("unused")
    public static void load() {
        try {
            BufferedReader in = new BufferedReader(new FileReader("data/spawns.cfg"));
            String input;
            int on = 0;
            String[] spawnDefinitions;
            while ((input = in.readLine()) != null) {
                spawnDefinitions = null;
                if (!input.startsWith("/") && input.contains("\t")) {
                    spawnDefinitions = input.split("\t");
                    int npcID = Integer.parseInt(spawnDefinitions[0]);
                    int x = Integer.parseInt(spawnDefinitions[1]);
                    int y = Integer.parseInt(spawnDefinitions[2]);
                    int z = Integer.parseInt(spawnDefinitions[3]);
                    int lowX = Integer.parseInt(spawnDefinitions[4]);
                    int lowY = Integer.parseInt(spawnDefinitions[5]);
                    int highX = Integer.parseInt(spawnDefinitions[6]);
                    int highY = Integer.parseInt(spawnDefinitions[7]);
                    int walkType = Integer.parseInt(spawnDefinitions[8]);
                    String description = spawnDefinitions[9];
                    appendNpc(npcID, new Position(x, y, z));
                    /*NPC npc = (NPC) World.getWorld().getNPCRepository().get(on);
                    npc.setLeastX(lowX);
                    npc.setLeastY(lowY);
                    npc.setMaxX(highX);
                    npc.setMaxY(highY);*/
                    ++on;
                }
            }
            logger.info(on + " Npcs Spawned");
            in.close();

        } catch (Exception e) {
            System.out.println("Error Loading NPCs:" + e);
        }
    }

    /**
     * Appends an npc to the game world
     * 
     * @param id
     *            - the npc id
     * @param loc
     *            - the location of the npc
     * @return 
     */
    public static NPC appendNpc(int id, Position pos) {
        NPCDefinition definition = NPCDefinition.forId(id);
        NPC npc = new NPC(definition, pos);
        Region region = World.getWorld().getRegionRepository().getRegionByPosition(npc.getPosition());
        region.addNpc(npc);
        World.getWorld().register(npc);
        return npc;
    }
}
