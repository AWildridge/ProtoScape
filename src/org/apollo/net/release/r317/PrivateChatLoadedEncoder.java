package org.apollo.net.release.r317;

import org.apollo.game.event.impl.PrivateChatLoadedEvent;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.EventEncoder;


public final class PrivateChatLoadedEncoder extends EventEncoder<PrivateChatLoadedEvent> {

	@Override
	public GamePacket encode(PrivateChatLoadedEvent event) {
		GamePacketBuilder builder = new GamePacketBuilder(221);
                builder.put(DataType.BYTE, event.getId());
		return builder.toGamePacket();
	}

}
