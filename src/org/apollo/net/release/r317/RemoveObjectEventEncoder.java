package org.apollo.net.release.r317;

import org.apollo.game.event.impl.RemoveObjectEvent;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.EventEncoder;

/**
 * RemoveObjectEncoder.java
 * @author The Wanderer
 */
public class RemoveObjectEventEncoder extends EventEncoder<RemoveObjectEvent> {

        @Override
        public GamePacket encode(RemoveObjectEvent event) {
            GamePacketBuilder builder = new GamePacketBuilder(101);
            builder.put(DataType.BYTE, DataTransformation.NEGATE, (event.getType() << 2) + (event.getFace() & 3));
            builder.put(DataType.BYTE, 0); //TODO: Implement object slot functionality.
            return builder.toGamePacket();
        }   
}
