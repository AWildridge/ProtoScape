package org.apollo.net.release.r317;

import org.apollo.game.event.impl.PrivateChatEvent;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.EventDecoder;
import org.apollo.util.NameUtil;
import org.apollo.util.TextUtil;

public class PrivateChatEventDecoder extends EventDecoder<PrivateChatEvent> {

	@Override
	public PrivateChatEvent decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);

		long friend = (long) reader.getSigned(DataType.LONG);
		String strFriend = TextUtil.capitalize(NameUtil.decodeBase37(friend));

		final int length = (byte) (packet.getLength()-8);

		byte[] originalCompressed = new byte[length];
		reader.getBytes(originalCompressed);

		String uncompressed = TextUtil.uncompress(originalCompressed, length);
		uncompressed = TextUtil.filterInvalidCharacters(uncompressed);
		uncompressed = TextUtil.capitalize(uncompressed);

		byte[] recompressed = new byte[length];
		TextUtil.compress(uncompressed, recompressed);

		return new PrivateChatEvent(uncompressed, recompressed, strFriend);
	}
	
}