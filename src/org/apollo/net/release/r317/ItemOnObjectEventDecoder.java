package org.apollo.net.release.r317;

import org.apollo.game.event.impl.ItemOnObjectActionEvent;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.EventDecoder;
/**
 * ItemOnObjectEventDecoder.java
 * @author The Wanderer
 */
public class ItemOnObjectEventDecoder extends EventDecoder<ItemOnObjectActionEvent> {
    
    @Override
    public ItemOnObjectActionEvent decode(GamePacket packet) {
        GamePacketReader reader = new GamePacketReader(packet);
        int interfaceId = (int) reader.getUnsigned(DataType.SHORT, DataTransformation.ADD);
        int objectId = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);
        int objectY = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD);
        int slot = (int) reader.getUnsigned(DataType.SHORT, DataOrder.BIG) / 256;
        int objectX = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD);
        int id = (int) reader.getUnsigned(DataType.SHORT);
        System.out.println("[Item on Object]- Frame ID: " + interfaceId + ", Object ID: " + objectId + ", Object X: " + objectX + ", Object Y: " + objectY + ", Slot ID: " + slot + ", Item ID: " + id);
        return new ItemOnObjectActionEvent(interfaceId, id, slot, objectId, objectX, objectY);
    }
    
}
