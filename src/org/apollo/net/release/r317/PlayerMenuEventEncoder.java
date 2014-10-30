package org.apollo.net.release.r317;

import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.release.EventEncoder;
import org.apollo.game.event.impl.PlayerMenuEvent;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.meta.PacketType;

/**
 * An {@link EventEncoder} for the {@link PlayerMenuEvent}.
 * @author Lucas Beau
 * @version 0.1
 */
public class PlayerMenuEventEncoder extends EventEncoder<PlayerMenuEvent> {

    /*
     * (non-Javadoc)
     * @see org.apollo.net.release.EventDecoder#decode(org.apollo.net.codec.game.GamePacket)
     */
    @Override
    public GamePacket encode(PlayerMenuEvent event) {
        GamePacketBuilder builder = new GamePacketBuilder(104, PacketType.VARIABLE_BYTE);
	builder.put(DataType.BYTE, DataTransformation.NEGATE, event.getOptionNumber());
	builder.put(DataType.BYTE, DataTransformation.ADD, event.getOrder() ? 1 : 0);
	builder.putString(event.getMessage());
	return builder.toGamePacket();
    }

}