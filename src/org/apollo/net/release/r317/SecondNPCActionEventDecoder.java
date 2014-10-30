package org.apollo.net.release.r317;

import org.apollo.game.event.impl.SecondNPCActionEvent;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.EventDecoder;
/**
 * SecondNPCActionEventDecoder.java
 * @author The Wanderer
 */
public class SecondNPCActionEventDecoder extends EventDecoder<SecondNPCActionEvent> {

	@Override
	public SecondNPCActionEvent decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
                int index = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD);
                System.out.println("[Second NPC]- index: " + index);
		return new SecondNPCActionEvent(index);
	}
}
