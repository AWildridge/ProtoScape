package org.apollo.net.release.r317;

import org.apollo.game.event.impl.SetInterfaceComponentEvent;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.EventEncoder;

/**
 * An {@link EventEncoder} for the {@link SetInterfaceComponentEvent}.
 * @author Chris Fletcher
 */
final class SetInterfaceComponentEventEncoder extends EventEncoder<SetInterfaceComponentEvent> {

	@Override
	public GamePacket encode(SetInterfaceComponentEvent event) {
		GamePacketBuilder builder = new GamePacketBuilder(171);

		builder.put(DataType.BYTE, event.isVisible() ? 0 : 1);
		builder.put(DataType.SHORT, event.getComponentId());

		return builder.toGamePacket();
	}

}