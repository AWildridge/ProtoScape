package org.apollo.net.release.r317;

import org.apollo.game.event.impl.SendConfigEvent;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.EventEncoder;

/**
 * SendConfigEventEncoder.java
 * @author The Wanderer
 */
public class SendConfigEventEncoder extends EventEncoder<SendConfigEvent> {

    @Override
    public GamePacket encode(SendConfigEvent event) {
        if (event.getState() < Byte.MIN_VALUE || event.getState() > Byte.MAX_VALUE) {
            GamePacketBuilder builder = new GamePacketBuilder(87);
            builder.put(DataType.SHORT, DataOrder.LITTLE, event.getConfigId());
            builder.put(DataType.INT, event.getState());
            return builder.toGamePacket();
        } else {
            GamePacketBuilder builder = new GamePacketBuilder(36);
            builder.put(DataType.SHORT, DataOrder.LITTLE, event.getConfigId());
            builder.put(DataType.BYTE, event.getState());
            return builder.toGamePacket();
        }
    }
}
