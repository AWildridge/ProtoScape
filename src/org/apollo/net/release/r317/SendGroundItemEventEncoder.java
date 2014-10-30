package org.apollo.net.release.r317;

import org.apollo.game.event.impl.SendGroundItemEvent;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.EventEncoder;

/**
 * SendGroundItemEventEncoder.java
 * @author The Wanderer
 */
public class SendGroundItemEventEncoder extends EventEncoder<SendGroundItemEvent> {

        @Override
        public GamePacket encode(SendGroundItemEvent event) {
            GamePacketBuilder builder = new GamePacketBuilder(44);
            builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, event.getItem().getId());
            builder.put(DataType.SHORT, event.getItem().getAmount());
            builder.put(DataType.BYTE, 0);
            return builder.toGamePacket();
        }
}
