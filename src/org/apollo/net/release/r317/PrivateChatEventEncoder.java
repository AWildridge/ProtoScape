package org.apollo.net.release.r317;

import org.apollo.game.event.impl.PrivateChatEvent;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.EventEncoder;

public class PrivateChatEventEncoder extends EventEncoder<PrivateChatEvent> {

	@Override
	public GamePacket encode(PrivateChatEvent event) {
		GamePacketBuilder builder = new GamePacketBuilder(196, PacketType.VARIABLE_BYTE);
		builder.put(DataType.LONG, event.getFriendLong());
		builder.put(DataType.LONG, event.getLastId());
		builder.put(DataType.BYTE, event.getFriendRights());
		builder.putBytes(event.getMessageCompressed());
		return builder.toGamePacket();
	}

}