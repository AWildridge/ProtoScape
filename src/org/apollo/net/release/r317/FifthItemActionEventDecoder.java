package org.apollo.net.release.r317;

import org.apollo.game.event.impl.FifthItemActionEvent;
import org.apollo.game.event.impl.FourthItemActionEvent;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.EventDecoder;

/**
 * An {@link EventDecoder} for the {@link FourthItemActionEvent}.
 * @author Graham
 */
public final class FifthItemActionEventDecoder extends EventDecoder<FifthItemActionEvent> {

        @Override
        public FifthItemActionEvent decode(GamePacket packet) {
                GamePacketReader reader = new GamePacketReader(packet);
                int slot = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);
                int interfaceId = (int) reader.getUnsigned(DataType.SHORT, DataTransformation.ADD);
                int id = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);
                System.out.println("[ItemOp5] Interface: " + interfaceId + ", slot: " + slot + ", id: " + id);
                return new FifthItemActionEvent(interfaceId, id, slot);
        }

}