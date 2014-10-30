package org.apollo.net.release.r317;

import org.apollo.game.event.impl.WalkableInterfaceEvent;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.EventEncoder;

/**
 * Displays an walkable interface event.
 *
 *
 * @author Rodrigo Molina
 */
public class WalkableInterfaceEventEncoder extends EventEncoder<WalkableInterfaceEvent> {

    	@Override
    	public GamePacket encode(WalkableInterfaceEvent event) {
		GamePacketBuilder pack = new GamePacketBuilder(208);
		pack.put(DataType.SHORT, DataOrder.LITTLE, event.getInterfaceId());
    	    	return pack.toGamePacket();
    	}

}
