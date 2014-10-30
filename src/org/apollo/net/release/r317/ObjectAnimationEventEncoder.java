package org.apollo.net.release.r317;

import org.apollo.game.event.impl.ObjectAnimationEvent;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.EventEncoder;

/**
 * @author Rodrigo Molina
 */
public class ObjectAnimationEventEncoder extends EventEncoder<ObjectAnimationEvent> {

    @Override
    public GamePacket encode(ObjectAnimationEvent event) {
	GamePacketBuilder builder = new GamePacketBuilder(160);
	builder.put(DataType.BYTE, DataTransformation.SUBTRACT, 0);
	builder.put(DataType.BYTE, DataTransformation.SUBTRACT, (event.getTileObjectType() << 2) + (event.getOrientation() & 3));
	builder.put(DataType.SHORT, DataTransformation.ADD, event.getAnim().getId());
	return builder.toGamePacket();
    }

}
