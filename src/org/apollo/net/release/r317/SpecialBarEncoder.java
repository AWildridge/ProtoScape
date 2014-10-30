package org.apollo.net.release.r317;

import org.apollo.game.event.impl.SpecialBarEvent;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.EventEncoder;

public class SpecialBarEncoder extends EventEncoder<SpecialBarEvent> {

	@Override
	public GamePacket encode(SpecialBarEvent event) {
		GamePacketBuilder builder = new GamePacketBuilder(171);
                builder.put(DataType.BYTE, event.getState());
                builder.put(DataType.SHORT, event.getId());
		return builder.toGamePacket();
	}

}