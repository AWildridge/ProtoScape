package org.apollo.net.release.r317;

import org.apollo.game.event.impl.SendNPCHeadEvent;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.EventEncoder;

/**
 * An npc head event encoder.
 *
 *
 * @author Rodrigo Molina
 */
public class SendNPCHeadEventEncoder extends EventEncoder<SendNPCHeadEvent> {

    	@Override
    	public GamePacket encode(SendNPCHeadEvent event) {
    	    GamePacketBuilder builder = new GamePacketBuilder(75);
    	    builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, event.getInterfaceId());
    	    builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, event.getNPCId());
    	    return builder.toGamePacket();
    	}

}
