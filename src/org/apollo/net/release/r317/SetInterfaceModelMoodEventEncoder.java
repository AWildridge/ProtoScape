package org.apollo.net.release.r317;

import org.apollo.game.event.impl.SetInterfaceModelMoodEvent;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.EventEncoder;

/**
 * An {@link EventEncoder} for the {@link SetInterfaceModelMoodEvent}.
 * @author Chris Fletcher
 */
final class SetInterfaceModelMoodEventEncoder extends EventEncoder<SetInterfaceModelMoodEvent> {

	@Override
	public GamePacket encode(SetInterfaceModelMoodEvent event) {
		GamePacketBuilder builder = new GamePacketBuilder(200);

		builder.put(DataType.SHORT, event.getInterfaceId() & 0xFFFF);
		builder.put(DataType.SHORT, event.getMood());

		return builder.toGamePacket();
	}

}