package org.apollo.net.release.r317;

import org.apollo.game.event.impl.OpenChatInterfaceEvent;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.EventEncoder;
/**
 * OpenChatInterfaceEventEncoder.java
 * @author The Wanderer
 */
public class OpenChatInterfaceEventEncoder extends EventEncoder<OpenChatInterfaceEvent> {

        @Override
        public GamePacket encode(OpenChatInterfaceEvent event) {
                GamePacketBuilder builder = new GamePacketBuilder(164);
                builder.put(DataType.SHORT, DataOrder.LITTLE, event.getId());
                return builder.toGamePacket();
        }
}
