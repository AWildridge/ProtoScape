package org.apollo.net.release.r317;

import org.apollo.game.event.impl.SendProjectileEvent;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.EventEncoder;

/**
 * SendProjectileEventEncoder.java
 * @author The Wanderer
 * @author Jake_G
 */
public class SendProjectileEventEncoder extends EventEncoder<SendProjectileEvent> {

        @Override
        public GamePacket encode(SendProjectileEvent event) {
                GamePacketBuilder builder = new GamePacketBuilder(117);
                builder.put(DataType.BYTE, 0); //im just going to be a fag and put 0 here for now
                builder.put(DataType.BYTE, event.getOffsetX());
                builder.put(DataType.BYTE, event.getOffsetY());
                builder.put(DataType.SHORT, event.getLockon());
                builder.put(DataType.SHORT, event.getId());
                builder.put(DataType.BYTE, event.getStartHeight());
                builder.put(DataType.BYTE, event.getEndHeight());
                builder.put(DataType.SHORT, event.getDelay());
                builder.put(DataType.SHORT, event.getSpeed());
                builder.put(DataType.BYTE, event.getSlope());
                builder.put(DataType.BYTE, event.getAngle()); 
                return builder.toGamePacket(); //alright start her up
        }
}
