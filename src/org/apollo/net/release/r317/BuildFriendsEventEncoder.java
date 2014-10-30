package org.apollo.net.release.r317;

import org.apollo.game.event.impl.BuildFriendsEvent;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.EventEncoder;

/**
 * Send a private chat (Online or offline)
 * @author Steve
 */

public class BuildFriendsEventEncoder extends EventEncoder<BuildFriendsEvent> {

	@Override
	public GamePacket encode(BuildFriendsEvent event) {
		GamePacketBuilder builder = new GamePacketBuilder(50);
		builder.put(DataType.LONG, event.getFriendLong());
		builder.put(DataType.BYTE, event.getChat());
		return builder.toGamePacket();
	}

}