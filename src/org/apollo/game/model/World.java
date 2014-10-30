package org.apollo.game.model;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apollo.Service;
import org.apollo.fs.IndexedFileSystem;
import org.apollo.fs.parser.ItemDefinitionParser;
import org.apollo.fs.parser.NPCDefinitionParser;
import org.apollo.fs.parser.StaticObjectDefinitionParser;
import org.apollo.fs.parser.StaticObjectParser;
import org.apollo.game.action.impl.DoorAction;
import org.apollo.game.command.CommandDispatcher;
import org.apollo.game.event.impl.CreateObjectEvent;
import org.apollo.game.model.def.EquipmentDefinition;
import org.apollo.game.model.def.ItemDefinition;
import org.apollo.game.model.def.NPCDefinition;
import org.apollo.game.model.def.StaticObjectDefinition;
import org.apollo.game.model.obj.StaticObject;
import org.apollo.game.model.region.RegionRepository;
import org.apollo.game.scheduling.ScheduledTask;
import org.apollo.game.scheduling.Scheduler;
import org.apollo.game.scheduling.impl.ProcessPrivateChatTask;
import org.apollo.game.scheduling.impl.SpawnGroundItemsTask;
import org.apollo.io.EquipmentDefinitionParser;
import org.apollo.net.release.r317.CreateObjectEventEncoder;
import org.apollo.util.CharacterRepository;
import org.apollo.util.XStreamUtil;
import org.apollo.util.plugin.PluginManager;

/**
 * The world class is a singleton which contains objects like the
 * {@link CharacterRepository} for players and NPCs. It should only contain
 * things relevant to the in-game world and not classes which deal with I/O and
 * such (these may be better off inside some custom {@link Service} or other
 * code, however, the circumstances are rare).
 * @author Graham
 */
public final class World {

    /**
     * The logger for this class.
     */
    private static final Logger logger = Logger.getLogger(World.class.getName());
    /**
     * The world.
     */
    private static final World world = new World();
    /**
     * The region repository.
     */
    private final RegionRepository regionRepository = new RegionRepository();

    /**
     * Represents the different status codes for registering a player.
     * @author Graham
     */
    public enum RegistrationStatus {

        /**
         * Indicates the world is full.
         */
        WORLD_FULL,
        /**
         * Indicates that the player is already online.
         */
        ALREADY_ONLINE,
        /**
         * Indicates that the player was registered successfully.
         */
        OK;
    }

    /**
     * Gets the world.
     * @return The world.
     */
    public static World getWorld() {
        return world;
    }

    public RegionRepository getRegionRepository() {
        return regionRepository;
    }
    /**
     * The scheduler.
     */
    // TODO: better place than here?
    private final Scheduler scheduler = new Scheduler();
    /**
     * The command dispatcher.
     */
    // TODO: better place than here?
    private final CommandDispatcher dispatcher = new CommandDispatcher();
    // TODO: better place than here!!
    private PluginManager pluginManager;
    /**
     * The {@link CharacterRepository} of {@link Player}s.
     */
    private final CharacterRepository<Player> playerRepository = new CharacterRepository<Player>(WorldConstants.MAXIMUM_PLAYERS);
    /**
     * A character repository of both.
     */
    private final CharacterRepository<Character> characterRepository = new CharacterRepository<Character>(WorldConstants.MAXIMUM_NPCS + WorldConstants.MAXIMUM_PLAYERS);
    /**
     * The {@link CharacterRepository} of {@link NPC}s.
     */
    private final CharacterRepository<NPC> npcRepository = new CharacterRepository<NPC>(WorldConstants.MAXIMUM_NPCS);

    /**
     * Creates the world.
     */
    private World() {
    }

    /**
     * Initialises the world by loading definitions from the specified file
     * system.
     * @param release The release number.
     * @param fs The file system.
     * @param mgr The plugin manager. TODO move this.
     * @throws IOException if an I/O error occurs.
     */
    public void init(int release, IndexedFileSystem fs, PluginManager mgr) throws IOException {
        logger.info("Loading item definitions...");
        ItemDefinitionParser itemParser = new ItemDefinitionParser(fs);
        ItemDefinition[] itemDefs = itemParser.parse();
        ItemDefinition.init(itemDefs);
        logger.info("Done (loaded " + itemDefs.length + " item definitions).");

        int nonNull = 0;
        InputStream is = new BufferedInputStream(new FileInputStream("data/equipment-" + release + ".dat"));
        try {
            EquipmentDefinitionParser equipParser = new EquipmentDefinitionParser(is);
            EquipmentDefinition[] equipDefs = equipParser.parse();
            for (EquipmentDefinition def : equipDefs) {
                if (def != null) {
                    nonNull++;
                }
            }
            EquipmentDefinition.init(equipDefs);
        } finally {
            is.close();
        }
        logger.info("Done (loaded " + nonNull + " equipment definitions).");
        
        logger.info("Loading npc definitions...");
        NPCDefinitionParser npcParser = new NPCDefinitionParser(fs);
        NPCDefinition[] npcDefs = npcParser.parse();
        NPCDefinition.init(npcDefs);
        NPCManager.loadHP();
        logger.info("Done (loaded " + npcDefs.length + " npc definitions.");

        logger.info("Loading object definitions...");
        StaticObjectDefinitionParser objParser = new StaticObjectDefinitionParser(fs);
        StaticObjectDefinition[] objDefs = objParser.parse();
        StaticObjectDefinition.init(objDefs);
        logger.info("Done (loaded " + objDefs.length + " object definitions).");

        DoorAction.init();

        logger.info("Parsing landscape archives...");
        StaticObjectParser objectParser = new StaticObjectParser(fs);
        StaticObject[] staticObjects = objectParser.parse();
        StaticObject.init(staticObjects);
        logger.info("Loaded " + staticObjects.length + " static objects!");

        XStreamUtil.loadAllFiles();
        logger.info("Loading npc spawns...");
        NPCManager.load();

        NPCDropLoader drops = new NPCDropLoader();
        drops.load();

        initalizeTasks();
        this.pluginManager = mgr; // TODO move!!
    }

    public void initalizeTasks() {
        World.getWorld().schedule(new ProcessPrivateChatTask());
        World.getWorld().schedule(new SpawnGroundItemsTask());
    }

    /**
     * Gets the character repository. NOTE:
     * {@link CharacterRepository#add(Character)} and
     * {@link CharacterRepository#remove(Character)} should not be called
     * directly! These mutation methods are not guaranteed to work in future
     * releases!
     * <p>
     * Instead, use the {@link World#register(Player)} and
     * {@link World#unregister(Player)} methods which do the same thing and
     * will continue to work as normal in future releases.
     * @return The character repository.
     */
    public CharacterRepository<Player> getPlayerRepository() {
        return playerRepository;
    }

    public CharacterRepository<NPC> getNPCRepository() {
        return npcRepository;
    }

    public CharacterRepository<Character> getCharacterRepository() {
        return characterRepository;
    }

    /**
     * Registers the specified player.
     * @param player The player.
     * @return A {@link RegistrationStatus}.
     */
    public RegistrationStatus register(final Player player) {
        boolean success = playerRepository.add(player);
        if (success) {
            logger.log(Level.INFO, "Registered player: {0} [online={1}] [Ip Address={2}]", new Object[]{player, playerRepository.size(), player.getPlayerhost()});
            return RegistrationStatus.OK;
        } else {
            logger.log(Level.WARNING, "Failed to register player (server full): {0} [online={1}]", new Object[]{player, playerRepository.size()});
            return RegistrationStatus.WORLD_FULL;
        }
    }

    /**
     * Registers the specified player.
     * @param player The player.
     * @return A {@link RegistrationStatus}.
     */
    public void register(final NPC npc) {
        boolean success = npcRepository.add(npc);
        if (!success) {
            logger.warning("Failed to register npc");
        }
    }

    /**
     * Checks if the specified player is online.
     * @param name The player's name.
     * @return {@code true} if so, {@code false} if not.
     */
    public boolean isPlayerOnline(String name) {
        // TODO: use a hash set or map in the future?
        for (Player player : playerRepository) {
            if (player.getUndefinedName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Unregisters the specified player.
     * @param player The player.
     */
    public void unregister(Player player) {
        if (playerRepository.remove(player)) {
            logger.info("Unregistered player: " + player + " [online=" + playerRepository.size() + "]");
        } else {
            logger.warning("Could not find player to unregister: " + player + "!");
        }
    }

    /**
     * Unregisters the specified npc.
     * @param player The npc.
     */
    public void unregister(NPC npc) {
        if (npcRepository.remove(npc)) {
        } else {
            logger.warning("Could not find npc to unregister: " + npc + "!");
        }
    }

    /**
     * Registers a new object.
     * @param object The object to register.
     */
    public void register(final StaticObject object) {
        getRegionRepository().getRegionByPosition(object.getPosition()).addObject(object);
        getRegionRepository().getRegionByPosition(object.getPosition()).getTile(object.getPosition()).setStaticObject(object.getType(), object);
        for (Player player : getRegionRepository().getRegionByPosition(object.getPosition()).getPlayers()) {
            if (object.isOwnedBy(player.getUndefinedName())) {
                player.sendNewObject(object.getPosition(), object.getDefinition().getId(), object.getRotation(), object.getType());
            }
        }
    }

    /**
     * Unregisters a new ground object.
     * @param object The object to unregister.
     */
    public void unregister(StaticObject object) {
        getRegionRepository().getRegionByPosition(object.getPosition()).removeObject(object);
        getRegionRepository().getRegionByPosition(object.getPosition()).getTile(object.getPosition()).setStaticObject(object.getType(), null);
        for (Player player : getRegionRepository().getRegionByPosition(object.getPosition()).getPlayers()) {
            if (object.isOwnedBy(player.getUndefinedName())) {
                player.sendRemoveObject(object.getPosition(), object.getRotation(), object.getType());
            }
        }
    }

    /**
     * Schedules a new task.
     * @param task The {@link ScheduledTask}.
     */
    public void schedule(ScheduledTask task) {
        scheduler.schedule(task);
    }

    /**
     * Calls the {@link Scheduler#pulse()} method.
     */
    public void pulse() {
        scheduler.pulse();
    }

    /**
     * Gets the command dispatcher. TODO should this be here?
     * @return The command dispatcher.
     */
    public CommandDispatcher getCommandDispatcher() {
        return dispatcher;
    }

    /**
     * Gets the plugin manager. TODO should this be here?
     * @return The plugin manager.
     */
    public PluginManager getPluginManager() {
        return pluginManager;
    }
}
