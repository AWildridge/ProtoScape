package org.apollo.net.release.r317;


import org.apollo.game.event.impl.PickupItemEvent;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.EventDecoder;
import org.apollo.net.release.EventEncoder;

/**
 * An {@link EventEncoder} for the {@link GroundItemEventEncoder}.
 * @author Arrowzftw
 */
public final class PickupItemEventDecoder extends EventDecoder<PickupItemEvent> {

	@Override
	public PickupItemEvent decode(GamePacket packet) {
            GamePacketReader reader = new GamePacketReader(packet);
            int y = (int) reader.getSigned(DataType.SHORT,DataOrder.LITTLE);
            int id = (int) reader.getSigned(DataType.SHORT);
            int x = (int) reader.getSigned(DataType.SHORT,DataOrder.LITTLE);
            return new PickupItemEvent(id, x, y);
	}

}