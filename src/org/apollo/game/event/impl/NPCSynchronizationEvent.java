package org.apollo.game.event.impl;

import java.util.List;

import org.apollo.game.event.Event;
import org.apollo.game.model.Position;
import org.apollo.game.sync.seg.SynchronizationSegment;

/**
 * NPCSynchronizationEvent.java
 * @author The Wanderer & Zuppers
 */
public class NPCSynchronizationEvent extends Event {

    /**
     * The npc's position.
     */
    private final Position position;

    /**
     * The number of local players.
     */
    private final int localNPCs;

    /**
     * A list of segments.
     */
    private final List<SynchronizationSegment> segments;

    /**
     * Creates the player synchronization event.
     * @param position The player's current position.
     * @param localPlayers The number of local players.
     * @param segments A list of segments.
     */
    public NPCSynchronizationEvent(Position position, int localNPCs, List<SynchronizationSegment> segments) {
        this.position = position;
        this.localNPCs = localNPCs;
        this.segments = segments;
    }

    /**
     * Gets the player's position.
     * @return The player's position.
     */
    public Position getPosition() {
        return position;
    }


    /**
     * Gets the number of local players.
     * @return The number of local players.
     */
    public int getLocalNPCs() {
        return localNPCs;
    }

    /**
     * Gets the synchronization segments.
     * @return The segments.
     */
    public List<SynchronizationSegment> getSegments() {
        return segments;
    }
}