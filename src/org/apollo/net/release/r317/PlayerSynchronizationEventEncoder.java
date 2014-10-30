package org.apollo.net.release.r317;

import org.apollo.game.event.impl.DamageEvent;
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
import org.apollo.game.sync.block.SecondHitUpdateBlock;
import org.apollo.game.sync.block.ForceChatBlock;
import org.apollo.game.sync.block.ForceMovementBlock;
import org.apollo.game.sync.block.GraphicBlock;
import org.apollo.game.sync.block.HitUpdateBlock;
import org.apollo.game.sync.block.InteractingCharacterBlock;
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
 * @author Graham
 */
public final class PlayerSynchronizationEventEncoder extends EventEncoder<PlayerSynchronizationEvent> {

    @Override
    public GamePacket encode(PlayerSynchronizationEvent event) {
        GamePacketBuilder builder = new GamePacketBuilder(81, PacketType.VARIABLE_SHORT);
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
                putAddCharacterUpdate((AddCharacterSegment) segment, event, builder);
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
     * Puts a remove character update.
     * @param builder The builder.
     */
    private void putRemoveCharacterUpdate(GamePacketBuilder builder) {
        builder.putBits(1, 1);
        builder.putBits(2, 3);
    }

    /**
     * Puts an add character update.
     * @param seg The segment.
     * @param event The event.
     * @param builder The builder.
     */
    private void putAddCharacterUpdate(AddCharacterSegment seg, PlayerSynchronizationEvent event, GamePacketBuilder builder) {
        boolean updateRequired = seg.getBlockSet().size() > 0;
        Position player = event.getPosition();
        Position other = seg.getPosition();
        builder.putBits(11, seg.getIndex());
        builder.putBits(1, updateRequired ? 1 : 0);
        builder.putBits(1, 1); // discard walking queue?
        builder.putBits(5, other.getY() - player.getY());
        builder.putBits(5, other.getX() - player.getX());
    }

    /**
     * Puts a movement update for the specified segment.
     * @param seg The segment.
     * @param event The event.
     * @param builder The builder.
     */
    private void putMovementUpdate(SynchronizationSegment seg, PlayerSynchronizationEvent event, GamePacketBuilder builder) {
        boolean updateRequired = seg.getBlockSet().size() > 0;
        if (seg.getType() == SegmentType.TELEPORT) {
            Position pos = ((TeleportSegment) seg).getDestination();
            builder.putBits(1, 1);
            builder.putBits(2, 3);
            builder.putBits(2, pos.getHeight());
            builder.putBits(1, event.hasRegionChanged() ? 0 : 1);
            builder.putBits(1, updateRequired ? 1 : 0);
            builder.putBits(7, pos.getLocalY(event.getLastKnownRegion()));
            builder.putBits(7, pos.getLocalX(event.getLastKnownRegion()));
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
     * Puts the blocks for the specified segment.
     * @param segment The segment.
     * @param blockBuilder The block builder.
     */
    private void putBlocks(SynchronizationSegment segment,
            GamePacketBuilder blockBuilder) {
        SynchronizationBlockSet blockSet = segment.getBlockSet();
        if (blockSet.size() > 0) {
            int mask = 0;

            if (blockSet.contains(ForceMovementBlock.class)) {
                mask |= 0x400;
            }
            if (blockSet.contains(GraphicBlock.class)) {
                mask |= 0x100;
            }
            if (blockSet.contains(AnimationBlock.class)) {
                mask |= 0x8;
            }
            if (blockSet.contains(ForceChatBlock.class)) {
                mask |= 0x4;
            }
            if (blockSet.contains(ChatBlock.class)) {
                mask |= 0x80;
            }
            if (blockSet.contains(InteractingCharacterBlock.class)) {
                mask |= 0x1;
            }
            if (blockSet.contains(AppearanceBlock.class)) {
                mask |= 0x10;
            }
            if (blockSet.contains(TurnToPositionBlock.class)) {
                mask |= 0x2;
            }
            if (blockSet.contains(HitUpdateBlock.class)) {
                mask |= 0x20;
            }
            if (blockSet.contains(SecondHitUpdateBlock.class)) {
                mask |= 0x200;
            }

            if (mask >= 0x100) {
                mask |= 0x40;
                blockBuilder.put(DataType.SHORT, DataOrder.LITTLE, mask);
            } else {
                blockBuilder.put(DataType.BYTE, mask);
            }

            if (blockSet.contains(ForceMovementBlock.class)) {
                putForceMovementBlock(blockSet.get(ForceMovementBlock.class),
                        blockBuilder);
            }
            if (blockSet.contains(GraphicBlock.class)) {
                putGraphicBlock(blockSet.get(GraphicBlock.class), blockBuilder);
            }
            if (blockSet.contains(AnimationBlock.class)) {
                putAnimationBlock(blockSet.get(AnimationBlock.class),
                        blockBuilder);
            }
            if (blockSet.contains(ForceChatBlock.class)) {
                putForceChatBlock(blockSet.get(ForceChatBlock.class),
                        blockBuilder);
            }
            if (blockSet.contains(ChatBlock.class)) {
                putChatBlock(blockSet.get(ChatBlock.class), blockBuilder);
            }
            if (blockSet.contains(InteractingCharacterBlock.class)) {
                putInteractingCharacterBlock(
                        blockSet.get(InteractingCharacterBlock.class),
                        blockBuilder);
            }
            if (blockSet.contains(AppearanceBlock.class)) {
                putAppearanceBlock(blockSet.get(AppearanceBlock.class),
                        blockBuilder);
            }
            if (blockSet.contains(TurnToPositionBlock.class)) {
                putTurnToPositionBlock(blockSet.get(TurnToPositionBlock.class),
                        blockBuilder);
            }
            if (blockSet.contains(HitUpdateBlock.class)) {
                putHitUpdateBlock(blockSet.get(HitUpdateBlock.class),
                        blockBuilder);
            }
            if (blockSet.contains(SecondHitUpdateBlock.class)) {
                putSecondHitUpdateBlock(
                        blockSet.get(SecondHitUpdateBlock.class), blockBuilder);
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
        blockBuilder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, pos.getX() * 2 + 1);
        blockBuilder.put(DataType.SHORT, DataOrder.LITTLE, pos.getY() * 2 + 1);
    }

    /**
     * Puts a graphic block into the specified builder.
     * @param block The block.
     * @param blockBuilder The builder.
     */
    private void putGraphicBlock(GraphicBlock block, GamePacketBuilder blockBuilder) {
        Graphic graphic = block.getGraphic();
        blockBuilder.put(DataType.SHORT, DataOrder.LITTLE, graphic.getId());
        blockBuilder.put(DataType.INT, (graphic.getHeight() << 16) | (graphic.getDelay() & 0xFFFF));
    }

    /**
     * Puts a hit block into the specified builder.
     * @param block The block.
     * @param blockBuilder The builder.
     */
    private void putHitUpdateBlock(HitUpdateBlock block, GamePacketBuilder blockBuilder) {
        DamageEvent damage = block.getDamage();
        blockBuilder.put(DataType.BYTE, damage.getDamageDone());
        blockBuilder.put(DataType.BYTE, DataTransformation.ADD, damage.getHitType());
        blockBuilder.put(DataType.BYTE, DataTransformation.NEGATE, damage.getHp() - damage.getDamageDone());
        blockBuilder.put(DataType.BYTE, damage.getMaxHp());
    }

    private void putSecondHitUpdateBlock(SecondHitUpdateBlock block, GamePacketBuilder blockBuilder) {
        DamageEvent damage = block.getDamage();
        blockBuilder.put(DataType.BYTE, damage.getDamageDone());
        blockBuilder.put(DataType.BYTE, DataTransformation.SUBTRACT, damage.getHitType());
        blockBuilder.put(DataType.BYTE, damage.getHp());
        blockBuilder.put(DataType.BYTE, DataTransformation.NEGATE, damage.getMaxHp());
    }

    /**
     * Puts an animation block into the specified builder.
     * @param block The block.
     * @param blockBuilder The builder.
     */
    private void putAnimationBlock(AnimationBlock block, GamePacketBuilder blockBuilder) {
        Animation animation = block.getAnimation();
        blockBuilder.put(DataType.SHORT, DataOrder.LITTLE, animation.getId());
        blockBuilder.put(DataType.BYTE, DataTransformation.NEGATE, animation.getDelay());
    }

    private void putForceChatBlock(ForceChatBlock block, GamePacketBuilder blockBuilder) {
        blockBuilder.putString(block.getText());
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
        builder.put(DataType.SHORT, DataOrder.LITTLE,
                block.getInteractingCharacterIndex());
    }

    /**
     * Puts a Force Movement block in the specified builder.
     * 
     * @param forceMovementBlock
     *            The block.
     * @param builder
     *            The builder.
     */
    private void putForceMovementBlock(ForceMovementBlock block,
            GamePacketBuilder builder) {
        builder.put(DataType.BYTE, DataTransformation.SUBTRACT,
                block.getInitialX());
        builder.put(DataType.BYTE, DataTransformation.SUBTRACT,
                block.getInitialY());
        builder.put(DataType.BYTE, DataTransformation.SUBTRACT,
                block.getFinalX());
        builder.put(DataType.BYTE, DataTransformation.SUBTRACT,
                block.getFinalY());
        builder.put(DataType.SHORT, DataOrder.LITTLE, block.getSpeedX());
        builder.put(DataType.SHORT, DataTransformation.ADD, block.getSpeedY());
        builder.put(DataType.BYTE, DataTransformation.SUBTRACT,
                block.getDirection());
    }

    /**
     * Puts a chat block into the specified builder.
     * @param block The block.
     * @param blockBuilder The builder.
     */
    private void putChatBlock(ChatBlock block, GamePacketBuilder blockBuilder) {
        byte[] bytes = block.getCompressedMessage();
        blockBuilder.put(DataType.SHORT, DataOrder.LITTLE, (block.getTextColor() << 8) | block.getTextEffects());
        blockBuilder.put(DataType.BYTE, block.getPrivilegeLevel().toInteger());
        blockBuilder.put(DataType.BYTE, DataTransformation.NEGATE, bytes.length);
        blockBuilder.putBytesReverse(bytes);
    }

    /**
     * Puts an appearance block into the specified builder.
     * @param block The block.
     * @param blockBuilder The builder.
     */
    private void putAppearanceBlock(AppearanceBlock block, GamePacketBuilder blockBuilder) {
        Appearance appearance = block.getAppearance();
        GamePacketBuilder playerProperties = new GamePacketBuilder();

        playerProperties.put(DataType.BYTE, appearance.getGender().toInteger()); // gender
        playerProperties.put(DataType.BYTE, appearance.getHeadIcon()); // head icon
        playerProperties.put(DataType.BYTE, appearance.getSkullIcon()); //skull icon
        playerProperties.put(DataType.BYTE, appearance.getHintIcon()); //hint icon
        //TODO: Find a better way?
        EquipmentDefinition def = null;
        Inventory equipment = block.getEquipment();
        int[] style = appearance.getStyle();
        Item item, chest, helm;

        if (appearance.getNpcId() == -1) {
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
                def = EquipmentDefinition.forId(chest.getId());
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
                def = EquipmentDefinition.forId(helm.getId());
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

            def = null;
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
        } else {
            playerProperties.put(DataType.SHORT, -1);
            playerProperties.put(DataType.SHORT, appearance.getNpcId());
        }
        int[] colors = appearance.getColors();
        for (int i = 0; i < colors.length; i++) {
            playerProperties.put(DataType.BYTE, colors[i]);
        }


        Item wep = equipment.get(EquipmentConstants.WEAPON);
        if (wep == null || wep.getEquipmentDefinition() == null) {
            playerProperties.put(DataType.SHORT, 0x328); // stand
            playerProperties.put(DataType.SHORT, 0x337); // stand turn
            playerProperties.put(DataType.SHORT, 0x333); // walk
            playerProperties.put(DataType.SHORT, 0x334); // turn 180
            playerProperties.put(DataType.SHORT, 0x335); // turn 90 cw
            playerProperties.put(DataType.SHORT, 0x336); // turn 90 ccw
            playerProperties.put(DataType.SHORT, 0x338); // run
        } else if (wep.getEquipmentDefinition() != null && wep != null) {
            EquipmentDefinition wepDef = wep.getEquipmentDefinition();
            def = wepDef;
            if (wepDef != null) {
                if (wepDef.getAnimation() != null) {

                    if (def.getAnimation().getStand() != null) {
                        playerProperties.put(DataType.SHORT, def.getAnimation().getStand().getId()); // stand
                    } else {
                        playerProperties.put(DataType.SHORT, 0x328); // stand
                    }
                    if (def.getAnimation().getStandTurn() != null) {
                        playerProperties.put(DataType.SHORT, def.getAnimation().getStandTurn().getId()); // stand turn
                    } else {
                        playerProperties.put(DataType.SHORT, 0x337); // stand turn
                    }
                    if (def.getAnimation().getWalk() != null) {
                        playerProperties.put(DataType.SHORT, def.getAnimation().getWalk().getId()); // walk
                    } else {
                        playerProperties.put(DataType.SHORT, 0x333); // walk
                    }

                    if (def.getAnimation().getTurn180() != null) {
                        playerProperties.put(DataType.SHORT, def.getAnimation().getTurn180().getId()); // turn 180
                    } else {
                        playerProperties.put(DataType.SHORT, 0x334); // turn 180
                    }

                    if (def.getAnimation().getTurn90ClockWise() != null) {
                        playerProperties.put(DataType.SHORT, def.getAnimation().getTurn90ClockWise().getId()); // turn 90 cw
                    } else {
                        playerProperties.put(DataType.SHORT, 0x335); // turn 90 cw
                    }

                    if (def.getAnimation().getTurn90CounterClockWise() != null) {
                        playerProperties.put(DataType.SHORT, def.getAnimation().getTurn90CounterClockWise().getId()); // turn 90 ccw
                    } else {
                        playerProperties.put(DataType.SHORT, 0x336); // turn 90 ccw
                    }

                    if (def.getAnimation().getRun() != null) {
                        playerProperties.put(DataType.SHORT, def.getAnimation().getRun().getId()); // run
                    } else {
                        playerProperties.put(DataType.SHORT, 0x338); // run
                    }

                }
            }
        }
        playerProperties.put(DataType.LONG, block.getName());
        playerProperties.put(DataType.BYTE, block.getCombatLevel()); // combat level
        playerProperties.put(DataType.SHORT, block.getSkillLevel()); // total skill level

        blockBuilder.put(DataType.BYTE, DataTransformation.NEGATE, playerProperties.getLength());
        blockBuilder.putRawBuilder(playerProperties);
    }
}
