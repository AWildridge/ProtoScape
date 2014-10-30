package org.apollo.net.release.r317;

import org.apollo.game.event.impl.SetInterfaceNpcModelEvent;
import org.apollo.net.codec.game.*;
import org.apollo.net.release.EventEncoder;

/**
 * An {@link EventEncoder} for the {@link SetInterfaceNpcModelEvent}.
 * @author Chris Fletcher
 */
final class SetInterfaceNpcModelEventEncoder extends EventEncoder<SetInterfaceNpcModelEvent> {

	@Override
	public GamePacket encode(SetInterfaceNpcModelEvent event) {
		GamePacketBuilder builder = new GamePacketBuilder(75);

		builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, event.getModelId());
		builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, event.getInterfaceId());

		return builder.toGamePacket();
	}

}
