package org.apollo.net.release.r317;

import org.apollo.game.event.impl.SetInterfaceItemModelEvent;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.EventEncoder;

/**
 * An {@link EventEncoder} for the {@link SetInterfaceItemModelEvent}.
 * @author Chris Fletcher
 */
final class SetInterfaceItemModelEventEncoder extends EventEncoder<SetInterfaceItemModelEvent> {

	@Override
	public GamePacket encode(SetInterfaceItemModelEvent event) {
		GamePacketBuilder builder = new GamePacketBuilder(246);

		builder.put(DataType.SHORT, DataOrder.LITTLE, event.getInterfaceId());
		builder.put(DataType.SHORT, event.getZoom());
		builder.put(DataType.SHORT, event.getModelId());

		return builder.toGamePacket();
	}

}
