package org.apollo.game.model.region;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apollo.game.model.Character;
import org.apollo.game.model.NPC;
import org.apollo.game.model.Player;
import org.apollo.game.model.Position;
import org.apollo.game.model.Tile;
import org.apollo.game.model.World;
import org.apollo.game.model.obj.StaticObject;

/**
 * Manages the repository of regions.
 * @author Graham
 */
public class RegionRepository {

    /**
     * The region size.
     */
    public static final int REGION_SIZE = 32;
    /**
     * The lower bound that splits the region in half.
     */
    @SuppressWarnings("unused")
    private static final int LOWER_BOUND = REGION_SIZE / 2 - 1;
    /**
     * Constant values used by the bitmask.
     */
    public static final int NORTH_TRAVERSAL_PERMITTED = 1,
            EAST_TRAVERSAL_PERMITTED = 2,
            SOUTH_TRAVERSAL_PERMITTED = 4,
            WEST_TRAVERSAL_PERMITTED = 8;
    /**
     * A tile in which traversal in no directions is permitted.
     */
    public static final Tile SOLID_TILE = new Tile(0);
    /**
     * A tile in which traversal in any direction is permitted.
     */
    public static final Tile EMPTY_TILE = new Tile(NORTH_TRAVERSAL_PERMITTED
            | EAST_TRAVERSAL_PERMITTED
            | SOUTH_TRAVERSAL_PERMITTED
            | WEST_TRAVERSAL_PERMITTED);
    /**
     * The active (loaded) region map.
     */
    private Map<RegionCoordinates, Region> activeRegions = new HashMap<RegionCoordinates, Region>();

    /**
     * Gets the local players around an character.
     * @param character The character.
     * @return The collection of local players.
     */
    public Collection<Player> getLocalPlayers(Character character) {
        List<Player> localPlayers = character.getLocalPlayerList();
        Region[] regions = getSurroundingRegions(character.getPosition());
        for (@SuppressWarnings("unused") Region region : regions) {
            for (Player player : World.getWorld().getPlayerRepository()) {
                if (player.getPosition().isWithinDistance(character.getPosition())) {
                    localPlayers.add(player);
                }
            }
        }
        return Collections.unmodifiableCollection(localPlayers);
    }

    /**
     * Gets the local NPCs around an character.
     * @param character The character.
     * @return The collection of local NPCs.
     */
    @SuppressWarnings("unused")
    public List<NPC> getLocalNpcs(Character character) {
        List<NPC> localNpcs = character.getLocalNPCList();
        Region[] regions = getSurroundingRegions(character.getPosition());
        for (Region region : regions) {
            for (NPC npc : World.getWorld().getNPCRepository()) {
                if (npc.getPosition().isWithinDistance(character.getPosition())) {
                    localNpcs.add(npc);
                }
            }
        }
        return (localNpcs);
    }

    /**
     * Gets the local characters around an entity.
     * @param character The entity.
     * @return The collection of local characters.
     */
    public Collection<Character> getLocalCharacters(Character character) {
        List<Character> localCharacters = new LinkedList<Character>();
        Region[] regions = getSurroundingRegions(character.getPosition());
        for (@SuppressWarnings("unused") Region region : regions) {
            for (Character characters : World.getWorld().getCharacterRepository()) {
                if (characters.getPosition().isWithinDistance(character.getPosition())) {
                    localCharacters.add(characters);
                }
            }
        }
        return Collections.unmodifiableCollection(localCharacters);
    }

    /**
     * Gets a local game object.
     * @param position The object's position.
     * @param id The object's id.
     * @return The <code>StaticObject</code> or <code>null</code> if no game object was found to be existent.
     */
    public StaticObject getStaticObject(Position position, int id) {
        Region[] regions = getSurroundingRegions(position);
        for (Region region : regions) {
            for (StaticObject object : region.getStaticObjects()) {
                if (object.getPosition().equals(position) && object.getDefinition().getId() == id) {
                    return object;
                }
            }
        }
        return null;
    }

    /**
     * Gets the regions surrounding a position.
     * @param position The position.
     * @return The regions surrounding the position.
     */
    public Region[] getSurroundingRegions(Position position) {
        int regionX = position.getX() / REGION_SIZE;
        int regionY = position.getY() / REGION_SIZE;

//		int regionPositionX = position.getX() % REGION_SIZE;
//		int regionPositionY = position.getY() % REGION_SIZE;

        Region[] surrounding = new Region[9];
        surrounding[0] = getRegion(regionX, regionY);
        surrounding[1] = getRegion(regionX - 1, regionY - 1);
        surrounding[2] = getRegion(regionX + 1, regionY + 1);
        surrounding[3] = getRegion(regionX - 1, regionY);
        surrounding[4] = getRegion(regionX, regionY - 1);
        surrounding[5] = getRegion(regionX + 1, regionY);
        surrounding[6] = getRegion(regionX, regionY + 1);
        surrounding[7] = getRegion(regionX - 1, regionY + 1);
        surrounding[8] = getRegion(regionX + 1, regionY - 1);

//		FIXME
//		if(regionPositionX <= LOWER_BOUND) {
//			if(regionPositionY <= LOWER_BOUND) {
//				surrounding[1] = getRegion(regionX - 1, regionY - 1);
//				surrounding[2] = getRegion(regionX - 1, regionY);
//				surrounding[3] = getRegion(regionX, regionY - 1);
//			} else {
//				surrounding[1] = getRegion(regionX + 1, regionY - 1);
//				surrounding[2] = getRegion(regionX + 1, regionY);
//				surrounding[3] = getRegion(regionX, regionY - 1);
//			}
//		} else {
//			if(regionPositionY <= LOWER_BOUND) {
//				surrounding[1] = getRegion(regionX - 1, regionY + 1);
//				surrounding[2] = getRegion(regionX - 1, regionY);
//				surrounding[3] = getRegion(regionX, regionY + 1);
//			} else {
//				surrounding[1] = getRegion(regionX + 1, regionY + 1);
//				surrounding[2] = getRegion(regionX + 1, regionY);
//				surrounding[3] = getRegion(regionX, regionY + 1);
//			}
//		}

        return surrounding;
    }

    /**
     * Gets a region by position.
     * @param position The position.
     * @return The region.
     */
    public Region getRegionByPosition(Position position) {
        return getRegion(position.getX() / REGION_SIZE, position.getY() / REGION_SIZE);
    }

    /**
     * Gets a region by its x and y coordinates.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @return The region.
     */
    public Region getRegion(int x, int y) {
        RegionCoordinates key = new RegionCoordinates(x, y);
        if (activeRegions.containsKey(key)) {
            return activeRegions.get(key);
        } else {
            Region region = new Region(key);
            activeRegions.put(key, region);
            return region;
        }
    }
}
