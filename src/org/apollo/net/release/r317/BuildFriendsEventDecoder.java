package org.apollo.net.release.r317;

import org.apollo.game.event.impl.BuildFriendsEvent;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.EventDecoder;
import org.apollo.util.NameUtil;
import org.apollo.util.TextUtil;

/**
 * Decode a friends list event.
 * @author Steve
 */

public class BuildFriendsEventDecoder extends EventDecoder<BuildFriendsEvent> {

	@Override
	public BuildFriendsEvent decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
		long l = (long) reader.getSigned(DataType.LONG);
		String friend = TextUtil.capitalize(NameUtil.decodeBase37(l));
		return new BuildFriendsEvent(friend, packet.getOpcode());
	}
	
}