package org.apollo.net.release.r317;

import org.apollo.game.event.impl.SetInterfaceModelEvent;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.EventEncoder;
/**
 * SetInterfaceModelEventEncoder.java
 * @author The Wanderer
 */
public class SetInterfaceModelEventEncoder extends EventEncoder<SetInterfaceModelEvent> {

        @Override
        public GamePacket encode(SetInterfaceModelEvent event) {
                GamePacketBuilder builder = new GamePacketBuilder(246);
                builder.put(DataType.SHORT, DataOrder.LITTLE, event.getInterfaceId());
                builder.put(DataType.SHORT, event.getZoom());
                builder.put(DataType.SHORT, event.getModel());
                return builder.toGamePacket();
        }
}
