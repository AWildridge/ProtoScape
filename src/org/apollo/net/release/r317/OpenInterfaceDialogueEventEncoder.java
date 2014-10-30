package org.apollo.net.release.r317;

import org.apollo.game.event.impl.OpenInterfaceDialogueEvent;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.EventEncoder;

/**
 * An {@link EventEncoder} for the {@link OpenInterfaceDialogueEvent}.
 * @author Chris Fletcher
 */
final class OpenInterfaceDialogueEventEncoder extends EventEncoder<OpenInterfaceDialogueEvent> {

	@Override
	public GamePacket encode(OpenInterfaceDialogueEvent event) {
		GamePacketBuilder builder = new GamePacketBuilder(164);
		builder.put(DataType.SHORT, DataOrder.LITTLE, event.getInterfaceId());
		return builder.toGamePacket();
	}

}