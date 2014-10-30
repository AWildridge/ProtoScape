package org.apollo.net.release.r317;

import org.apollo.game.event.impl.UpdateItemsEvent;
import org.apollo.game.model.Item;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.EventEncoder;

/**
 * An {@link EventEncoder} for the {@link UpdateItemsEvent}.
 * @author Graham
 */
public final class UpdateItemsEventEncoder extends EventEncoder<UpdateItemsEvent> {

	@Override
	public GamePacket encode(UpdateItemsEvent event) {
		GamePacketBuilder builder = new GamePacketBuilder(53, PacketType.VARIABLE_SHORT);

		Item[] items = event.getItems();

		builder.put(DataType.SHORT, event.getInterfaceId());
		builder.put(DataType.SHORT, items.length);

		for (Item item : items) {
			
			/*int id = item == null ? -1 : item.getId();
			int amount = item == null ? 0 : item.getAmount();

			builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, id + 1);

			if (amount > 254) {
				builder.put(DataType.BYTE, DataTransformation.NEGATE, 255);
				builder.put(DataType.INT, DataOrder.LITTLE, amount);
			} else {
				builder.put(DataType.BYTE, DataTransformation.NEGATE, amount);
			}*/
                    
                    if(item != null) {
                        if(item.getAmount() > 254) {
                            builder.put(DataType.BYTE, 255);
                            builder.put(DataType.INT, DataOrder.INVERSED_MIDDLE, item.getAmount());
                        } else {
                            builder.put(DataType.BYTE, item.getAmount());
                        }
                        
                        builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, item.getId()+1);
                        
                        
                    } else {
                        builder.put(DataType.BYTE, 0);
                        builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD,0);
                    }
		}

		return builder.toGamePacket();
	}

}
