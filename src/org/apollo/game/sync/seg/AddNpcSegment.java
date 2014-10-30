package org.apollo.game.sync.seg;

import org.apollo.game.model.Position;
import org.apollo.game.sync.block.SynchronizationBlockSet;

/**
 *
 * @author Zuppers
 */
public final class AddNpcSegment extends SynchronizationSegment {

    /**
     * The index.
     */
    private final int index;
    /**
     * The position.
     */
    private final Position position;
    /**
     * The npc id.
     */
    private final int npcid;

    /**
     * Creates the add character segment.
     * @param blockSet The block set.
     * @param index The characters's index.
     * @param position The position.
     */
    public AddNpcSegment(SynchronizationBlockSet blockSet, int index, Position position, int npcid) {
        super(blockSet);
        this.index = index;
        this.position = position;
        this.npcid = npcid;
    }

    /**
     * Gets the character's index.
     * @return The index.
     */
    public int getIndex() {
        return index;
    }

    /**
     * Gets the position.
     * @return The position.
     */
    public Position getPosition() {
        return position;
    }

    @Override
    public SegmentType getType() {
        return SegmentType.ADD_CHARACTER;
    }

    /**
     * @return the npcid
     */
    public int getNpcid() {
        return npcid;
    }
}