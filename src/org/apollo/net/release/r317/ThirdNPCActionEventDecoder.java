package org.apollo.net.release.r317;

import org.apollo.game.event.impl.ThirdNPCActionEvent;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.EventDecoder;
/**
 * ThirdNPCActionEventDecoder.java
 * @author The Wanderer
 */
public class ThirdNPCActionEventDecoder extends EventDecoder<ThirdNPCActionEvent> {

	@Override
	public ThirdNPCActionEvent decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
                int index = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD);
                System.out.println("[Third NPC]- index: " + index);
		return new ThirdNPCActionEvent(index);
	}
}
