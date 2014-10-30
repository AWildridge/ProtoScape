package org.apollo.net.release.r317;

import org.apollo.game.event.impl.TalismanLocateEvent;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.EventDecoder;
/**
 * TalismanLocateOptionPacketHandler.java
 * @author The Wanderer
 */
public class TalismanLocateOptionEventDecoder extends EventDecoder<TalismanLocateEvent> {

	@Override
	public TalismanLocateEvent decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
		int interfaceId = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD);
                int slot = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);
                int item = (int) reader.getUnsigned(DataType.SHORT, DataTransformation.ADD);
                System.out.println("[TALISMAN LOCATING]: interface - " + interfaceId + ", slot - " + slot + ", item - " + item);
		return new TalismanLocateEvent(interfaceId, item, slot);
	}
}
