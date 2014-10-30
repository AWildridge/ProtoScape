package org.apollo.net.release.r317;

import org.apollo.game.event.impl.SetInterfacePlayerModelEvent;
import org.apollo.net.codec.game.*;
import org.apollo.net.release.EventEncoder;

/**
 * An {@link EventEncoder} for the {@link SetInterfacePlayerModelEvent}.
 * @author Chris Fletcher
 */
final class SetInterfacePlayerModelEventEncoder extends EventEncoder<SetInterfacePlayerModelEvent> {

	@Override
	public GamePacket encode(SetInterfacePlayerModelEvent event) {
		GamePacketBuilder builder = new GamePacketBuilder(185);
		builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, event.getInterfaceId());
		return builder.toGamePacket();
	}

}