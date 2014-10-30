package org.apollo.net.release.r317;

import org.apollo.game.event.impl.AttackPlayerEvent;
import org.apollo.game.model.Player;
import org.apollo.game.model.World;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.EventDecoder;

/**
 * AttackPlayerEventDecoder.java
 * @author The Wanderer
 */
public class AttackPlayerEventDecoder extends EventDecoder<AttackPlayerEvent> {

    @Override
    public AttackPlayerEvent decode(GamePacket packet) {
        GamePacketReader reader = new GamePacketReader(packet);
        int index = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);
        Player player = (Player)World.getWorld().getPlayerRepository().get(index);
        return new AttackPlayerEvent(index);
    }
}
