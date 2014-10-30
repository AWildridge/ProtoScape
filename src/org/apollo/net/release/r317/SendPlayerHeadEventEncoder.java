package org.apollo.net.release.r317;

import org.apollo.game.event.impl.SendPlayerHeadEvent;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.EventEncoder;

/**
 *
 *
 *
 * @author Rodrigo Molina
 */
public class SendPlayerHeadEventEncoder extends EventEncoder<SendPlayerHeadEvent> {

        @Override
        public GamePacket encode(SendPlayerHeadEvent event) {
            GamePacketBuilder builder = new GamePacketBuilder(185);
            builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, event.getFrame());
            return builder.toGamePacket();
        }

}
