package org.apollo.net.release.r317;

import org.apollo.game.event.impl.CreateObjectEvent;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.EventEncoder;

/**
 * CreateObjectEventEncoder.java
 * @author The Wanderer
 */
public class CreateObjectEventEncoder extends EventEncoder<CreateObjectEvent> {

        @Override
        public GamePacket encode(CreateObjectEvent event) {
            GamePacketBuilder builder = new GamePacketBuilder(151);
            builder.put(DataType.BYTE, DataTransformation.ADD, 0); //TODO: offset
            builder.put(DataType.SHORT, DataOrder.LITTLE, event.getId()); //id
            builder.put(DataType.BYTE, DataTransformation.SUBTRACT, (event.getType() << 2) + (event.getFace() & 3));
            return builder.toGamePacket();
        }
}
