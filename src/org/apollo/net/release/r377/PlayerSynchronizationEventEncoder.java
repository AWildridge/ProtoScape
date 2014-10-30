package org.apollo.net.release.r377;

import org.apollo.game.event.impl.PlayerSynchronizationEvent;
import org.apollo.game.model.Animation;
import org.apollo.game.model.Appearance;
import org.apollo.game.model.Direction;
import org.apollo.game.model.EquipmentConstants;
import org.apollo.game.model.EquipmentConstants.EquipmentType;
import org.apollo.game.model.Gender;
import org.apollo.game.model.Graphic;
import org.apollo.game.model.Inventory;
import org.apollo.game.model.Item;
import org.apollo.game.model.Position;
import org.apollo.game.model.def.EquipmentDefinition;
import org.apollo.game.sync.block.AnimationBlock;
import org.apollo.game.sync.block.AppearanceBlock;
import org.apollo.game.sync.block.ChatBlock;
import org.apollo.game.sync.block.ForceChatBlock;
import org.apollo.game.sync.block.ForceMovementBlock;
import org.apollo.game.sync.block.GraphicBlock;
import org.apollo.game.sync.block.HitUpdateBlock;
import org.apollo.game.sync.block.InteractingCharacterBlock;
import org.apollo.game.sync.block.SecondHitUpdateBlock;
import org.apollo.game.sync.block.SynchronizationBlockSet;
import org.apollo.game.sync.block.TurnToPositionBlock;
import org.apollo.game.sync.seg.AddCharacterSegment;
import org.apollo.game.sync.seg.MovementSegment;
import org.apollo.game.sync.seg.SegmentType;
import org.apollo.game.sync.seg.SynchronizationSegment;
import org.apollo.game.sync.seg.TeleportSegment;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.EventEncoder;

/**
 * An {@link EventEncoder} for the {@link PlayerSynchronizationEvent}.
 * 
 * @author Graham
 * @author Major
 */
public final class PlayerSynchronizationEventEncoder extends EventEncoder<PlayerSynchronizationEvent> {

    @Override
    public GamePacket encode(PlayerSynchronizationEvent event) {
        GamePacketBuilder builder = new GamePacketBuilder(90,
                PacketType.VARIABLE_SHORT);
        builder.switchToBitAccess();

        GamePacketBuilder blockBuilder = new GamePacketBuilder();

        putMovementUpdate(event.getSegment(), event, builder);
        putBlocks(event.getSegment(), blockBuilder);

        builder.putBits(8, event.getLocalPlayers());

        for (SynchronizationSegment segment : event.getSegments()) {
            SegmentType type = segment.getType();
            if (type == SegmentType.REMOVE_CHARACTER) {
                putRemoveCharacterUpdate(builder);
            } else if (type == SegmentType.ADD_CHARACTER) {
                putAddCharacterUpdate((AddCharacterSegment) segment, event,
                        builder);
                putBlocks(segment, blockBuilder);
            } else {
                putMovementUpdate(segment, event, builder);
                putBlocks(segment, blockBuilder);
            }
        }

        if (blockBuilder.getLength() > 0) {
            builder.putBits(11, 2047);
            builder.switchToByteAccess();
            builder.putRawBuilder(blockBuilder);
        } else {
            builder.switchToByteAccess();
        }

        return builder.toGamePacket();
    }

    /**
     * Puts the blocks for the specified segment.
     * 
     * @param segment
     *            The segment.
     * @param blockBuilder
     *            The block builder.
     */
    private void putBlocks(SynchronizationSegment segment,
            GamePacketBuilder blockBuilder) {
        SynchronizationBlockSet blockSet = segment.getBlockSet();
        if (blockSet.size() > 0) {
            int mask = 0;

            if (blockSet.contains(AnimationBlock.class)) {
                mask |= 0x8;
            }
            if (blockSet.contains(ForceChatBlock.class)) {
                mask |= 0x10;
            }
            if (blockSet.contains(ForceMovementBlock.class)) {
                mask |= 0x100;
            }
            if (blockSet.contains(InteractingCharacterBlock.class)) {
                mask |= 0x1;
            }
            if (blockSet.contains(TurnToPositionBlock.class)) {
                mask |= 0x2;
            }
            if (blockSet.contains(GraphicBlock.class)) {
                mask |= 0x200;
            }
            if (blockSet.contains(AppearanceBlock.class)) {
                mask |= 0x4;
            }
            if (blockSet.contains(SecondHitUpdateBlock.class)) {
                mask |= 0x400;
            }
            if (blockSet.contains(ChatBlock.class)) {
                mask |= 0x40;
            }
            if (blockSet.contains(HitUpdateBlock.class)) {
                mask |= 0x80;
            }

            if (mask >= 0x100) {
                mask |= 0x20;
                blockBuilder.put(DataType.SHORT, DataOrder.LITTLE, mask);
            } else {
                blockBuilder.put(DataType.BYTE, mask);
            }

            if (blockSet.contains(AnimationBlock.class)) {
                putAnimationBlock(blockSet.get(AnimationBlock.class),
                        blockBuilder);
            }
            if (blockSet.contains(ForceChatBlock.class)) {
                putForceChatBlock(blockSet.get(ForceChatBlock.class),
                        blockBuilder);
            }
            if (blockSet.contains(ForceMovementBlock.class)) {
                putForceMovementBlock(blockSet.get(ForceMovementBlock.class),
                        blockBuilder);
            }
            if (blockSet.contains(InteractingCharacterBlock.class)) {
                putInteractingCharacterBlock(
                        blockSet.get(InteractingCharacterBlock.class),
                        blockBuilder);
            }
            if (blockSet.contains(TurnToPositionBlock.class)) {
                putTurnToPositionBlock(blockSet.get(TurnToPositionBlock.class),
                        blockBuilder);
            }
            if (blockSet.contains(GraphicBlock.class)) {
                putGraphicBlock(blockSet.get(GraphicBlock.class), blockBuilder);
            }
            if (blockSet.contains(AppearanceBlock.class)) {
                putAppearanceBlock(blockSet.get(AppearanceBlock.class),
                        blockBuilder);
            }
            if (blockSet.contains(SecondHitUpdateBlock.class)) {
                putSecondHitUpdateBlock(
                        blockSet.get(SecondHitUpdateBlock.class), blockBuilder);
            }
            if (blockSet.contains(ChatBlock.class)) {
                putChatBlock(blockSet.get(ChatBlock.class), blockBuilder);
            }
            if (blockSet.contains(HitUpdateBlock.class)) {
                putHitUpdateBlock(blockSet.get(HitUpdateBlock.class),
                        blockBuilder);
            }

        }
    }

    /**
     * Puts a movement update for the specified segment.
     * 
     * @param seg
     *            The segment.
     * @param event
     *            The event.
     * @param builder
     *            The builder.
     */
    private void putMovementUpdate(SynchronizationSegment seg,
            PlayerSynchronizationEvent event, GamePacketBuilder builder) {
        boolean updateRequired = seg.getBlockSet().size() > 0;
        if (seg.getType() == SegmentType.TELEPORT) {
            Position pos = ((TeleportSegment) seg).getDestination();
            builder.putBits(1, 1);
            builder.putBits(2, 3);
            builder.putBits(1, event.hasRegionChanged() ? 0 : 1);
            builder.putBits(2, pos.getHeight());
            builder.putBits(7, pos.getLocalY(event.getLastKnownRegion()));
            builder.putBits(7, pos.getLocalX(event.getLastKnownRegion()));
            builder.putBits(1, updateRequired ? 1 : 0);
        } else if (seg.getType() == SegmentType.RUN) {
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
     * Puts an add character update.
     * 
     * @param seg
     *            The segment.
     * @param event
     *            The event.
     * @param builder
     *            The builder.
     */
    private void putAddCharacterUpdate(AddCharacterSegment seg,
            PlayerSynchronizationEvent event, GamePacketBuilder builder) {
        boolean updateRequired = seg.getBlockSet().size() > 0;
        Position player = event.getPosition();
        Position other = seg.getPosition();
        builder.putBits(11, seg.getIndex());
        builder.putBits(5, other.getX() - player.getX());
        builder.putBits(1, updateRequired ? 1 : 0);
        builder.putBits(1, 1); // discard walking queue?
        builder.putBits(5, other.getY() - player.getY());
    }

    /**
     * Puts a remove character update.
     * 
     * @param builder
     *            The builder.
     */
    private void putRemoveCharacterUpdate(GamePacketBuilder builder) {
        builder.putBits(1, 1);
        builder.putBits(2, 3);
    }

    /**
     * Puts an Interacting Character block into the specified builder.
     * 
     * @param block
     *            The block.
     * @param builder
     *            The builder.
     */
    private void putInteractingCharacterBlock(InteractingCharacterBlock block,
            GamePacketBuilder builder) {
        builder.put(DataType.SHORT, DataTransformation.ADD,
                block.getInteractingCharacterIndex());
    }

    /**
     * Puts a Turn To Position block into the specified builder.
     * 
     * @param block
     *            The block.
     * @param blockBuilder
     *            The builder.
     */
    private void putTurnToPositionBlock(TurnToPositionBlock block,
            GamePacketBuilder blockBuilder) {
        Position pos = block.getPosition();
        blockBuilder.put(DataType.SHORT, pos.getX() * 2 + 1);
        blockBuilder.put(DataType.SHORT, pos.getY() * 2 + 1);
    }

    /**
     * Puts an Appearance block into the specified builder.
     * 
     * @param block
     *            The block.
     * @param blockBuilder
     *            The builder.
     */
    private void putAppearanceBlock(AppearanceBlock block,
            GamePacketBuilder blockBuilder) {
        Appearance appearance = block.getAppearance();
        GamePacketBuilder playerProperties = new GamePacketBuilder();

        playerProperties.put(DataType.BYTE, appearance.getGender().toInteger());
        playerProperties.put(DataType.BYTE, -1); // skull icon
        playerProperties.put(DataType.BYTE, -1); // prayer icon

        Inventory equipment = block.getEquipment();
        int[] style = appearance.getStyle();
        Item item, chest, helm;

        for (int slot = 0; slot < 4; slot++) {
            if ((item = equipment.get(slot)) != null) {
                playerProperties.put(DataType.SHORT, 0x200 + item.getId());
            } else {
                playerProperties.put(DataType.BYTE, 0);
            }
        }

        if ((chest = equipment.get(EquipmentConstants.CHEST)) != null) {
            playerProperties.put(DataType.SHORT, 0x200 + chest.getId());
        } else {
            playerProperties.put(DataType.SHORT, 0x100 + style[2]);
        }

        if ((item = equipment.get(EquipmentConstants.SHIELD)) != null) {
            playerProperties.put(DataType.SHORT, 0x200 + item.getId());
        } else {
            playerProperties.put(DataType.BYTE, 0);
        }

        if (chest != null) {
            EquipmentDefinition def = EquipmentDefinition.forId(chest.getId());
            if (def != null && def.getType() != EquipmentType.PLATEBODY) {
                playerProperties.put(DataType.SHORT, 0x100 + style[3]);
            } else {
                playerProperties.put(DataType.BYTE, 0);
            }
        } else {
            playerProperties.put(DataType.SHORT, 0x100 + style[3]);
        }

        if ((item = equipment.get(EquipmentConstants.LEGS)) != null) {
            playerProperties.put(DataType.SHORT, 0x200 + item.getId());
        } else {
            playerProperties.put(DataType.SHORT, 0x100 + style[5]);
        }

        if ((helm = equipment.get(EquipmentConstants.HAT)) != null) {
            EquipmentDefinition def = EquipmentDefinition.forId(helm.getId());
            if (def != null && def.getType() != EquipmentType.FULL_HELM && def.getType() != EquipmentType.FULL_MASK) {
                playerProperties.put(DataType.SHORT, 0x100 + style[0]);
            } else {
                playerProperties.put(DataType.BYTE, 0);
            }
        } else {
            playerProperties.put(DataType.SHORT, 0x100 + style[0]);
        }

        if ((item = equipment.get(EquipmentConstants.HANDS)) != null) {
            playerProperties.put(DataType.SHORT, 0x200 + item.getId());
        } else {
            playerProperties.put(DataType.SHORT, 0x100 + style[4]);
        }

        if ((item = equipment.get(EquipmentConstants.FEET)) != null) {
            playerProperties.put(DataType.SHORT, 0x200 + item.getId());
        } else {
            playerProperties.put(DataType.SHORT, 0x100 + style[6]);
        }

        EquipmentDefinition def = null;
        if (helm != null) {
            def = EquipmentDefinition.forId(helm.getId());
        }
        if ((def != null && (def.getType() == EquipmentType.FULL_HELM ||
                def.getType() == EquipmentType.FULL_MASK)) ||
                appearance.getGender() == Gender.FEMALE) {
            playerProperties.put(DataType.BYTE, 0);
        } else {
            playerProperties.put(DataType.SHORT, 0x100 + style[1]);
        }

        int[] colors = appearance.getColors();
        for (int color : colors) {
            playerProperties.put(DataType.BYTE, color);
        }

        playerProperties.put(DataType.SHORT, 0x328); // stand
        playerProperties.put(DataType.SHORT, 0x337); // stand turn
        playerProperties.put(DataType.SHORT, 0x333); // walk
        playerProperties.put(DataType.SHORT, 0x334); // turn 180
        playerProperties.put(DataType.SHORT, 0x335); // turn 90 cw
        playerProperties.put(DataType.SHORT, 0x336); // turn 90 ccw
        playerProperties.put(DataType.SHORT, 0x338); // run

        playerProperties.put(DataType.LONG, block.getName());
        playerProperties.put(DataType.BYTE, block.getCombatLevel());
        playerProperties.put(DataType.SHORT, block.getSkillLevel());

        blockBuilder.put(DataType.BYTE, playerProperties.getLength());
        blockBuilder.putRawBuilderReverse(playerProperties);
    }

    /**
     * Puts an Animation block into the specified builder.
     * 
     * @param block
     *            The block.
     * @param blockBuilder
     *            The builder.
     */
    private void putAnimationBlock(AnimationBlock block,
            GamePacketBuilder blockBuilder) {
        Animation animation = block.getAnimation();
        blockBuilder.put(DataType.SHORT, animation.getId());
        blockBuilder.put(DataType.BYTE, DataTransformation.ADD,
                animation.getDelay());
    }

    /**
     * Puts a Force Chat block into the specified builder.
     * 
     * @param block
     *            The block.
     * @param builder
     *            The builder.
     */
    private void putForceChatBlock(ForceChatBlock block,
            GamePacketBuilder builder) {
        builder.putString(block.getText());
    }

    /**
     * Puts a Chat block into the specified builder.
     * 
     * @param block
     *            The block.
     * @param blockBuilder
     *            The builder.
     */
    private void putChatBlock(ChatBlock block, GamePacketBuilder blockBuilder) {
        byte[] bytes = block.getCompressedMessage();
        blockBuilder.put(DataType.SHORT, DataOrder.LITTLE,
                block.getTextEffects() << 8 | block.getTextColor());
        blockBuilder.put(DataType.BYTE, DataTransformation.NEGATE, block.getPrivilegeLevel().toInteger());
        blockBuilder.put(DataType.BYTE, DataTransformation.ADD, bytes.length);
        blockBuilder.putBytes(DataTransformation.ADD, bytes);
    }

    /**
     * Puts a Hit Update block into the specified builder.
     * 
     * @param block
     *            The block.
     * @param builder
     *            The builder.
     */
    private void putHitUpdateBlock(HitUpdateBlock block,
            GamePacketBuilder builder) {
        builder.put(DataType.BYTE, DataTransformation.SUBTRACT,
                block.getDamage().getDamageDone());
        builder.put(DataType.BYTE, DataTransformation.NEGATE, block.getDamage().getHitType());
        builder.put(DataType.BYTE, DataTransformation.SUBTRACT,
                block.getDamage().getHp());
        builder.put(DataType.BYTE, block.getDamage().getMaxHp());
    }

    /**
     * Puts a Force Movement block into the specified builder.
     * 
     * @param block
     *            The block.
     * @param builder
     *            The builder.
     */
    private void putForceMovementBlock(ForceMovementBlock block,
            GamePacketBuilder builder) {
        builder.put(DataType.BYTE, DataTransformation.ADD, block.getInitialX());
        builder.put(DataType.BYTE, DataTransformation.NEGATE,
                block.getInitialY());
        builder.put(DataType.BYTE, DataTransformation.SUBTRACT,
                block.getFinalX());
        builder.put(DataType.BYTE, block.getFinalY());
        builder.put(DataType.SHORT, block.getSpeedX());
        builder.put(DataType.SHORT, DataTransformation.ADD, block.getSpeedY());
        builder.put(DataType.BYTE, block.getDirection());
    }

    /**
     * Puts a Graphic block into the specified builder.
     * 
     * @param block
     *            The block.
     * @param blockBuilder
     *            The builder.
     */
    private void putGraphicBlock(GraphicBlock block,
            GamePacketBuilder blockBuilder) {
        Graphic graphic = block.getGraphic();
        blockBuilder.put(DataType.SHORT, DataTransformation.ADD,
                graphic.getId());
        blockBuilder.put(DataType.INT, DataOrder.MIDDLE,
                graphic.getHeight() << 16 & 0xFFFF0000 | graphic.getDelay()
                & 0x0000FFFF);
    }

    /**
     * Puts a SecondHitUpdate block into the specified builder.
     * 
     * @param block
     *            The block.
     * @param builder
     *            The builder.
     */
    private void putSecondHitUpdateBlock(SecondHitUpdateBlock block, GamePacketBuilder builder) {
        builder.put(DataType.BYTE, DataTransformation.ADD, block.getDamage().getDamageDone());
        builder.put(DataType.BYTE, DataTransformation.SUBTRACT, block.getDamage().getHitType());
        builder.put(DataType.BYTE, DataTransformation.NEGATE,
                block.getDamage().getHp());
        builder.put(DataType.BYTE, block.getDamage().getMaxHp());
    }
}