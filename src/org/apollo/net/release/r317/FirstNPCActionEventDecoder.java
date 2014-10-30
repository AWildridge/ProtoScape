package org.apollo.net.release.r317;

import org.apollo.game.event.impl.FirstNPCActionEvent;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.EventDecoder;
/**
 * FirstNPCActionEventDecoder.java
 * @author The Wanderer
 */
public final class FirstNPCActionEventDecoder extends EventDecoder<FirstNPCActionEvent> {

	@Override
	public FirstNPCActionEvent decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
                int index = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);
                System.out.println("[First NPC]- index: " + index);
		return new FirstNPCActionEvent(index);
	}
}
