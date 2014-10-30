package org.apollo.game.model.region;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apollo.game.model.NPC;
import org.apollo.game.model.Player;
import org.apollo.game.model.Position;
import org.apollo.game.model.Tile;
import org.apollo.game.model.World;
import org.apollo.game.model.def.MapTile;
import org.apollo.game.model.obj.StaticObject;

/**
 * Represents an 8x8 region.
 * @author Graham
 */
public final class Region {

    private RegionCoordinates coordinate;
    /**
     * The width and height of the region in tiles.
     */
    public static final int REGION_SIZE = 8;
    /**
     * A list of NPCs in this region.
     */
    private List<NPC> npcs = new LinkedList<NPC>();
    /**
     * A list of players in this region.
     */
    private List<Player> players = new LinkedList<Player>();
    /**
     * A list of tiles in this region, only initiated when the region is called upon.
     */
    private Map<Position, Tile> tiles;
    /**
     * A list of map tiles in this region.
     */
    private Map<Position, MapTile> mapTiles = new HashMap<Position, MapTile>();
    /**
     * A list of objects in this region.
     */
    private List<StaticObject> objects = new LinkedList<StaticObject>();
    /**
     * A list of objects in this region.
     */
    private List<StaticObject> removedObjects = new LinkedList<StaticObject>();

    /**
     * Creates a region.
     * @param coordinate The coordinate.
     */
    public Region(RegionCoordinates coordinate) {
        this.coordinate = coordinate;
    }

    /**
     * @return the tiles
     */
    public Map<Position, MapTile> getMapTiles() {
        return mapTiles;
    }

    /**
     * Sets a tile.
     * @param tile The tile.
     * @param position The position.
     */
    public void setTile(Tile tile, Position position) {
        if (tiles == null) {
            tiles = new HashMap<Position, Tile>();
            populateTiles(position.getHeight());
        }
        tiles.put(position, tile);
    }

    /**
     * Gets a tile
     * @param position The position of the tile.
     * @return The tile.
     */
    public MapTile getMapTile(Position position) {
        if (mapTiles.isEmpty()) {
            /*World.getWorld().getObjectMap().loadArea(
            (coordinate.getX() * RegionRepository.REGION_SIZE) / 64,
            (coordinate.getY() * RegionRepository.REGION_SIZE) / 64);*/
            System.err.println("Error: mapTiles is empty.");
        }
        return mapTiles.get(position);
    }

    /**
     * Sets a tile.
     * @param tile The tile.
     * @param location The location.
     */
    public void setMapTile(MapTile tile, Position position) {
        mapTiles.put(position, tile);
    }

    /**
     * Populates the tile map on a specified plane.
     * @param plane The plane.
     */
    public void populateTiles(int plane) {
        if (mapTiles.isEmpty()) {
            /*World.getWorld().getObjectMap().loadArea(
            (coordinate.getX() * RegionRepository.REGION_SIZE) / 64,
            (coordinate.getY() * RegionRepository.REGION_SIZE) / 64);*/
            System.err.println("Error: mapTiles is empty.");
        }
        for (MapTile mt : getMapTiles().values()) {
            if (mt.getPosition().getHeight() == plane && (mt.getFlags() & MapTile.TILE_FLAGS_CLIPPING_BIT) != 0) {
                if ((getMapTile(new Position(mt.getPosition().getX(), mt.getPosition().getY(), 1)).getFlags() & MapTile.TILE_FLAGS_BRIDGE_BIT) != 0) {
                    if (mt.getPosition().getHeight() != 3 && (getMapTile(mt.getPosition().transform(0, 0, 1)).getFlags() & MapTile.TILE_FLAGS_CLIPPING_BIT) != 0) {
                        setTile(RegionRepository.SOLID_TILE, mt.getPosition());
                    } else {
                        setTile(RegionRepository.EMPTY_TILE, mt.getPosition());
                    }
                } else {
                    setTile(RegionRepository.SOLID_TILE, mt.getPosition());
                }

            } else {
                setTile(RegionRepository.EMPTY_TILE, mt.getPosition());
            }
        }
        for (StaticObject obj : objects) {
            if ((!obj.getDefinition().solid()) || obj.getDefinition().walkable()) {
                continue;
            }
            Position loc = obj.getPosition();
            if (loc.getHeight() != plane) {
                continue;
            }

            int sizeX = obj.getDefinition().getXSize();
            int sizeY = obj.getDefinition().getYSize();
            // position in the tile map
            if (obj.getRotation() == 1 || obj.getRotation() == 3) {
                // switch sizes if rotated
                int temp = sizeX;
                sizeX = sizeY;
                sizeY = temp;
            }

            if (obj.getType() >= 0 && obj.getType() <= 3) {
                // walls

                //int finalRotation = (obj.getType() + obj.getRotation()) % 4;
                int finalRotation = obj.getRotation();
                // finalRotation - 0 = west, 1 = north, 2 = east, 3 = south
                Tile t = getTile(obj.getPosition());
                int flags = t.getTraversalMask();
                // clear flags
                if (finalRotation == 0) {
                    flags &= ~RegionRepository.WEST_TRAVERSAL_PERMITTED;
                } else if (finalRotation == 1) {
                    flags &= ~RegionRepository.NORTH_TRAVERSAL_PERMITTED;
                } else if (finalRotation == 2) {
                    flags &= ~RegionRepository.EAST_TRAVERSAL_PERMITTED;
                } else {
                    flags &= ~RegionRepository.SOUTH_TRAVERSAL_PERMITTED;
                }
                if (flags != t.getTraversalMask()) {
                    setTile(new Tile(flags), obj.getPosition());
                }
            } else if (obj.getType() == 9 || obj.getType() == 10 || obj.getType() == 11) {
                // world objects

                for (int offX = 0; offX < sizeX; offX++) {
                    for (int offY = 0; offY < sizeY; offY++) {
                        int x = offX + obj.getPosition().getX();
                        int y = offY + obj.getPosition().getY();
                        setTile(RegionRepository.SOLID_TILE, new Position(x, y, plane));
                    }
                }
            } else if (obj.getType() == 22) {
                // floor decoration
                if (obj.getDefinition().hasActions()) {
                    setTile(RegionRepository.SOLID_TILE, obj.getPosition());
                }
            } else {
                // 4-8 are wall decorations and 12-21 are roofs
                // we can ignore those
            }
        }
    }

    /**
     * Gets a tile
     * @param position The position of the tile.
     * @return The tile.
     */
    public Tile getTile(Position position) {
        if (tiles == null) {
            tiles = new HashMap<Position, Tile>();
            populateTiles(position.getHeight());
        }
        return tiles.get(position) == null ? RegionRepository.SOLID_TILE : tiles.get(position);
    }

    /**
     * Gets the list of players.
     * @return The list of players.
     */
    public Collection<Player> getPlayers() {
        synchronized (this) {
            return Collections.unmodifiableCollection(new LinkedList<Player>(players));
        }
    }

    /**
     * Gets the list of objects.
     * @return The list of objects.
     */
    public Collection<StaticObject> getStaticObjects() {
        if (objects.isEmpty()) {
            //TODO: Add per-region landscape and maptile archive parsing.
            /*World.getWorld().getObjectMap().loadArea( 
            (coordinate.getX() * RegionRepository.REGION_SIZE) / 64,
            (coordinate.getY() * RegionRepository.REGION_SIZE) / 64);*/
            System.err.println("Error: objects is empty.");
        }

        return Collections.unmodifiableCollection(objects);
    }

    /**
     * Removes an old GameObject.
     * @param object The GameObject to remove.
     */
    public void removeObject(StaticObject object) {
        synchronized (this) {
            objects.remove(object);
            if (object.isGlobal() && object.isClientside() && !removedObjects.contains(object)) {
                removedObjects.add(object);
            }
        }
    }

    /**
     * Adds a new player.
     * @param player The player to add.
     */
    public void addPlayer(Player player) {
        synchronized (this) {
            players.add(player);
        }
    }

    /**
     * Removes an old player.
     * @param player The player to remove.
     */
    public void removePlayer(Player player) {
        synchronized (this) {
            players.remove(player);
        }
    }

    /**
     * Adds a new NPC.
     * @param npc The NPC to add.
     */
    public void addNpc(NPC npc) {
        synchronized (this) {
            npcs.add(npc);
        }
    }

    /**
     * Removes an old NPC.
     * @param npc The NPC to remove.
     */
    public void removeNpc(NPC npc) {
        synchronized (this) {
            npcs.remove(npc);
        }
    }

    /*
     * Adds a new StaticObject. 
     * @param object The StaticObject to add.
     */
    public void addObject(StaticObject object) {
        synchronized (this) {
            objects.add(object);
            if (object.isGlobal() && object.isClientside() && removedObjects.contains(object)) {
                removedObjects.remove(object);
            }
        }
    }
}
