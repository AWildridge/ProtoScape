package org.apollo.net.release.r317;

import org.apollo.game.event.impl.OpenInterfaceSidebarEvent;
import org.apollo.game.event.impl.SetSidebarEvent;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.EventEncoder;

/**
 * An {@link EventEncoder} for the {@link OpenInterfaceSidebarEvent}.
 * @author Graham
 */
public final class SetSidebarEventEncoder extends EventEncoder<SetSidebarEvent> {

	@Override
	public GamePacket encode(SetSidebarEvent event) {
		GamePacketBuilder builder = new GamePacketBuilder(71);
		builder.put(DataType.SHORT, event.getInterfaceId());
		builder.put(DataType.BYTE, DataTransformation.ADD, event.getMenuId());
		return builder.toGamePacket();
	}

}
