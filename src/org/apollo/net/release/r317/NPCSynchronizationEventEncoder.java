package org.apollo.net.release.r317;

import org.apollo.game.event.impl.DamageEvent;
import org.apollo.game.event.impl.NPCSynchronizationEvent;
import org.apollo.game.model.Animation;
import org.apollo.game.model.Direction;
import org.apollo.game.model.Graphic;
import org.apollo.game.model.Position;
import org.apollo.game.sync.block.AnimationBlock;
import org.apollo.game.sync.block.SecondHitUpdateBlock;
import org.apollo.game.sync.block.ForceChatBlock;
import org.apollo.game.sync.block.GraphicBlock;
import org.apollo.game.sync.block.HitUpdateBlock;
import org.apollo.game.sync.block.SynchronizationBlockSet;
import org.apollo.game.sync.block.TurnToPositionBlock;
import org.apollo.game.sync.seg.AddNpcSegment;
import org.apollo.game.sync.seg.MovementSegment;
import org.apollo.game.sync.seg.SegmentType;
import org.apollo.game.sync.seg.SynchronizationSegment;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.EventEncoder;

/**
 * NPCSynchronizationEventEncoder.java
 * @author The Wanderer & Zuppers
 */
public class NPCSynchronizationEventEncoder extends EventEncoder<NPCSynchronizationEvent> {

    @Override
    public GamePacket encode(NPCSynchronizationEvent event) {
        GamePacketBuilder builder = new GamePacketBuilder(65, PacketType.VARIABLE_SHORT);
        builder.switchToBitAccess();

        GamePacketBuilder blockBuilder = new GamePacketBuilder();

        /*
         * Write the current size of the npc list.
         */
        builder.putBits(8, event.getLocalNPCs());

        for (SynchronizationSegment segment : event.getSegments()) {
            SegmentType type = segment.getType();
            if (type == SegmentType.REMOVE_CHARACTER) {
                putRemoveCharacterUpdate(builder);
            } else if (type == SegmentType.ADD_CHARACTER) {
                putAddCharacterUpdate((AddNpcSegment) segment, event, builder);
                putBlocks(segment, blockBuilder);
            } else {
                putMovementUpdate(segment, event, builder);
                putBlocks(segment, blockBuilder);
            }
        }

        /*
         * Check if the update block isn't empty.
         */
        if (blockBuilder.getLength() > 0) {
            /*
             * If so, put a flag indicating that an update block follows.
             */
            builder.putBits(14, 16383);
            builder.switchToByteAccess();

            /*
             * And append the update block.
             */
            builder.putRawBuilder(blockBuilder);
        } else {
            /*
             * Terminate the packet normally.
             */
            builder.switchToByteAccess();
        }

        /*
         * Write the packet.
         */
        return builder.toGamePacket();
    }

    /**
     * Adds a new NPC.
     * @param packet The main packet.
     * @param npc The npc to add.
     */
    private void putAddCharacterUpdate(AddNpcSegment seg, NPCSynchronizationEvent event, GamePacketBuilder builder) {
        boolean updateRequired = seg.getBlockSet().size() > 0;
        Position npc = seg.getPosition();
        Position other = event.getPosition();
        builder.putBits(14, seg.getIndex());
        builder.putBits(5, npc.getY() - other.getY());
        builder.putBits(5, npc.getX() - other.getX());
        builder.putBits(1, 0);
        builder.putBits(12, seg.getNpcid());
        builder.putBits(1, updateRequired ? 1 : 0);
    }

    /**
     * Puts a remove character update.
     * @param builder The builder.
     */
    private void putRemoveCharacterUpdate(GamePacketBuilder builder) {
        builder.putBits(1, 1);
        builder.putBits(2, 3);
    }

    /**
     * Update an NPC's movement.
     * @param packet The main packet.
     * @param npc The npc.
     */
    private void putMovementUpdate(SynchronizationSegment seg, NPCSynchronizationEvent event, GamePacketBuilder builder) {
        boolean updateRequired = seg.getBlockSet().size() > 0;
        if (seg.getType() == SegmentType.RUN) {
            Direction[] directions = ((MovementSegment) seg).getDirections();
            builder.putBits(1, 1);
            builder.putBits(2, 2);
            builder.putBits(3, directions[0].toInteger());
            builder.putBits(3, directions[1].toInteger());
            builder.putBits(1, updateRequired ? 1 : 0);
        } else if (seg.getType() == SegmentType.WALK) {
            Direction[] directions = ((MovementSegment) seg).getDirections();
            builder.putBits(1, 1);
            builder.putBits(2, 1);
            builder.putBits(3, directions[0].toInteger());
            builder.putBits(1, updateRequired ? 1 : 0);
        } else {
            if (updateRequired) {
                builder.putBits(1, 1);
                builder.putBits(2, 0);
            } else {
                builder.putBits(1, 0);
            }
        }
    }

    /**
     * Update an NPC.
     * @param packet The update block.
     * @param npc The npc.
     */
    private void putBlocks(SynchronizationSegment segment, GamePacketBuilder blockBuilder) {
        SynchronizationBlockSet blockSet = segment.getBlockSet();
        if (blockSet.size() > 0) {
            int mask = 0;
            //TODO: masks Hit, Hit_2, Transform, Face Entity, and Forced Chat
            if (blockSet.contains(AnimationBlock.class)) {
                mask |= 0x10;
            }

            if (blockSet.contains(HitUpdateBlock.class)) {
                mask |= 0x8;
            }

            if (blockSet.contains(GraphicBlock.class)) {
                mask |= 0x80;
            }

            if (blockSet.contains(ForceChatBlock.class)) {
                mask |= 0x1;
            }

            if (blockSet.contains(SecondHitUpdateBlock.class)) {
                mask |= 0x40;
            }

            if (blockSet.contains(TurnToPositionBlock.class)) {
                mask |= 0x4;
            }

            blockBuilder.put(DataType.BYTE, mask);

            if (blockSet.contains(AnimationBlock.class)) {
                putAnimationBlock(blockSet.get(AnimationBlock.class), blockBuilder);
            }

            if (blockSet.contains(HitUpdateBlock.class)) {
                putHitUpdateBlock(blockSet.get(HitUpdateBlock.class), blockBuilder);
            }

            if (blockSet.contains(GraphicBlock.class)) {
                putGraphicBlock(blockSet.get(GraphicBlock.class), blockBuilder);
            }

            if (blockSet.contains(ForceChatBlock.class)) {
                putForceChat(blockSet.get(ForceChatBlock.class), blockBuilder);
            }

            if (blockSet.contains(SecondHitUpdateBlock.class)) {
                putDoubleHitUpdateBlock(blockSet.get(SecondHitUpdateBlock.class), blockBuilder);
            }

            if (blockSet.contains(TurnToPositionBlock.class)) {
                putTurnToPositionBlock(blockSet.get(TurnToPositionBlock.class), blockBuilder);
            }
        }
    }

    /**
     * Puts a turn to position block into the specified builder.
     * @param block The block.
     * @param blockBuilder The builder.
     */
    private void putTurnToPositionBlock(TurnToPositionBlock block, GamePacketBuilder blockBuilder) {
        Position pos = block.getPosition();
        blockBuilder.put(DataType.SHORT, DataOrder.LITTLE, pos.getX() * 2 + 1);
        blockBuilder.put(DataType.SHORT, DataOrder.LITTLE, pos.getY() * 2 + 1);
    }

    private void putHitUpdateBlock(HitUpdateBlock block, GamePacketBuilder blockBuilder) {
        DamageEvent damage = block.getDamage();
        blockBuilder.put(DataType.BYTE, DataTransformation.ADD, damage.getDamageDone());
        blockBuilder.put(DataType.BYTE, DataTransformation.NEGATE, damage.getHitType());
        blockBuilder.put(DataType.BYTE, DataTransformation.ADD, damage.getHp() - damage.getDamageDone());
        blockBuilder.put(DataType.BYTE, damage.getMaxHp());
    }

    private void putDoubleHitUpdateBlock(SecondHitUpdateBlock block, GamePacketBuilder blockBuilder) {
        DamageEvent damage = block.getDamage();
        blockBuilder.put(DataType.BYTE, DataTransformation.NEGATE, damage.getDamageDone());
        blockBuilder.put(DataType.BYTE, DataTransformation.SUBTRACT, damage.getHitType());
        blockBuilder.put(DataType.BYTE, DataTransformation.SUBTRACT, damage.getHp() - damage.getDamageDone());
        blockBuilder.put(DataType.BYTE, DataTransformation.NEGATE, damage.getMaxHp());
    }

    private void putForceChat(ForceChatBlock block, GamePacketBuilder blockBuilder) {
        blockBuilder.putString(block.getText());
    }

    /**
     * Puts a graphic block into the specified builder.
     * @param block The block.
     * @param blockBuilder The builder.
     */
    private void putGraphicBlock(GraphicBlock block, GamePacketBuilder blockBuilder) {
        Graphic graphic = block.getGraphic();
        blockBuilder.put(DataType.SHORT, graphic.getId());
        blockBuilder.put(DataType.INT, graphic.getDelay());
    }

    /**
     * Puts an animation block into the specified builder.
     * @param block The block.
     * @param blockBuilder The builder.
     */
    private void putAnimationBlock(AnimationBlock block, GamePacketBuilder blockBuilder) {
        Animation animation = block.getAnimation();
        blockBuilder.put(DataType.SHORT, DataOrder.LITTLE, animation.getId());
        blockBuilder.put(DataType.BYTE, animation.getDelay());
    }
}